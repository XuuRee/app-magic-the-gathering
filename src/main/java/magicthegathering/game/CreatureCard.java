package magicthegathering.game;

/**
 * Represents creature card.
 *
 * @author Zuzana Wolfova
 */
public interface CreatureCard extends Card {

    /**
     * Get the sum of costs for all the mana types.
     *
     * @return total cost
     */
    int getTotalCost();

    /**
     * Get cost for specific mana type.
     * If the creature does not use a certain mana type, 0 is returned.
     *
     * @param mana mana
     * @return cost for given mana type
     */
    int getSpecialCost(ManaType mana);

    /**
     * Get name of the creature.
     *
     * @return name
     */
    String getName();

    /**
     * Get power of the creature. This is the value user for attack.
     *
     * @return power
     */
    int getPower();

    /**
     * Get toughness of the creature. This value is used for defense.
     *
     * @return toughness
     */
    int getToughness();

    /**
     * Check, whether the creature has summoning sickness.
     *
     * @return summoning sickness
     */
    boolean hasSummoningSickness();

    /**
     * Set summoning sickness. This value is set after the creature is first put on table.
     */
    void setSummoningSickness();

    /**
     * Unset summoning sickness so that the creature can attack now.
     */
    void unsetSummoningSickness();
}
