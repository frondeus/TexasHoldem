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



### Kompilacja i testy
```
mvn clean compile test assembly:single
```
### Kompilacja testy i uruchomienie w jednym:

####Serwer:
```
mvn clean compile assembly:single test exec:java -Dexec.mainClass="lubiezurek.texasholdem.Program" -Dexec.args="7777 4"
```
Argumenty w kolejności:
* Port
* Liczba Graczy
* Początkowe Pieniądze
* Mała Ciemna
* Duża Ciemna

Lub shellscript (domyślne argumenty):
```
./start_server
```



####Klient:
Klient dostępny jest pod adresem 
[frondeus.pl/TexasHoldem](http://frondeus.pl/TexasHoldem/)

Jeśli przy logowaniu nie wpiszemy żadnego adresu URL, klient domyślnie połączy się z serwerem localhost:7777


Domyślnie klient wczytany z dysku (a nie poprzez powyższy link) jest blokowany (np. przez Google Chrome) ze względów bezpieczeństwa. Aby ominąć zabezpieczenie, należy uruchomić przeglądarkę z odpowiednią flagą. Przykład dla Chromium:
```
chromium --allow-file-access-from-files websocket-client/index.html
```
Shellscript (otwiera on odrazu 4 karty).
```
./start_client
```


### Diagramy i inne

Główne klasy i interfejsy:

![UML](https://github.com/frondeus/TexasHoldem/raw/master/Klasy.png)
