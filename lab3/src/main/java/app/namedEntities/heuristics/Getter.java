package app.namedEntities.heuristics;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import scala.reflect.ClassTag;
import scala.reflect.ClassTag$;

public class Getter {

  // Funcion para obtener las heuristicas disponibles
  public static List<String> getHeuristics() {
    List<String> heuristics = new ArrayList<>();
    heuristics.add("CapitalizedWordHeuristic");
    heuristics.add("CapitalizedWordHeuristicWithExclusions");
    heuristics.add("CapitalizedWordHeuristicWithExclusionsAndConectors");
    return heuristics;
  }

  // Funcion para obtener una lista de entidades nombradas usando la heuristica
  // especificada
  // {PRE: validHeuristic}
  public static JavaRDD<String> getNamedEntities(String heuristic, SparkSession spark, String fileBigData) {
    SparkContext sc = spark.sparkContext();
    ClassTag<String> tag = ClassTag$.MODULE$.apply(String.class);
    JavaRDD<String> candidatesRDD = sc.emptyRDD(tag).toJavaRDD();

    // no queremos extraer nombres de la fecha de publicacion ni del link
    if (heuristic.equals("CapitalizedWordHeuristic")) {
      candidatesRDD = CapitalizedWordHeuristic.extractCandidates(spark, fileBigData);
    }
    if (heuristic.equals("CapitalizedWordHeuristicWithExclusions")) {
      candidatesRDD = CapitalizedWordHeuristicWithExclusions.extractCandidates(spark, fileBigData);
    }
    if (heuristic.equals("CapitalizedWordHeuristicWithExclusionsAndConectors")) {
      candidatesRDD = CapitalizedWordHeuristicWithExclusionsAndConectors.extractCandidates(spark, fileBigData);
    }
    return candidatesRDD;
  }
}
