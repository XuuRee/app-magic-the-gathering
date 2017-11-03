package magicthegathering.game;

import magicthegathering.impl.CreatureCardImpl;
import magicthegathering.impl.LandCardImpl;

import java.security.SecureRandom;
import java.util.Random;

import static magicthegathering.game.Game.CREATURE_COUNT;
import static magicthegathering.game.Game.LAND_COUNT;
import static magicthegathering.game.Game.TOTAL_CARD_AMOUNT;

/**
 * Class used for generating cards.
 *
 * @author Marek Sabo, Zuzana Wolfova
 */
public class Generator {

    private static final int CREATURE_MAX_POWER_STRENGTH = 8;
    private static final int CREATURE_MAX_POWER_TOUGHNESS = 5;
    private static final int CREATURE_MAX_MANA = 3;

    private static final Random RANDOM = new SecureRandom();
    private static LandCard[] lands = new LandCard[LAND_COUNT];

    /**
     * Method used for generating {@link Game#TOTAL_CARD_AMOUNT} cards.
     * It generates {@link Game#LAND_COUNT} amount of lands and  {@link Game#CREATURE_COUNT} amount of creatures.
     *
     * @return generated lands and creatures
     */
    public static Card[] generateCards() {
        Card[] cards = new Card[TOTAL_CARD_AMOUNT];

        for (int i = 0; i < LAND_COUNT; i++) {
            lands[i] = generateLand();
            cards[i] = lands[i];
        }

        for (int i = 0; i < CREATURE_COUNT; i++) {
            cards[i + LAND_COUNT] = generateCreature("Creature " + i);
        }

        return cards;
    }


    private static LandCard generateLand() {
        return new LandCardImpl(LandCardType.values()[
                RANDOM.nextInt(LandCardType.values().length)
                ]);
    }

    private static CreatureCard generateCreature(String name) {
        return new CreatureCardImpl(name,
                generateCreatureManaCost(),
                RANDOM.nextInt(CREATURE_MAX_POWER_STRENGTH) + 1,
                RANDOM.nextInt(CREATURE_MAX_POWER_TOUGHNESS) + 1);
    }

    private static ManaType[] generateCreatureManaCost() {
        ManaType[] creatureManaCost = new ManaType[RANDOM.nextInt(CREATURE_MAX_MANA) + 1];
        for (int y = 0; y < creatureManaCost.length; y++) {
            creatureManaCost[y] = lands[RANDOM.nextInt(lands.length)].getManaType();
        }
        return creatureManaCost;
    }


}
