package namedEntities.heuristics;

import java.util.ArrayList;
import java.util.List;
import feed.*;

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
  public static List<String> getNamedEntities(String heuristic, List<Article> articles) {
    List<String> namedEntities = new ArrayList<>();
    for (Article article : articles) {
      String title = article.getTitle();
      String description = article.getDescription();
      // no queremos extraer nombres de la fecha de publicacion ni del link
      if (heuristic.equals("CapitalizedWordHeuristic")) {
        namedEntities.addAll(CapitalizedWordHeuristic.extractCandidates(title));
        namedEntities.addAll(CapitalizedWordHeuristic.extractCandidates(description));
      }
      if (heuristic.equals("CapitalizedWordHeuristicWithExclusions")) {
        namedEntities.addAll(CapitalizedWordHeuristicWithExclusions.extractCandidates(title));
        namedEntities.addAll(CapitalizedWordHeuristicWithExclusions.extractCandidates(description));
      }
      if (heuristic.equals("CapitalizedWordHeuristicWithExclusionsAndConectors")) {
        namedEntities.addAll(CapitalizedWordHeuristicWithExclusionsAndConectors.extractCandidates(title));
        namedEntities.addAll(CapitalizedWordHeuristicWithExclusionsAndConectors.extractCandidates(description));
      }
    }
    return namedEntities;
  }
}
