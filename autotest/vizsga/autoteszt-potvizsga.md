# Tesztautomatizálás pótvizsga

## Batch fájlok

Írj egy `typefile.bat` fájlt, mely a következőképpen működik:

* Nem írhatja ki a parancsokat, mielőtt kiadja azokat
* Ékezetes üzeneteket kell kiírnia, amik jól jelennek meg a konzolban
* Definiálnia kell egy `DEFAULT_FILE` környezeti változót
* A `DEFAULT_FILE` környezeti változó értéke `a.txt` legyen
* A bat fájlt egy paraméterrel kell meghívni, ekkor megvizsgálja, hogy ez a fájl létezik-e. Ha létezik, kiírja annak tartalmát
* Ha a paraméterként átadott fájl nem létezik, akkor kiírja, hogy `Megpróbálom az alapértelmezett <DEFAULT_FILE környezeti változó értéke> fájlt kiírni!`, majd a megvizsgálja, hogy a 
	`DEFAULT_FILE` környezeti változóban definiált fájl létezik-e, ha igen, kiírja. A `<DEFAULT_FILE környezeti változó értéke>` helyén a `DEFAULT_FILE` értéke jelenjen meg!
* Ha a paraméterként átadott fájl nem létezik, és a `DEFAULT_FILE` környezeti változóban definiált fájl sem létezik, kiírja, hogy `Nem találtam fájlt!`

## Selenium IDE

Adott egy alkalmazás, mely két egész számot vár, és ezek alapján megvizsgálja, hogy kinek kell elvégeznie a hitelbírálatot. Az alkalmazás megtalálható a következő címen:
http://tadev.training360.com/epbva/
Tervezd meg fejben a teszteseteket, majd implementáld őket Selenium IDE segítségével! Legalább négy tesztesetet implementálj!
Mentsd el a projektet, és töltd fel!
A projektet és a teszteseteket értelmesen nevezd el! Ne felejtsd az asserteket! A tesztesetekben CSAK a szükséges lépések legyenek, 
egyéb más klikk sem szerepelhet benne!

## SoapUI

Az előző alkalmazás SOAP webszolgáltatást is biztosít. Ennek WSDL állománya elérhető a következő címen:
http://tadev.training360.com/epbva/services/credit-assessment?wsdl
Térképezd fel a webszolgáltatás működését! Milyen input esetén milyen értékeket ad vissza?
Implementáld az előbb megtervezett teszteseteket SoapUI-ban is. Beszédesen nevezd el a teszteseteket!
Ne felejts el assertet írni! Egy Test Suite legyen, azon belül legalább négy teszteset. A tesztesetekben egy lépés elegendő.
A SoapUI projekt fájlt töltsd fel!

## Naplóelemzés

Jelentkezz be SSH-val a `tadev.training360.com` gépre, ahol a felhasználónév `developer`, a jelszó `developer`! 

* Egy alkalmazás napló állományát eléred a HOME könyvtárban (`cd ~`). Írd ide a parancsot, mellyel kilistázod a könyvtár tartalmát!
* Az előző könyvtárban egy fájlt találsz. Másodikként írd ide a parancsot, mely kiírja ezen állomány tartalmát!
* Írd ki ezen állomány utolsó 50 sorát. Írd ide, milyen paranccsal teszed ezt!
* Írd ki, hogy az állomány utolsó 50 sorában hányszor szerepel a `Map` szó! Írd ide, milyen paranccsal teszed ezt!
* Írja ki azokat a sorokat, ahol a `Map` és a `RestTemplate` szó is megtalálható! Írd ide, milyen paranccsal teszed ezt!