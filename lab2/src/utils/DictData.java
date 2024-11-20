package utils;

import java.util.List;

public class DictData {

  private String label;
  private String Category;
  private List<String> Topics;
  private List<String> keywords;

  // constructor
  public DictData(String label, String Category, List<String> Topics, List<String> keywords) {
    this.label = label;
    this.Category = Category;
    this.Topics = Topics;
    this.keywords = keywords;
  }

  // Getters

  public String getLabel() {
    return label;
  }

  public String getCategory() {
    return Category;
  }

  public List<String> getTopics() {
    return Topics;
  }

  public List<String> getKeywords() {
    return keywords;
  }
}
