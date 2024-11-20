package app.namedEntities;

import java.io.Serializable;

public class EntityFactoryProducer implements Serializable {
  public static EntityFactory getFactory(String category) {
    switch (category) {
      case "PERSON":
        return new PersonFactory();
      case "ORGANIZATION":
        return new OrganizationFactory();
      case "LOCATION":
        return new LocationFactory();
      default:
        return new OtherFactory();
    }
  }
}
