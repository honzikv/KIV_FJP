# KIV/FJP - Překladač C--

Jednoduchý překladač, který umí přeložit C-like syntaxi do
virtuálního stroje PL/0.

## Quickstart

- Ke překladu aplikace je potřeba SDK Javy 17+ a Maven 3.8+

### Spuštění
a) Pomocí skriptu compile.bat / compile.sh
b) pomocí `mvn clean install` a spuštění target/FJPSem-1.0-SNAPSHOT-jar-with-dependencies.jar

Výsledný jar rozběhneme klasicky pomocí příkazu: `java -jar <filename>.jar -i <input_file>`


## Bonusové body:

- Základní datové typy **boolean** ✅, **float** (nakonec nedodělaný) (1b)
- For cyklus, while cyklus, do while cyklus (2b) ✅
- If + else (1b) ✅
- Chained přiřazení (x = y = z = 2) - (1b) ✅
- Předávání parametrů do funkce hodnotou (2b) ✅
- Typeof operátor (3b) - test, zda-li je proměnná daného typu ✅
- Návratová hodnota funkce (2b) ✅
- Typová kontrola (3b) ✅
