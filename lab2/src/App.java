import feed.Article;
import feed.FeedParser;
import namedEntities.*;
import namedEntities.heuristics.*;
import namedEntities.entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Config;
import utils.DictData;
import utils.FeedsData;
import utils.JSONParser;
import utils.UserInterface;

public class App {

  public static void main(String[] args) {

    List<FeedsData> feedsDataArray = new ArrayList<>();
    List<DictData> dictDataArray = new ArrayList<>();
    try {
      // llamamos al metodo de JSONParser, le pasamos el filepath y asi cargamos los
      // datos json en las variables
      feedsDataArray = JSONParser.parseJsonFeedsData("src/data/feeds.json");
      dictDataArray = JSONParser.parseJsonDictData("src/data/dictionary.json");
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    UserInterface ui = new UserInterface();
    Config config = ui.handleInput(args);

    run(config, feedsDataArray, dictDataArray);
  }

  // TODO: Change the signature of this function if needed
  private static void run(Config config, List<FeedsData> feedsDataArray, List<DictData> dictDataArray) {

    if (feedsDataArray == null || feedsDataArray.size() == 0) {
      System.out.println("No feeds data found");
      return;
    }
    // si le pasamos la flag -h, que printee la ayuda y que salga 
    if (config.getPrintHelp()) {
      printHelp(feedsDataArray);
      System.exit(0);
    }

    List<Article> allArticles = processFeed(config, feedsDataArray);

    // si le pasamos -pf, pero no las flags -ne y -sf, que printee el feed
    if (config.getPrintFeed() && !config.getComputeNamedEntities() && !config.getStatsFormat()) {
      printFeed(allArticles, config);
    }

    // si le pasamos -ne, que compute las entidades
    if (config.getComputeNamedEntities()) {
      List<NamedEntity> namedEntities = computeNamedEntities(config, allArticles, dictDataArray);
      // y si le pasamos -ne y -sf, que printee las entidades y sus estadisticas, si no solo las entidades
      if (config.getStatsFormat()) {
        printNamedEntities(namedEntities);
        printStats(config, namedEntities);
      } else {
        printNamedEntities(namedEntities);
      }
    } else if (config.getStatsFormat()) {
      // si solo le pasamos la flag -sf, tirar mensaje de error
      System.out.println("Error: Stats format specified without computing named entities");
    }
  }

  private static List<Article> processFeed(Config config,
      List<FeedsData> feedsDataArray) {
    // Process Feed(s)
    List<Article> allArticles = new ArrayList<>();

    if (config.getProcessFeed()) {
      String feedKey = config.getFeedKey();
      if (!UserInterface.validFeedKey(feedKey, feedsDataArray)) {
        System.out.println("invalid feedKey");
      } else {
        String feedURL = "";
        String feedXML = "";
        System.out.println("Processing feed " + feedKey + "...");
        for (FeedsData feedData : feedsDataArray) {
          if (feedKey.equals(feedData.getLabel()))
            feedURL = feedData.getUrl();
        }

        try {
          feedXML = FeedParser.fetchFeed(feedURL);
        } catch (Exception e) {
          System.out.println("feed not found");
          e.printStackTrace();
        }
        allArticles = FeedParser.parseXML(feedXML);
      }

    } else {
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
    }
    return allArticles;
  }

  private static void printFeed(List<Article> allArticles, Config config) {
    if (config.getPrintFeed() || !config.getComputeNamedEntities()) {
      System.out.println("Printing feed(s)...");
      for (Article article : allArticles) {
        article.print();
      }
    }
  }

  private static List<NamedEntity> computeNamedEntities(Config config,
      List<Article> allArticles, List<DictData> dictDataArray) {
    if (config.getComputeNamedEntities()) {
      String heuristic = config.getHeuristic();
      if (!UserInterface.validHeuristic(heuristic)) {
        System.out.println("heuristic not found");
      } else {
        System.out.println("Computing named entities using " + heuristic);
        List<String> namedEntitiesList = new ArrayList<>();

        // guardar las entidades del articulo en la lista, segun la heuristica
        namedEntitiesList = Getter.getNamedEntities(heuristic, allArticles);
        if (namedEntitiesList != null) {
          List<NamedEntity> named_entities = Classifier.ClassifyWithDict(namedEntitiesList, dictDataArray);
          return named_entities;
        }
      }
    }
    throw new RuntimeException("Cant compute named entities");
  }

  private static void printNamedEntities(List<NamedEntity> namedEntities) {
    for (NamedEntity entity : namedEntities) {
      if (entity instanceof Location) {
        
        Location location = (Location) entity;
        System.out.println("\n------START ENTITY---------");
        System.out.println("Entity: " + entity.getName());
        System.out.println("Category: " + entity.getCategory());
        System.out.println("Topics: " + entity.getTopics());
        System.out.println("Latitud: " + location.getLatitude());
        System.out.println("Longitud: " + location.getLongitude());
        System.out.println("------END ENTITY---------");

      } else if (entity instanceof Person) {

        Person person = (Person) entity;
        System.out.println("\n------START ENTITY---------");
        System.out.println("Entity: " + entity.getName());
        System.out.println("Category: " + entity.getCategory());
        System.out.println("Topics: " + entity.getTopics());
        System.out.println("Occupation: " + person.getOccupation());
        System.out.println("------END ENTITY---------");  

      } else {

        System.out.println("\n------START ENTITY---------");
        System.out.println("Entity: " + entity.getName());
        System.out.println("Category: " + entity.getCategory());
        System.out.println("Topics: " + entity.getTopics());
        System.out.println("------END ENTITY---------");
      }
    }
  }

  private static void printStatsByCategory(List<NamedEntity> named_entities) {
    Stats stats = new Stats();
    stats.setStatsByCategory(named_entities);
    stats.printByCategory();
  }

  private static void printStatsByTopic(List<NamedEntity> named_entities) {
    Stats stats = new Stats();
    stats.setStatsByTopics(named_entities);
    stats.printByTopic();
  }

  private static void printStats(Config config, List<NamedEntity> namedEntities) {
    if (!UserInterface.validFormat(config.getFormat())) {
      System.out.println("Invalid format");
      return;
    }

    System.out.println("Printing stats in format " + config.getFormat() + "...");
    if (config.getFormat().equals("cat")) {
      printStatsByCategory(namedEntities);
    } else if (config.getFormat().equals("topic")) {
      printStatsByTopic(namedEntities);
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
