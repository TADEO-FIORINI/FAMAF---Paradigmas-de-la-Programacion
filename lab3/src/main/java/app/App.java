package app;

import app.feed.Article;
import app.feed.FeedParser;
import app.namedEntities.*;
import app.namedEntities.heuristics.*;
import app.namedEntities.entities.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import app.utils.Config;
import app.utils.DictData;
import app.utils.FeedsData;
import app.utils.JSONParser;
import app.utils.MyTimer;
import app.utils.UserInterface;

public class App {

  public static void main(String[] args) {

    List<FeedsData> feedsDataArray = new ArrayList<>();
    List<DictData> dictDataArray = new ArrayList<>();
    try {
      feedsDataArray = JSONParser.parseJsonFeedsData("/feeds.json");
      dictDataArray = JSONParser.parseJsonDictData("/dictionary.json");

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    UserInterface ui = new UserInterface();
    Config config = ui.handleInput(args);

    run(config, feedsDataArray, dictDataArray);
  }

  private static void run(Config config, List<FeedsData> feedsDataArray, List<DictData> dictDataArray) {
    Set<String> topics = new HashSet<>();
    for (DictData data : dictDataArray) {
      for (String topic : data.getTopics()) {
        topics.add(topic);
      }
    }

    MyTimer myTimer = new MyTimer();
    myTimer.startTimer(); // Iniciar el temporizador

    if (feedsDataArray == null || feedsDataArray.size() == 0) {
      System.out.println("No feeds data found");
      return;
    }
    // si le pasamos la flag -h, que printee la ayuda y que salga
    if (config.getPrintHelp()) {
      printHelp(feedsDataArray);
      System.exit(0);
    }

    SparkConf conf = new SparkConf()
        .setAppName("NamedEntities");

    JavaSparkContext sc = new JavaSparkContext(conf);

    SparkSession spark = SparkSession
        .builder()
        .config(conf)
        .getOrCreate();

    // por defecto procesa el archivo de 1.57 GB
    String fileBigData = "src/main/resources/wiki_dump_parcial.txt";

    // con el flag -f procesa todos los feeds y crea un archivo bigdata
    if (config.getProcessFeeds()) {
      List<Article> allArticles = processFeed(config, feedsDataArray);
      writeFeed(allArticles, config);
      fileBigData = "src/main/resources/bigdata.txt";
    } else {
      System.out.println("Processing big example file...");
    }

    // si le pasamos -ne, que compute las entidades
    if (config.getComputeNamedEntities()) {
      // JavaRDD<NamedEntity> namedEntitiesRDD = sc.parallelize(namedEntitiesRDD,100);
      JavaRDD<NamedEntity> namedEntitiesRDD = computeNamedEntities(config, spark, dictDataArray, fileBigData);
      // y si le pasamos -ne y -sf, que printee las entidades y sus estadisticas, si
      // no solo las entidades
      if (config.getStatsFormat()) {
        writeStats(config, namedEntitiesRDD, sc, spark, topics);
      } else {
        writeNamedEntities(namedEntitiesRDD);
      }
    } else if (config.getStatsFormat()) {
      // si solo le pasamos la flag -sf, tirar mensaje de error
      System.out.println("Error: Stats format specified without computing named entities");
    }

    myTimer.stopTimer(); // Detener el temporizador

    spark.close();
  }

  private static List<Article> processFeed(Config config,
      List<FeedsData> feedsDataArray) {
    // Process Feed(s)
    List<Article> allArticles = new ArrayList<>();
    System.out.println("Processing all feeds...");
    for (FeedsData feedsData : feedsDataArray) {
      List<Article> articles = new ArrayList<>();
      String feedURL = feedsData.getUrl();
      String feedXML = "";
      try {
        feedXML = FeedParser.fetchFeed(feedURL);
      } catch (Exception e) {
        System.out.println("feed not found");
        e.printStackTrace();
      }
      articles = FeedParser.parseXML(feedXML);
      allArticles.addAll(articles);
    }
    return allArticles;
  }

  private static void writeFeed(List<Article> allArticles, Config config) {

    try {
      try {
        FileUtils.delete(new File("src/main/resources/bigdata.txt"));
      } catch (NoSuchFileException e) {
        System.err.println("bigdata doesn't exist yet");
      }
      FileWriter myWriter = new FileWriter("src/main/resources/bigdata.txt");
      for (Article article : allArticles) {
        myWriter.write(article.getString());
      }
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static JavaRDD<NamedEntity> computeNamedEntities(Config config,
      SparkSession spark, List<DictData> dictDataArray, String fileBigData) {
    if (config.getComputeNamedEntities()) {
      String heuristic = config.getHeuristic();
      if (!UserInterface.validHeuristic(heuristic)) {
        System.out.println("heuristic not found");
      } else {
        System.out.println("Computing named entities using " + heuristic + "...");
        // guardar las entidades del articulo en la lista, segun la heuristica
        JavaRDD<String> entitiesRDD = Getter.getNamedEntities(heuristic, spark, fileBigData);
        // Classify each candidate
        JavaRDD<NamedEntity> named_entities = Classifier.ClassifyWithDict(spark, entitiesRDD, dictDataArray);
        return named_entities;
      }
    }
    throw new RuntimeException("Cant compute named entities");
  }

  private static void writeNamedEntities(JavaRDD<NamedEntity> namedEntitiesRDD) {
    JavaRDD<String> entities_out = namedEntitiesRDD.map(entity -> {
      return entity.toString();
    });
    try {
      FileUtils.deleteDirectory(new File("output/classifiedEntities"));
      entities_out.saveAsTextFile("output/classifiedEntities");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void writeStats(Config config, JavaRDD<NamedEntity> namedEntitiesRDD, JavaSparkContext sc,
      SparkSession spark, Set<String> topics) {
    if (!UserInterface.validFormat(config.getFormat())) {
      System.out.println("Invalid format");
      return;
    }
    System.out.println("Writing stats in format " + config.getFormat() + "...");
    if (config.getFormat().equals("cat")) {
      Stats stats = new Stats();
      stats.writeStatsByCategory(namedEntitiesRDD, spark);
    } else if (config.getFormat().equals("topic")) {
      Stats stats = new Stats();
      stats.writeStatsByTopic(sc, namedEntitiesRDD, topics);
    } else {
      System.out.println("Unknown format");
    }

  }

  private static void printHelp(List<FeedsData> feedsDataArray) {
    System.out.println("Usage: make run ARGS=\"[OPTION]\"");
    System.out.println("Options:");
    System.out.println("  -h, --help: Show this help message and exit");
    System.out.println("  -f, --feed <feedKey>:                Fetch and "
        + "process the feed with");
    System.out.println(
        "                                       the specified key");
    System.out.println(
        "                                       Available feed keys are: ");
    for (FeedsData feedData : feedsDataArray) {
      System.out.println("                                       " +
          feedData.getLabel());
    }
    System.out.println("  -ne, --named-entity <heuristicName>: Use the "
        + "specified heuristic to extract");
    System.out.println("                                       named entities");
    System.out.println("                                       Available "
        + "heuristic names are: ");
    List<String> heuristics = Getter.getHeuristics();
    for (String heuristic : heuristics) {
      System.out.println("                                       " + heuristic);
    }
    System.out.println(
        "                                       <name>: <description>");
    System.out.println(
        "  -pf, --print-feed:                   Print the fetched feed");
    System.out.println("  -sf, --stats-format <format>:        Print the "
        + "stats in the specified format");
    System.out.println(
        "                                       Available formats are: ");
    System.out.println(
        "                                       cat: Category-wise stats");
    System.out.println(
        "                                       topic: Topic-wise stats");
  }
}
