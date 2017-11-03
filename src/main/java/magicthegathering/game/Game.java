package magicthegathering.game;

/**
 * Game interface.
 *
 * @author Zuzana Wolfova, Marek Sabo
 */
public interface Game {

    /**
     * Amount of generated creatures.
     */
    int CREATURE_COUNT = 6;

    /**
     * Amount of generated lands.
     */
    int LAND_COUNT = 8;

    /**
     * Amount of generated cards.
     */
    int TOTAL_CARD_AMOUNT = CREATURE_COUNT + LAND_COUNT;

    /**
     * Generates player's cards.
     */
    void initGame();

    /**
     * Set (pick) next player.
     */
    void changePlayer();

    /**
     * Prepare player for turn: untap all the cards and unset summoning sickness on creatures.
     */
    void prepareCurrentPlayerForTurn();

    /**
     * Get current player.
     *
     * @return current player
     */
    Player getCurrentPlayer();

    /**
     * Get the other player.
     *
     * @return other player
     */
    Player getSecondPlayer();

    /**
     * Perform attack from current player to the second player.
     * All attacking creatures are tapped.
     *
     * @param creatures creatures which are going to attack
     */
    void performAttack(CreatureCard[] creatures);

    /**
     * Checks whether creatures which are going to attack do not break any rules.
     *
     * @param attackingCreatures array of attacking creatures
     * @return true if all creatures are able to attack, false if any creature has summoning sickness,
     * is tapped, does not belong to the current player or the array contains duplicate creatures
     */
    boolean isCreaturesAttackValid(CreatureCard[] attackingCreatures);

    /**
     * Checks whether blocking does not break any rules.
     * Null elements represent that creature is not going to be blocked.
     *
     * @param attackingCreatures array of attacking creatures
     * @param blockingCreatures array of blocking creatures
     * @return false if arrays have different length, contains duplicates elements excluding null,
     * or attacking/blocking creatures do not belong to the current/second player
     * or blocking creature is tapped
     */
    boolean isCreaturesBlockValid(CreatureCard[] attackingCreatures, CreatureCard[] blockingCreatures);

    /**
     * Evaluates the block, damage to the creatures, their removal, damage to the player.
     *
     * @param attackingCreatures array of attacking creatures
     * @param blockingCreatures array of blocking creatures
     */
    void performBlockAndDamage(CreatureCard[] attackingCreatures, CreatureCard[] blockingCreatures);

}
