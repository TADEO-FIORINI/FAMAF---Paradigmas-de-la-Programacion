package app.namedEntities;

import java.util.List;

import app.utils.DictData;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

public class Classifier {
  // función para clasificar las entidades nombradas
  // se recorre una lista de entidades y un diccionario buscando coincidencias, si
  // se encuentran
  // se clasifican, si no se printea que no se encontro la entidad en el dict
  public static JavaRDD<NamedEntity> ClassifyWithDict(SparkSession spark, JavaRDD<String> entitiesRDD,
      List<DictData> dictionary) {
    JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
    // Broadcast the dictionary, como no cambia distribuirlo con broadcast es mas
    // eficiente
    final Broadcast<List<DictData>> broadcastDict = sc.broadcast(dictionary);
    EntityRegistry registry = EntityRegistry.getInstance();
    JavaPairRDD<String, Integer> ne_ones = entitiesRDD.mapToPair(entity -> {
      return new Tuple2<>(entity, 1);
    });
    JavaPairRDD<String, Integer> ne_counts = ne_ones.reduceByKey((i1, i2) -> i1 + i2);

    // Serializamos todos los objectos que se usan en map
    JavaRDD<NamedEntity> namedEntitiesRDD = ne_counts.flatMap(tuple -> {
      List<NamedEntity> mappedEntities = new ArrayList<>();
      List<DictData> dictDataList = broadcastDict.value();
      NamedEntity classif_ent;
      for (DictData data : dictDataList) {
        if (data.getKeywords().contains(tuple._1())) {
          // Conseguimos la factory que tenemos que usar por medio de la categoria
          EntityFactory factory = EntityFactoryProducer.getFactory(data.getCategory());
          // Usamos un registro para mantener la lista de entidades y no repetirlas,
          // tambien
          // podemos contar la cantidad de veces que se nombran
          classif_ent = registry.updateOrCreateEntity(tuple._1(), tuple._2(), data.getTopics(), factory);
          // el registry devuelve null si ya existe para que no lo agreguemos de nuevo
          if (classif_ent != null) {
            mappedEntities.add(classif_ent);
          }
          return mappedEntities.iterator();
        }
      }
      // Aca podriamos llamar a la API de algun LLM
      EntityFactory factory = EntityFactoryProducer.getFactory("OTHER");
      classif_ent = registry.updateOrCreateEntity(tuple._1(), tuple._2(), Arrays.asList("OTHER"), factory);
      if (classif_ent != null) {
        mappedEntities.add(classif_ent);
      }
      return mappedEntities.iterator();
    });

    namedEntitiesRDD = namedEntitiesRDD.persist(StorageLevel.MEMORY_AND_DISK());

    return namedEntitiesRDD;
  }
}
