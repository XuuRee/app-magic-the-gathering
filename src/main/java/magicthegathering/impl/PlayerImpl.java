/**
 *
 * @author Filip Valchar
 */
package magicthegathering.impl;

import java.util.Arrays;
import magicthegathering.game.Card;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.LandCard;
import magicthegathering.game.ManaType;
import magicthegathering.game.Player;

/**
 * Class PlayerImpl represents player in the game 
 * and implements Player interface.
 * 
 * @author Filip Valchar
 */
public class PlayerImpl implements Player {

    private final String name;
    private int lives;
    private Card[] cards;
    
    /**
     * Constructor. Create new player.
     * 
     * @param name name of the player 
     */
    public PlayerImpl(String name) {
        this.name = name;
        this.lives = INIT_LIVES;
    }
    
    @Override
    public String toString() {
        return getName() + "(" + getLife() +")";
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getLife() {
        return lives;
    }

    @Override
    public void subtractLives(int lives) {
        this.lives = getLife() - lives;
    }

    @Override
    public boolean isDead() {
        return getLife() <= 0;
    }

    @Override
    public void initCards(Card[] cards) {
        this.cards = Arrays.copyOf(cards, cards.length);
    }

    @Override
    public Card[] getCardsInHand() {
        return ArrayUtils.filterInHand(this.cards);
    }

    @Override
    public Card[] getCardsOnTable() {
        return ArrayUtils.filterOnTable(this.cards);
    }

    @Override
    public LandCard[] getLandsOnTable() {
        Card[] cardsOnTable = getCardsOnTable();
        return ArrayUtils.filterLands(cardsOnTable);
    }

    @Override
    public CreatureCard[] getCreaturesOnTable() {
        Card[] cardsOnTable = getCardsOnTable();
        return ArrayUtils.filterCreatures(cardsOnTable);
    }

    @Override
    public LandCard[] getLandsInHand() {
        Card[] cardsInHand = getCardsInHand();
        return ArrayUtils.filterLands(cardsInHand);
    }

    @Override
    public CreatureCard[] getCreaturesInHand() {
        Card[] cardsInHand = getCardsInHand();
        return ArrayUtils.filterCreatures(cardsInHand);
    }

    @Override
    public void untapAllCards() {
        Card[] cardsOnTable = getCardsOnTable();      
        for (Card card : cardsOnTable) {
            card.untap();
        }
    }

    @Override
    public void prepareAllCreatures() {
        CreatureCard[] creaturesInHand = getCreaturesOnTable();      
        for (CreatureCard card : creaturesInHand) {
            card.unsetSummoningSickness();
        }
    }

    @Override
    public boolean putLandOnTable(LandCard landCard) {
        LandCard[] lands = getLandsInHand();
        
        if (!ArrayUtils.containsCard(landCard, lands)) {
            return false;
        }
        
        landCard.putOnTable();  
        return true;
    }

    @Override
    public boolean putCreatureOnTable(CreatureCard creatureCard) {
        CreatureCard[] creaturesInHand = getCreaturesInHand();
        CreatureCard[] creaturesOnTable = getCreaturesOnTable();
        
        if (ArrayUtils.containsCard(creatureCard, creaturesOnTable)) {
            return false;
        }
        if (!ArrayUtils.containsCard(creatureCard, creaturesInHand)) {
            return false;
        }
        if (!hasManaForCreature(creatureCard)) {
            return false;
        }
        
        tapManaForCreature(creatureCard);
        creatureCard.putOnTable(); 
        creatureCard.setSummoningSickness();   
        return true;
    }

    @Override
    public boolean hasManaForCreature(CreatureCard creature) {
        int[] untappedLands = calculateUntappedLands();
        
        for (ManaType mana : ManaType.values()) {
            if (creature.getSpecialCost(mana) > untappedLands[mana.ordinal()]) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public int[] calculateUntappedLands() {
        LandCard[] lands = getLandsOnTable();
        int[] untappedLands = {0, 0, 0, 0, 0};
        
        for (LandCard card : lands) {
            if (!card.isTapped()) {
                ManaType mana = card.getManaType();
                untappedLands[mana.ordinal()] += 1;
            }
        }
        
        return untappedLands;
    }

    @Override
    public void tapManaForCreature(CreatureCard creature) {
        LandCard[] lands = getLandsOnTable();
        
        for (LandCard card : lands) {
            if (!card.isTapped()) {
                if (creature.getSpecialCost(card.getManaType()) != 0) {
                    card.tap();
                }
            }
        }
    }

    @Override
    public void destroyCreature(CreatureCard creature) { 
        initCards(ArrayUtils.removeCard(creature, this.cards));
    }

}

