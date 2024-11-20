package api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPerson {
  private static final String API_KEY = "AIzaSyCij7ruXmGyYqdEuI8qZ_Jnt5cKfBxz2dk";
  private static final String API_URL = "https://kgsearch.googleapis.com/v1/entities:search";

  public static JSONObject searchKnowledgeGraph(String query) throws IOException, Exception {
    // guardamos la URL en un string
    String urlString = String.format("%s?query=%s&key=%s&limit=10&indent=true", API_URL, query.replaceAll("\\s+", ""),
        API_KEY);
    // instanciamos la clase URL
    URL url = new URL(urlString);
    // abrimos la conexión
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("GET");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("User-Agent", "paradogmen");
    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);

    // hacemos la petición
    String jsonResponse = request(connection);

    JSONObject jsonObject = new JSONObject(jsonResponse);
    return jsonObject;
  }

  // método para extraer la ocupación de la respuesta JSON
  public static String extractOccupation(JSONObject jsonObject) {
    // obtenemos el JSONArray
    JSONArray itemListElement = jsonObject.getJSONArray("itemListElement");

    // iteremos sobre este
    for (int i = 0; i < itemListElement.length(); i++) {
      // buscamos el i-ésimo objeto JSON dentro del arreglo
      JSONObject entitySearchResult = itemListElement.getJSONObject(i);
      // buscamos que hay en el campo result
      JSONObject result = entitySearchResult.getJSONObject("result");

      if (result.has("description")) {
        return result.getString("description");
      }
    }
    return null; // si no se encontró el campo "description"

  }

  static String request(HttpURLConnection connection) throws IOException, Exception {
    int status = connection.getResponseCode();
    if (status != 200) {
      throw new Exception("HTTP error code: " + status);
    } else {
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuffer content = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      connection.disconnect();
      return content.toString();
    }
  }
}
