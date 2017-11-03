/**
 *
 * @author Filip Valchar
 */
package magicthegathering.impl;

import java.util.Arrays;
import magicthegathering.game.AbstractCard;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.ManaType;

/**
 * Class CreatureCardImpl represents creature, extends 
 * AbstractCard and implements CreatureCard interface.
 * 
 * @author Filip Valchar
 */
public class CreatureCardImpl extends AbstractCard implements CreatureCard {
    
    private final String name;
    private final ManaType[] mana;
    private final int power, toughness;
    private boolean summoningSickness;
    
    /**
     * Constructor. Create new creature card.
     * 
     * @param name name of the creature
     * @param mana mana for summon creature
     * @param power for attack
     * @param toughness for defense
     */
    public CreatureCardImpl(String name, ManaType[] mana, int power, int toughness) {
        this.name = name;
        this.mana = mana;
        this.power = power;
        this.toughness = toughness;
        unsetSummoningSickness();
    }
    
    @Override
    public String toString() {
        String result = getName() + " " + Arrays.toString(mana) + " " + getPower() + " / " + getToughness();
        
        if (!hasSummoningSickness()) {
            result += " can attack";
        }
        
        if (isTapped()) {
            result += " TAPPED";
        }
        
        return result;
    }

    @Override
    public int getTotalCost() {
        return getPower() + getToughness();
    }

    @Override
    public int getSpecialCost(ManaType mana) {
        int counter = 0;
        
        for (ManaType iteration : this.mana) {
            if (iteration == mana) {
                counter++;
            }
        }
        
        return counter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public int getToughness() {
        return toughness;
    }

    @Override
    public boolean hasSummoningSickness() {
        return summoningSickness;
    }

    @Override
    public void setSummoningSickness() {
        summoningSickness = true;
    }

    @Override
    public void unsetSummoningSickness() {
        summoningSickness = false;
    }

}

