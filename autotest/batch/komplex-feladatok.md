# Gyakorló feladatok

## Batch file-ok

### Ismétlés

A következő parancsok fognak kelleni a feladatok megoldásához:

* `echo off`
* `@echo off`
* `echo`
* `REM`
* Környezeti változó deklarálása és értékadás (`set`)
* Paraméterek: `%1`, `%2`
* Elágazások:
    * `if exist %1`
    * `if /i "%myVar%" == "test"`
    * `if not defined myVar`
* `call`
* `find`
* `start`
* `exit`
* `chcp`
* `ERRORLEVEL` használata
* `%date%` és `%time%`
* `goto` és címkék használata
* Érték beolvasása: `set /p nev=:`



### Áttekintés

Adott egy alkalmazás, mely kedvenc helyeket tart nyilván, azok neveit és koordinátájukat.

A feladat egy szerver alkalmazást indító, valamint annak üzemeltetéséhez szükséges batch fájlok megírása.

Megegyzésben írd minden batch fájl elejére, hogy mit csinál!

A parancsok, s outputjaik NE jelenjenek meg a képernyőn!

A feladatokat a Subversion repository gyökér könyvtárában az `architekturak` alkönyvtárban találod.

A megoldásokat töltsd fel Subversionbe a _saját könyvtárad alatt_ az `architekturak` könyvtárba!

### Telepítési környezet előkészítése

A telepítendő alkalmazás egy Java nyelven írt alkalmazás. Az alkalmazás háromrétegű alkalmazás, MariaDB adatbázissal, Java backenddel és HTML, CSS, JavaScript frontenddel.

#### Adatbázis

##### Séma létrehozás

Az alkalmazás futtatásához telepíteni kell egy MariaDB adatbázist. Készítsünk egy bat fájt, ami létrehozza az adatbázis sémát, és hozzá tartozó felhasználókat!

Írj egy `create-schema.bat` fájlt!

Amennyiben a MariaDB nem fut, írjon ki egy hibaüzenetet, és álljon le a batch fájl futása.
A MariaDB nem fut, ha a `mysql.exe` hívása egy SQL fájlal - pl. ami lekérdezi a sémákat - hibát ad vissza - `errorlevel` ellenőrzése.

A hibaüzenetben használj ékezetes karaktereket! Ekkor a notepad pl. egy BOM karaktert tesz a fájl elejére, ekkor viszont az első sor nem értelmezhető. Notepad++-ban át kell állítani a karakterkódolást, hogy UTF-8 BOM nélkül.

Ahhoz, hogy az ékezetes karakterek jól jelenjenek meg, az ablak karakterkódolását is át kell állítani a következő paranccsal:

```
chcp 65001
```

Úgy is ellenőrizhető, hogy fut-e, hogy meghívjuk a `tasklist` parancsot, és abban keresünk (`find` - elágazás `errorlevel` alapján).

A `mysql` helye default XAMPP telepítés esetén `C:\xampp\mysql\bin\mysql.exe`. Amennyiben a bat fájl nem találja a megadott könyvtárat, ahol a `mysql.exe` van, írjon ki egy hibaüzenetet.

A mysql könyvtára egy környezeti változóban legyen definiálva!

A bat fájl létrehoz egy `locations` nevű sémát, és hozzá egy `locations` nevű felhasználót, ugyanezzel a jelszóval. A karakterkészlet legyen `utf8` és a sorrendezés `utf8_hungarian_ci`. A felhasználóval bármilyen másik gépről be lehessen jelentkezni.

Iránymutatás: írni kell egy sql állományt, mely a sémát és felhasználókat létrehozza, és a mysql parancsot kell úgy meghívni, hogy át kell irányítani a bemenetére az sql állományt.

A MySQL XAMPP Control Panelen indítható és leállítható.

##### Séma és felhasználó eldobása

Hozz létre egy `drop-schema.bat` fájlt, ami eldobja a `locations` sémát és a `locations` felhasználót.

##### Táblák eldobása

Hozz létre egy `drop-tables.bat` fájlt, ami eldobja a `locations` és a `schema_version`  _táblát_. Figyelj arra, hogy akkor is hiba nélkül fusson le, ha a táblák nem léteznek! (Azaz egymás után is meghívható legyen a script!)

##### Adatok importálása és ürítése

Írj egy `import-data.bat` fájlt, amely, ha megadsz neki paramétert, akkor a paraméterként
megadott sql állományt futtatja le. Amennyiben az állomány nem létezik, írjon ki hibaüzenetet! Amennyiben a felhasználó nem ad meg paramétert, egy default `default-import.sql` állományt hívjon meg.

A tábla, amit az alkalmazás létre fog hozni, a következő formátumú:

```sql
create table locations(id int auto_increment primary key, name varchar(255), lat double, lon double);
```

A `default-import.sql` tartalmazzon kb. öt insert utasítást, mely példa adatokat szúr be a `locations` táblába. Próbáld ki úgy, hogy kézzel hozd létre a `locations` táblát.

Írj egy olyan SQL szkriptet `clear-data.sql` néven, mely kitörli a `locations` tábla tartalmát, és hívd meg a `clear-data.bat` állományból!

##### Egy bat-ba kombinálás

Írj egy `control-mysql.bat` bat fájlt, ami paraméterként várja, hogy mit tegyen, és hívja meg
az előbbi bat állományokat.

Pl. `control-sql.bat drop`, hívja meg a `drop-schema.bat` fájlt. Ez a bat fájl definiálja a `mysql.exe` elérését. A kis bat fájlok ezt akkor írják felül, ha a környezeti változó nincs beállítva. Lehetséges paraméterek tehát:

* `create-schema`
* `drop-schema`
* `drop-tables`
* `import-data`
* `clear-data`

Amennyiben nem ad meg paramétert, írjon ki hibaüzenetet, hogy mik közül lehet választani!

#### Export/import

Importáld be a `locations` táblába az adatokat a `tables.helyseg_hu.xls` Excel állományból.
Ehhez először át kell alakítani olyan formátumra, hogy ne fok és perceket tartalmazzon, hanem tört
fokokat. (Használj függvényeket!)

Ha ez megvan, ki kell menteni CSV állományba. Majd egy `import-csv.sql` állományban használd a `load data` parancsot:

https://stackoverflow.com/questions/6605765/importing-a-csv-into-mysql-via-command-line

Mentsd ki az adatbázist a `mysqldump` paranccsal egy `exported.sql` állományba.

https://dev.mysql.com/doc/refman/8.0/en/mysqldump.html

Töröld ki a `locations` táblát, majd importáld vissza! Hozz létre két bat fájlt a dumpra és importra!

#### Java alkalmazás

Töltsd le a `locations-app-0.0.1-SNAPSHOT.jar` állományt. Az alkalmazás 10-es Java-t követel meg. Telepítsd fel a már feltelepített 8-as Java mellé a 10-es Java-t is. Letölthető a http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html címről, `jdk-10.0.2_windows-x64_bin.exe` néven.
Ekkor a `C:\Program Files (x86)\Java` könyvtárba kerül feltelepítésre.

Írj egy `check-java.bat` állományt, mely megvizsgálja, hogy 8-as vagy 10-es Java verzió fut-e. Ezt úgy tudod, hogy a `java -version` parancsot kell kiadnod. Ha ebben szerepel a `10.0.2` karaktersorozat, akkor ne írjon ki semmit, ellenkező esetben adjon egy hibaüzenetet! A `find` parancsot kell használnod.

A Java könyvtára a `JAVA_HOME` környezeti változóban legyen definiálva!

##### Java alkalmazás futtatása

A Java alkalmazás a következő paranccsal futtatható:

```
java -jar locations-app-0.0.1-SNAPSHOT.jar
```

Próbáld futtatni az alkalmazást. Nézd meg a napló állományt, hogy az alkalmazás melyik porton figyel. Írd be a böngészőbe! Teszteld a működését! Nézd a naplót!

Parancssori paraméterként átadható, hogy hol a konfigurációs állománya:

```
-Dspring.config.location=conf/application.properties
```

Parancssori paraméterként átadható, hogy hol a naplózás konfigurációs állománya:

```
-Dlogback.configuration=file:conf\logback.xml
```

A parancssori paramétereket egy `PROG_OPTS` környezeti változóban tárold!

Hozz létre egy `application.properties` állományt a `conf` könyvtárba, a
JAR állományban (ami valójában egy ZIP állomány) lévő `/BOOT-INF/classes/application.properties` fájl alapján. A felhasználónév és jelszó legyen a `locations/locations`.

Hozz létre egy `logback.xml` állomány, amely az alkalmazás üzeneteit INFO szinten, míg minden mást WARN szinten naplóz. Az alkalmazás loggerei a `com.training360` néven hivatkozhatóak.

Hozz létre egy `application-tadev.properties` állományt, ami a `tadev` szerveren lévő adatbázishoz kapcsolódik.

Hozz létre egy `logback-debug.xml` állományt, mely az alkalmazás üzeneteit DEBUG szinten, míg minden mást WARN szinten naplóz.

Az alkalmazás kimenete kerüljön az `log/stdout.log` állományba.

Hozz létre egy `ctrl-locations.bat` állományt, mely elindítja az alkalmazást, paraméterül átadva neki a `conf/application.properties`, valamint a `conf/logback.xml` állományt! Indítás előtt kérdezze meg, hogy törölje-e az összes log állományt. Amennyiben a válasz `I`, törölje le a `log` könyvtárban a `.log` kiterjesztésű állományokat!

Amennyiben `localinfo` paramétert kap, a lokális adatbázishoz kapcsolódjon, és INFO szinten naplózzon. Amennyiben `remoteinfo` paramétert kap, a távoli adatbázishoz kapcsolódjon, és INFO szinten naplózzon. A `localdebug` és `remotedebug` rendre a lokális és távoli adatbázishoz csatlakozzon, és DEBUG szinten naplózzon.

(Ezt megoldani úgy lehet, hogy paraméterül a megfelelő `properties` és `logback` állományokat kapja.)

##### Naplózás

Módosítsd a Logback konfigurációs állományait, hogy az alkalmazás írjon a `log/locations.log` állományba.

Töltsd le a 7-Zip programot, tedd a `C:\Utils` könyvtárba. Tedd a könyvtárat a `PATH`-ba!

Írj egy `archive-logs.bat` állományt, mely az `stdout.log` és `locations.log` állományokat betömöríti úgy, hogy a fájlnévben szerepeljen benne a dátum és az idő, pl. `locations-log-20180917-120304.zip`. Lásd az alábbi hivatkozást:

https://stackoverflow.com/questions/7727114/batch-command-date-and-time-in-file-name

Amennyiben a bat fájl nem találja a 7-Zip-et, írjon ki hibaüzenetet!

Majd ezt az állományt mozgassa a `logs\archive` könyvtárba. Ha a könyvtár nem létezik, hozza létre!
