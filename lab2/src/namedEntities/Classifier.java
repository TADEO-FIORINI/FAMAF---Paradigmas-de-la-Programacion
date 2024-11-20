package namedEntities;

import java.util.List;
import utils.DictData;
import java.util.Arrays;

public class Classifier {
  // función para clasificar las entidades nombradas
  // se recorre una lista de entidades y un diccionario buscando coincidencias, si
  // se encuentran
  // se clasifican, si no se printea que no se encontro la entidad en el dict
  public static List<NamedEntity> ClassifyWithDict(List<String> entities, List<DictData> dictionary) {
    EntityRegistry registry = EntityRegistry.getInstance();
    for (String entity : entities) {
      boolean classified = false;
      for (DictData data : dictionary) {
        if (data.getKeywords().contains(entity)) {
          // Conseguimos la factory que tenemos que usar por medio de la categoria
          EntityFactory factory = EntityFactoryProducer.getFactory(data.getCategory());
          // Usamos un registro para mantener la lista de entidades y no repetirlas,
          // tambien
          // podemos contar la cantidad de veces que se nombran
          registry.updateOrCreateEntity(entity, data.getTopics(), factory);
          classified = true;
          break;
        }
      }
      // Aca podriamos llamar a la API de algun LLM
      if (!classified) {
        EntityFactory factory = EntityFactoryProducer.getFactory("OTHER");
        registry.updateOrCreateEntity(entity, Arrays.asList("OTHER"), factory);
      }
    }
    return registry.getResitry();
  }
}
