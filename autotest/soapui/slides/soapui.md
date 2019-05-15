class: inverse, center, middle

# SOAP webszolgáltatások tesztelése SoapUI-jal

---

## SoapUI

* REST and SOAP testing tool
* Különböző kiadások
    * Open Source
    * Professional (ReadyAPI csomag része)

---

## SoapUI Pro

* Követelmények kezelése
* Több környezet támogatása
    * Service URL-ek, JDBC kapcsolatok, változók (properties)
* Data Driven Testing (beolvasás és kiírás különböző formátumokban)
* Lefedettség WSDL alapján
* Test assertion támogatás (pl. XPath generálás)
* CI/CD integráció
* Reporting (különböző formátumokban)
* Support

---

## Data Driven Testing

* Adatok beolvasása különböző forrásokból: Excel, XML, JDBC, CSV, stb.
* Kérések (XML) beolvasása fájlból
* Data Generator: adatgenerálás (pl. véletlenszám, script, stb.)
* Adatbázisba mentés
        
---

## Használható

* Különböző webszolgáltatások tesztelésére: SOAP és REST
* API funkcionális tesztelés
* Teljesítmény tesztelés
* Biztonság tesztelése

---

## Egyéb tulajdonságok

* Mockolás (szimuláció)
* Groovy szkriptelhetőség
* Parancssori futtatás
* Data-Driven Testing
* Jelentéskészítés

---

## Első projekt (gyakorlat)

* Új projekt létrehozása WSDL alapján
* Példa request létrehozása futtatása

---

## Projekt kezelése

* Save project
* Import project

---

## Teszt csomagok

* TestSuite
* TestCase
* Test Step
    * SOAP Request
    * Assert: contains, XPATH
    
```
declare namespace l='http://training360.com/locations'
//l:location/l:name
```

---

## További Test Step fajták

* Run Testcase (másik teszteset meghívása)
* Properties (változók deklarálása)
* Property Transfer
* Assert létrehozása

---

## Változók használata

```
<userName>${#TestCase#username}</userName>
```

---

## Property transfer

* Előző response-ból adat áthozatala következő requestbe

```
declare namespace l='http://training360.com/locations'
//l:location/l:id

//l:updateLocationRequest/l:id
```

---

## JDBC Test Step

* `mariadb-java-client-2.4.1.jar` a `lib` könyvtárba
* Driver: `org.mariadb.jdbc.Driver`
* Connection string: `jdbc:mysql://localhost:3306/testdb?user=testuser&password=testpwd`

---

## Load test

* Fajtái
  * Baseline
  * Load
  * Stress
  * Soak (hosszan futó)
  * Scalability
* Load Test   
  * Assertálható
  * Exportálható

---

## SOAP Service Mocking

* Webszolgáltatás prototípus
* Pl. kliens alkalmazások tesztelésére

---

## Ismétlő kérdések

* Mi a SoapUI elsődleges célja?
* Mi a különbség a különböző kiadások között?
* Milyen tesztelésre használható?
* Hogyan épülnek fel a tesztesetek? Milyen teszt lépéseket ismersz?
* Hogyan lehet assertet megfogalmazni?
* Hogyan kell változót használni?
* Hogyan kell adatbázisból olvasni?

---

## Ismétlő kérdések 2.

* Hogyan lehet terheléses tesztelést futtatni?
* Hogyan lehet egy szolgáltatást mockolni?
* Hogyan lehet különböző környezeteket használni?
* Mi az a Data Driven tesztelés?
* Mi alapján lehet lefedettséget mérni?
* Hogyan lehet jelentéseket generálni?
