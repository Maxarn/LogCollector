# Introduktion #
Log Data Collector är uppbyggd i webb ramverket Vaadin.
För att utveckla denna applikation så krävs följande:
* [JDK 8 u60 eller en senare version](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](http://maven.apache.org/download.cgi)
* Applikationsserver (varav [Glassfish Server ver 3 eller senare rekommenderas](https://glassfish.java.net/download.html))
* [MySQL Server](http://dev.mysql.com/downloads/)

# Värt att notera: #
### IDE ###
Det finns inget specifikt krav på vilken IDE som du utvecklar en Vaadin applikation, men det rekommenderas att använda antingen NetBeans, Eclipse eller IntelliJ, då dessa har Vaadin plugins som underlättar utvecklingen väldigt mycket.
Log Collector Server har utvecklats som ett NetBeans projekt därmed rekommenderas det att vidareutveckling av webbapplikationen utförs i NetBeans.

### Maven ###
Vid installation av Maven så måste du även se till att Maven läggs till i PATH.

# Kommandon #
### Vaadin & Maven ###
Ofta när man har arbetat på en klass eller ett objekt utanför Vaadins MyUI klassen (ibland även innanför) så blir inte alltid projektet propert kompilerat, så därmed bör du ofta efter större förändringar öppna en terminal, flytta in dig på webb applikationens projekt path och skriv följande:

```
mvn install
```

### Import av MySQL databas ###
För att importera databasen som är byggd för webb applikationen så får du öppna ett terminalfönster och byt path till database mappen som ligger placerat i grunden av projektets repository. Sedan logga in på din MySQL databas och skapa en ny tom databas, sedan logga ut igen och skriv följande i terminalen:

```
mysql -u {root användarnamn} -p {den nya databasens namn} < v3_1_cleaned.sql
```

Detta kommer att importera in projektets databas till den nya databasen.

### OBS! ###
Log Data Collector kommer att generera en .properties fil vid första uppstarten som måste fyllas med en URL som pekar på den skapade databasen samt användarnamn och lösenord till en användare på databasservern som har tillgång till att redigera databasen.

Du kan hitta den genererade .properties filen i din glassfish installationsmapp i pathen:

```
glassfish{ver}/glassfish/domains/{domännamn}/config/LogServerConfig.properties
```


Domännamnet som NetBeans använder brukar heta “domain1”.

Databasens URL bör skrivas som till exempel:

```
localhost:3306/logdatacollector
```
Då URL:en innehåller:

```
{databas serverns adress}/{databasens namn}
```
