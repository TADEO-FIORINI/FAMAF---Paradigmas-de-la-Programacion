package app.namedEntities.heuristics;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.Normalizer;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.api.java.JavaRDD;

public class CapitalizedWordHeuristic implements HeuristicInterface {

  public static JavaRDD<String> extractCandidates(SparkSession spark, String fileBigData) {
    JavaRDD<String> lines = spark.read().textFile(fileBigData).javaRDD();

    // Definir el patrón para encontrar palabras capitalizadas
    Pattern pattern = Pattern.compile("[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*");
    JavaRDD<String> matchesRDD = lines.flatMap(text -> {
      text = text.replaceAll("[-+.^:,\"]", " "); // un nombre propio no deberia
      // tener estos signos internamente
      text = Normalizer.normalize(text, Normalizer.Form.NFD);
      text = text.replaceAll("\\p{M}", "");
      Matcher matcher = pattern.matcher(text);
      List<String> matches = new ArrayList<>();
      while (matcher.find()) {
        matches.add(matcher.group());
      }
      return matches.iterator();
    });
    return matchesRDD;

  }
}
