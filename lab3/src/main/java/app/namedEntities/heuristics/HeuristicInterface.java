package app.namedEntities.heuristics;

import java.util.ArrayList;
import java.util.List;

// interfaz de las heuristicas para asegurarnos que todas implementen "extractCandidates"
public interface HeuristicInterface {
  public static List<String> extractCandidates(String text) {
    List<String> namedEntities = new ArrayList<>();
    return namedEntities;
  }
}
