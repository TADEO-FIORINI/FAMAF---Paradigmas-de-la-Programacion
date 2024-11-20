package app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.namedEntities.heuristics.Getter;

public class UserInterface {

  private HashMap<String, String> optionDict;
  private List<Option> options;

  public UserInterface() {
    options = new ArrayList<Option>();
    options.add(new Option("-h", "--help", 0));
    options.add(new Option("-f", "--feed", 0));
    options.add(new Option("-ne", "--named-entity", 1));
    options.add(new Option("-sf", "--stats-format", 1));

    optionDict = new HashMap<String, String>();
  }

  public Config handleInput(String[] args) {

    for (Integer i = 0; i < args.length; i++) {
      for (Option option : options) {
        if (option.getName().equals(args[i]) || option.getLongName().equals(args[i])) {
          if (option.getnumValues() == 0) {
            optionDict.put(option.getName(), null);
          } else {
            if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
              optionDict.put(option.getName(), args[i + 1]);
              i++;
            } else {
              System.out.println("Invalid inputs");
              System.exit(1);
            }
          }
        }
      }
    }

    // Detectamos los inputs del usuario

    Boolean printHelp = optionDict.containsKey(("-h"));

    Boolean processFeed = optionDict.containsKey("-f");

    Boolean computeNamedEntities = optionDict.containsKey("-ne");
    String heuristic = optionDict.get("-ne");

    Boolean statsFormat = optionDict.containsKey("-sf");
    String format = optionDict.get("-sf");

    // instanciamos un objeto de la clase Config para inicializar el programa

    return new Config(printHelp, processFeed, computeNamedEntities, heuristic, statsFormat, format);
  }

  // funciones para ver si los argumentos de los flags son validos

  public static boolean validHeuristic(String heuristic) {
    boolean valid = false;
    List<String> heuristics = Getter.getHeuristics();
    for (String h : heuristics) {
      valid |= heuristic.equals(h);
    }
    return valid;
  }

  public static boolean validFormat(String format) {
    boolean valid = format.equals("cat") || format.equals("topic");
    return valid;
  }

}
