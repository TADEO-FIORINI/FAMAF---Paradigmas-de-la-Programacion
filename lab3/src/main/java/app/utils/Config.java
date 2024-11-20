package app.utils;

public class Config {
  private boolean printHelp;
  private boolean processFeeds;
  private boolean computeNamedEntities;
  private String heuristic;
  private boolean statsFormat;
  private String format;

  // completamos la clase con todos los argumentos del programa
  public Config(boolean printHelp
      , boolean processFeeds
      , boolean computeNamedEntities, String heuristic
      , boolean statsFormat, String format) {
    this.printHelp = printHelp;
    this.processFeeds = processFeeds;
    this.computeNamedEntities = computeNamedEntities;
    this.heuristic = heuristic;
    this.statsFormat = statsFormat;
    this.format = format;
  }

  public boolean getPrintHelp() {
    return printHelp;
  }

  public boolean getProcessFeeds() {
    return processFeeds;
  }

  public boolean getComputeNamedEntities() {
    return computeNamedEntities;
  }

  public String getHeuristic() {
    return heuristic;
  }

  public boolean getStatsFormat() {
    return statsFormat;
  }

  public String getFormat() {
    return format;
  }

}
