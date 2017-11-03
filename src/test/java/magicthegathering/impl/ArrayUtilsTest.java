package magicthegathering.impl;

import magicthegathering.game.Card;
import magicthegathering.game.LandCardType;
import magicthegathering.game.ManaType;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class tests static array methods.
 *
 * @author Marek Sabo
 */
public class ArrayUtilsTest {

    private Card redLand = new LandCardImpl(LandCardType.MOUNTAIN);
    private Card blueLand = new LandCardImpl(LandCardType.ISLAND);

    private Card creature1 = new CreatureCardImpl("Goblin",
            new ManaType[]{ManaType.RED}, 1, 1);

    private Card creature2 = new CreatureCardImpl("Faerie",
            new ManaType[]{ManaType.BLUE}, 0, 1);
    
    private Card[] cards = new Card[]{
            redLand,
            creature1,
            blueLand,
            creature2,
    };

    @Test
    public void testLandsFiltering() {
        assertArrayEquals(new Card[]{redLand, blueLand}, ArrayUtils.filterLands(cards));
    }

    @Test
    public void testCreaturesFiltering() {
        assertArrayEquals(new Card[]{creature1, creature2}, ArrayUtils.filterCreatures(cards));
    }

    @Test
    public void testHandFiltering() {
        assertArrayEquals(cards, ArrayUtils.filterInHand(cards));
        redLand.putOnTable();
        creature1.putOnTable();
        assertArrayEquals(new Card[]{blueLand, creature2}, ArrayUtils.filterInHand(cards));
    }

    @Test
    public void testOnTableFiltering() {
        redLand.putOnTable();
        creature1.putOnTable();
        assertArrayEquals(new Card[]{redLand, creature1}, ArrayUtils.filterOnTable(cards));
    }

    @SuppressWarnings("AccessStaticViaInstance")
    @Test
    public void arrayWithoutDuplicates() {
        assertFalse(new ArrayUtils().hasDuplicatesExceptNull(cards));
    }

    @Test
    public void arrayWithNullDuplicates() {
        assertFalse(ArrayUtils.hasDuplicatesExceptNull(new Card[] {null, redLand, null, creature1}));
    }

    @Test
    public void arrayWithDuplicates() {
        assertTrue(ArrayUtils.hasDuplicatesExceptNull(new Card[] {null, redLand, null, creature1, redLand}));
    }

    @Test
    public void testContainsCard() {
        assertTrue(ArrayUtils.containsCard(redLand, cards));
        assertTrue(ArrayUtils.containsCard(null, new Card[] {null}));
        assertFalse(ArrayUtils.containsCard(creature2, new Card[] {creature1}));
    }

    @Test
    public void testFindCardIndex() {
        assertEquals(1, ArrayUtils.findCardIndex(null, new Card[] { redLand, null, null}));
        assertEquals(3, ArrayUtils.findCardIndex(creature2, cards));
        assertEquals(-1, ArrayUtils.findCardIndex(creature1, new Card[] { redLand}));
    }

    @Test
    public void testRemoveCard() {
        cards = ArrayUtils.removeCard(creature1, cards);
        assertFalse(ArrayUtils.containsCard(creature1, cards));
        assertEquals(3, cards.length);

        cards = ArrayUtils.removeCard(redLand, cards);
        assertFalse(ArrayUtils.containsCard(redLand, cards));
        assertEquals(2, cards.length);
    }
}
