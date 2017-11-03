package magicthegathering.game;

/**
 * This interface represents common card methods.
 *
 * @author Zuzana Wolfova
 */
public interface Card {

    /**
     * Set the card as tapped.
     */
    void tap();

    /**
     * Untap the card.
     */
    void untap();

    /**
     * Get the status if a card is tapped.
     *
     * @return true if the card is tapped
     */
    boolean isTapped();

    /**
     * Put the card on table.
     */
    void putOnTable();

    /**
     * Get status if the card is on table.
     *
     * @return true if the card is on table
     */
    boolean isOnTable();

}
