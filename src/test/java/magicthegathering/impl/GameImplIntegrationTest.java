package magicthegathering.impl;

import magicthegathering.game.Card;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.Game;
import magicthegathering.game.LandCard;
import magicthegathering.game.ManaType;
import magicthegathering.game.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Zuzana Wolfova
 */
public class GameImplIntegrationTest {

    private Player first;
    private Player second;
    private Game game;
    private CreatureCard creature1;
    private CreatureCard creature2;
    private CreatureCard creature3;
    private CreatureCard creature4;

    @Before
    public void setUp() {
        first = new PlayerImpl("Marek");
        second = new PlayerImpl("Zuzka");
        game = new GameImpl(first, second);

        creature1 = new CreatureCardImpl("Artifact creature",
                new ManaType[]{},
                0,
                2);
        creature2 = new CreatureCardImpl("Hybrid creature",
                new ManaType[]{},
                1,
                1);
        creature3 = new CreatureCardImpl("Strong creature",
                new ManaType[]{},
                1,
                2);
        creature4 = new CreatureCardImpl("Weak creature",
                new ManaType[]{},
                0,
                1);
        first.initCards(new Card[]{creature1, creature2});
        second.initCards(new Card[]{creature3, creature4});
    }

    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    @Test
    @RepeatRule.Repeat(times = 500)
    public void testGameAFewTimes() {

        first = new PlayerImpl("Marek");
        second = new PlayerImpl("Zuzka");
        game = new GameImpl(first, second);

        testInit();

        game.prepareCurrentPlayerForTurn();

        firstTurn(game.getCurrentPlayer());

        nextRound();

        firstTurn(game.getCurrentPlayer());

        CreatureCard[] attackingCreatures;

        final int MAX_ATTEMPTS = 100;
        int i;
        for (i = 0; i < MAX_ATTEMPTS; i++) {
            if (gameEnded()) break;
            else {
                nextRound();
                attackingCreatures = attackWithAllCreatures(game.getCurrentPlayer());
                if (attackingCreatures.length != 0) blockWithCreatures(game.getSecondPlayer(), attackingCreatures);
            }
        }

        if (i == MAX_ATTEMPTS) fail("Game is cycling");
    }

    private boolean gameEnded() {
        return game.getCurrentPlayer().isDead() || game.getSecondPlayer().isDead()
                || (game.getCurrentPlayer().getCreaturesOnTable().length == 0
                && game.getSecondPlayer().getCreaturesOnTable().length == 0);
    }

    private void testInit() {
        assertEquals(first, game.getCurrentPlayer());
        assertEquals(second, game.getSecondPlayer());

        game.initGame();

        checkCardsAmount(first);
        checkCardsAmount(second);

        assertEquals(first, game.getCurrentPlayer());

        assertFalse(first.isDead());
        assertFalse(second.isDead());
    }

    private void checkCardsAmount(Player p) {
        assertEquals(Game.LAND_COUNT, p.getLandsInHand().length);
        assertEquals(Game.CREATURE_COUNT, p.getCreaturesInHand().length);
        assertEquals(Game.TOTAL_CARD_AMOUNT, p.getCardsInHand().length);
        assertEquals(0, p.getCardsOnTable().length);
    }

    private void firstTurn(Player p) {
        putEverythingPossibleOnTable(p);
        assertEquals("All creatures should have summoning sickness",
                0, attackWithAllCreatures(game.getCurrentPlayer()).length);

    }

    private void putEverythingPossibleOnTable(Player p) {
        putAllLandsOnTable(p);
        assertEquals(Game.LAND_COUNT, p.getLandsOnTable().length);

        int creaturesOnTableAmount = putCreaturesOnTable(p.getCreaturesInHand());
        assertEquals(creaturesOnTableAmount, p.getCreaturesOnTable().length);
    }

    private void nextRound() {
        Player newCurrent = game.getCurrentPlayer() == first ? second : first;
        game.changePlayer();
        assertEquals(newCurrent, game.getCurrentPlayer());
        game.prepareCurrentPlayerForTurn();
        for (Card c : game.getCurrentPlayer().getCardsOnTable()) assertFalse(c.isTapped());
    }

    private int putCreaturesOnTable(CreatureCard[] creatures) {
        int numberOfCreaturesOnTable = 0;
        for (CreatureCard creature : creatures) {
            if (game.getCurrentPlayer().putCreatureOnTable(creature)) numberOfCreaturesOnTable++;
        }
        return numberOfCreaturesOnTable;
    }

    private void putAllLandsOnTable(Player p) {
        for (LandCard land : p.getLandsInHand()) {
            assertTrue(p.putLandOnTable(land));
        }
        assertEquals(Game.LAND_COUNT, p.getLandsOnTable().length);
    }

    private CreatureCard[] attackWithAllCreatures(Player p) {
        CreatureCard[] attacking = Arrays.stream(p.getCreaturesOnTable())
                .filter(x -> !x.hasSummoningSickness() && !x.isTapped())
                .toArray(CreatureCardImpl[]::new);

        if (attacking.length != 0) {
            assertTrue(game.isCreaturesAttackValid(attacking));
            game.performAttack(attacking);
        }

        for (CreatureCard c : attacking) assertTrue(c.isTapped());

        return attacking;
    }

    private void blockWithCreatures(Player p, CreatureCard[] attacking) {
        CreatureCard[] blocking = Arrays.stream(p.getCreaturesOnTable())
                .filter(x -> !x.isTapped())
                .limit(attacking.length)
                .toArray(CreatureCardImpl[]::new);

        boolean lifeShouldDecrease = false;
        if (blocking.length < attacking.length) {
            blocking = Arrays.copyOf(blocking, attacking.length);
            lifeShouldDecrease = true;
        }

        assertTrue(game.isCreaturesBlockValid(attacking, blocking));

        int oldLife = p.getLife();
        game.performBlockAndDamage(attacking, blocking);
        if (lifeShouldDecrease) assertTrue(p.getLife() < oldLife);
    }

    @Test
    public void attackWithSummoningSickness() {
        game.getCurrentPlayer().putCreatureOnTable(creature1);
        assertFalse(game.isCreaturesAttackValid(new CreatureCard[]{creature1}));
    }

    @Test
    public void attackWithTappedCreature() {
        prepareCreatureForAttack(creature1);
        creature1.tap();
        assertFalse(game.isCreaturesAttackValid(new CreatureCard[]{creature1}));
    }

    @Test
    public void attackWithDuplicateCreature() {
        prepareCreatureForAttack(creature1);
        assertFalse(game.isCreaturesAttackValid(new CreatureCard[]{creature1, creature1}));
    }

    @Test
    public void creatureAttackNotBelongingToCurrentPlayer() {
        CreatureCard c = new CreatureCardImpl("Rogue",
                new ManaType[]{ManaType.BLACK},
                2,
                1);
        c.putOnTable();
        c.unsetSummoningSickness();
        assertFalse(game.isCreaturesAttackValid(new CreatureCard[]{c}));
    }

    @Test
    public void correctAttack() {
        prepareCreatureForAttack(creature1);
        assertTrue(game.isCreaturesAttackValid(new CreatureCard[]{creature1}));
    }

    private void prepareCreatureForAttack(CreatureCard creature) {
        game.getCurrentPlayer().putCreatureOnTable(creature);
        creature.unsetSummoningSickness();
    }

    @Test
    public void blockHasDifferentArraysLength() {
        prepareCreatureForAttack(creature1);
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{null, null}));
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{}));
    }

    @Test
    public void attackOrBlockHasDuplicateCreatures() {
        prepareAttackAndBlock();

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature1},
                new CreatureCard[]{null, null}));

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2, creature2},
                new CreatureCard[]{null, null, creature3}));

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{creature3, creature3}));
    }

    private void prepareAttackAndBlock() {
        prepareAttack(game.getCurrentPlayer());
        prepareAttack(game.getSecondPlayer());
    }

    private void prepareAttack(Player p) {
        for (CreatureCard c : p.getCreaturesInHand()) {
            p.putCreatureOnTable(c);
            c.unsetSummoningSickness();
        }
    }

    @Test
    public void attackOrBlockCreaturesAreNotOnTable() {
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{null}));
        prepareCreatureForAttack(creature1);
        assertFalse(game.isCreaturesBlockValid(new CreatureCard[]{creature1}, new CreatureCard[]{creature3}));
    }

    @Test
    public void attackOrBlockCreaturesDoesNotBelongToPlayer() {
        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{new CreatureCardImpl("Orc", new ManaType[]{}, 1, 1)},
                new CreatureCard[]{null}));

        prepareCreatureForAttack(creature1);
        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1},
                new CreatureCard[]{new CreatureCardImpl("Orc", new ManaType[]{}, 1, 1)}));
    }

    @Test
    public void blockingWithTappedCreature() {
        prepareAttackAndBlock();
        creature3.tap();

        assertFalse(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{creature4, creature3}));

    }

    @Test
    public void correctBlock() {
        prepareAttackAndBlock();

        assertTrue(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{creature4, creature3}));

        assertTrue(game.isCreaturesBlockValid(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{null, null}));
    }

    @Test
    public void nonBlockedCreaturesDamagePlayer() {
        prepareAttackAndBlock();
        game.performBlockAndDamage(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{null, null});
        assertEquals(Player.INIT_LIVES - creature1.getPower() - creature2.getPower(),
                game.getSecondPlayer().getLife());
    }


    @Test
    public void blockingCreatureShouldDie() {
        prepareAttackAndBlock();
        game.performBlockAndDamage(
                new CreatureCard[]{creature2},
                new CreatureCard[]{creature4});
        assertFalse(ArrayUtils.containsCard(creature4, game.getSecondPlayer().getCreaturesOnTable()));
        assertTrue(ArrayUtils.containsCard(creature2, game.getCurrentPlayer().getCreaturesOnTable()));
    }

    @Test
    public void blockedCreatureShouldDie() {
        prepareAttackAndBlock();
        game.performBlockAndDamage(
                new CreatureCard[]{creature2},
                new CreatureCard[]{creature3});
        assertFalse(ArrayUtils.containsCard(creature2, game.getCurrentPlayer().getCreaturesOnTable()));
        assertTrue(ArrayUtils.containsCard(creature3, game.getSecondPlayer().getCreaturesOnTable()));
    }

    @Test
    public void creatureShouldDie() {
        prepareAttackAndBlock();
        game.performBlockAndDamage(
                new CreatureCard[]{creature1, creature2},
                new CreatureCard[]{creature4, creature3});
        assertTrue(ArrayUtils.containsCard(creature1, game.getCurrentPlayer().getCreaturesOnTable()));
        assertFalse(ArrayUtils.containsCard(creature2, game.getCurrentPlayer().getCreaturesOnTable()));
        assertTrue(ArrayUtils.containsCard(creature3, game.getSecondPlayer().getCreaturesOnTable()));
        assertTrue(ArrayUtils.containsCard(creature4, game.getSecondPlayer().getCreaturesOnTable()));
    }

}
