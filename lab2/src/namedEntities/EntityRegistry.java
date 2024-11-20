package namedEntities;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

// Registry Pattern
// Class to registry entities by their fabrics, if it exist then sum 1 to the counter of the mentions
// if it dosnt exist then it creates the entity
public class EntityRegistry {
  private static EntityRegistry instance;
  private Map<String, NamedEntity> registry;

  private EntityRegistry() {
    registry = new HashMap<>();
  }

  // Singleton
  public static synchronized EntityRegistry getInstance() {
    if (instance == null) {
      instance = new EntityRegistry();
    }
    return instance;
  }

  public void updateOrCreateEntity(String name, List<String> topics, EntityFactory factory) {
    NamedEntity entity = registry.get(name);
    if (entity != null) {
      entity.incrementMentionCount();
    } else {
      entity = factory.createEntity(name, topics);
      registry.put(name, entity);
    }
  }

  public List<NamedEntity> getResitry() {
    List<NamedEntity> named_entities = new ArrayList<NamedEntity>(this.registry.values());
    return named_entities;
  }
}
