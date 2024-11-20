module Pred (
    Pred,
    cambiar,
    anyDib,
    allDib,
    orP,
    andP,
    falla,
    esRedundante,
) where

import Dibujo

type Pred a = a -> Bool

-- Dado un predicado sobre básicas, cambiar todas las que satisfacen
-- el predicado por la figura básica indicada por el segundo argumento.
-- Por ejemplo, `cambiar (== Triangulo) (\x -> Rotar (Figura x))` rota
-- todos los triángulos.
cambiar :: Pred a -> (a -> Dibujo a) -> Dibujo a -> Dibujo a
cambiar p f d = mapDib (\x -> if p x then f x else figura x) d

-- De las siguientes dos funciones esperaria que anyDib sea mas rapida
-- Alguna básica satisface el predicado.
-- foldr que 'reduce' una lista a un resultado
-- tenemos el elemento actual (x), el acumulador(acc)
-- que comienza en False y la lista (arr)
anyDib :: Pred a -> Dibujo a -> Bool
anyDib p d = foldr (\x acc -> p x || acc) False arr
  where
    arr = figuras d -- extraer las figuras y guardarlas en arr

-- Todas las básicas satisfacen el predicado.
allDib :: Pred a -> Dibujo a -> Bool
allDib p d = and arr -- and se aplica a todos los elementos de la lista arr y devuelve True si todos son True
-- foldr (&&) True arr es equivalente a esto
  where
    arr = map p (figuras d)

-- Los dos predicados se cumplen para el elemento recibido.
-- Pred a -> Pred a -> Bool -> a
andP :: Pred a -> Pred a -> Pred a
andP p1 p2 x = p1 x && p2 x

-- Algún predicado se cumple para el elemento recibido.
orP :: Pred a -> Pred a -> Pred a
orP p1 p2 x = p1 x || p2 x

falla :: Bool
falla = True

-- Predicado para verificar si hay construcciones redundantes
esRedundante :: (Eq a) => Pred (Dibujo a)
esRedundante d = esRedundanteEspejarFig d || esRedundanteRotarFig d

esRedundanteEspejarFig :: Pred (Dibujo a)
esRedundanteEspejarFig dib = any (\x -> x >= 2) (contarEspejadosList dib)

esRedundanteRotarFig :: Pred (Dibujo a)
esRedundanteRotarFig dib = any (\x -> x >= 360) (sumaAngulosList dib)

sumaAngulosList :: Dibujo a -> [Float]
sumaAngulosList d = foldDib fFig fRot fRotA fNoRot fNoRot fAp fJun fEnc d
  where
    fFig _ = [0] -- seria el acc
    fRot a = map (+ 90) a
    fRotA alpha a = map (+ alpha) a
    fNoRot a = a
    fAp _ _ acc1 acc2 = acc1 ++ acc2
    fJun _ _ acc1 acc2 = acc1 ++ acc2
    fEnc acc1 acc2 = acc1 ++ acc2

contarEspejadosList :: Dibujo a -> [Int]
contarEspejadosList d = foldDib fFig fRot fRotA fEsp fRot45 fAp fJun fEnc d
  where
    fFig _ = [0] -- seria el acc
    fRot a = a
    fRotA _ a = a
    fEsp a = map (+ 1) a
    fRot45 a = a
    fAp _ _ acc1 acc2 = acc1 ++ acc2
    fJun _ _ acc1 acc2 = acc1 ++ acc2
    fEnc acc1 acc2 = acc1 ++ acc2
