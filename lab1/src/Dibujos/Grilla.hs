module Dibujos.Grilla where

import Dibujo --(Dibujo, apilar, encimar, espejar, figura, juntar, rot45, rotar, rotarA, (///), r90)
import FloatingPic (Conf (..), Output, half, zero)
import Graphics.Gloss (Picture, arc, blue, circle, color, line, pictures, polygon, red, rotate, scale, translate)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V

import Debug.Trace

data Simbolos = ParentesisAbrir | ParentesisCerrar | Coma | Cero | Uno | Dos | Tres | Cuatro | Cinco | Seis | Siete
    deriving (Show, Eq)

type Tupla = Dibujo Simbolos -> Dibujo Simbolos -> Dibujo Simbolos -> Dibujo Simbolos -> Dibujo Simbolos -> Dibujo Simbolos

interp :: Output Simbolos
interp Cero x w y =
    pictures
        [ line [x V.+ (cc1 V.* y) V.+ (cc2 V.* w) | (cc1, cc2) <- zip [0.9, 0.8, 0.5, 0.2, 0.1, 0.2, 0.5, 0.8, 0.9] [0.5, 0.25, 0.15, 0.25, 0.5, 0.75, 0.85, 0.75, 0.5]]
        ]
interp Uno x w y =
    pictures
        [ line [x V.+ half w V.+ (0.1 V.* y), x V.+ half w V.+ (0.9 V.* y), x V.+ (0.75 V.* y) V.+ (0.25 V.* w)]
        ]
interp Dos x w y =
    pictures
        [ line
            [x V.+ (cc1 V.* y) V.+ (cc2 V.* w) | (cc1, cc2) <- zip [0.5, 0.8, 0.9, 0.8, 0.5] [0.85, 0.75, 0.5, 0.25, 0.15, 0.25]]
        , line
            [x V.+ (0.5 V.* y) V.+ (0.85 V.* w), x V.+ (0.1 V.* y) V.+ (0.1 V.* w), x V.+ (0.1 V.* y) V.+ (0.85 V.* w)]
        ]
interp Tres x w y =
    pictures
        [ line
            [ x V.+ (0.2 V.* w) V.+ (0.9 V.* y)
            , x V.+ (0.9 V.* w) V.+ (0.9 V.* y)
            , x V.+ (0.2 V.* w) V.+ (0.5 V.* y)
            , x V.+ (0.7 V.* w) V.+ (0.5 V.* y)
            , x V.+ (0.9 V.* w) V.+ (0.4 V.* y)
            , x V.+ (0.9 V.* w) V.+ (0.2 V.* y)
            , x V.+ (0.7 V.* w) V.+ (0.1 V.* y)
            , x V.+ (0.2 V.* w) V.+ (0.1 V.* y)
            ]
        ]
interp Cuatro x w y =
    pictures
        [ line [x V.+ (0.8 V.* w) V.+ (0.1 V.* y), x V.+ (0.8 V.* w) V.+ (0.9 V.* y)]
        , line [x V.+ (0.2 V.* w) V.+ (0.5 V.* y), x V.+ (0.4 V.* w) V.+ (0.9 V.* y)]
        , line [x V.+ (0.2 V.* w) V.+ (0.5 V.* y), x V.+ (0.8 V.* w) V.+ (0.5 V.* y)]
        ]
interp Cinco x w y =
    pictures
        [ line [x V.+ (0.2 V.* w) V.+ (0.5 V.* y), x V.+ (0.2 V.* w) V.+ (0.9 V.* y), x V.+ (0.8 V.* w) V.+ (0.9 V.* y)]
        , line [x V.+ (0.1 V.* y) V.+ (0.8 V.* w), x V.+ (0.1 V.* y) V.+ (0.2 V.* w)]
        , line [x V.+ (0.8 V.* w) V.+ (0.1 V.* y), x V.+ (0.8 V.* w) V.+ (0.5 V.* y)]
        , line [x V.+ (0.50 V.* y) V.+ (0.8 V.* w), x V.+ (0.50 V.* y) V.+ (0.2 V.* w)]
        ]
interp Seis x w y =
    pictures
        [ line [x V.+ (0.2 V.* w) V.+ (0.1 V.* y), x V.+ (0.2 V.* w) V.+ (0.9 V.* y), x V.+ (0.8 V.* w) V.+ (0.9 V.* y)]
        , line [x V.+ (0.1 V.* y) V.+ (0.8 V.* w), x V.+ (0.1 V.* y) V.+ (0.2 V.* w)]
        , line [x V.+ (0.8 V.* w) V.+ (0.1 V.* y), x V.+ (0.8 V.* w) V.+ (0.5 V.* y)]
        , line [x V.+ (0.50 V.* y) V.+ (0.8 V.* w), x V.+ (0.50 V.* y) V.+ (0.2 V.* w)]
        ]
interp Siete x w y =
    pictures
        [ line [x V.+ (0.25 V.* w) V.+ (0.9 V.* y), x V.+ (0.75 V.* w) V.+ (0.9 V.* y)]
        , line [x V.+ (0.75 V.* w) V.+ (0.9 V.* y), x V.+ (0.5 V.* w) V.+ (0.1 V.* y)]
        , line [x V.+ (0.7 V.* w) V.+ (0.5 V.* y), x V.+ (0.40 V.* w) V.+ (0.5 V.* y)]
        ]
interp ParentesisAbrir x w y =
    pictures
        [ line [x V.+ (0.6 V.* w) V.+ (0.9 V.* y), x V.+ half w V.+ (0.8 V.* y), x V.+ half w V.+ (0.2 V.* y), x V.+ (0.6 V.* w) V.+ (0.1 V.* y)]
        ]
interp ParentesisCerrar x w y =
    pictures
        [ line [x V.+ (0.4 V.* w) V.+ (0.9 V.* y), x V.+ half w V.+ (0.8 V.* y), x V.+ half w V.+ (0.2 V.* y), x V.+ (0.4 V.* w) V.+ (0.1 V.* y)]
        ]
interp Coma x w y =
    pictures
        [ line [x V.+ half w V.+ (0.2 V.* y), x V.+ (0.45 V.* w) V.+ (0.05 V.* y)]
        ]

-- Diferentes tests para ver que estén bien las operaciones
fig :: Simbolos -> Dibujo Simbolos
fig b = figura b

juntarSim :: Dibujo Simbolos -> Dibujo Simbolos -> Dibujo Simbolos
juntarSim a b = (///) a b

overSim :: Dibujo Simbolos -> Dibujo Simbolos -> Dibujo Simbolos
overSim a b = encimar a b

figTupla :: Tupla
figTupla p1 n1 c n2 p2 = overSim (juntarSim (juntarSim p1 n1) (juntarSim n2 p2)) c

row :: [Dibujo a] -> Dibujo a
row [] = error "row: no puede ser vacío"
row [d] = d
row (d : ds) = juntar 1 (fromIntegral $ length ds) d (row ds)

column :: [Dibujo a] -> Dibujo a
column [] = error "column: no puede ser vacío"
column [d] = d
column (d : ds) = apilar 1 (fromIntegral $ length ds) d (column ds)

grilla :: [[Dibujo a]] -> Dibujo a
grilla = column . map row

testAll :: Dibujo Simbolos
testAll =
    grilla
       [ [ column
               [ row
                   [figTupla (fig ParentesisAbrir) (fig n1) (fig Coma) (fig n2) (fig ParentesisCerrar) | n2 <- fila]
               ]
         ]
       | n1 <- fila
       ]
    
  where
    fila = [Cero, Uno, Dos, Tres, Cuatro, Cinco, Seis, Siete]


grillaConf :: Conf
grillaConf =
    Conf
        { name = "Grilla"
        , pic = testAll
        , bas = interp
        }
