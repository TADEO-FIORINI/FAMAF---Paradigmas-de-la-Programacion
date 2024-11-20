package app.namedEntities;

import java.io.Serializable;
import java.util.List;

public abstract class NamedEntity implements Serializable {
  private String name;
  private String category;
  private List<String> topics;
  private int mentionsCount;

  // Constructor to initialize the fields
  public NamedEntity(String name, String category, List<String> topics, Integer counts) {
    this.name = name;
    this.category = category;
    this.topics = topics;
    this.mentionsCount = counts;
  }

  // getters
  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public List<String> getTopics() {
    return topics;
  }

  public int getMentionsCount() {
    return mentionsCount;
  }

  public void incrementMentionCount() {
    mentionsCount++;
  }

  public String toString() {
    return "entity='" + this.name + "' category='" + this.category + "' topics='" + this.topics + "'";

  }

  public String printTopic() {
    return "entity='" + this.name + "' topics='" + this.topics + "'";
  }
}
