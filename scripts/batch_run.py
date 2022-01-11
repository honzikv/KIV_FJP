import os
from pathlib import Path


files = os.listdir('../testinputs')
outputdir = '../output'

Path(outputdir).mkdir(parents=True, exist_ok=True)

for file in files:
    print(f'java -jar ../target/FJPSem-1.0-SNAPSHOT-jar-with-dependencies.jar -i ../testinputs/{file} -o {outputdir}/{file}.pl0')
    os.system(f'java -jar ../target/FJPSem-1.0-SNAPSHOT-jar-with-dependencies.jar -i ../testinputs/{file} -o {outputdir}/{file}.pl0')

