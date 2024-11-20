module Dibujos.SuperRotar where

import Dibujo
import Graphics.Gloss

import FloatingPic

import qualified Graphics.Gloss.Data.Point.Arithmetic as V

data Figura = Linea | TrianguloEq | TrianguloRec | Blanca

interp :: Output Figura
interp Linea x w y =
    line
        [ x V.+ (0.5 V.* w) V.+ (0.15 V.* y)
        , x V.+ (0.5 V.* w) V.+ (0.85 V.* y)
        ]
interp TrianguloEq x w y =
    line
        [ x V.+ (0.5 V.* w) V.+ (0.85 V.* y)
        , x V.+ (0.85 V.* w) V.+ (0.15 V.* y)
        , x V.+ (0.15 V.* w) V.+ (0.15 V.* y)
        , x V.+ (0.5 V.* w) V.+ (0.85 V.* y)
        ]
interp TrianguloRec x w y =
    line
        [ x V.+ (0.15 V.* w) V.+ (0.15 V.* y)
        , x V.+ (0.15 V.* w) V.+ (0.85 V.* y)
        , x V.+ (0.85 V.* w) V.+ (0.15 V.* y)
        , x V.+ (0.15 V.* w) V.+ (0.15 V.* y)
        ]
interp Blanca _ _ _ = blank

blanca :: Dibujo Figura
blanca = Figura Blanca

nRotsAlphaGrades :: Int -> Float -> Float -> Dibujo Figura -> Dibujo Figura
nRotsAlphaGrades 1 a _ d = rotarA a d
nRotsAlphaGrades n a aR d = encimar (rotarA a d) (nRotsAlphaGrades (n - 1) (a + aR) aR d)

nRots :: Int -> Dibujo Figura -> Dibujo Figura
nRots n d = if 0 < n && n <= 360 then nRotsAlphaGrades n 0 aR d else blanca
  where
    aR = fromIntegral (360 `div` n)

-- los dibujos son simetros si n es divisor de 360
-- a es el angulo donde comienza a rotar
superRotar :: Int -> Figura -> Dibujo Figura
superRotar n f = nRots n d
  where
    d = figura f

testAll :: Dibujo Figura
testAll =
    -- superRotar 10 Linea
    -- superRotar 10 TrianguloEq
    -- superRotar 10 TrianguloRec
    encimar (superRotar 10 Linea) (superRotar 10 TrianguloEq)

superRotarConf :: Conf
superRotarConf =
    Conf
        { name = "SuperRotar"
        , pic = testAll
        , bas = interp
        }
