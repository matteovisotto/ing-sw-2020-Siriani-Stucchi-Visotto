# ing-sw-2020-Siriani-Stucchi-Visotto

## Gruppo (GC5)
- Mattia Siriani - mattia.siriani@mail.polimi.it
- Francesco Stucchi - francesco2.stucchi@mail.polimi.it
- Matteo Visotto - matteo.visotto@mail.polimi.it

## Funzioni implementate

| Funzionalità | Stato |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI |[![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#)|
| Advanced Gods | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Undo | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

## Tecnologie utilizzate

- Java 14
- Swing
- Libreria `gson` per il parsing del file JSON contenente le descrizioni delle carte divinità in GUI.
- Libreria `junit` per i test
- Plugin `maven` per la generazione di JAR, test, ...

## UML

Nella cartella `deliveries/uml` sono presenti l'uml iniziale, vari flow diagram per rappresentare le interazioni con le carte divinità (di inizio progetto).

In `deliveries/uml/Final UML` sono presenti gli UML finali generati con il tool di IntelliJ e/o creati appositamente. E' presente un UML per ogni package
con attributi e metodi delle classi e uno per le dipendenze tra i package.

## JavaDoc

I Javadoc generati si trovano nella cartella `javadoc`.

## Lancio JAR

### Linux e macOs

Esistono 2 jar che possono essere trovati nella cartella `deliveries/jars` e 3 script sh
per il lancio che si trovano nella root del progetto:

- jar per il client (GUI) lanciabile tramite lo script `santorini-client.sh`
- jar per il client (CLI) lanciabile tramite lo script `santorini-cli-client.sh`
- jar per il server lanciabile tramite lo script `santorini-server.sh`

I jar, invece, possono essere eseguiti tramite 
- jar client GUI `java -jar santoriniClient.jar`
- jar client CLI `java -jar santoriniClient.jar -cli`
- jar server `java -jar santoriniServer.jar`

### Windows

Ci sono due modi per far partire i jar: 

- Nel caso di utilizzo di Git Bash come terminale si possono utilizzare gli script `.sh`
- Per utilizzare il prompt di windows utilizzare il comando `java -jar <jar file>`.

### File allegati

Nella stessa directory in cui è presente il jar del client è necessario che vi sia il file `server.properties`, dal quale è possibile modificare l'indirizzo di rete in cui è eseguito il server.


## Generazione JAR

I 2 jar per client e server sono generabili tramite maven con `mvn clean package`. 
Vengono creati nella cartella `target` con i nomi `GC5-1.0-SNAPSHOT.jar` per il server e `GC5-1.0-SNAPSHOT-jar-with-dependencies.jar` per il client.

Il file `server.properties` è invece recuperabile dalla directory principale.
