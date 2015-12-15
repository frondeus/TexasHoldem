# Teksański klińcz

Technologia Programowania.

Lista 4.

Wojciech Polak i Błażej Michalik.

## Kompilacja

```
mvn clean compile assembly:single 
```

### Uruchomienie
Serwer:
```
java -jar target/TexasHoldem-0.0.1-SNAPSHOT-jar-with-dependencies.jar -s
```

Klient:
```
java -jar target/TexasHoldem-0.0.1-SNAPSHOT-jar-with-dependencies.jar -c
```

### Diagramy i inne

![Klasy](https://raw.githubusercontent.com/frondeus/TexasHoldem/feature/GameState/klasy.jpg)

![Wiadomosci](https://raw.githubusercontent.com/frondeus/TexasHoldem/feature/GameState/wiadomosci.jpg)
