/**
 *
 * @author Filip Valchar
 */
package magicthegathering.impl;

import magicthegathering.game.AbstractCard;
import magicthegathering.game.LandCardType;
import magicthegathering.game.LandCard;
import magicthegathering.game.ManaType;

/**
 * Class LandCardImpl represents land, extends 
 * AbstractCard and implements LandCard interface.
 * 
 * @author Filip Valchar
 */
public class LandCardImpl extends AbstractCard implements LandCard {
    
    private final LandCardType land;
    
    /**
     * Constructor. Create new land card.
     * 
     * @param land type of land 
     */
    public LandCardImpl(LandCardType land) {
        this.land = land;
    }
    
    @Override
    public String toString() {
        String result = "Land " + getLandType().name().toLowerCase();
        ManaType type = getManaType();
        
        if (type != null) {
            result += ", " + type.name();
        }
        
        return result;   
    }

    @Override
    public LandCardType getLandType() {
        return land;
    }

    @Override
    public ManaType getManaType() {
        switch (getLandType()) {
            case FOREST:
                return ManaType.GREEN;
            case ISLAND:
                return ManaType.BLUE;
            case MOUNTAIN:
                return ManaType.RED;
            case PLAINS:
                return ManaType.WHITE;
            case SWAMP:
                return ManaType.BLACK;
            default:
                return null;
        }
    }
    
}
