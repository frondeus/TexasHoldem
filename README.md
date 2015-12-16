# Teksański klińcz

Technologia Programowania.

Lista 4.

Wojciech Polak i Błażej Michalik.

## Kompilacja
[![Build Status](https://travis-ci.org/frondeus/TexasHoldem.svg)](https://travis-ci.org/frondeus/TexasHoldem)

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

Główne klasy i interfejsy:

![UML](https://github.com/frondeus/TexasHoldem/raw/master/Klasy.png)
![Klasy](https://raw.githubusercontent.com/frondeus/TexasHoldem/feature/GameState/klasy.jpg)

Wiadomości i sposób ich przetwarzania:
![Wiadomosci](https://raw.githubusercontent.com/frondeus/TexasHoldem/feature/GameState/wiadomosci.jpg)
