---
title: Laboratorio de Programación Orientada a Objetos
author: Ali Yamil, Fiorini Niño Tadeo, Vilardo Gregorio 
---

El enunciado del laboratorio se encuentra en [este link](https://docs.google.com/document/d/1wLhuEOjhdLwgZ4rlW0AftgKD4QIPPx37Dzs--P1gIU4/edit#heading=h.xe9t6iq9fo58).

# 1. Tareas
- Completamos la clase Config con todos los parametros que puede definir el usuario

- Costruimos el objeto de la clase Config con los argumentos dados por el usuario

- Teniendo en cuenta los atributos del objeto de la clase Config, se desarrolla el esqueleto logico de la clase App,
  para que la semantica del programa se corresponda al enunciado segun los argumentos que pase el usuario  

- Se completa la clase Article con los atributos que representan las partes de un artículo de un feed

- Se implmenta el método parseXML de la clase FeedParser:

    1.  Creación de la lista de artículos (List<Article> articles = new ArrayList<>();):
        Se declara una lista vacía de objetos Article llamada articles.

    2.  Parseo del XML:
        Se utiliza la fábrica de constructores de documentos (DocumentBuilderFactory) para obtener una instancia.
        Se crea un constructor de documentos (DocumentBuilder) a partir de la fábrica.
        Se parsea el XML representado como una cadena y se obtiene un documento XML (Document) utilizando InputSource.

    3.  Normalización del documento (doc.getDocumentElement().normalize();):
        Se normaliza el documento XML para garantizar que esté estructurado de manera coherente y uniforme.

    4.  Obtención de los nodos de elementos (NodeList itemNodeList = doc.getElementsByTagName("item");):
        Se obtiene una lista de nodos (NodeList) que representan los elementos <item> del XML.

    5.  Se itera sobre la lista de nodos de elementos <item>.
        Para cada elemento <item>, se convierte a un elemento Element.
        Se extraen los valores de los elementos hijos de <item> como título, descripción, fecha de publicación y enlace utilizando el método getElementValue.

    6.  Creación de objetos Article:
        Se crea un nuevo objeto Article con los valores obtenidos del elemento <item>.
        Se añade el artículo recién creado a la lista de artículos (articles.add(article)).

    7.  Manejo de excepciones (catch (Exception e) { e.printStackTrace(); }):
        Se capturan y manejan cualquier excepción que pueda ocurrir durante el proceso de parseo del XML. En este caso, se imprime la traza de la excepción.

    8.  Se devuelve la lista de artículos una vez que se han parseado todos los elementos <item> del XML.

    El método getElementValue obtiene el contenido de un elemento dado su nombre de etiqueta (tagName)  dentro de un elemento Element. Si no se encuentra ningún elemento con ese nombre de etiqueta, devuelve una cadena vacía. Este método se utiliza para extraer los valores de los elementos <title>, <description>, <pubDate> y <link> dentro de cada elemento <item>.

- Utilizando los metodos de la clase FeedParser, los feeds se procesan correctamente 
  (obtenemos una lista de objetos de la clase Article)
  Tambien se pueden imprimir los feeds procesados haciendo un print de cada Article de la lista

- Extraemos todas las palabras de la lista de articulos a un solo String a traves de un nuevo metodo de FeedParser
  y Computamos las entidades nombradas segun la heuristica seleccionada por el usuario 

- Se agrega el arhivo DictData.java, que nos permite obtener los datos en el .json provisto.
- Se crea el archivo Classifier.java, que se usa para clasificar las entidades nombradas, en forma de lista.
- Se utiliza un patrón de diseño **Factory** y **Registry**, estos estan explicados en las secciones que siguen.
- Se crea un archivo Stats.java que nos sirve para computar las estadísticas.
- Se modifica el archivo App.java a conveniencia y siguiendo decisiones de diseño que tomamos.
- Se intentó documentar el código lo máximo posible con comentarios, intentando que estos sean lo mas explicativos posible.



## Verificación de que pueden hacer las cosas.
- [x] Java 17 instalado. Deben poder compilar con `make` y correr con `make run` para obtener el mensaje de ayuda del programa.

## 1.1. Interfaz de usuario
- [x] Estructurar opciones
- [x] Construir el objeto de clase `Config`

## 1.2. FeedParser
- [x] `class Article`
    - [x] Atributos
    - [x] Constructor
    - [x] Método `print`
    - [x] _Accessors_
- [x] `parseXML`

## 1.3. Entidades nombradas
- [x] Pensar estructura y validarla con el docente
- [x] Implementarla
- [x] Extracción
    - [x] Implementación de heurísticas
- [x] Clasificación
    - [x] Por tópicos
    - [x] Por categorías
- Estadísticas
    - [x] Por tópicos
    - [x] Por categorías
    - [x] Impresión de estadísticas

## 1.4 Limpieza de código
- [x] Pasar un formateador de código
- [x] Revisar TODOs

# 2. Experiencia
En cuanto a la experiencia, y específicamente la experiencia con la POO, creemos que pudimos adaptarnos correctamente a este nuevo paradigma que estamos estudiando. El trabajo de laboratorio fue interesante y desafiante, lo cual nos motivo a realizarlo y a intentar comprender este paradigma y el lenguaje Java en particular.
En lo que refiere trabajo grupal, pudimos desenvolvernos de manera eficaz, dividiendo las tareas a realizar, adaptandonos a los tiempos de cada uno, además, nos mantuvimos en comunicación permanente, ya que esto nos parece clave para entender lo que cada uno implementó, para entender mejor las cosas y para despejarnos dudas entre nosotros.

# 3. Preguntas
1. Explicar brevemente la estructura de datos elegida para las entidades nombradas.

La estructura de datos para las entidades nombradas fue crear una clase abstracta llamada `NamedEntity` la cual heredan nuestras entidades
que surgen de las categorías. Esto nos pareció bastante intuitivo, ya que no podemos instanciar una NamedEntity, sino que tenemos que instanciar algunas de sus subclases como Person, Location, Organization o Other. 
Creemos que esta estructura nos ayudó después a poder darle a cada subclase sus atributos particulares. Algo que nos permitió hacer esto más simple fueron los patrones de
diseño que implementamos.
Implementamos el patrón de diseño **Factory** para generar instancias de las entidades nombradas.
Elegimos este patrón de diseño que, si bien aumenta relativamente la complejidad del código también nos permite separar la lógica para la generación de cada subclase lo cual nos permite generar entidades más completas, así por ejemplo cuando generamos una instancia de Location podemos en su fábrica implementar la lógica para obtener las coordenadas. Esto vuelve el código mucho más escalable, ya que podemos agregar funcionalidades específicas para cada entidad en sus fábricas sin modificar otras partes del código.
Tambien implementamos el patrón de diseño **Registry** el cual se complementa con la Factory, ya que nos permite pasarle la fábrica que obtenemos mediante la categoría de la entidad, pero sin nosotros saber específicamente cual se pasa, simplemente pasamos una fábrica de alguna entidad cualquiera y el registry la usa para generar un objeto o actualizarlo
en el caso de que esta ya exista, la actualización en este caso consta de sumar el contador de menciones.

2. Explicar brevemente cómo se implementaron las heurísticas de extracción.

- A continuacion mostramos las heuristicas disponibles
        
    Heuristica: CapitalizedWordHeuristic

        Descripcion:
        - suprime algunos caracteres como puntos, dos puntos, comas, signos mas y menos, etc
        - detecta secuencias de palabras que comienzan con mayuscula

        Ejemplos: 
          input <-  El General José Francisco de San Martín y Matorras  
                  _ El Día de la Diversidad Cultural 
                  _ Juan vive en Cordoba
                  _ había jugado Fulano. Mengano hizo el gol

          output <- El General Jose Francisco
                  , San Martin
                  , Matorras
                  , El Dia
                  , Diversidad Cultural
                  , Juan
                  , Cordoba
                  , Fulano Mengano  

    Heuristica: CapitalizedWordHeuristicWithExclusions
            
        Descripcion:
        - en vez de suprimir puntos, los cambia por un espacio (soluciona casos como el "Fulano Mengano")
        - detecta secuencias de palabras que comienzan con mayuscula y no estan en una lista de exclusiones

        Ejemplos: 
          input <- El General José Francisco de San Martín y Matorras  
                 _ El Día de la Diversidad Cultural 
                 _ Juan vive en Cordoba
                 _ había jugado Fulano, Mengano hizo el gol

          exclusiones <- El, Ambas

          output <- General Jose Francisco
                  , San Martin
                  , Matorras
                  , Dia
                  , Diversidad Cultural
                  , Juan
                  , Cordoba
                  , Fulano
                  , Mengano

    Heuristica: CapitalizedWordHeuristicWithExclusionsAndConectors

        Descripcion:
          - como CapitalizedWordHeuristicWithExclusions pero con indefinidas palabras conectoras entre
            cada palabra que comienza con mayuscula y no esta en la lista de exclusiones

        Ejemplos: 
           input <- El General José Francisco de San Martín y Matorras  
                 _ El Día de la Diversidad Cultural 
                 _ Juan vive en Cordoba
                 _ había jugado Fulano, Mengano hizo el gol

          exclusiones <- El, Ambas

          conectores <- de, y, de, la

          output <- General Jose Francisco de San Martin y Matorras
                  , Dia de la Diversidad Cultural
                  , Juan
                  , Cordoba
                  , Fulano
                  , Mengano

# 4. Extras

1) Añadimos una llamada a la API Geocode para obtener las coordenadas de la entidad Location.

2) Añadimos una llamada a la API de Gemini (Google IA) para obtener una mejor clasificacion de las entidades, ya que la mayoria se clasificaba como Other. Esta parte se encuentra en una branch aparte llamada **llm**.

3) Añadimos una llamada a la API Knowledge Graph de Google Cloud, que nos permite clasificar mejor a las entidades de la clase Person, para poder obtener la ocupación de algunas entidades (no de todas, ya que la API no tiene varias de las entidades que se extraen).


Notar que estas 3 APIs son gratuitas por los que nos dan un limite de tiempo, y en el caso de Gemini y Knowledge Graph, de uso. Es por esto que pusimos un sleep de un segundo cada vez que llamamos a algunas de ellas, y esto hace que demore mucho mas en ejecutarse, sobre todo cuando usamos Gemini, es por esto que esta en otra branch.

