class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
# Felületi tesztelés Seleniummal

---

## Selenium

* Böngésző automatizálás, tipikusan webes alkalmazások tesztelésére
* Egyéb automatizálási feladatokra
* Felvétel és visszajátszás
* Szkriptelhető
* DSL: Selenese
* Különböző programozási nyelvekhez illesztés: C#, Groovy, Java, Perl, PHP, Python, Ruby and Scala
* Képes meghajtani a különböző böngészőket is
* Platformfüggetlen
* Nyílt forráskódú, ingyenes

---

## Selenium IDE

* FireFox Add-On
* Felvétel, szerkesztés, futtatás
* IDE, pl. autocomplete, debug
* https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/

---

## Selenium IDE felépítése

* Project
  * Base URL
* Test suites
* Tests (test cases)
* Command
    * Target
    * Value
    * Comment

---

## Gyakori parancsok

* `open`
* `type`
* `click`
* assert, pl. `assert title`, `assert text` - ha hamis, leáll a futás
* verify, pl. `verify title`, `verify text` - ha hamis, warning

---

## Komponensek kijelölése

* Formátuma: `locatorType=location`
* Identifier alapján (id, majd name)
* Id alapján
* Name alapján
* XPath
    * Developer tools/Inspector/Copy XPath
* Link
* CSS

---

## XPath

* W3C szabvány
* Egy XML dokumentum elemei és attribútumai közötti navigációt biztosítja
* XPath szintaktika segítségével definiálhatjuk az XML dokumentum részeit
* Kifejezések segítségével mozoghatunk az XML dokumentumban
* Tartalmaz egy standard függvénykönyvtárat

---

## Példa XML állomány

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!-- Catalog of books -->
<catalog>    
    <book isbn10="059610149X">
        <title>Java and XML</title>
        <available />
    </book>
    <book isbn10="1590597060">
        <title>Pro XML Development
            with Java Technology</title>
    </book>
</catalog>
```
---

## Példa XPath kifejezések

* `catalog` – csomópont neve, kiválasztja az összes `catalog` elemet
* `/catalog` – kiválasztja a gyökérelem alatt lévő összes `catalog` elemet
* `catalog/book` – kiválasztja a `catalog` elem összes `book` gyerek elemet
* `//book` – kiválasztja a dokumentum összes `book` elemét, függetlenül annak alhelyezkedésétől
* `catalog//book` – kiválasztja a `catalog` elem összes leszármazott `book` elemét
* `//@lang` – kiválasztja az összes lang nevű attribútumot

---

## XPath predicates 1.

* `/catalog/book[1] ` - A `catalog` első `book` gyerekelemét találja meg
* `/catalog/book[last()]`  - a `catalog` utolsó `book` gyerekelemét találja meg
* `/catalog/book[last()-1]` – a `catalog` utolsó előtti `book` gyerekelemét találja meg
* `/catalog/book[position()<3]`  - kiválasztja a `catalog` első két `book` gyerekelemét

---

## XPath predicates 2.

* `//title[@lang]`  - kiválasztja az összes `title` elemet, aminek van `lang` attribútuma
* `//title[@lang='eng']` – kiválasztja az összes `title` elemet, aminek `eng` értékű lang `attribútuma` van
* `/catalog/book[price>35.00]` – kiválasztja a `catalog` összes olyan `book` elemét, aminél az `price` attribútum értéke 35.00 felett van
* `/catalog/book[price>35.00]/title` – kiválasztja a `catalog` összes olyan `book` elemének a `title` elemét, amelyeknél a `book` elem `price` attribútumának értéke 35.00 felett van.

---

## XPath ismeretlen csomópontok

* `/bookstore/*` - kiválasztja a `bookstore` összes gyerek elemét
* `//*` - kiválasztja a dokumentum összes elemét
* `//title[@*]` – kiválasztja az összes `title` elemet, ami attribútummal rendelkezik


---

## Selenium WebDriver

* Külön futó alkalmazás
* Selenese és Client API-n hívható
* Vezérli a böngészőt
* Client API különböző nyelveken hívható
* Driver böngészőnként (Firefoxhoz geckodriver, https://github.com/mozilla/geckodriver)

---

## Selenium Pythonnal

* https://selenium-python.readthedocs.io/

---

## PyCharm

* JetBrains fejlesztőeszköz
    * Community
* Projektkezelés
* Autocomplete
* Run, debug

---

## Új projekt

* `requirements.txt` állományba `selenium`
    * Install requirements
* Új könyvtár létrehozása
    * `.py` Ptyhon állományok létrehozása

---

## Első teszteset

```python
from selenium import webdriver

driver = webdriver.Firefox(executable_path="C:\Java\geckodriver-v0.20.1-win64\geckodriver.exe")
driver.get("http://localhost:8080/")
assert "Test Application" in driver.title

# driver.close()
```

---

## Input

```python
nameInput = driver.find_element_by_id("txtUserName")
nameInput.clear()
nameInput.send_keys("vicziani")

button = driver.find_element_by_xpath("/html/body/form/button")
button.click()
```

---

## Elem szövegének vizsgálata

```python
secondAnswerLabel = driver.find_element_by_xpath("/html/body/form/label[2]")
print (secondAnswerLabel.text)
assert "1b válasz" in secondAnswerLabel.text
```

---

## Várakozás

```python
driver.implicitly_wait(5)
```

---

## Újrafelhasználás, page object

---

## Adatvezérelt tesztelés