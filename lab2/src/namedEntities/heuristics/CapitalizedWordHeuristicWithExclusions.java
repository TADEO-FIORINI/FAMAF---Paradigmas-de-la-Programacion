package namedEntities.heuristics;

import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapitalizedWordHeuristicWithExclusions implements HeuristicInterface {

  public static List<String> extractCandidates(String text) {

    /*
     * El objetivo de esta heuristica, ademas de detectar secuencias de palabras que
     * comienzan con mayuscula
     * y continuan con minusculas, es tener una lista de exclusiones, de modo que no
     * considera parte de la
     * secuencia del nombre propio ninguna palabra que este dentro de esa lista
     */

    List<String> candidates = new ArrayList<>();

    text = text.replaceAll("[-+.^:,\"]", " "); // un nombre propio no deberia tener estos signos internamente
    text = Normalizer.normalize(text, Normalizer.Form.NFD);
    text = text.replaceAll("\\p{M}", "");

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

    Pattern pattern = Pattern.compile(String.format(
        "(?!(?:%s)\\b)[A-Z][a-z]" // primer palabra con mayuscula sin contar exclusiones
            + "+(?:\\s(?!(?:%s)\\b)[A-Z][a-z]+)*" // indefinida cantidad de palabras con mayuscula sin contar
                                                  // exclusiones
        , exclusiones, exclusiones));

    Matcher matcher = pattern.matcher(text);

    while (matcher.find()) {
      candidates.add(matcher.group());
    }
    return candidates;
  }
}
