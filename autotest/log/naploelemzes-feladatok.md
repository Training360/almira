# Naplóelemzés

A naplóelemzéshez fel kell telepíteni a Locations alkalmazást egy Linux gépre. Használd a kapott VMWare Ubuntu Linuxos
virtuális gépet!

Az indítás után a virtuális gépre a következő felhasználónév/jelszó párossal lehet bejelentkezni: `osboxes`, `osboxes.org`.

Majd keresd meg a virtuális gép által kapott ip-címet. Ehhez a bejelentkezés után add ki a `ifconfig` parancsot!
Valahol egy `192.168.6.128` formátumú ip-címnek kell megjelennie (kb. második sor). Ez lesz a virtuális gép ip-címe.

A virtuális gépen nem lehet copy-paste műveleteket elvégezni, mert a VMWare nem támogatja, a parancsokat be kell írni.

Ezek után lépj ki a terminálból (`exit`), és Putty-tyal jelentkezz be a virtuális gépre (ip-cím megadásával, majd a felhasználónév és jelszó
beírásával). Itt már lehet copy-paste-et használni.

## Java telepítése

Add ki a következő parancsokat:

```
sudo add-apt-repository ppa:linuxuprising/java
sudo apt update
sudo apt install oracle-java10-installer
```

Telepítés közben meg kell adni a jelszót.

Fogadd el a licence feltételeket!

Ellenőrizni, hogy a Java sikeresen feltelepült-e, a következő parancs kiadásával tudod:

```
java -version
```

## MariaDB telepítése

Az adatbázis telepítéséhez add ki a következő parancsot:

```
sudo apt-get install mariadb-server mariadb-client
```

Telepítés közben meg kell adni a jelszót.

Parancssoros klienssel a következő paranccsal tudsz kapcsolódni a MariaDB-hez:

```
sudo mysql -u root -p
```

Próbaként add ki a `show schemas;` parancsot. Majd hozz létre egy `locations` adatbázist, és hozzá felhasználót, a következő
parancsokkal:

```sql
create schema if not exists locations character set utf8 collate utf8_hungarian_ci;
CREATE USER if not exists 'locations'@'%' IDENTIFIED BY 'locations';
use locations;
GRANT ALL PRIVILEGES ON locations.* TO 'locations'@'%';
```

Kilépni az `exit` utasítással tudsz.

A szerver ezután kéréseket csak localhostról fogad. Szerkeszd meg a `/etc/mysql/mariadb.conf.d/50-server.cnf` állományt!

```
sudo nano /etc/mysql/mariadb.conf.d/50-server.cnf
```

Ahol azt látod, hogy:

```
bind-address = 127.0.0.1
```

Azt írd át a következőre:

```
bind-address = 0.0.0.0
```

Majd indítsd újra a szervert:

```
sudo systemctl restart mariadb.service
```

Ha az megtörtént, ellenőrizd a telepítést és a séma létrehozását a HeidiSQL-ből is. Itt phpMyAdmint nem tudsz használni, mert az csak
lokális adatbázishoz kapcsolódik. (A Linux szerverre nem telepítjük fel.)

## Alkalmazás telepítése

Másold fel az alkalmazást (`locations-app.jar`) a felhasználó home könyvtárába (ami a `/home/osboxes` könyvtár)! (Pl. WinSCP segítségével!)

Konfiguráld az alkalmazást, hogy a `8081`-es porton fusson! Ehhez a fájllal azonos könyvtárban hozz létre egy `application.properties`
állományt. (Használj ismételten nano parancsot!) A fájl tartalma legyen:

```
server.port=8081
```

Indítsd el az alkalmazást a következő paranccsal:

```
java -jar locations-app.jar
```

Ha elindult, a következőt kell kiírnia: `Started LocationsAppApplication in 9.835 seconds (JVM running for 12.684)`

Ezek után már elérhető az alkalmazás a böngészőből a megfelelő ip-címmel és porttal, pl. `http://192.168.6.128:8081/`

Készíts erről egy screenshotot, és tedd fel a Subversionbe a saját mappádba!

## Fut-e

Jelentkezz be ugyanerre a szerverre egy másik Putty ablakban, és nézd meg, hogy fut-e Javas folyamat. Használd a `ps -ef` parancsot,
de csak azokat szűrd le, melyek tartalmazzák a `java` szót. Készíts róla egy screenshotot!

A Putty-ból kilépni az `exit` paranccsal, vagy a `Ctrl + d` billentyűkombinációval lehet.

## Selenium

Futtass rá erre a környezetre egy Selenium UI tesztet! Már létező tesztet nyiss meg, és a Base URL-t írd át. Majd ellenőrizd HeidiSQL-ben,
hogy az adatok bekerültek-e az adatbázisba!

## Naplózás

Indítsd úgy az alkalmazást, hogy a kimenetét irányítsd át egy `locations-app.log` fájlba! Egy másik Putty ablakban jelentkezz be, és
monitorozd a log állományt! Készíts róla screenshotot!

Hozz létre pár helyet! Monitorozd úgy a log fájlt, hogy csak a létrehozások kerüljenek kiírásra. Készíts róla screenshotot!

Szakítsd meg a monitorozást! Írd ki, hogy amióta a log fájl létezik, hány hely került létrehozásra! Készíts róla screenshotot!

Írd ki, hogy csak az adott órában hány hely került létrehozásra! Készíts róla screenshotot!

Idézz elő egy hibát! (Pl. írj be indokolatlanul hosszú nevet!) Keresd meg a log fájlban a pontos hibaüzenetet! Jelöld ki, és készíts
róla screenshotot!

Kapcsold be az access logot az alkalmazásban! Ez egy olyan napló állomány, mely a beérkező kéréseket naplózza. Az `application.properties`
állományt egészítsd ki a továbbiakkal:

```
server.tomcat.basedir=locations-app
server.tomcat.accesslog.enabled=true
```

Ha az alkalmazást újra elindítod, akkor megjelenik egy `locations-app` könyvtár, abban egy `logs` könyvtár, és abban egy `access_log`
állomány, pl. ` access_log.2018-09-24.log`. Monitorozd ezt az állományt egy másik Putty ablakban, miközben használod az alkalmazást! Készíts róla screenshotot!

VIGYÁZZ! Teljesítményjavítás miatt a napló állományba a bejegyzések csak 10 mp-es késéssel kerülnek.

Milyen formátumú a napló állomány? Írd ki, hogy a `js` állományok hányszor lettek lekérve! Írd ki csak a POST metódusú http kéréseket!
Validációs hibánál milyen státuszkód megy vissza? Írd ki csak ezzel a státuszkóddal rendelkező kéréseket! Írd ki a csak _NEM_ 304-es státuszkóddal
visszatérő tartalmakat (csak a nem cache-elt tartalmak!). Mindegyikről készíts screenshotot!

Írd ki csak az ebben az órában megjelenő olyan kéréseket, ahol a lapozás nem a 0-tól indult! Készíts screenshotot!
