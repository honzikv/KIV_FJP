import os
from pathlib import Path


files = os.listdir('testinputs')
inputdir = 'testinputs'
outputdir = 'batch_run_output'

Path(outputdir).mkdir(parents=True, exist_ok=True)

for file in files:
    os.system(f'java -jar ../target/FJPSem-1.0-SNAPSHOT-jar-with-dependencies.jar
        -i {inputdir}/{file} -o {outputdir}/{file}.pl0')

