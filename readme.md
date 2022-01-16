# KIV/FJP - Překladač C--

Jednoduchý překladač, který umí přeložit C-like syntaxi do virtuálního stroje PL/0.

## Struktura projektu

- `src` - zdrojový kód programu
- `testinputs` - testovací vstupy
- `doc` - pdf s dokumentací
- `batch_run.py` - python skript, který jsme použili pro přeložení všeho v testinputs - k použití není potřeba
- `target` - výstup z mvn package / install
- `compile.bat` a `compile.sh` jsou skripty pro přeložení a vytvoření spustitelného jaru v kořenovém adresáři pro
  Windows a Linux

## Quickstart

- Ke překladu aplikace je potřeba SDK Javy 17+ a Maven 3.8+

### Překlad a spuštění

- Pomocí skriptu compile.bat / compile.sh nebo `mvn clean install`, který vygeneruje
  FJPSem-1.0-SNAPSHOT-jar-with-dependencies.jar ve složce `target`.
- Výsledný jar rozběhneme klasicky pomocí příkazu: `java -jar <filename>.jar -i <input_file>`
- Detailní návod se nachází v dokumentaci v souboru `doc/KIV_FJP_Dokumentace.pdf`

## Bonusové body:

- Základní datové typy **boolean** ✅, **float** (nakonec nedodělaný) (1b)
- For cyklus, while cyklus, do while cyklus (2b) ✅
- If + else (1b) ✅
- Chained přiřazení (x = y = z = 2) - (1b) ✅
- Předávání parametrů do funkce hodnotou (2b) ✅
- Typeof operátor (3b) - test, zda-li je proměnná daného typu ✅
- Návratová hodnota funkce (2b) ✅
- Typová kontrola (3b) ✅
