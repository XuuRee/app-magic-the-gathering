/**
 *
 * @author Filip Valchar
 */
package magicthegathering.impl;

import java.util.Arrays;
import magicthegathering.game.Card;
import magicthegathering.game.LandCard;
import magicthegathering.game.CreatureCard;

/**
 * Class ArrayUtils contains static methods for 
 * work with arrays of cards.
 * 
 * @author Filip Valchar
 */
public class ArrayUtils {
    
    /**
     * Filter lands from given cards.
     * 
     * @param cards array of cards
     * @return new array of land cards
     */
    public static LandCard[] filterLands(Card[] cards) {
        return Arrays.stream(cards).filter(card -> card instanceof LandCard).toArray(LandCard[]::new);
    }
    
    /**
     * Filter creatures from given cards.
     * 
     * @param cards array of cards
     * @return new array of creatures cards
     */
    public static CreatureCard[] filterCreatures(Card[] cards) {
        return Arrays.stream(cards).filter(card -> card instanceof CreatureCard).toArray(CreatureCard[]::new);
    }
    
    /**
     * Filter cards in hand.
     * 
     * @param cards array of cards
     * @return new array of cards in hand
     */
    public static Card[] filterInHand(Card[] cards) {
        return Arrays.stream(cards).filter(card -> !card.isOnTable()).toArray(Card[]::new);
    }
    
    /**
     * Filter cards on table.
     * 
     * @param cards array of cards
     * @return new array of cards on table
     */
    public static Card[] filterOnTable(Card[] cards) {
        return Arrays.stream(cards).filter(card -> card.isOnTable()).toArray(Card[]::new);
    }
    
    /**
     * Check whether given cards has duplicate, excluding null.
     * 
     * @param cards array of cards
     * @return true if given cards has duplicate, false otherwise
     */
    public static boolean hasDuplicatesExceptNull(Card[] cards) {
        for (int i = 0; i < cards.length; i++) {
            for (int j = i + 1; j < cards.length; j++) {
                if ((cards[i] != null || cards[j] != null) && cards[i] == cards[j]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check whether array of cards has one specific card.
     * 
     * @param searchedCard searched card
     * @param cards array of cards
     * @return true if searched card is in array of cards, false otherwise
     */
    public static boolean containsCard(Card searchedCard, Card[] cards) {
        for (Card card : cards) {
            if (card == searchedCard) {    
                return true;
            }
        }
        return false;
    }
    
    /**
     * Find specific card index in array of cards.
     * 
     * @param searchedCard searched card
     * @param cards array of cards
     * @return index of searched card, otherwise -1
     */
    public static int findCardIndex(Card searchedCard, Card[] cards) {
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == searchedCard) {     
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Swap two items in array.
     * 
     * @param cards array of cards
     * @param i index i position
     * @param j index j position
     */
    private static void swap(Card[] cards, int i, int j) {
        Card card = cards[i];
        cards[i] = cards[j];
        cards[j] = card;
    }
    
    /**
     * Remove unwanted card from array of cards.
     * 
     * @param unwantedCard unwanted card
     * @param cards array of cards
     * @return new array without unwanted card.
     */
    public static Card[] removeCard(Card unwantedCard, Card[] cards) {
        int index = findCardIndex(unwantedCard, cards);
        swap(cards, index, cards.length - 1);
        return Arrays.copyOf(cards, cards.length - 1);
    }
    
}

