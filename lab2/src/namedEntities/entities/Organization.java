package namedEntities.entities;

import namedEntities.NamedEntity;
import java.util.List;

public class Organization extends NamedEntity {
  private String industry;

  public Organization(String name, List<String> topics) {
    super(name, "ORGANIZATION", topics); // Initialize superclass fields
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
