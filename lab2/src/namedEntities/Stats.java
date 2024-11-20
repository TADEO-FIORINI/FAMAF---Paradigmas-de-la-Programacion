package namedEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import namedEntities.entities.*;

public class Stats {
  // Categories
  List<Person> persons = new ArrayList<>();
  List<Location> locations = new ArrayList<>();
  List<Organization> organizations = new ArrayList<>();
  List<Other> others = new ArrayList<>();
  List<NamedEntity> entities = new ArrayList<>();
  boolean is_categories;
  // Topics
  Map<String, List<NamedEntity>> topics = new HashMap<>();
  boolean is_topics;

  public void setStatsByCategory(List<NamedEntity> list_entities) {
    for (NamedEntity entity : list_entities) {
      if (entity instanceof Person) {
        this.persons.add((Person) entity);
      }
      if (entity instanceof Location) {
        this.locations.add((Location) entity);
      }
      if (entity instanceof Organization) {
        this.organizations.add((Organization) entity);
      }
      if (entity instanceof Other) {
        this.others.add((Other) entity);
      }
      this.is_categories = true;
    }
    entities.addAll(this.persons);
    entities.addAll(this.locations);
    entities.addAll(this.organizations);
    entities.addAll(this.others);
  }

  public void printByCategory() {
    if (!this.is_categories) {
      throw new IllegalStateException("setStatsByCategory() must be called before printing the stats");
    }
    if (entities.isEmpty()) {
      System.out.println("No entities found.");
      return;
    }

    String category;
    category = entities.get(0).getCategory();
    System.out.println("Category: " + category);
    for (NamedEntity entity : entities) {
      if (entity.getCategory() != category) {
        category = entity.getCategory();
        System.out.println("Category: " + category);
      }
      System.out.println(String.format("          %s (%d)", entity.getName(), entity.getMentionsCount()));
    }
  }

  public void setStatsByTopics(List<NamedEntity> list_entities) {
    // para cada entidad en la lista de entidades
    for (NamedEntity entity : list_entities) {
      // para cada topico en la lista de topicos de la entidad
      for (String topic : entity.getTopics()) {
        // verificar si el tópico ya está en el mapa
        if (!topics.containsKey(topic)) {
          // si no está, crear una nueva lista y agregarla al mapa
          topics.put(topic, new ArrayList<>());
        }
        // se obtiene la lista asociada al topico
        // y se agrega la entidad a la lista correspondiente al tópico
        topics.get(topic).add(entity);
      }
      this.is_topics = true;
    }
  }

  public void printByTopic() {
    if (!this.is_topics) {
      throw new IllegalStateException("setStatsByTopic() must be called before printing the stats");
    }
    // iteración por cada entrada del map (par topico, lista de entidades), sobre el
    // conjunto de
    // todas las entradas en el map topics (map actua como un dict (pares
    // clave:valor))
    for (Map.Entry<String, List<NamedEntity>> entry : topics.entrySet()) {
      printTopicStats(entry.getKey(), entry.getValue());
    }
  }

  private void printTopicStats(String topic, List<NamedEntity> entities) {
    System.out.println("Topic: " + topic);
    if (entities.isEmpty()) {
      System.out.println("    No entities found.");
    } else {
      for (NamedEntity entity : entities) {
        System.out.println(String.format("          %s (%d)", entity.getName(), entity.getMentionsCount()));
      }
    }
  }

}
