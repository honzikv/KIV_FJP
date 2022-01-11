import os
from pathlib import Path

# Testovaci skript pro batch run vsech zdrojovych kodu. Vysledky umistime do output

files = os.listdir('testinputs')
inputdir = 'testinputs'
outputdir = 'batch_run_output'

Path(outputdir).mkdir(parents=True, exist_ok=True)

for file in files:
    command = f'java -jar ./target/FJPSem-1.0-SNAPSHOT-jar-with-dependencies.jar -i {inputdir}/{file} ' \
              f'-o {outputdir}/{file}.pl0'
    print(command)
    os.system(command)
