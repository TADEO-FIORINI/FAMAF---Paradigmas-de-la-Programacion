package feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FeedParser {

  // funcion para extraer una lista de articulos de una pagina xml
  public static List<Article> parseXML(String xmlData) {
    List<Article> articles = new ArrayList<>();
    try {
      // System.out.println(xmlData);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(xmlData)));
      doc.getDocumentElement().normalize();
      NodeList itemNodeList = doc.getElementsByTagName("item");

      for (int i = 0; i < itemNodeList.getLength(); i++) {
        Element itemElement = (Element) itemNodeList.item(i);
        String title = getElementValue(itemElement, "title");
        String description = getElementValue(itemElement, "description");
        String pubDate = getElementValue(itemElement, "pubDate");
        String link = getElementValue(itemElement, "link");

        Article article = new Article(title, description, pubDate, link);
        articles.add(article);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return articles;
  }

  // funcion auxiliar que transforma una lista de articulos en un string
  /*
   * solo usar para extraer entidades nombradas, ya que cada miembro del articulo
   * esta separado por un _ y eso no tiene sentido salvo para que las heuristicas
   * no se confundan tomando como nombre propio una secuencia de palabras de
   * diferentes miembros de un articulo, o inclusive de otro articulo
   */
  /*
   * si alguien tiene una mejor idea lo cambiamos
   */
  public static String wordsFromArticle(List<Article> allArticles) {
    String text = "";
    for (Article article : allArticles) {
      text = text + "_" + article.getTitle() + "_" + article.getDescription() +
          "_" + article.getPubDate() + "_" + article.getLink();
    }
    return text;
  }

  public static String fetchFeed(String feedURL)
      throws MalformedURLException, IOException, Exception {

    URL url = new URL(feedURL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("GET");
    connection.setRequestProperty("Content-Type", "application/json");

    // Si todos los grupos usan el mismo user-agent, el servidor puede bloquear
    // las solicitudes.
    connection.setRequestProperty("User-agent", "paradogmen");
    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);

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

  private static String getElementValue(Element element, String tagName) {
    NodeList nodeList = element.getElementsByTagName(tagName);
    if (nodeList != null && nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    } else {
      return "";
    }
  }
}
