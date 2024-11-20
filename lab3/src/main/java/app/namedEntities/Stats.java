package app.namedEntities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import app.namedEntities.entities.*;
import scala.Tuple2;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;

public class Stats {
  // Categories
  List<Person> persons = new ArrayList<>();
  List<Location> locations = new ArrayList<>();
  List<Organization> organizations = new ArrayList<>();
  List<Other> others = new ArrayList<>();
  List<NamedEntity> entities = new ArrayList<>();
  boolean is_categories;
  // Topics
  Map<String, List<NamedEntity>> topics = new HashMap<>();
  boolean is_topics;

  private List<String> writtenFiles = new ArrayList<>(); // Lista para almacenar nombres de archivos escritos

  public void writeStatsByCategory(JavaRDD<NamedEntity> entitiesRDD, SparkSession spark) {
    JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
    String outputPath = "output/classificationCat";

    // Process categories sequentially
    List<String> allEntities = new ArrayList<>();

    allEntities.addAll(fsmRDD(entitiesRDD, Person.class, sc));
    allEntities.addAll(fsmRDD(entitiesRDD, Organization.class, sc));
    allEntities.addAll(fsmRDD(entitiesRDD, Location.class, sc));

    writeFile(sc, allEntities, outputPath);
  }

  public void writeStatsByTopic(JavaSparkContext sc, JavaRDD<NamedEntity> entitiesRDD, Set<String> topics) {
    String outputPath = "output/classificationTopic";
    entitiesRDD = entitiesRDD.filter(entity -> !entity.getTopics().contains("OTHER"));
    List<String> allEntities = new ArrayList<>();
    for (String topic : topics) {
      if (!topic.equals("OTHER")) {
        allEntities.add("Topic: " + topic);
        List<String> entitiesByTop = entitiesRDD
            .filter(entity -> entity.getTopics().contains(topic))
            .sortBy((Function<NamedEntity, Integer>) NamedEntity::getMentionsCount, false, 1)
            .map(entity -> "      " + entity.getName() + " (" + entity.getMentionsCount() + ")")
            .collect();
        allEntities.addAll(entitiesByTop);

      }
    }
    writeFile(sc, allEntities, outputPath);
  }

  static List<String> fsmRDD(JavaRDD<NamedEntity> entitiesRDD, Class<? extends NamedEntity> classz,
      JavaSparkContext sc) {
    List<String> res = new ArrayList<>();
    res.add("Category: " + classz.getSimpleName().toUpperCase());
    List<String> resultRDD = entitiesRDD
        .filter(entity -> classz.isInstance(entity))
        .sortBy((Function<NamedEntity, Integer>) NamedEntity::getMentionsCount, false, 1)
        .map(entity -> "      " + entity.getName() + " (" + entity.getMentionsCount() + ")")
        .collect();
    res.addAll(resultRDD);
    return res;
  }

  private void writeFile(JavaSparkContext sc, List<String> allEntities, String outputPath) {
    try {
      FileUtils.deleteDirectory(new File(outputPath));
      JavaRDD<String> finalRDD = sc.parallelize(allEntities);
      // Write to a single file
      finalRDD.coalesce(1, true).saveAsTextFile(outputPath);
      writtenFiles.add(outputPath);
      printWrittenFiles(outputPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void printWrittenFiles(String outputPath) {
    // Obtener archivos generados
    File[] generatedFiles = new File(outputPath).listFiles();
    if (generatedFiles != null) {
      // Iterar sobre los archivos generados
      for (File file : generatedFiles) {
        // Verificar si el archivo termina en .crc
        if (!file.getAbsolutePath().endsWith(".crc")) {
          try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String linea;
            while ((linea = br.readLine()) != null) {
              System.out.println(linea);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    } else {
      System.out.println("No se encontraron archivos en el directorio: " + outputPath);
    }
  }

}
