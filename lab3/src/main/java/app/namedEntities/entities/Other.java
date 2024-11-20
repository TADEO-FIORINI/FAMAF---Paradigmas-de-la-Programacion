package app.namedEntities.entities;

import app.namedEntities.NamedEntity;
import java.util.List;

public class Other extends NamedEntity {
  public Other(String name, List<String> topics, Integer counts) {
    super(name, "OTHER", topics, counts); // Initialize superclass fields
  }
}
