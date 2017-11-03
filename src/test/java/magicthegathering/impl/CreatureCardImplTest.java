package magicthegathering.impl;

import magicthegathering.game.CreatureCard;
import magicthegathering.game.ManaType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Zuzana Wolfova
 */
public class CreatureCardImplTest {

    private CreatureCard elf;

    @Before
    public void setUp() throws Exception {
        elf = new CreatureCardImpl(
                "Elf",
                new ManaType[]{ManaType.WHITE, ManaType.GREEN, ManaType.GREEN},
                2,
                1);
    }

    @Test
    public void testSummoningSickness() {
        elf.setSummoningSickness();
        assertTrue(elf.hasSummoningSickness());

        elf.unsetSummoningSickness();
        assertFalse(elf.hasSummoningSickness());
    }

    @Test
    public void totalCostIsThree() {
        assertEquals(3, elf.getTotalCost());
    }

    @Test
    public void testGetters() {
        assertEquals("Elf", elf.getName());
        assertEquals(2, elf.getPower());
        assertEquals(1, elf.getToughness());
    }

    @Test
    public void specialCostIsWhiteAndGreen() {
        assertEquals(1, elf.getSpecialCost(ManaType.WHITE));
        assertEquals(0, elf.getSpecialCost(ManaType.RED));
        assertEquals(2, elf.getSpecialCost(ManaType.GREEN));
        assertEquals(0, elf.getSpecialCost(ManaType.BLUE));
        assertEquals(0, elf.getSpecialCost(ManaType.BLACK));
    }

    @Test
    public void toStringMessage() {
        elf.setSummoningSickness();
        assertEquals("Elf [WHITE, GREEN, GREEN] 2 / 1", elf.toString());
        elf.unsetSummoningSickness();
        assertEquals("Elf [WHITE, GREEN, GREEN] 2 / 1 can attack", elf.toString());
        elf.tap();
        assertEquals("Elf [WHITE, GREEN, GREEN] 2 / 1 can attack TAPPED", elf.toString());
        elf.setSummoningSickness();
        assertEquals("Elf [WHITE, GREEN, GREEN] 2 / 1 TAPPED", elf.toString());
    }

}
