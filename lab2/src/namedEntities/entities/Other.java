package namedEntities.entities;

import namedEntities.NamedEntity;
import java.util.List;

public class Other extends NamedEntity {
  public Other(String name, List<String> topics) {
    super(name, "OTHER", topics); // Initialize superclass fields
  }
}
