package app.namedEntities.entities;

import app.namedEntities.NamedEntity;
import java.util.List;

public class Organization extends NamedEntity {
  private String industry;

  public Organization(String name, List<String> topics, Integer counts) {
    super(name, "ORGANIZATION", topics, counts); // Initialize superclass fields
  }

  // Getter for industry
  public String getIndustry() {
    return industry;
  }

  // Setter for industry
  public void setIndustry(String industry) {
    this.industry = industry;
  }
}
