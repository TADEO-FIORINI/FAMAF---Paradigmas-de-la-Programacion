package utils;

public class Config {
  private boolean printHelp = false;
  private boolean printFeed = false;
  private boolean processFeed = false;
  private String feedKey;
  private boolean computeNamedEntities = false;
  private String heuristic;
  private boolean statsFormat = false;
  private String format;

  // completamos la clase con todos los argumentos del programa
  public Config(boolean printHelp, boolean processFeed, String feedKey, boolean computeNamedEntities, String heuristic,
      boolean printFeed, boolean statsFormat, String format) {
    this.printHelp = printHelp;
    this.feedKey = feedKey;
    this.processFeed = processFeed;
    this.computeNamedEntities = computeNamedEntities;
    this.heuristic = heuristic;
    this.printFeed = printFeed;
    this.statsFormat = statsFormat;
    this.format = format;
  }

  public boolean getPrintHelp() {
    return printHelp;
  }

  public boolean getProcessFeed() {
    return processFeed;
  }

  public String getFeedKey() {
    return feedKey;
  }

  public boolean getPrintFeed() {
    return printFeed;
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
