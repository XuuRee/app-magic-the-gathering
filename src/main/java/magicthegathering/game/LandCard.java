package magicthegathering.game;

/**
 * Land Card interface.
 *
 * @author Zuzana Wolfova
 */
public interface LandCard extends Card {

    /**
     * Returns type of the given land.
     *
     * @return land type
     */
    LandCardType getLandType();

    /**
     * Returns mana for the given land type.
     * If none of the types fit, it returns null.
     *
     * @return mana type
     */
    ManaType getManaType();
}
