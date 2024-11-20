module Main (main) where

import Control.Monad (when)
import Dibujo
import Pred
import System.Exit (exitFailure, exitSuccess)
import Test.HUnit (
    Counts (errors, failures),
    Test (..),
    assertBool,
    assertEqual,
    runTestTT,
 )

-- Define algunos tipos de ejemplo para usar en las pruebas
data FiguraBasica = Rectangulo | Triangulo deriving (Eq, Show)

type DibujoBasico = Dibujo FiguraBasica

type DibujoChar = Dibujo Char

-- Predicados de ejemplo
esRectangulo :: Pred FiguraBasica
esRectangulo Rectangulo = True
esRectangulo _ = False

esTriangulo :: Pred FiguraBasica
esTriangulo Triangulo = True
esTriangulo _ = False

esCharA :: Pred Char
esCharA 'A' = True
esCharA _ = False

-- test que siempre dara True (solo tenemos triangulos y rectangulos) pero sirve para testear andP
esTrianguloORectangulo :: Pred FiguraBasica
esTrianguloORectangulo figu = esTriangulo figu || esRectangulo figu

-- Funciones de ejemplo para cambiar figuras
rotarFigura :: FiguraBasica -> DibujoBasico
rotarFigura x = Rotar (Figura x)

rotarFiguraChar45 :: Char -> DibujoChar
rotarFiguraChar45 x = Rot45 (Figura x)

espejarFigura :: FiguraBasica -> DibujoBasico
espejarFigura x = Espejar (Figura x)

apilarFigura :: FiguraBasica -> Dibujo FiguraBasica
apilarFigura x = Apilar 1 2 (Figura x) (Figura x)

-- Casos de prueba
-- Este test verifica que la funcion cambiar haga lo esperado, es decir, si se satsiface
-- un predicado, aplicar una funcion a la figura que lo satisface, si no, dejarla como esta
-- en el primer caso, tomamos un dibujo, y esperamos obtener la aplicacion de la funcion Rotar
-- a aquellas figuras que satisfagan el predicado de ser rectangulos, a las demas se las deja como estan
testCambiar :: Test
testCambiar = TestCase $ do
    let dibujoEntrada = Apilar 1 2 (Figura Rectangulo) (Juntar 1 1 (Figura Rectangulo) (Figura Triangulo))
    let dibujoEsperado = Apilar 1 2 (Rotar (Figura Rectangulo)) (Juntar 1 1 (Rotar (Figura Rectangulo)) (Figura Triangulo))
    let resultado = cambiar esRectangulo rotarFigura dibujoEntrada
    assertEqual "Cambiar Rectangulo" dibujoEsperado resultado

    let dibujoEntrada' = Apilar 1 2 (Figura Triangulo) (Juntar 1 1 (Figura Rectangulo) (Figura Rectangulo))
    let dibujoEsperado' = Apilar 1 2 (Espejar (Figura Triangulo)) (Juntar 1 1 (Figura Rectangulo) (Figura Rectangulo))
    let resultado' = cambiar esTriangulo espejarFigura dibujoEntrada'
    assertEqual "Cambiar Rectangulo" dibujoEsperado' resultado'

    let dibujoEntrada'' = Apilar 1 2 (Figura Rectangulo) (Juntar 1 1 (Figura Triangulo) (Figura Triangulo))
    let dibujoEsperado'' = Apilar 1 2 (Apilar 1 2 (Figura Rectangulo) (Figura Rectangulo)) (Juntar 1 1 (Figura Triangulo) (Figura Triangulo))
    let resultado'' = cambiar esRectangulo apilarFigura dibujoEntrada''
    assertEqual "Cambiar Figuras apiladas" dibujoEsperado'' resultado''

    let dibujoEntrada''' = Figura 'A'
    let dibujoEsperado''' = Rot45 dibujoEntrada'''
    let resultado''' = cambiar esCharA rotarFiguraChar45 dibujoEntrada'''
    assertEqual "Cambiar Char" dibujoEsperado''' resultado'''

testAnyDib :: Test
testAnyDib = TestCase $ do
    let dibujoEntrada = Apilar 1 2 (Figura Triangulo) (Juntar 1 2 (Figura Rectangulo) (Figura Rectangulo))
    let resultadoEsperado = True
    let resultadoObtenido = anyDib esTriangulo dibujoEntrada
    assertEqual "Alguna es un triangulo" resultadoEsperado resultadoObtenido

    let dibujoEntrada' = Apilar 1 2 (Figura Rectangulo) (Juntar 1 2 (Figura Rectangulo) (Figura Rectangulo))
    let resultadoEsperado' = False
    let resultadoObtenido' = anyDib esTriangulo dibujoEntrada'
    assertEqual "Ninguna es un triangulo" resultadoEsperado' resultadoObtenido'

testAllDib :: Test
testAllDib = TestCase $ do
    let dibujoEntrada = Apilar 1 2 (Figura Rectangulo) (Rot45 (Juntar 1 2 (Figura Rectangulo) (Figura Rectangulo)))
    let resultadoEsperado = True
    let resultadoObtenido = allDib esRectangulo dibujoEntrada
    assertEqual "Todas son rectangulos" resultadoEsperado resultadoObtenido

    let dibujoEntrada' = Apilar 1 2 (Figura Triangulo) (Rot45 (Juntar 1 2 (Figura Triangulo) (Figura Rectangulo)))
    let resultadoEsperado' = False
    let resultadoObtenido' = allDib esRectangulo dibujoEntrada'
    assertEqual "Todas son rectangulos" resultadoEsperado' resultadoObtenido'

testAndP :: Test
testAndP = TestCase $ do
    -- ambos pred se satisfacen, deberia dar True y pasar el test
    let figuraEntrada = Rectangulo
    let resultadoEsperado = True
    let resultadoObtenido = andP esRectangulo esTrianguloORectangulo figuraEntrada -- obvio, pero sirve para testear
    assertEqual "Se satisfacen ambos predicados" resultadoEsperado resultadoObtenido

    -- uno de los pred no se satisface, deberia dar False, y por ende pasar este test
    let figuraEntrada' = Rectangulo
    let resultadoEsperado' = False
    let resultadoObtenido' = andP esRectangulo esTriangulo figuraEntrada'
    assertEqual "Uno de los predicados no se satisface" resultadoEsperado' resultadoObtenido'

    -- ninguno de los pred se satisface, deberia dar False, y por ende pasar este test
    let figuraEntrada'' = Rectangulo
    let resultadoEsperado'' = False
    let resultadoObtenido'' = andP esTriangulo esTriangulo figuraEntrada''
    assertEqual "Ninguno de los predicados no se satisface" resultadoEsperado'' resultadoObtenido''

testOrP :: Test
testOrP = TestCase $ do
    -- cuando uno de los predicados se satisface, deberia dar True
    let figuraEntrada = Triangulo
    let resultadoEsperado = True
    let resultadoObtenido = orP esTriangulo esRectangulo figuraEntrada
    assertEqual "Uno de los predicados se satisface" resultadoEsperado resultadoObtenido

    -- Cuando ninguno de los predicados se satisface, deberia dar False, y por ende pasar este test
    let figuraEntrada' = Triangulo
    let resultadoEsperado' = False
    let resultadoObtenido' = orP esRectangulo esRectangulo figuraEntrada'
    assertEqual "Ninguno de los predicados se satisface" resultadoEsperado' resultadoObtenido'

-- Caso de prueba para verificar que la función `esRedundante` funciona correctamente.
-- Todos estos casos (menos el 5) son redundantes (deberian ser detectados como tales)
testEsRedundante :: Test
testEsRedundante =
    TestList
        [ TestCase $ do
            let dib1 = Rotar (rot45 (Espejar (Espejar (Figura 'A'))))
            assertBool "esRedundante 1" (esRedundante dib1)
        , TestCase $ do
            let dib2 = Encimar (Espejar (Rotar (Rotar (Rotar (Rotar (Figura 'B')))))) (Figura 'B')
            assertBool "esRedundante 2" (esRedundante dib2)
        , TestCase $ do
            let dib3 = RotarA 30 (RotarA 60 (RotarA 180 (RotarA 90 (Figura 'A'))))
            assertBool "esRedundante 3" (esRedundante dib3)
        , TestCase $ do
            let dib4 = RotarA 30 (RotarA 60 (RotarA 180 (Rotar (Figura 'A')))) -- esto es 360 == 0 grados
            assertBool "esRedundante 4" (esRedundante dib4)
        , TestCase $ do
            let dib5 = Encimar (RotarA 180 (RotarA 20 (Figura 'A'))) (RotarA 180 (Figura 'B')) -- esto NO es redundante
            assertBool "esRedundante 5" (not (esRedundante dib5)) -- verificamos que NO es redundante
        ]

-- Lista de todos los casos de prueba.
tests :: Test
tests =
    TestList
        [ TestLabel "testCambiar" testCambiar
        , TestLabel "testAnyDib" testAnyDib
        , TestLabel "testAllDib" testAllDib
        , TestLabel "testAndP" testAndP
        , TestLabel "testOrP" testOrP
        , TestLabel "testEsRedundante" testEsRedundante
        ]

-- Ejecutar los casos de prueba.
main :: IO ()
main = do
    testCounts <- runTestTT tests
    when (errors testCounts /= 0 || failures testCounts /= 0) exitFailure
    exitSuccess

