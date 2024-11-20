package app.namedEntities.heuristics;

import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

public class CapitalizedWordHeuristicWithExclusionsAndConectors implements HeuristicInterface {

  public static JavaRDD<String> extractCandidates(SparkSession spark, String fileBigData) {

    /*
     * La idea de esta heuristica es que ademas de una sucesion de palabras que
     * comienzan con mayuculas y no estan en
     * una lista de exclusiones, tambien haya conectores entre estas palabras. Esto
     * para que fechas como
     * el Dia Internacional de la Diversidad Cultural, o nombres como Manuel José
     * Joaquín del Corazón de Jesús Belgrano,
     * sean efectivamente considerados como un solo nombre propio
     */

    String articulos = "El|Los|La|Las";
    String dias = "Lunes|Martes|Miercoles|Jueves|Viernes|Sabado|Domingo";
    String meses = "|Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre";
    String interrogativas = "|Que|Quien|Quienes|Donde|Cuando|Por que|Como|Cual|Cuales|Cuanto|Cuanta|Cuantos|Cuantas";
    String preposiciones = "|A|Ante|Bajo|Con|Contra|De|Desde|Durante|En|Entre|Hacia|Hasta|Para|Por|Segun|Sin|Sobre|Tras";
    String pronombres = "|Yo|Tu|Usted|Ella|Nosotros|Nosotras|Vosotros|Vosotras|Ellos|Ellas|Ustedes|Me|Te|Se|Nos|Os|Este"
        + "|Esta|Esto|Ese|Esa|Eso|Aquel|Aquella|Aquello|Mi|Tu|Su|Nuestro|Nuestra|Vuestro|Vuestra|Alguien|Nadie|Todos|Todo"
        + "|Nada|Algo|Cualquiera|Un|Unos|Una|Unas|Hace|Hacia";
    String verbos = "|Tiene|Tienen|Comenzo";
    String otras = "|Uno|Si|No|Es|Esta|Luego|Entonces|Al|Para|Pero|Fue|Sigue|Sera|Reitero";

    String exclusiones = articulos + dias + meses + interrogativas + preposiciones
        + pronombres + verbos + otras;

    String conectores = "y|e|o|de|del|la|las|el|los|a|con|por|para";

    // convencion: P = palabra que comienza con mayuscula y no está en la lista de
    // exclusionies
    Pattern pattern = Pattern.compile(String.format(
        "(?!(?:%s)\\b)[A-Z][a-z]" // primera P
            + "+(?:(?!(?:%s)\\b)\\s[A-Z][a-z]+)*" // indefinida cantidad de Ps
            + "+(?:(?:\\s(?:%s)\\b)*+(?:\\s(?!(?:%s)\\b)[A-Z][a-z]+)+(?:\\s(?!(?:%s)\\b)[A-Z][a-z]+)*)*"
        // indefinida cantidad de conectores seguidos de al menos una P, todo indefinida
        // cantidad de veces
        , exclusiones, exclusiones, conectores, exclusiones, exclusiones));

    JavaRDD<String> lines = spark.read().textFile(fileBigData).javaRDD();
    JavaRDD<String> matchesRDD = lines.flatMap(text -> {
      text = text.replaceAll("[-+.^:\"]", " ");
      text = text.replaceAll(",", "");
      text = Normalizer.normalize(text, Normalizer.Form.NFD);
      text = text.replaceAll("\\p{M}", "");
      Matcher matcher = pattern.matcher(text);
      List<String> matches = new ArrayList<>();
      while (matcher.find()) {
        matches.add(matcher.group());
      }
      return matches.iterator();
    });
    return matchesRDD;
  }
}