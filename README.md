# Gefangenendilemma - Coding Competition

Das [Gefangenendilemma](https://de.wikipedia.org/wiki/Gefangenendilemma) ist ein Gedankenexperiment aus der Spieltheorie. Es beschreibt ein mathematisches Spiel,
bei dem sich zwei Akteure, die „Spieler“, unabhängig voneinander und ohne Rücksprache zwischen zwei  
Verhaltensweisen entscheiden müssen. Der Erfolg für den Einzelnen hängt stark von der Entscheidung des 
jeweils anderen ab, wodurch ein interessantes Spannungsverhältnis entsteht.

## Implementierung

In diesem Projekt geht es darum, das Gefangenendilemma in Form eines Coding-Wettbewerbs zu simulieren. Das
Projekt bietet dazu ein Interface namens `Prisoner`, das implementiert werden kann, um die Strategie eines Spielers
zu definieren.

### Voraussetzungen

* Java Development Kit (JDK 17 oder höher)
* Maven

### Schritt-für-Schritt Anleitung

1. **Repository klonen**  
   Zuerst musst du das Repository klonen, um Zugriff auf den Quellcode zu erhalten. Führe dazu folgenden Befehl
   im Terminal aus:

   ```sh
   git clone https://github.com/KyleKreuter/gefangenendilemma.git
   cd gefangenendilemma
   ```

2. **Maven Build**  
   Um das Projekt zu kompilieren und alle Abhängigkeiten zu installieren, führe den folgenden Befehl aus:
   ```sh
   mvn clean install
   ```
   Dieser Schritt stellt sicher, dass alle notwendigen Bibliotheken heruntergeladen und das Projekt vollständig gebaut
   wird. Du kannst die API nun nutzen, indem du in einem neuen Maven Projekt folgendes als Dependency angibst:
   ```xml
   <dependency>
    <groupId>de.kyle</groupId>
    <artifactId>gefangenendilemma</artifactId>
    <version>1.0.0</version>
   </dependency>
   ```
   
3. **Einen Client erstellen**  
   Nachdem du das Projekt erfolgreich gebaut hast, kannst du deinen eigenen Gefangenen (Spieler) programmieren,
   indem du das Interface `Prisoner` implementierst. Erstelle hierzu eine neue Klasse in einem neuen Projekt und
   implementiere die folgenden Methoden des Prisoner Interfaces:
   ```java
    package de.meinname.gefangenendilemma.client;
    
    import de.kyle.gefangenendilemma.api.Prisoner;
    import de.kyle.gefangenendilemma.api.event.PostMessEvent;
    import de.kyle.gefangenendilemma.api.result.PrisonerMessResult;

    public class MyPrisoner implements Prisoner {
    
        @Override
        public String getName() {
            return "MeinGefangener";
        }
    
        @Override
        public PrisonerMessResult messAround(String opponent) {
            // Implementiere hier deine Strategie: BETRAY oder COOPERATE
            return PrisonerMessResult.COOPERATE; // Beispiel: immer kooperieren
        }
    
        @Override
        public void onPostMessEvent(PostMessEvent postMessEvent) {
            // Nutze Informationen über vorherige Runden zur Anpassung deiner Strategie
        }
    }
   ```
   
4. **Client registrieren**  
   Damit dein Client am Wettbewerb teilnehmen kann, musst du ihn als `-jar`-Datei packen und in den competitors-
   Ordner legen. Dazu erstelle eine Datei `prisoner.properties` unter `resources`, um wichtige Informationen anzugeben:
   ```properties
   prisoner.entry=de.meinname.gefangenendilemma.client.MyPrisoner
   prisoner.name=MeinGefangener
   ```
   Achte darauf, dass prisoner.entry den vollständigen Klassennamen deiner Prisoner-Implementierung angibt

### Das Interface `Prisoner`
Das `Prisoner` Interface definiert die Spielregeln, die jeder Teilnehmer befolgen muss:  
* `getName()`: Gibt den Namen des Gefangenen zurück. Dieser Name dient zur eindeutigen Identifizierung jedes   
  Teilnehmers
* `messAround()`: Bestimmt die Entscheidung des Gefangenen während einer Runde des Dilemmas, ob er  
  kooperieren oder betrügen möchte
* `onPostMessEvent(PostMessEvent postMessEvent)`: Wird nach jeder Runde aufgerufen und übergibt die  
  Ergebnisse der letzten Runde. Diese Informationen können in zukünftigen Runden genutzt werden

### Regeln des Spiels
Das Spiel folgt einer Reihe von festgelegten Regeln:
* Das Spiel wird **standardmäßig über 100 Runden** gespielt (Nicht fix!)
* Jeder Spieler tritt genau zweimal gegen einen Gegner an

### Punktesystem
Das Punktesystem belohnt oder bestraft die Entscheidungen der Spieler wie folgt:
* Betrügt ein Spieler, während der Mitspieler kooperiert, erhält der Betrügende 3 Punkte, und der kooperierende  
  Mitspieler geht leer aus
* Kooperieren beide Spieler, erhalten beide jeweils 1 Punkt
* Betrügen beide Spieler, erhalten beide keine Punkte

### Ziel des Wettbewerbs
Das Ziel des Wettbewerbs ist es, eine möglichst effiziente und kluge Strategie zu entwickeln, die in unterschiedlichen Spielsituationen zu einer hohen Gesamtpunktzahl führt.


### Testen der Algorithmen
Um den programmierten Gefangenen zu testen, muss die Jar kompiliert werden und dann in den `competitors`-Ordner
geschoben werden. Dieser wird dann automatisch geladen und getestet. Um die Wettbewerbs-jar zu starten, muss der Befehl
`java -jar gefangenendilemma-1.0.0.jar` ausgeführt werden. Ein Gefangener, der alleine keine 200 Punkte errreicht, ist 
überdurchschnittlich schlecht und wird es in einem Wettbewerb wahrscheinlich schwer haben.
