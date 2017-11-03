# Magic the Gathering

## Strutura projektu

1. Balicek ```magicthegathering``` obsahuje main
2. Balicek ```magicthegathering.game``` obsahuje tridy a rozhrani
3. Balicek ```magicthegathering.impl``` obsahuje implementaci

## Kompilace projektu
```bash
mvn clean install -Dcheckstyle.fail=true
```

## Testy
```bash
target/site/jacoco/index.html
```

## Pravidla hry Magic the Gathering

V [složce doc najdete prirucku s popisem pravidel hry](doc/MagicTheGathering-QuickStartGuide.pdf).
