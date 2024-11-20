package app.namedEntities;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

// Registry Pattern
// Class to registry entities by their fabrics, if it exist then sum 1 to the counter of the mentions
// if it dosnt exist then it creates the entity
public class EntityRegistry implements Serializable {
  private static EntityRegistry instance;
  private ConcurrentHashMap<String, NamedEntity> registry;

  private EntityRegistry() {
    registry = new ConcurrentHashMap<>();
  }

  // Singleton
  public static EntityRegistry getInstance() {
    if (instance == null) {
      synchronized (EntityRegistry.class) {
        if (instance == null) {
          instance = new EntityRegistry();
        }
      }
    }
    return instance;
  }

  public NamedEntity updateOrCreateEntity(String name, Integer counts, List<String> topics, EntityFactory factory) {
    NamedEntity entity = registry.get(name);
    if (entity != null) {
      entity.incrementMentionCount();
      return null;
    } else {
      entity = factory.createEntity(name, topics, counts);
      registry.put(name, entity);
    }
    return entity;
  }

  public List<NamedEntity> getResitry() {
    List<NamedEntity> named_entities = new ArrayList<NamedEntity>(this.registry.values());
    return named_entities;
  }
}
