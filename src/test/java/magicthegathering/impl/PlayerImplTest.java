package magicthegathering.impl;

import magicthegathering.game.Card;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.LandCard;
import magicthegathering.game.LandCardType;
import magicthegathering.game.ManaType;
import magicthegathering.game.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Zuzana Wolfova
 */
public class PlayerImplTest {

    private PlayerImpl marek;
    private CreatureCardImpl kitkin;
    private LandCardImpl plains;
    private LandCardImpl swamp;
    private LandCard[] landsAllColors;

    @Before
    public void setUp() throws Exception {
        marek = new PlayerImpl("Marek");
        plains = new LandCardImpl(LandCardType.PLAINS);
        swamp = new LandCardImpl(LandCardType.SWAMP);
        kitkin = new CreatureCardImpl(
                "Kitkin",
                new ManaType[]{ManaType.WHITE},
                2,
                1);
        marek.initCards(new Card[]{
                swamp,
                plains,
                kitkin
        });
        landsAllColors = new LandCard[]{
                new LandCardImpl(LandCardType.PLAINS),
                new LandCardImpl(LandCardType.MOUNTAIN),
                new LandCardImpl(LandCardType.FOREST),
                new LandCardImpl(LandCardType.ISLAND),
                new LandCardImpl(LandCardType.SWAMP)
        };
    }

    @Test
    public void testNameAndLife() {
        assertEquals("Marek", marek.getName());

        assertEquals(Player.INIT_LIVES, marek.getLife());
        int subtract = 8;
        marek.subtractLives(subtract);
        assertEquals(Player.INIT_LIVES - subtract, marek.getLife());
    }

    @Test
    public void playerIsDead() {
        marek.subtractLives(20);
        assertEquals(true, marek.isDead());
    }

    @Test
    public void playerIsAlive() {
        marek.subtractLives(19);
        assertEquals(false, marek.isDead());
    }

    @Test
    public void testCardsInHand() {

        for (Card card : marek.getCardsInHand()) {
            assertFalse(card.isTapped());
            assertFalse(card.isOnTable());
        }

        assertEquals(1, marek.getCreaturesInHand().length);
        assertEquals(2, marek.getLandsInHand().length);
        assertEquals(3, marek.getCardsInHand().length);
    }


    @Test
    public void testCardsOnTable() {

        putEverythingUntappedOnTable();

        for (Card card : marek.getCardsOnTable()) {
            assertFalse(card.isTapped());
            assertTrue(card.isOnTable());
        }

        assertEquals(1, marek.getCreaturesOnTable().length);
        assertEquals(2, marek.getLandsOnTable().length);
        assertEquals(3, marek.getCardsOnTable().length);
    }

    private void putEverythingUntappedOnTable() {
        marek.putLandOnTable(plains);
        marek.putLandOnTable(swamp);
        marek.putCreatureOnTable(kitkin);
        plains.untap();
    }

    @Test
    public void testUntapAllCards() {

        putEverythingUntappedOnTable();
        plains.tap();
        kitkin.tap();

        assertTrue(plains.isTapped());
        assertTrue(kitkin.isTapped());

        marek.untapAllCards();

        assertFalse(plains.isTapped());
        assertFalse(swamp.isTapped());
        assertFalse(kitkin.isTapped());
    }

    @Test
    public void putLandOnTableTwice() {
        assertTrue(marek.putLandOnTable(plains));
        assertFalse(marek.putLandOnTable(plains));
    }

    @Test
    public void putLandOnTableWhichIsNotInHand() {
        assertFalse(marek.putLandOnTable(new LandCardImpl(LandCardType.MOUNTAIN)));
    }

    @Test
    public void putCreatureOnTableWithoutLand() {
        assertFalse(marek.putCreatureOnTable(kitkin));
    }

    @Test
    public void putCreatureOnTableTwice() {
        marek.putLandOnTable(plains);
        assertTrue(marek.putCreatureOnTable(kitkin));

        assertFalse(kitkin.isTapped());
        assertTrue(kitkin.hasSummoningSickness());

        assertFalse(marek.putCreatureOnTable(kitkin));
    }

    @Test
    public void putCreatureOnTableCorrectly() {
        assertTrue(marek.putLandOnTable(plains));
        assertTrue(marek.putCreatureOnTable(kitkin));
    }

    @Test
    public void putCreatureOnTableWhichIsNotInHand() {
        assertTrue(marek.putLandOnTable(plains));
        assertFalse(marek.putCreatureOnTable(
                new CreatureCardImpl("White Knight", new ManaType[] {ManaType.WHITE}, 1, 1)));
    }

    @Test
    public void preparedCreatureCanAttack() {
        putEverythingUntappedOnTable();
        marek.prepareAllCreatures();
        assertFalse(kitkin.hasSummoningSickness());
    }

    @Test
    public void testSummoningSickness() {
        putEverythingUntappedOnTable();
        assertTrue(marek.getCreaturesOnTable()[0].hasSummoningSickness());
    }

    @Test
    public void testManaForCreature() {
        assertFalse(marek.hasManaForCreature(kitkin));
        marek.putLandOnTable(swamp);
        assertFalse(marek.hasManaForCreature(kitkin));
        marek.putLandOnTable(plains);
        assertTrue(marek.hasManaForCreature(kitkin));
    }

    @Test
    public void testAllManaTypes() {
        CreatureCard hybrid = new CreatureCardImpl("Hybrid",
                new ManaType[]{
                        ManaType.WHITE,
                        ManaType.RED,
                        ManaType.GREEN,
                        ManaType.BLUE,
                        ManaType.BLACK
                }, 1, 1);
        Player eva = new PlayerImpl("Eva");
        eva.initCards(new Card[] {
                landsAllColors[0], landsAllColors[1], landsAllColors[2],
                landsAllColors[3], landsAllColors[4], hybrid
        });

        assertFalse(eva.hasManaForCreature(hybrid));
        eva.putLandOnTable(landsAllColors[0]);
        assertFalse(eva.hasManaForCreature(hybrid));
        eva.putLandOnTable(landsAllColors[1]);
        assertFalse(eva.hasManaForCreature(hybrid));
        eva.putLandOnTable(landsAllColors[2]);
        assertFalse(eva.hasManaForCreature(hybrid));
        eva.putLandOnTable(landsAllColors[3]);
        assertFalse(eva.hasManaForCreature(hybrid));
        eva.putLandOnTable(landsAllColors[4]);
        assertTrue(eva.hasManaForCreature(hybrid));
    }

    @Test
    public void testUntappedLandsCalculation() {
        int[] lands = { 0, 0, 0, 0, 0, };
        Player chris = new PlayerImpl("Chris");
        chris.initCards(landsAllColors);

        assertArrayEquals(lands, chris.calculateUntappedLands());

        for (int i = 0; i < 5; i++) {
            lands[i] += 1;
            chris.putLandOnTable(landsAllColors[i]);
            assertArrayEquals(lands, chris.calculateUntappedLands());
        }

        chris.getLandsOnTable()[1].tap();
        lands[1] -= 1;
        assertArrayEquals(lands, chris.calculateUntappedLands());

    }

    @Test
    public void testTappedManaForCreature() {
        marek.putLandOnTable(plains);
        marek.putLandOnTable(swamp);
        marek.tapManaForCreature(kitkin);
        assertTrue(plains.isTapped());
        assertFalse(swamp.isTapped());
    }


    @Test
    public void creatureIsDestroyed() {
        marek.putLandOnTable(plains);
        marek.putCreatureOnTable(kitkin);
        assertEquals(1, marek.getCreaturesOnTable().length);
        marek.destroyCreature(kitkin);
        assertEquals(0, marek.getCreaturesOnTable().length);
    }

    @Test
    public void testStringMessage() {
        assertEquals("Marek(20)", marek.toString());
    }
}
