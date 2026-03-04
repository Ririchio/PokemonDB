## ДЗ4: Hilt + Room (PokeDex)

Зарайкина Анна Сергеевна
Б9123-09.03.03пикд

### API
PokeAPI (https://pokeapi.co)

### Endpoints
- GET https://pokeapi.co/api/v2/pokemon?limit=50
- GET https://pokeapi.co/api/v2/pokemon/{id}

### Room
Сценарий: Favourites (избранное переживает перезапуск)

Таблица: favorite_pokemon  
Поля:
- pokemonId (Int, PK)
- addedAt (Long)

### Как проверить
1) Открыть список покемонов
2) Нажать сердечко у 1–2 покемонов
3) Перезапустить приложение (закрыть полностью и открыть снова)
4) Перейти в "Избранное" — выбранные покемоны остались
5) (Опционально) В Database Inspector выполнить:
   `SELECT * FROM favorite_pokemon;` — видны строки с pokemonId (звездочка это плохо!!!!)

### Скриншоты
- Loading  
  (loading.png)
- Error + Retry  
  (error_retry.png)
- List  
  (list.png)
- Detail  
  (detail.png)
- Favorites  
  (favorites.png)
- Room (Database Inspector)  
  (room.png)