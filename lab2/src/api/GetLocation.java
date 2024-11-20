package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class GetLocation {
  // Deberia esconder esto pero bueno no es tan importante
  private static final String GEOCODE_API = "664f9283a62b7064460444otgc309bd";

  static String requestGeocode(String location)
      throws InterruptedException, IOException, Exception {

    String url_string = String.format("https://geocode.maps.co/search?q=%s&format=xml&api_key=%s", location,
        GEOCODE_API);

    URL url = new URL(url_string);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("GET");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("User-agent", "paradogmen");
    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);
    return request(connection);

  }

  public static double[] GetLatLon(String location)
      throws InterruptedException, IOException, Exception {
    // Devuelve un array donde el primer elemento es la latitud y el
    // segundo elemento es la longitud
    try {
      String xmlData = requestGeocode(location);
      double array[] = parseXMLgeocode(xmlData);
      // La API nos deja una request por segundo
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      return array;
    } catch (Exception e) {
      throw e;
    }
  }

  static double[] parseXMLgeocode(String input)
      throws IOException, Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new InputSource(new StringReader(input)));
    doc.getDocumentElement().normalize();
    NodeList placeList = doc.getElementsByTagName("place");
    Element place = (Element) placeList.item(0);
    double lat = Float.parseFloat(place.getAttribute("lat"));
    double lon = Float.parseFloat(place.getAttribute("lon"));
    double array[] = { lat, lon };
    return array;
  }

  static String request(HttpURLConnection connection) throws IOException, Exception {
    int status = connection.getResponseCode();
    if (status != 200) {
      throw new Exception("HTTP error code: " + status);
    } else {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
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
