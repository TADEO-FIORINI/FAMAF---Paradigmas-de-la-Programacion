package namedEntities;

public class EntityFactoryProducer {
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
