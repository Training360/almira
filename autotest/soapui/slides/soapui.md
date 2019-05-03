class: inverse, center, middle

# SOAP webszolgáltatások tesztelése SoapUI-jal

---

## SoapUI

* REST and SOAP testing tool
* Különböző kiadások
    * Open Source
    * Professional (ReadyAPI csomag része)
        * Követelmények kezelése
        * Data Driven Testing
            * Adatok beolvasása különböző forrásokból: Excel, XML, JDBC, CSV, stb.
            * Kérések beolvasása fájlból
            * Data Generator: adatgenerálás (pl. véletlenszám, script, stb.)
            * Adatbázisba mentés
        * Lefedettség WSDL alapján
        * Test assertion
        * Több környezet támogatása
            * Service URL-ek, JDBC kapcsolatok, változók (properties)
        * CI/CD integráció
        * Reporting (különböző formátumokban)
        * Support
        
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

## TODO

* Parancssor
* Groovy
* Mocking
* Performance
* Data-Driven Testing
* Test reporting
* JDBC test step