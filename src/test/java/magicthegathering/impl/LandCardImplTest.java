package magicthegathering.impl;

import magicthegathering.game.LandCard;
import magicthegathering.game.LandCardType;
import magicthegathering.game.ManaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Zuzana Wolfova
 */
public class LandCardImplTest {

    @Test
    public void testGetLandType() {
        LandCard card = new LandCardImpl(LandCardType.ISLAND);
        assertEquals(LandCardType.ISLAND, card.getLandType());
    }

    @Test
    public void testGetMana() {
        testMana(ManaType.WHITE, new LandCardImpl(LandCardType.PLAINS));
        testMana(ManaType.RED, new LandCardImpl(LandCardType.MOUNTAIN));
        testMana(ManaType.GREEN, new LandCardImpl(LandCardType.FOREST));
        testMana(ManaType.BLUE, new LandCardImpl(LandCardType.ISLAND));
        testMana(ManaType.BLACK, new LandCardImpl(LandCardType.SWAMP));
    }

    private void testMana(ManaType manaType, LandCard card) {
        assertEquals(manaType, card.getManaType());
    }

    @Test
    public void test100EnumsCoverage() {
        LandCardType.valueOf(LandCardType.PLAINS.toString());
        ManaType.valueOf(ManaType.WHITE.toString());
    }


    @Test
    public void toStringMessage() {
        assertEquals("Land swamp, BLACK",new LandCardImpl(LandCardType.SWAMP).toString());
        assertEquals("Land plains, WHITE",new LandCardImpl(LandCardType.PLAINS).toString());
    }

}
