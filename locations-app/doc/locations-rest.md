# REST API leírás

## Lekérdezés

A kedvenc helyek lekérdezhetők a `api/locations` végponton.

## Egy kedvenc hely lekérése

Lekérdezhető a `locations/{id}` végponton, ahol az `id`
a kedvenc hely egyedi azonosítója.

## Kedvenc hely felvétele

A `api/locations` végponton `POST` metódussal kell a következő
JSON-t felküldeni:

```javascript
{"name":"Budapest","coords":"47.4979,19.0402"}
```

## Kedvenc hely módosítása

A `locations/{id}` végpontra kell a következő JSON-t küldeni:

```javascript
{"name":"Budapest","coords":"47.4979,19.0402"}
```

## Kedvenc hely törlése

A `locations/{id}` végponton `DELETE` metódussal.
