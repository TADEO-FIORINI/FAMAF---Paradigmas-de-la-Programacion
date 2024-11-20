# App de procesamiento de texto con calculo distribuido por Spark 3.5.1

## Abstract

En este trabajo de laboratorio vamos a estar experimentando con el concepto de la computación distribuida, que tiene como objetivo realizar cálculos/cómputos en varias máquinas. Esto es de mucha ayuda cuando estamos trabajando con BigData.

El concepto se basa en un clúster de máquinas distribuidas, identificando una máquina master que distribuirá el trabajo y k-máquinas workers que harán el trabajo y lo entregarán a la máquina master.

## Introducción

En el lab 2 teniamos una App que dados X feeds de diarios de Argentina, obtenia su pagina xml y la utilizaba para hacerse de los diferentes articulos del feed procesado. Luego podia extraer las entidades nombradas usando alguna heurística así como también clasificarlas segun categoría o tópico.

Pero nos planteemos que pasaria si tenemos un numero muy grande de feeds, de modo que nuestro programa secuencial tardaria horas, o incluso dias procesando datos. Una idea seria hacer el programa concurrente para la mayor cantidad de CPUs posible de la maquina. Sin embargo podemos tener muchisimos datos, siendo escalablemente un trabajo inviable para una sola maquina. De ahi surge la idea de distribuir el cálculo en varias maquinas. En este lab en particular la parte del programa que queremos distribuir es, en principio, el computo de entidades nombradas. Luego también fue distribuído la clasificacion de entidades por tópico y categoría.

## Especificaciones del proyecto

### Herramienta

En este laboratorio utilizamos Apache Spark, un framework que facilita la distribución de computo en K máquinas o servidores, a traves de su API para Java. Spark tiene una arquitectura de master-slave: una máquina master distribuye el calculo y los workers calculan. Sin embargo, como solo contamos con nuestra propia máquina, master y workers son implementados en forma local. En particular creamos un cluster Spark con dos (en principio) workers.

Apache Spark utiliza el RDD o Resilient Distributed DataSet, que es un multiset de ítems de datos distribuidos a lo largo de un clúster de máquinas.

### Preparación del entorno

Primero instalamos maven, una herramienta de gestion de proyectos muy utilizada en Java. En el contexto de cálculo distribuido con Spark, utilizamos maven para gestionar las dependencias del proyecto (bibliotecas de Spark) y compilar el código fuente. El archivo de configuración utilizado por Maven es pom.xml (Project Object Model) y se utiliza para definir el proyecto y listar sus dependencias, otras tareas relacionadas a la configuración del proyecto.

Luego descargamos Spark 3.5.1 y lo descomprimimos en un direcotirio DIR. El proximo paso es crear una variable de entorno SPARK_HOME con el contenido path/your/DIR, e.i. la ruta al directorio donde descomprimimos Spark. Configurar SPARK_HOME es clave para que las herramientas de Spark funcionen correctamente pues muchos comandos de Spark (como spark-submit) dependen de esta variable para encontrar el directorio de instalación de Spark.

### Compilación

Para compilar el proyecto ejecutamos ```./compile.sh```  (script de compilación)  

El script de compile.sh tiene una sola linea: ```mvn clean package```.

- ```mvn``` es el comando para ejecutar la herramienta maven

- ```clean``` elimina todos los archivos generador por compilaciones anteriores

- ```package``` crea un JAR que contiene el código y las dependencias necesarias para ejecutar la app con Spark

NOTA: Existe la posibilidad de que se deba usar ```sudo``` junto con el scrip para compilar el proyecto.

### Ejecución

Para ejecutar el proyecto ejecutamos ```./run.sh ARGS``` (script de ejecución) donde ARGS representa las flags del programa:

- por defecto trabajamos con el archivo de ejemplo de 1.57 GB 

Observación: el archivo era demasiado grande para agregarlo al repositorio, asi que deberán agregarlo por cuenta propia en la carpeta **resources** que se ubica en *src/main/resources*.

- el script genera un spark-env segun los workers que le especifiquemos, y luego corre spark-master y spark-worker.

- con el flag ```-f``` escribimos todos los feeds en un archivo y trabajamos con eso.

- con el flag ```-sf cat/topic``` elegimos el formato de estadistica, ya sea por categoría o tópico, escribiendo las entidades segun el formato. Sino utilizamos este flag se escriben las entidades recolectadas.

P.ej. ```./run.sh -ne CapitalizedWordHeuristic -f -sf cat``` clasifica las entidades nombradas de TODOS los feeds por categoria y ```./run.sh -ne CapitalizedWordHeuristic -sf cat```clasifica las entidades nombradas del archivo de 1.57 GB de ejemplo.

Tambien podemos ejecutarlo con local de la siguiente manera:

- ```$SPARK_HOME/bin/spark-submit --master "local[4]" target/group_37_lab3_2024-1.0-SNAPSHOT-jar-with-dependencies.jar -- FLAGS 2>/dev/null``` 

- n para la cantidad de threads.

- ```$SPARK_HOME/bin/spark-submit``` usa la variable de entorno SPARK_HOME para acceder a spark-submit, que es la herramienta de Apache Spark utilizada para enviar aplicaciones Spark al clúster de Spark para su ejecución.

- ```target/group_37_lab3_2024-1.0-SNAPSHOT-jar-with-dependencies.jar``` es el camino al archivo JAR que contiene la app Spark compilada con maven.

- ```-- FLAGS``` indica los flags con los que se desea configurar la app.

- ```2>/dev/null``` se usa para que toda la información relativa a Spark no sea visible en nuestra aplicación de consola.

## Implementaciones relativas al uso de Spark

### Creación de BigData

Implementamos una funcion llamada writeFeed que escribe en un archivo los articulos de todos los feeds. Llamamos a este archivo bigdata porque contiene toda la informacion necesaria que la app debe procesar para cumplir su proposito, ademas de porque es indeterminadamente grande. Este archivo es el objetivo que sera atacado por los workers y distribuido por el master.

El otro archivo de bigdata que utilizamos es el provisto por la cátedra (wiki_dump_parcial)

### Crear sesión de Spark

Creamos una configuración de Spark (SparkConf), llamando a nuestra aplicacion de Spark NamedEntities, y creando una sesion spark (SparkSession) con ella.

### Clasificación y Cómputo de Entidades Nombradas

Para efectivamente distribuir el calculo de entidades nombradas con Spark, modificamos las implementaciones de extractCandidates en cada heurística:

- en principio utilizamos la sesion de Spark para leer el archivo de bigdata y cargarlo en un RDD donde cada elemento RDD es una línea del archivo bigdata

- se usa flatMap en el RDD para aplicar la expresión regular a cada línea del archivo y si encuentra coincidencias con el patron definido los agrega a una lista

- finalmente recolectamos el iterador devuelto por flatMap que contiene nuestra lista de entidades nombradas

Observación: en nuestra implementación, al leer la bigdata, se muestran por pantalla las entidades y sus estadísticas, y además se crea una carpeta 'output' en el proyecto que también muestra las entidades.

El propósito de almacenar los datos en un RDD (Resilient Distributed Dataset) en vez de manipularlos directamente es que los datos en un RDD están distribuidos a lo largo de varios nodos de un cluster, lo que permite realizar operaciones paralelas y mejorar el rendimiento de las tareas de procesamiento de datos a gran escala.  

### Clasificaación de entidades nombradas

- Para clasificar las entidades nombradas primero las transformamos en una tupla y las guardamos en un JavaPairRDD<String, Integer> donde el Integer es un 1, luego reducimos por Key que en este caso es el nombre de la entidad y sumamos los unos para obtener la cantidad de veces que se nombra a la misma entidad.

- Una vez echo esto las clasificamos como en el lab 2 en Classifier cambiando un poco el codigo para que funcione con los RDD, ahora usamos un flatMap para retornar un JavaRDD\<NamedEntity> a partir de estas tuplas.

- Modificamos un poco el EntityRegistry y los constructores de las subclases de NamedEntity para que tambien se pueda especificar la cantidad de menciones al crear el objeto.

Hicimos este cambio en el registry porque había un overhead importante si sumabamos las menciones una vez creado el objeto, además de que había muchas repeticiones de entidades a pesar de usar un HashMap concurrente, con el cambio pudimos reducir a la mitad el tiempo que tomaba en ejecutarse. 

Realizamos también cambios en la clase `Stats` para poder generar una carpeta 'output' y poder guardar en un archivo las entidades nombradas y su clasificación y estadísticas, además de imprimir por pantalla estos mismos datos, esto se hace tanto para categoría como para tópicos.


## Mediciones

Para ver el rendimiento del calculo distribuido con Spark implementamos un temporizador. Este temporizador inicia justo antes de computar las entidades nombradas con Spark y se detiene justo despues de que el computo finaliza. El tiempo computando entidades se mide en milisegundos y, para que sea visible facilmente, es lo ultimo que muestra la app.

### Mediciones para la branch local:

local[1]:

medición 1 ----> Tiempo transcurrido: 80014 ms medición 2 ----> Tiempo transcurrido: 78252 ms Promedio: 79133 ms

local[2]:

medición 1 ----> Tiempo transcurrido: 44499 ms medición 2 ----> Tiempo transcurrido: 45957 ms Promedio: 45228 ms

local[4]:

medición 1 ----> Tiempo transcurrido: 29611 ms medición 2 ----> Tiempo transcurrido: 28936 ms Promedio: 29273 ms

### Mediciones para la branch main

1 worker:

medición 1 ----> Tiempo transcurrido: 79879 ms medición 2 ----> Tiempo transcurrido: 76357 ms Promedio: 78118 ms

2 worker:

medición 1 ----> Tiempo transcurrido: 46029 ms medición 2 ----> Tiempo transcurrido: 46487 ms Promedio: 46258 ms

4 worker:

medición 1 ----> Tiempo transcurrido: 33301 ms medición 2 ----> Tiempo transcurrido: 32670 ms Promedio: 32985 ms

## Discusión

En la branch local ejecutamos el proyecto utilizando spark-submit --master "local[X]". Luego, mirando la interfaz de spark en localhost, nos dimos cuenta que no habia ningun worker. Esta forma de ejecutar el proyecto esta inspirada en el repositorio de ejemplo brindado por la cátedra.

Optamos por dejar en la branch main la version en la que modificamos la forma de ejecutar el programa para solucionar este problema y, efectivamente, crear workers; y conservar la version anterior en una nueva branch local. Cabe destacar que, para la branch main, tuvimnos que modificar algunos detalles en la implementacion de las estadisticas, por ejemplo el metodo union() ya que no actuaba de manera deterministica.

Respecto a las mediciones, como podemos observar, parecen estar en el mismo rango en ambas versiones. Sin embargo es importante notar que esto no es necesariamente así en todos los casos. En particular, con las computadoras de los demas integrantes (que no registraron mediciones) funcionaba de manera esperada en la branch local, e.i. a mayor cantidad de hilos, mayor eficiencia. Pero en la branch main, al crear uno o mas workers se comportaba de manera impredecible, tardando incluso mas del doble de tiempo que el peor caso de la branch local, afectando también al funcionamiento general de la maquina, teniendo serias dificultades para ejecutar otros procesos como firefox o procesos del sistema operativo. En parte fue esto lo que nos inspiró a conservar la version local en una rama diferenciada, pero en todo caso sostenemos que esta cuestión tiene que ver con que este paradigma de cálculo distribuido esta diseñado para que cada worker se corresponda con una y solo una máquina, pudiendo ser extremadamente deficiente usar una distribución master-slave real en una sola máquina.

# Referencias
 
[Ejemplo repositorio de app Java distribuida con spark](https://bitbucket.org/paradigmas-programacion-famaf/spark-skeleton/src)

[Enunciado](https://sites.google.com/unc.edu.ar/paradigmas-2024/laboratorio?authuser=0)

[Documentación de Spark](https://spark.apache.org/docs/latest/spark-standalone.html)
