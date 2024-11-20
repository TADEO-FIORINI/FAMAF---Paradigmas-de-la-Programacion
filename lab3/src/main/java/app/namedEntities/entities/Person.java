package app.namedEntities.entities;

import app.namedEntities.NamedEntity;
import java.util.List;

public class Person extends NamedEntity {
  private String occupation;

  public Person(String name, List<String> topics, Integer counts) {
    super(name, "PERSON", topics, counts); // Initialize superclass fields
  }

  // Getter for occupation
  public String getOccupation() {
    return occupation;
  }

  // Setter for occupation
  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }
}
