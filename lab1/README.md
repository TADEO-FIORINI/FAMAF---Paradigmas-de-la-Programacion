---
title: Laboratorio de Funcional
author: Yamil Ali, Tadeo Fiorini, Gregorio Vilardo
---
La consigna del laboratorio está en https://tinyurl.com/funcional-2024-famaf

# 1. Tareas
Pueden usar esta checklist para indicar el avance.

## Verificación de que pueden hacer las cosas.
- [x] Haskell instalado y testeos provistos funcionando. (En Install.md están las instrucciones para instalar.)

## 1.1. Lenguaje
- [x] Módulo `Dibujo.hs` con el tipo `Dibujo` y combinadores. Puntos 1 a 3 de la consigna.
- [x] Definición de funciones (esquemas) para la manipulación de dibujos.
- [x] Módulo `Pred.hs`. Punto extra si definen predicados para transformaciones innecesarias (por ejemplo, espejar dos veces es la identidad).

## 1.2. Interpretación geométrica
- [x] Módulo `Interp.hs`.

## 1.3. Expresión artística (Utilizar el lenguaje)
- [x] El dibujo de `Dibujos/Feo.hs` se ve lindo.
- [x] Módulo `Dibujos/Grilla.hs`.
- [x] Módulo `Dibujos/Escher.hs`.
- [x] Listado de dibujos en `Main.hs`.

## 1.4 Tests
- [x] Tests para `Dibujo.hs`.
- [x] Tests para `Pred.hs`.

# 2. Experiencia
Nuestra experiencia en este laboratorio fue en general positiva, si bien en un principio tuvimos que acostumbranos a un lenguaje que no solemos utilizar mucho, junto con los materiales provistos y en particular leyendo el paper de Henderson, pudimos ir completando las distintas consignas satisfactoriamente.
 Del módulo Dibujo, quizás el que mas nos costó fue la Grilla, donde tuvimos que investigar sobre Gloss y decidir de que forma la implementariamos, pero al final, logramos implementarla.

Otro trabajo a destacar fue el de realizar los test's, ya que se nos sugeria utilizar GitHub Copilot para esta tarea, pero nosotros decidimos prescindir de esto y realizarlos sin dicha herramienta, lo que por un lado quizás nos hizo la tarea de realizar los test's un poco mas difícil, pero por otro nos sirvió para entender mejor como realizar test's y el código en general.

# 3. Preguntas
Al responder tranformar cada pregunta en una subsección para que sea más fácil de leer.

  1. ¿Por qué están separadas las funcionalidades en los módulos indicados? Explicar detalladamente la responsabilidad de cada módulo.

Se separan las funcionalidades de los módulos para una mejor comprensión y legibilidad del código.

Módulo Dibujo: en este módulo se define el tipo de dato y sus constructores y también las funciones/operaciones que se pueden realizar sobre un dibujo.

Módulo FloatingPic: se definen operaciones sobre/con vectores y la grilla.

Módulo Interp: define mediante funciones la interpretación de los dibujos para poder utilizarlos.

Módulo Pred: define predicados sobre dibujos y figuras.

Módulo Feo, Grilla, Escher, SuperRotar: aquí se implementan funciones para mostrar por pantalla los dibujos.

Módulo TestDibujo, TestPred: diferentes test's para corroborar la correcta funcionalidad de los módulos Dibujo y Pred.

  2. ¿Por qué las figuras básicas no están incluidas en la definición del lenguaje, y en vez de eso, es un parámetro del tipo?

Para poder definir y utilizar figuras básicas por nuestra cuenta, sin tener que depender de figuras que ya vengan incluidas en la definición, ya que esto nos limitaria.

  3. ¿Qué ventaja tiene utilizar una función de fold sobre hacer pattern-matching directo?
fold nos provee a nosotros varias ventajas sobre el pattern-matching, como por ejemplo la reutilización de código, y un código mas legible que al hacer un pattern-matching.
 También pattern-matching hace que tengamos que definir cada caso, esto quizás en un principio puede parecer mas fácil de entender pero en un código grande, como se menciono, perdemos legibilidad y eficiencia.

  4. ¿Cuál es la diferencia entre los predicados definidos en Pred.hs y los tests?
En Pred.hs se definen funciones y predicados generales, que luego uno en TestPred, testea que esas funciones funcionen correctamente mediante predicados puntuales.

# 4. Extras

* Se añade la funcion rotarA que rota una FloatingPic en alpha grados, de modo que rotarA 90 d == rotar d 
  (rotarA 45 d /= rot45 d)

Predicados agregados para cuando, en una construccion, aparece alguna de las secuencias:
- Rotar (Rotar (Rotar (Rotar _)))
- Espejar (Espejar _)
- una secuencia de RotarA x (...) en la que la suma de los x es o excede 360 
  (en esta secuencia tambien consideramos los rotar que suman 90)
  * p. ej. Rotar (RotarA 180 (Rotar d)) = d 
  * p. ej Encimar (RotarA 180 (RotarA 200 (Figura 'A'))) (RotarA 180 (Figura 'B')) es redundante
  * p. ej Encimar (RotarA 180 (RotarA 20 (Figura 'A'))) (RotarA 180 (Figura 'B')) NO es redundante

Aprovechando esta idea, para todos estos predicados se implementa una funcion para simplificar las construcciones, cambiando todas estas secuencias redundantes detectadas por los predicados. p ej  
Rotar (RotarA 180 (RotarA 100 (Figura 'A'))) se modifica a (RotarA 10 (Figura 'A'))

Se añade el dibujo fractal SuperRotar, en el que se pretende rotar una figura n veces
en un angulo de 360/n, por lo que 1 <= n <= 360
donde el dibujo es simetrico si y solo si n es divisor de 360
