{-# OPTIONS_GHC -Wno-overlapping-patterns #-}
{-# LANGUAGE BlockArguments #-}
{-# LANGUAGE IncoherentInstances #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Redundant bracket" #-}

module StackMachine where

import Data.Char (isDigit)


-- TIPOS DE DATOS --

type Stack a = [a]

data BOp = ADD | SUB | MUL | DIV
  deriving (Eq, Read, Show)

data UOp = NEG
  deriving (Eq, Read, Show)

data SynTree a = Binary BOp (SynTree a) (SynTree a) | Unary UOp (SynTree a) | Operand a
  deriving (Eq, Read, Show)

-- CONSTRUCCION DEL ARBOL SINTACTICO --
createSynTree :: [String] -> SynTree Integer
createSynTree [] = error "Expresión vacía"
createSynTree expr = case parseExpr expr of
    (tree, []) -> tree
    (_, remainder) -> error $ "Error de análisis en la expresión: " ++ unwords remainder

-- Función auxiliar para analizar la expresión
parseExpr :: [String] -> (SynTree Integer, [String])
parseExpr [] = error "Expresión vacía"
parseExpr (x:xs)
    | all isDigit x = (Operand (read x), xs)
    | x == "N" = let (subTree, rest) = parseExpr xs in (Unary NEG subTree, rest)
    | isBinaryOperator x = parseBinaryOp x xs
    | isNegativeNumber x = (Operand (read x), xs)
    | otherwise = error $ "Operador no válido: " ++ x

-- Función auxiliar para analizar operadores binarios
parseBinaryOp :: String -> [String] -> (SynTree Integer, [String])
parseBinaryOp op expr =
    let (leftTree, rest1) = parseExpr expr
        (rightTree, rest2) = parseExpr rest1
     in (Binary (readOp op) leftTree rightTree, rest2)
  where
    readOp "+" = ADD
    readOp "-" = SUB
    readOp "*" = MUL
    readOp "/" = DIV
    readOp unknownOp = error $ "Operador no válido: " ++ unknownOp

-- Función auxiliar para verificar si una cadena representa un número negativo
isNegativeNumber :: String -> Bool
isNegativeNumber str = head str == '-' && all isDigit (tail str)

-- Función auxiliar para verificar si una cadena representa un operador binario válido
isBinaryOperator :: String -> Bool
isBinaryOperator op = op `elem` ["+", "-", "*", "/"]

-- EVALUACION --
applyBinOp :: BOp -> Integer -> Integer ->  Integer
applyBinOp ADD x y =  (x + y)
applyBinOp SUB x y =  (x - y)
applyBinOp MUL x y =  (x * y)
applyBinOp DIV x y
  | y == 0    = error "¡División por cero!"  -- Lanza una excepción
  | otherwise = x `div` y


evalUnaryExpr :: UOp -> Integer -> Integer
evalUnaryExpr NEG x = (-x)

evalBinExpr :: BOp -> Integer -> Integer -> Integer
evalBinExpr = applyBinOp

evalSynTree :: SynTree Integer -> Integer
evalSynTree = evalPostOrder []
  where
    evalPostOrder :: Stack Integer -> SynTree Integer -> Integer
    evalPostOrder _ (Operand n) = n
    evalPostOrder stack (Unary uop expr) =
      case evalPostOrder stack expr of
        n -> evalUnaryExpr uop n
    evalPostOrder stack (Binary bop left right) =
      case (evalPostOrder stack left, evalPostOrder stack right) of
        (m, n) -> evalBinExpr bop m n

