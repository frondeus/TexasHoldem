# Teksański klińcz

Technologia Programowania.
Lista 4.
Wojciech Polak i Błażej Michalik.

### Status
* Master
  [![Build Status](https://travis-ci.org/frondeus/TexasHoldem.svg?branch=master)](https://travis-ci.org/frondeus/TexasHoldem)
  [![codecov.io](https://codecov.io/github/frondeus/TexasHoldem/coverage.svg?branch=master)](https://codecov.io/github/frondeus/TexasHoldem?branch=master)
  ![codecov.io](https://codecov.io/github/frondeus/TexasHoldem/branch.svg?branch=master)
* Dev: 
  [![Build Status](https://travis-ci.org/frondeus/TexasHoldem.svg?branch=dev)](https://travis-ci.org/frondeus/TexasHoldem)
  [![codecov.io](https://codecov.io/github/frondeus/TexasHoldem/coverage.svg?branch=dev)](https://codecov.io/github/frondeus/TexasHoldem?branch=dev)
  ![codecov.io](https://codecov.io/github/frondeus/TexasHoldem/branch.svg?branch=dev)

### Kompilacja testy i uruchomienie w jednym:
```
mvn clean compile assembly:single test exec:java -Dexec.mainClass="lubiezurek.texasholdem.Program"
```

### Kompilacja
```
mvn clean compile test assembly:single
```

### Uruchomienie
Serwer:
```
java -jar target/TexasHoldem-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Klient:
```
chromium --allow-file-access-from-files websocket-client/index.html
```

### Diagramy i inne

Główne klasy i interfejsy:

![UML](https://github.com/frondeus/TexasHoldem/raw/master/Klasy.png)
![Klasy](https://raw.githubusercontent.com/frondeus/TexasHoldem/feature/GameState/klasy.jpg)

Wiadomości i sposób ich przetwarzania:
![Wiadomosci](https://raw.githubusercontent.com/frondeus/TexasHoldem/feature/GameState/wiadomosci.jpg)
