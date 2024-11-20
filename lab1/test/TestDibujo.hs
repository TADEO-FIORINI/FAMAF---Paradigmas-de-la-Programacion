module Main (main) where

import Control.Monad (when)
import Debug.Trace (traceShow)
import Dibujo
import System.Exit
import Test.HUnit (
    Counts (errors, failures),
    Test (..),
    assertBool,
    assertEqual,
    runTestTT,
 )

-- Caso de prueba para verificar que la función `figura` funciona correctamente.
testFigura :: Test
testFigura = TestCase $ do
    let dib = 'A'
    assertEqual "figura" (Figura dib) (figura dib)

-- Caso de prueba para verificar que la función `rotar` funciona correctamente.
testRotar :: Test
testRotar = TestCase $ do
    let dib = Figura 'A'
    assertEqual "rotar" (Rotar dib) (rotar dib)

-- Caso de prueba para verificar que la función `rotar` funciona correctamente.
testRotarA :: Test
testRotarA = TestCase $ do
    let dib = Figura 'A'
    assertEqual "rotarA" (RotarA 1 dib) (rotarA 1 dib)

-- Caso de prueba para verificar que la función `espejar` funciona correctamente.
testEspejar :: Test
testEspejar = TestCase $ do
    let dib = Figura 'A'
    assertEqual "espejar" (Espejar dib) (espejar dib)

-- Caso de prueba para verificar que la función `rot45` funciona correctamente.
testRot45 :: Test
testRot45 = TestCase $ do
    let dib = Figura 'A'
    assertEqual "rot45" (Rot45 dib) (rot45 dib)

-- Caso de prueba para verificar que la función `apilar` funciona correctamente.
testApilar :: Test
testApilar = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
    assertEqual "apilar" (Apilar 1 1 dib1 dib2) (apilar 1 1 dib1 dib2)

-- Caso de prueba para verificar que la función `juntar` funciona correctamente.
testJuntar :: Test
testJuntar = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
    assertEqual "juntar" (Juntar 1 1 dib1 dib2) (juntar 1 1 dib1 dib2)

-- Caso de prueba para verificar que la función `encimar` funciona correctamente.
testEncimar :: Test
testEncimar = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
    assertEqual "encimar" (Encimar dib1 dib2) (encimar dib1 dib2)

-- Caso de prueba para verificar que la función `(^^^)` funciona correctamente.
testSuperpone :: Test
testSuperpone = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
    assertEqual "(^^^)" (Encimar dib1 dib2) ((^^^) dib1 dib2)

-- Caso de prueba para verificar que la función `(.-.)` funciona correctamente.
testApila1_1 :: Test
testApila1_1 = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
    assertEqual "(.-.)" (Apilar 1 1 dib1 dib2) ((.-.) dib1 dib2)

-- Caso de prueba para verificar que la función `(///)` funciona correctamente.
testPoneAlLado :: Test
testPoneAlLado = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
    assertEqual "(///)" (Juntar 1 1 dib1 dib2) ((///) dib1 dib2)

-- Caso de prueba para verificar que la función `r90` funciona correctamente.
testR90 :: Test
testR90 = TestCase $ do
    let dib = Figura 'A'
    assertEqual "r90" (Rotar dib) (r90 dib)

-- Caso de prueba para verificar que la función `r180` funciona correctamente.
testR180 :: Test
testR180 = TestCase $ do
    let dib = Figura 'A'
    assertEqual "r180" (Rotar (Rotar dib)) (r180 dib)

-- Caso de prueba para verificar que la función `r270` funciona correctamente.
testR270 :: Test
testR270 = TestCase $ do
    let dib = Figura 'A'
    assertEqual "r270" (Rotar (Rotar (Rotar dib))) (r270 dib)

-- Caso de prueba para verificar que la función `encimar4` funciona correctamente.
testEncimar4 :: Test
testEncimar4 = TestCase $ do
    let dib = Figura 'A'
    assertEqual
        "encimar4"
        ( Encimar
            (Encimar (Espejar (Rot45 dib)) (r90 (Espejar (Rot45 dib))))
            (Encimar (r90 (r90 (Espejar (Rot45 dib)))) (r90 (r90 (r90 (Espejar (Rot45 dib))))))
        )
        (encimar4 dib)

-- Caso de prueba para verificar que la función `cuarteto` funciona correctamente.
testCuarteto :: Test
testCuarteto = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
        dib3 = Figura 'C'
        dib4 = Figura 'D'
    assertEqual "cuarteto" ((.-.) ((///) dib1 dib2) ((///) dib3 dib4)) (cuarteto dib1 dib2 dib3 dib4)

-- Caso de prueba para verificar que la función `cuarteto` funciona correctamente.
testCiclar :: Test
testCiclar = TestCase $ do
    let dib = Figura 'A'
    assertEqual "ciclar" (cuarteto dib (r90 dib) (r180 dib) (r270 dib)) (ciclar dib)

-- Caso de prueba para verificar que la función `mapDib` funciona correctamente.
testMapDib :: Test
testMapDib = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Figura 'B'
        f c = Figura [c, c]
    assertEqual "mapDib" (Figura "AA") (mapDib f dib1)
    assertEqual "mapDib" (Encimar (Figura "AA") (Figura "BB")) (mapDib f (Encimar dib1 dib2))

-- Caso de prueba para verificar que la función `foldDib` funciona correctamente con cada constructor.
testFoldDib :: Test
testFoldDib = TestCase $ do
    let dib1 = Figura 'A'
        dib2 = Rotar (Figura 'B')
        dib3 = RotarA 1 (Figura 'C')
        dib4 = Espejar (Figura 'D')
        dib5 = Rot45 (Figura 'E')
        dib6 = apilar 1 1 (Figura 'F') (Figura 'G')
        dib7 = juntar 1 1 (Figura 'H') (Figura 'I')
        dib8 = encimar (Figura 'J') (Figura 'K')

        fFig _ = "fFig"
        fRot _ = "fRot"
        frotA _ _ = "frotA"
        fEsp _ = "fEsp"
        fRot45 _ = "fRot45"
        fAp _ _ _ _ = "fAp"
        fJun _ _ _ _ = "fJun"
        fEnc _ _ = "fEnc"

    assertEqual "FoldDib Figura" "fFig" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib1)
    assertEqual "FoldDib Rotar" "fRot" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib2)
    assertEqual "FoldDib Rotar_a" "frotA" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib3)
    assertEqual "FoldDib Espejar" "fEsp" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib4)
    assertEqual "FoldDib Rot45" "fRot45" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib5)
    assertEqual "FoldDib Apilar" "fAp" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib6)
    assertEqual "FoldDib Juntar" "fJun" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib7)
    assertEqual "FoldDib Encimar" "fEnc" (foldDib fFig fRot frotA fEsp fRot45 fAp fJun fEnc dib8)

-- Caso de prueba para verificar que la función `figuras` devuelve las figuras básicas de un dibujo.
testFiguras :: Test
testFiguras = TestCase $ do
    let dib1 = Apilar 1 1 (Figura 'A') (Figura 'B')
    let dib2 = Encimar (Encimar (Rotar (Figura 'A')) (Figura 'B')) (Juntar 1 1 (Figura 'C') (Rot45 (Figura 'D')))
    assertEqual "Figuras en un dibujo" ['A', 'B'] (figuras dib1)
    assertEqual "Figuras en un dibujo" ['A', 'B', 'C', 'D'] (figuras dib2)


-- Caso de prueba para verificar que la función `simplificarDibujo` funciona correctamente.
testSimplificarDibujo :: Test
testSimplificarDibujo =
    TestList
        [ TestCase $ do
            let dib1 = Rot45 (Espejar (Espejar (Figura 'A')))
            assertEqual "simplificarDibujo 1" (Rot45 (Figura 'A')) (simplificarDibujo dib1)
        , TestCase $ do
            let dib2 = Rotar (Rotar (Espejar (Espejar (Rotar (Rotar (Figura 'A'))))))
            assertEqual "simplificarDibujo 2" (Figura 'A') (simplificarDibujo dib2)
        , TestCase $ do
            let dib3 = Encimar (Rotar (Rotar (Espejar (Espejar (Rotar (Rotar (Figura 'A'))))))) (rot45 (Espejar (Espejar (Figura 'B'))))
            assertEqual "simplificarDibujo 3" (Encimar (Figura 'A') (Rot45 (Figura 'B'))) (simplificarDibujo dib3)
        , TestCase $ do
            let dib4 = Rotar (RotarA 180 (RotarA 100 (Figura 'A'))) -- esto es lo mismo que rotarA 370 osea rotarA 10
            assertEqual "simplificarDibujo 4" (RotarA 10 (Figura 'A')) (simplificarDibujo dib4)
        , TestCase $ do
            let dib5 = Rotar (Rotar (RotarA 90 (Rotar (Figura 'A')))) -- esto es 360 == 0 grados
            assertEqual "simplificarDibujo 5" (Figura 'A') (simplificarDibujo dib5)
        , TestCase $ do
            let dib6 = Encimar (RotarA 180 (Rotar (Figura 'A'))) (RotarA 180 (Figura 'B')) -- esto no se puede simplificar
            assertEqual "simplificarDibujo 6" (Encimar (RotarA 270 (Figura 'A')) (RotarA 180 (Figura 'B'))) (simplificarDibujo dib6)
        , TestCase $ do
            let dib7 = Encimar (Figura 'A') (Figura 'A')
            assertEqual "simplificarDibujo 7" (Figura 'A') (simplificarDibujo dib7)
        ]

-- Lista de todos los casos de prueba.
tests :: Test
tests =
    TestList
        [ TestLabel "testFigura" testFigura
        , TestLabel "testRotar" testRotar
        , TestLabel "testRotarA" testRotarA
        , TestLabel "testEspejar" testEspejar
        , TestLabel "testRot45" testRot45
        , TestLabel "testApilar" testApilar
        , TestLabel "testJuntar" testJuntar
        , TestLabel "testEncimar" testEncimar
        , TestLabel "test(^^^)" testSuperpone
        , TestLabel "test(.-.)" testApila1_1
        , TestLabel "test(///)" testPoneAlLado
        , TestLabel "testR90" testR90
        , TestLabel "testR180" testR180
        , TestLabel "testR270" testR270
        , TestLabel "testEncimar4" testEncimar4
        , TestLabel "testCuarteto" testCuarteto
        , TestLabel "testCiclar" testCiclar
        , TestLabel "testMapDib" testMapDib
        , TestLabel "testFoldDib" testFoldDib
        , TestLabel "testFiguras" testFiguras
        , TestLabel "testSimplificarDibujo" testSimplificarDibujo
        ]

-- Ejecutar los casos de prueba.
main :: IO ()
main = do
    testCounts <- runTestTT tests
    when (errors testCounts /= 0 || failures testCounts /= 0) exitFailure
    exitSuccess

