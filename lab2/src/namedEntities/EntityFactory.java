package namedEntities;

import api.GetLocation;
import api.GetPerson;
import namedEntities.entities.*;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

public interface EntityFactory {
  NamedEntity createEntity(String name, List<String> topics);
}

class PersonFactory implements EntityFactory {
  @Override
  public NamedEntity createEntity(String name, List<String> topics) {
    Person person = new Person(name, topics);
    // llamamos a la API para obtener información sobre la ocupación de la persona
    try {
      JSONObject jsonObject = GetPerson.searchKnowledgeGraph(name);
      // extraemos la ocupacion del jsonObject que nos genero la API, si es que esta
      // disponible
      String occupation = GetPerson.extractOccupation(jsonObject);
      if (occupation != null) {
        person.setOccupation(occupation);
      }
    } catch (IOException e) {
      System.out.println("Error al obtener información de la persona desde Knowledge Graph API");
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("Error inesperado al procesar la respuesta de la API");
      e.printStackTrace();
    }
    return person;
  }
}

class OrganizationFactory implements EntityFactory {
  @Override
  public NamedEntity createEntity(String name, List<String> topics) {
    return new Organization(name, topics);
  }
}

class LocationFactory implements EntityFactory {

  @Override
  public NamedEntity createEntity(String name, List<String> topics) {
    Location location = new Location(name, topics);
    try {
      // obtenemos el array con la longitud y latitud
      double[] res = GetLocation.GetLatLon(name);
      // las seteamos
      location.setLatitude(res[0]);
      location.setLongitude(res[1]);
    } catch (Exception e) {
      System.out.println("Location not found");
      e.printStackTrace();
    }
    return location;
  }
}

class OtherFactory implements EntityFactory {
  @Override
  public NamedEntity createEntity(String name, List<String> topics) {
    return new Other(name, topics);
  }
}
