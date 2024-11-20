package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParser {

  static public List<FeedsData> parseJsonFeedsData(String jsonFilePath) throws IOException {
    // jsonFilePath = filepath que luego le pasamos en App.java
    String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
    // declaaramos una lista que contendra los feeds
    List<FeedsData> feedsList = new ArrayList<>();

    // un json array que tiene la data de los feeds
    JSONArray jsonArray = new JSONArray(jsonData);
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i); // obtenemos los datos en cada iteración
      String label = jsonObject.getString("label"); // de los datos, extraemos cada "subdato" y lo guardamos en una
                                                    // variable
      String url = jsonObject.getString("url");
      String type = jsonObject.getString("type");
      feedsList.add(new FeedsData(label, url, type));
    }
    return feedsList;
  }

  static public List<DictData> parseJsonDictData(String jsonFilePath) throws IOException {
    String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
    List<DictData> dictList = new ArrayList<>();

    JSONArray jsonArray = new JSONArray(jsonData);
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject((i));
      String label = jsonObject.getString("label");
      String Category = jsonObject.getString("Category");

      // parseando topics
      // para cada String "topic" que esta en el topicsArray, lo agregamos a la
      // List<String> Topic
      JSONArray topicsArray = jsonObject.getJSONArray("Topics");
      List<String> Topics = new ArrayList<>();
      for (int j = 0; j < topicsArray.length(); j++) {
        Topics.add(topicsArray.getString(j));
      }
      // parsendo keywords
      JSONArray keywordsArray = jsonObject.getJSONArray("keywords");
      List<String> keywords = new ArrayList<>();
      for (int k = 0; k < keywordsArray.length(); k++) {
        keywords.add(keywordsArray.getString(k));
      }
      // agregamos los datos a la dictList
      dictList.add(new DictData(label, Category, Topics, keywords));
    }
    return dictList;
  }
}
