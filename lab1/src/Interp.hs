module Interp (
    interp,
    initial,
)
where

import Dibujo
import FloatingPic
import Graphics.Gloss (Display (InWindow), Picture, Vector, color, display, makeColorI, pictures, translate, white, rotate)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V
import FloatingPic (FloatingPic)
import Data.Fixed (mod')

-- Dada una computación que construye una configuración, mostramos por
-- pantalla la figura de la misma de acuerdo a la interpretación para
-- las figuras básicas. Permitimos una computación para poder leer
-- archivos, tomar argumentos, etc.
initial :: Conf -> Float -> IO ()
initial (Conf n dib intBas) size = display win white $ withGrid fig size
  where
    win = InWindow n (ceiling size, ceiling size) (0, 0)
    fig = interp intBas dib (0, 0) (size, 0) (0, size)
    desp = -(size / 2)
    withGrid p x = translate desp desp $ pictures [p, color grey $ grid (ceiling $ size / 10) (0, 0) x 10]
    grey = makeColorI 100 100 100 100

-- Interpretación de (^^^)
ov :: Picture -> Picture -> Picture
ov p q = pictures [p, q]

-- Interpretación de (rot45)
r45 :: FloatingPic -> FloatingPic
r45 f d w h = f (d V.+ half (w V.+ h)) (half (w V.+ h)) (half (h V.- w))

-- Interpretación de (rotar))
rot :: FloatingPic -> FloatingPic
rot f d w h = f (d V.+ w) h (V.negate w)

-- Interpretación de (espejar)
esp :: FloatingPic -> FloatingPic
esp f d w h = f (d V.+ w) (V.negate w) h

-- Interpretación de (encimar)
sup :: FloatingPic -> FloatingPic -> FloatingPic
-- pictures es una funcion que combina dos imagenes
sup f g d w h = pictures [f d w h, g d w h]

-- Interpretación de (juntar)
jun :: Float -> Float -> FloatingPic -> FloatingPic -> FloatingPic
jun m n f g d w h = pictures [f d w' h, g (d V.+ w') (r' V.* w) h]
  where
    r' = n / (n + m)
    r = m / (n + m)
    w' = r V.* w

-- Interpretación de (apilar)
api :: Float -> Float -> FloatingPic -> FloatingPic -> FloatingPic
api m n f g d w h = pictures [f (d V.+ h') w (r V.* h), g d w h']
  where
    r' = n / (m + n)
    r = m / (m + n)
    h' = r' V.* h

-- Función para rotar una FloatingPic en alpha grados alrededor del eje x
--esto gira sobre la esquina inferior izquierda, asi que despues lo trasladamos al medio
rotA :: Float -> FloatingPic -> FloatingPic
rotA alpha f d w h =  translate dx dy $ rotate (-alpha) (translate (-dx) (-dy) (f d w h))
  where
    dx = ( fst w + fst h) / 2
    dy = ( snd w + snd h) / 2


-- Función de interpretación general
-- pasar de una interpretacion de figura basica a una de dibujo ?
interp :: Output a -> Output (Dibujo a)
interp interBas dib = foldDib interBas rot rotA esp r45 api jun sup dib
