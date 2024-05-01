# Dokumentowe bazy danych – MongoDB

ćwiczenie 2


---

**Imiona i nazwiska autorów:**
Łukasz Kluza, Mateusz Sacha
--- 


## Yelp Dataset

- [www.yelp.com](http://www.yelp.com) - serwis społecznościowy – informacje o miejscach/lokalach
- restauracje, kluby, hotele itd. `businesses`,
- użytkownicy odwiedzają te miejsca - "meldują się"  `check-in`
- użytkownicy piszą recenzje `reviews` o miejscach/lokalach i wystawiają oceny oceny,
- przykładowy zbiór danych zawiera dane z 5 miast: Phoenix, Las Vegas, Madison, Waterloo i Edinburgh.

# Zadanie 1 - operacje wyszukiwania danych

Dla zbioru Yelp wykonaj następujące zapytania

W niektórych przypadkach może być potrzebne wykorzystanie mechanizmu Aggregation Pipeline

[https://www.mongodb.com/docs/manual/core/aggregation-pipeline/](https://www.mongodb.com/docs/manual/core/aggregation-pipeline/)


1. Zwróć dane wszystkich restauracji (kolekcja `business`, pole `categories` musi zawierać wartość "Restaurants"), które są otwarte w poniedziałki (pole hours) i mają ocenę co najmniej 4 gwiazdki (pole `stars`).  Zapytanie powinno zwracać: nazwę firmy, adres, kategorię, godziny otwarcia i gwiazdki. Posortuj wynik wg nazwy firmy.

2. Ile każda firma otrzymała ocen/wskazówek (kolekcja `tip` ) w 2012. Wynik powinien zawierać nazwę firmy oraz liczbę ocen/wskazówek Wynik posortuj według liczby ocen (`tip`).

3. Recenzje mogą być oceniane przez innych użytkowników jako `cool`, `funny` lub `useful` (kolekcja `review`, pole `votes`, jedna recenzja może mieć kilka głosów w każdej kategorii).  Napisz zapytanie, które zwraca dla każdej z tych kategorii, ile sumarycznie recenzji zostało oznaczonych przez te kategorie (np. recenzja ma kategorię `funny` jeśli co najmniej jedna osoba zagłosowała w ten sposób na daną recenzję)

4. Zwróć dane wszystkich użytkowników (kolekcja `user`), którzy nie mają ani jednego pozytywnego głosu (pole `votes`) z kategorii (`funny` lub `useful`), wynik posortuj alfabetycznie według nazwy użytkownika.

5. Wyznacz, jaką średnia ocenę uzyskała każda firma na podstawie wszystkich recenzji (kolekcja `review`, pole `stars`). Ogranicz do firm, które uzyskały średnią powyżej 3 gwiazdek.

	a) Wynik powinien zawierać id firmy oraz średnią ocenę. Posortuj wynik wg id firmy.

	b) Wynik powinien zawierać nazwę firmy oraz średnią ocenę. Posortuj wynik wg nazwy firmy.

## Zadanie 1  - rozwiązanie

> Wyniki: 
> 
> przykłady, kod, zrzuty ekranów, komentarz ...

```js
db.business.find({"categories": "Restaurants", "stars" : {$gte : 4}, "hours.Monday":{$exists : true}},
    {"_id": 0,"name" : 1, "full_address" : 1, "categories": 1, "hours": 1, "stars" :1}).sort({"name": 1})
```
![alt text](images/image-22.png)


```js
db.tip.aggregate([
  {
    $match: {
      date: { $gte: "2012-01-01", $lt: "2013-01-01" }
    }
  },
  {
    $group: {
      _id: "$business_id",
      tip_count: { $sum: 1 }
    }
  },
  {
    $lookup: {
      from: "business",
      localField: "_id",
      foreignField: "business_id",
      as: "business_info"
    }
  },
  {
    $unwind: "$business_info"
  },
  {
    $project: {
      _id: 0,
      name: "$business_info.name",
      tip_count: 1
    }
  },
//  {
//    $sort: { 'tip_count': -1 }
//  }
])
```

![alt text](images/image-23.png)

```js
db.review.aggregate([
    {
        $unwind: "$votes"
    },
    {
        $group:{
            _id: null,
            funny_sum: { $sum: "$votes.funny"},
            funny_useful: { $sum: "$votes.useful"},
            funny_cool: { $sum: "$votes.cool" }

        }
    },
    {
        $project:{
            _id: 0
        }
    }
])
```

![alt text](images/image-24.png)

```js
db.user.aggregate([
  {
    $group: {
      _id: "$_id",
      funny_sum: { $sum: "$votes.funny" },
      useful_sum: { $sum: "$votes.useful" },
      cool_sum: { $sum: "$votes.cool" },
      name: {$first: "$name"}
    }
  },
  {
    $match: { $or: [{ "funny_sum": 0 }, { "useful_sum": 0 }] }
  },
  {
    $sort :{ "name" : 1}
  },
  {
    $project: {
      _id: 0,
      name: 1
    }
  }
])
```
![alt text](images/image-25.png)

```js
db.business.aggregate([
  {
    $lookup: {
      from: "review",
      localField: "business_id",
      foreignField: "business_id",
      as: "reviews"
    }
  },
  {
    $group: {
      _id: 0,
      name: { $first: "$name" },
      average_stars: { $avg: "$reviews.stars" }
    }
  },
//  {
//    $sort: { "name": 1 }
//  }
])
```

# Zadanie 2 - modelowanie danych


Zaproponuj strukturę bazy danych dla wybranego/przykładowego zagadnienia/problemu

Należy wybrać jedno zagadnienie/problem (A lub B)

Przykład A
- Wykładowcy, przedmioty, studenci, oceny
	- Wykładowcy prowadzą zajęcia z poszczególnych przedmiotów
	- Studenci uczęszczają na zajęcia
	- Wykładowcy wystawiają oceny studentom
	- Studenci oceniają zajęcia

Przykład B
- Firmy, wycieczki, osoby
	- Firmy organizują wycieczki
	- Osoby rezerwują miejsca/wykupują bilety
	- Osoby oceniają wycieczki

a) Warto zaproponować/rozważyć różne warianty struktury bazy danych i dokumentów w poszczególnych kolekcjach oraz przeprowadzić dyskusję każdego wariantu (wskazać wady i zalety każdego z wariantów)

b) Kolekcje należy wypełnić przykładowymi danymi

c) W kontekście zaprezentowania wad/zalet należy zaprezentować kilka przykładów/zapytań/zadań/operacji oraz dla których dedykowany jest dany wariantów

W sprawozdaniu należy zamieścić przykładowe dokumenty w formacie JSON ( pkt a) i b)), oraz kod zapytań/operacji (pkt c)), wraz z odpowiednim komentarzem opisującym strukturę dokumentów oraz polecenia ilustrujące wykonanie przykładowych operacji na danych

Do sprawozdania należy kompletny zrzut wykonanych/przygotowanych baz danych (taki zrzut można wykonać np. za pomocą poleceń `mongoexport`, `mongdump` …) oraz plik z kodem operacji zapytań (załącznik powinien mieć format zip).


## Zadanie 2  - rozwiązanie

> Wyniki: 

### a)
#### Wariant 1 (embedding):
```JSON
Embedded documents:
{
  "_id": ObjectId,
  "company": {
    "name": String,
    "location": String
  },
  "tour": {
    "name": String,
    "date": Date,
    "duration": Number
  },
  "person": {
    "name": String,
    "email": String
  },
  "reservation": {
    "seats": Number,
    "price": Number
  },
  "rating": Number
}
```

#### Wariant 2 (references):
```JSON
Companies collection:
{
  "_id": ObjectId,
  "name": String,
  "location": String
}
```
```JSON
Trips collection:
{
  "_id": ObjectId,
  "companyId": ObjectId,
  "name": String,
  "date": Date,
  "duration": Number
}
```
```JSON
Persons collection:
{
  "_id": ObjectId,
  "name": String,
  "email": String
}
```
```JSON
Reservations collection:
{
  "_id": ObjectId,
  "tourId": ObjectId,
  "personId": ObjectId,
  "seats": Number,
  "price": Number,
  "rating": Number
}
```

### b)
#### Wariant 1 (przykładowe dane):

```JSON
{
  "_id":{"$oid":"66322ba7fc33997b00b115ac"},
  "company":{
    "name":"Adventure Tours Inc.",
    "location":"New York"
    },
  "tour":{
    "name":"City Bike Tour",
    "date":{"$date":"2024-05-10T09:00:00Z"},
    "duration":3
    },
  "person":{
    "name":"John Doe",
    "email":"john@example.com"
    },
  "reservation":{
    "seats":2,
    "price":100
    },
  "rating":4
}
```
![alt text](image.png)

#### Wariant 2 (przykładowe dane):
```JSON
Companies collection:
{
  "_id": ObjectId("60994c3d65c84c4bf432fc1a"),
  "name": "Adventure Tours Inc.",
  "location": "New York"
}
```
![alt text](image-1.png)
```JSON
Tours collection:
{
  "_id": ObjectId("60994c4f65c84c4bf432fc1b"),
  "companyId": ObjectId("60994c3d65c84c4bf432fc1a"),
  "name": "City Bike Tour",
  "date": ISODate("2024-05-10T09:00:00Z"),
  "duration": 3
}
```
![alt text](image-2.png)
```JSON
Persons collection:
{
  "_id": ObjectId("60994c7e65c84c4bf432fc1c"),
  "name": "John Doe",
  "email": "john@example.com"
}
```
![alt text](image-3.png)
```JSON
Reservations collection:
{
  "_id": ObjectId("60994c4f65c84c4bf432fc1b"),
  "tourId": ObjectId("60994c4f65c84c4bf432fc1b"),
  "personId": ObjectId("60994c7e65c84c4bf432fc1c"),
  "seats": 2,
  "price": 100,
  "rating": 4
}
```
![alt text](image-4.png)
---

Punktacja:

|         |     |
| ------- | --- |
| zadanie | pkt |
| 1       | 0,6 |
| 2       | 1,4 |
| razem   | 2   |



