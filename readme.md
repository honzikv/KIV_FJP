# KIV/FJP - Překladač C--

Jednoduchý překladač, který umí přeložit C-like syntaxi do
virtuálního stroje PL/0.

## Quickstart

- Ke překladu aplikace je potřeba SDK Javy 17+ a Maven 3.8+
- Přeložíme pomocí `mvn package`

## Bonusové body:

- Základní datové typy **boolean** ✅, **float** (nakonec nedodělaný) (1b)
- For cyklus, while cyklus, do while cyklus (2b) ✅
- If + else (1b) ✅
- Chained přiřazení (x = y = z = 2) - (1b) ✅
- Předávání parametrů do funkce hodnotou (2b) ✅
- Typeof operátor (3b) - vrátí typ proměnné ✅
- Návratová hodnota funkce (2b) ✅
- Typová kontrola (3b) ✅
