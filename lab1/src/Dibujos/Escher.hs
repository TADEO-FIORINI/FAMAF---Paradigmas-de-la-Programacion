module Dibujos.Escher where

import Dibujo
import Graphics.Gloss

import FloatingPic

import qualified Graphics.Gloss.Data.Point.Arithmetic as V

data Fig = Triangulo | Blanca

interp :: Output Fig
interp Triangulo x w y = line [x, x V.+ y, x V.+ w, x]
interp Blanca _ _ _ = blank

-- Supongamos que eligen.
type Escher = Fig

fig :: Escher -> Dibujo Escher
fig f = figura f

trian2 :: Dibujo Escher -> Dibujo Escher
trian2 f = espejar $ rot45 f

trian3 :: Dibujo Escher -> Dibujo Escher
trian3 f = rotar $ rotar $ rotar $ trian2 f

-- El dibujo u.
dibujoU :: Dibujo Escher -> Dibujo Escher
dibujoU f = encimar (encimar (trian2 f) (rotar $ trian2 f)) (encimar (rotar $ rotar $ trian2 f) (rotar $ rotar $ rotar $ trian2 f))

-- El dibujo t.
dibujoT :: Dibujo Escher -> Dibujo Escher
dibujoT f = encimar f (encimar (trian2 f) (trian3 f))

blanca :: Dibujo Escher
blanca = fig Blanca

lado :: Int -> Dibujo Escher -> Dibujo Escher
lado 1 f = cuarteto blanca blanca (rotar $ dibujoT f) (dibujoT f)
lado n f = cuarteto (lado (n - 1) f) (lado (n - 1) f) (rotar $ dibujoT f) (dibujoT f)

-- Esquina con nivel de detalle en base a la figura p.
esquina :: Int -> Dibujo Escher -> Dibujo Escher
esquina 1 f = cuarteto blanca blanca blanca (dibujoU f)
esquina n f = cuarteto (esquina (n - 1) f) (lado (n - 1) f) (rotar $ lado (n - 1) f) (dibujoU f)

-- -- Por suerte no tenemos que poner el tipo!
noneto p q r s t u v w x =
    apilar
        1
        2
        (juntar 1 2 p (juntar 1 1 q r))
        ( apilar
            1
            1
            (juntar 1 2 s (juntar 1 1 t u))
            (juntar 1 2 v (juntar 1 1 w x))
        )

-- El dibujo de Escher:
escher :: Int -> Escher -> Dibujo Escher
escher n e =
    noneto
        (esquina n f)
        (lado n f)
        (rotar $ rotar $ rotar $ esquina n f)
        (rotar $ lado n f)
        (dibujoU f)
        (rotar $ rotar $ rotar $ lado n f)
        (rotar $ esquina n f)
        (rotar $ rotar $ lado n f)
        (rotar $ rotar $ esquina n f)
  where
    f = fig e

testAll :: Dibujo Escher
testAll = escher 3 Triangulo

escherConf :: Conf
escherConf =
    Conf
        { name = "Escher"
        , pic = testAll
        , bas = interp
        }
