package namedEntities;

import java.util.List;

public abstract class NamedEntity {
  private String name;
  private String category;
  private List<String> topics;
  private int mentionsCount;

  // Constructor to initialize the fields
  public NamedEntity(String name, String category, List<String> topics) {
    this.name = name;
    this.category = category;
    this.topics = topics;
    this.mentionsCount = 1;
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
}
