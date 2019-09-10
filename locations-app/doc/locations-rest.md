# REST API leírás

## Lekérdezés

A kedvenc helyek lekérdezhetők a `api/locations` végponton. Két paraméter adható meg a lapozáshoz.
A `page` az oldal (nullától számozva), a `size`, az oldal mérete, hogy mennyi találat legyen egy oldalon.
Alapértelmezett paraméterezése: `/api/locations?page=0&size=10`.  

A visszaadott eredmény:

```javascript
{
    "content": [
        {
            "id": 40,
            "name": "Debrecen",
            "lat": 47.5316049,
            "lon": 21.6273124,
            "tags": []
        },
        {
            "id": 33,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 18,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 43,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 28,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 53,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 13,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 38,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 5,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 23,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        }
    ],
    "first": false,
    "last": false,
    "totalPages": 6,
    "totalElements": 54,
    "numberOfElements": 10,
    "size": 10,
    "number": 2,
    "sort": [
        {
            "property": "name",
            "direction": "ASC",
            "ignoreCase": false,
            "nullHandling": "NATIVE"
        }
    ]
}
```

## Egy kedvenc hely lekérése

Lekérdezhető a `locations/{id}` végponton, ahol az `id`
a kedvenc hely egyedi azonosítója.

A visszaadott eredmény:

```javascript
{
    "id": 1,
    "name": "Budapest",
    "lat": 47.497912,
    "lon": 19.040235
}
```

Amennyiben a megadott azonosítóval nincs kedvenc hely, `404` sátuszkóddal
tér vissza, és az eredmény:

```javascript
{
    "message": "Not found"
}
```

## Kedvenc hely felvétele

A `api/locations` végponton `POST` metódussal kell a következő
JSON-t felküldeni:

```javascript
{"name":"Budapest","coords":"47.497912,19.040235"}
```

A visszaadott eredmény:

```javascript
{
    "id": 1,
    "name": "Budapest",
    "lat": 47.497912,
    "lon": 19.040235
}
```

Amennyiben helytelenek az adatok, `400` státuszkóddal tér vissza. 
Ellenőrzések:

* Nem lehet üres a név. `Name can not be empty!`
* A koordináták nem megfelelő formátumban vannak megadva. `Invalid coordinates format!`
* A szélességi fok értéke -90 és 90 között kell legyen. `Latitude must be between -90 and 90`
* A hosszúsági fok értéke -180 és 180 között kell legyen. `Longitude must be between -180 and 180`

## Kedvenc hely módosítása

A `locations/{id}` végpontra kell a következő JSON-t küldeni:

```javascript
{"name":"Budapest","coords":"47.497912,19.040235"}
```

A visszaadott eredmény:

```javascript
{
    "id": 1,
    "name": "Budapest",
    "lat": 47.497912,
    "lon": 19.040235
}
```

Amennyiben a megadott azonosítóval nincs kedvenc hely, `404` sátuszkóddal
tér vissza. További ellenőrzések, mint a _Kedvenc hely felvitele_
funkciónál.

## Kedvenc hely törlése

A `locations/{id}` végponton `DELETE` metódussal.

Amennyiben a megadott azonosítóval nincs kedvenc hely, `404` sátuszkóddal
tér vissza.
