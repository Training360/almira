# API dokumentáció

## Jóslat létrehozása

A `/api/messages` címre kell elküldeni `POST` metódussal a következő JSON dokumentumot:

```javascript
{
	"content": "Veszedelmes borztámadás lesz...",
	"openAt": "2019-09-17T10:00:00",
	"creator": "Viczi"
}
```

## Jóslatok listázása

A `/api/messages` címen `GET` metódussal.

## Jóslat lekérése

A `/api/messages/{id}` címen `GET` metódussal. Példa: `http://localhost:8080/api/messages/e306c8ca-4ae5-41ab-9f5e-5a193c4a5596`.

## Jóslat módosítása

A `/api/messages/{id}` címen `POST` metódussal. Csak a tartalmat lehet módosítani:

```javascript
{
	"content": "Rozsomáktámadás lesz"
}
```

## Jóslat törlése

A `/api/messages/{id}` címen `DELETE` metódussal.