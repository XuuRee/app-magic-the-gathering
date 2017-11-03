# Domáca úloha č. 1 - Magic the Gathering

**Zverejnenie úlohy:** 7. 10. 2017

**Deadline:** 28. 10. 2017 23:59

## Predpoklady

Pre úspešnú implementáciu musíte poznať:

* Vytváranie tried, koštruktor
* Rozdiel medzi triedou a objektom
* Prepisovanie metód
* Statické premenné a metódy
* Pole
* Rozhrania (vytváranie a implementacia)
* Základné princípy dedičnosti

## Štruktúra projektu

1. Balíček ```magicthegathering``` obsahuje main a aplikačnú logiku
  - **Prosím nemodifikujte žiadnu z týchto tried**
2. Balíček ```magicthegathering.game``` obsahuje triedy a rozhrania, ktoré sú súčasťou zadania
  - **Prosím nemodifikujte žiadnu z týchto tried**
3. Package  ```magicthegathering.impl``` bude obsahovat Vašu implementáciu
  - **Čokoľvek mimo tohto balíčku bude pri hodnotení ignorované**

## Kompilácia projektu

Projekt môžte mimo vývojového prostredia skompilovať pomocou nasledujúceho príkazu:

```bash
mvn clean install -Dcheckstyle.fail=true
```

Pokrytie testov nájdete po kompilácii v `target/site/jacoco/index.html`.

## Pravidlá hry Magic the Gathering

V tomto projekte budete implementovať zjednodušenú hru _Magic the Gathering_.
V [zložke `doc` nájdete príručku s popisom pravidiel hry](doc/MagicTheGathering-QuickStartGuide.pdf), 
ktoré slúžia na objasnenie nasledujúcich pravidiel:

_Magic the Gathering_ je kartová hra pre dvoch hráčov.
Každý hráč má 20 životov a má k dispozícii karty typu _land_ a _creature_.
Karta typu land predstavuje predstavuje krajinu, ktorá generuje určitý typ many.
V príručke na str. 4 nájdete priradenie jednotlivých typov landov (Plains, Swamp, ...) k jednotlivým typom many.
Bezfarebnú manu v našom magicku nepoužívame. Rovnako nepoužívame iné typy kariet, či vlastnosti príšer.  

Za manu, ktorú produkuje land, je možné si potom "kúpiť" či vyvolať požadovanú príšeru.
Každá príšera má svoju cenu (cost), meno, power (silu útoku) a toughness (silu obrany).
Hráči môžu prostredníctvom príšer útočiť na súpera, ktorý sa buď ubráni svojimi príšerami, alebo príde o svoje životy.
Platba manou za príšeru laebo útok príšery sa robí pomocou tzv. "tapnutia" karty 
(v reálnej hre sa karta na stole obráti o 90°).
Tapnutie znamená, že karta už je v momentálnom ťahu použitá a nemožno ju v danom ťahu použiť znova.
Okrem operácie tap/untap majú príšery ešte špeciálny atribút zvaný _summoning sickness_.
Po vyložení na stôl "ochorejú" a útočiť môžu až v ďalšom kole. Príšery so summoning sickness však môžu hráča brániť. 

Pre zjednodušenie hry nemáme žiaden balíček, všetky svoje karty má hráč od začiatku na ruke a postupne ich vykladá.

## Priebeh jedného kola

### 0. Príprava na kolo
   
Hráč untapne všetky svoje karty a zruší summoning sickness u všetkých svojich príšer.

### 1. Fáza vyloženia landu

Hráč môže vyložiť na stôl jednu zo svojim kariet typu land (voľba závisí na hráčovi).

### 2. Fáza povolávania príšer

Hráč si vyberie príšeru, ktorú by chcel vyložiť na stôl.
Ak má hráč na stole dostatok NEtapnutej many správneho typu, táto mana bude tapnutá, príšera vyložená na stôl.
Hráč si môže v tomto kole kúpiť viac príšer.

### 3. Útok / Obrana

Hráč si vyberie príšery, ktorými chce útočiť.
Útočiace príšery musia byť NEtaputé a musia byť schopné útoku (nemajú summoning sickness).
Blokovať môžu iba netapnuté príšery. Každá príšera môže útočiť a blokovať iba raz
(t.j. nemôže útočiť dvakrát, ani blokujúca príšera nemôže blokovať dve príšery).

### 4. Vyhodnotenie útoku, zranenia

Ak hráč príšeru neblokoval, príšera mu zoberie toľko životov, aká je jej sila.
Ak hráč príšeru blokoval, príšery si vymenia zranenia.
Ak je počet zranení príšery aspoň taký veľký ako jej obrana/výdrž (toughness), tak príšera zomrie (zmizne zo stolu).
Napríklad, keď si príšery 3 / 1 a 2 / 4 vymenia zranenia, tak 3 / 1 zomrie, ale 2 / 4 prežije. 

Hra končí, akonáhle jeden z hráčov umrie, t.j. jeho život klesne na/pod 0.

## Implementácia úlohy

Vašou úlohou je postupne implementovať rozhrania popísané nižšie a budú sa nachádzať v balíčku `magicthegathering.impl`.

Úloha obsahuje spustiteľný súbor, ktorý simuluje hru.
Pokiaľ správne implementujete úlohu, mali by ste byť schopní si náš jednoduchý magic reálne zahrať :-)

V každej triede implementujte príslušný konštruktor. Nepoužívajte set metódy, pokiaľ nie sú uvedené v rozhraniach.

### Krok 1: Implementujte `LandCard` a `CreatureCard` 

Triedy _CreatureCardImpl_ a _LandCardImpl_ dedia z `AbstractCard` a slúžia k uchovávaniu stavových informácií o sebe.

Metóda `toString()` vráti pre land: `Land <typ landu>, <typ many>`, pričom typ landu je malými písmenami.
 
Metóda `toString()` vráti pre príšeru: `<name> <cost> <power> / <toughness>` + ak príšera nemá summoning sickness,
na koniec reťazca sa pridá ` can attack`, ak je tapnutá, tak ` TAPPED`. 

### Krok 2: Implementujte `ArrayUtils`

Trieda slúži na prácu s poľom a obsahuje statické metódy:

- `LandCard[] filterLands(Card[] cards)` vyfiltruje z kariet landy
- `CreatureCard[] filterCreatures(Card[] cards)` vyfiltruje z kariet príšery
- `Card[] filterInHand(Card[] cards)` vyfiltruje karty na ruke
- `Card[] filterOnTable(Card[] cards)` vyfiltruje karty na stole
- `boolean hasDuplicatesExceptNull(Card[] cards)` zistí, či pole obsahuje 2 rovnaké prvky (`null` sa neráta)
- `boolean containsCard(Card searchedCard, Card[] cards)` zistí, či pole obsahuje hľadanú kartu 
- `int findCardIndex(Card searchedCard, Card[] cards)` vráti index poľa hľadanej karty, 
-1, ak sa v poli karta nenachádza
- `Card[] removeCard(Card unwantedCard, Card[] cards)` odstráni kartu z poľa a vráti nové pole
  (prvky nemusia byť v pôvodnom poradí)

Metódy vždy vracajú nové pole, keďže veľkosť poľa sa nedá dynamicky meniť.

### Krok 3: Implementujte `Player`

Trieda _PlayerImpl_ obsahuje ako stavové informácie, tak aj značnú časť hernej logiky či správu stavu hráča.

Metóda `toString()` vráti meno hráča a v zátvorke počet jeho životov, napr. `Marek(20)`. 

### Krok 4: Implementujte `Game`

Trieda _GameImpl_ poskytuje správu hráčov, kôl, útokov a blokov. 
Zároveň by táto trieda mala v metóde `initGame()` vygenerovať karty použitím triedy `Generator`.
Útok a blokovanie príšer je vo forme dvoch polí. 
Polia `[ creature1, creature2], [ null, creature3]` reprezentujú 2 útočiace príšery a jednu blokujúcu.
Prvá príšera je neblokovaná (damage ide do hráča), druhá je blokovaná príšerou č. 3 (vymenia si zranenia).

## Hinty

- Metódy by mali používať existujúce metódy, napr. `getCardsInHand()` používa `filterInHand`.
- Existuje ternárny operátor podmienky: `1 == 1 ? "yes" : "no".
- Ak nie je explicitne povedané, nemusíte riešiť, že samotné polia alebo ich prvky môžu byť `null`.
- V `ArrayUtils` využite `Arrays.copyOf` a `instanceof`.
- Na vypisovanie poľa v `CreatureCardImpl` využijte `Arrays.toString`.
- Metóda `removeCard` môže využiť metódu swap (prehodiť 2 prvky).
