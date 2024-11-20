package app.feed;

public class Article {

  private String title;
  private String description;
  private String pubDate;
  private String link;

  public Article(String title, String description, String pubDate, String link) {
    this.title = title;
    this.description = description;
    this.pubDate = pubDate;
    this.link = link;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getPubDate() {
    return pubDate;
  }

  public String getLink() {
    return link;
  }

  public String getString() {
    String st = String.format("Title: %s\nDescription: %s\n", title, description);
    return st;
  }

  public void print() {
    System.out.println("Title: " + title);
    System.out.println("Description: " + description);
    System.out.println("Publication Date: " + pubDate);
    System.out.println("Link: " + link);
    System.out.println("********************************************************************************");
  }
}
