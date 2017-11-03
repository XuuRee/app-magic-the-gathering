/**
 *
 * @author Filip Valchar
 */
package magicthegathering.impl;

import magicthegathering.game.CreatureCard;
import magicthegathering.game.Game;
import magicthegathering.game.Generator;
import magicthegathering.game.Player;

/**
 * Class GameImpl represents game with their 
 * players and implements Game interface.
 * 
 * @author Filip Valchar
 */
public class GameImpl implements Game {
    
    private final Player player1;
    private final Player player2;
    private Player current;
    
    /**
     * Constructor. Create new game.
     * 
     * @param player1 first player 
     * @param player2 second player
     */
    public GameImpl(Player player1, Player player2) {
        this.player1 = player1;
        this.current = player1;
        this.player2 = player2;
    }

    @Override
    public void initGame() {
        player1.initCards(Generator.generateCards());
        player2.initCards(Generator.generateCards());
    }

    @Override
    public void changePlayer() {
        if (this.current == player2) {    
            this.current = player1;
        } else {
            this.current = player2;
        }
    }

    @Override
    public void prepareCurrentPlayerForTurn() {
        getCurrentPlayer().untapAllCards();
        getCurrentPlayer().prepareAllCreatures();
    }

    @Override
    public Player getCurrentPlayer() {
        return current;
    }

    @Override
    public Player getSecondPlayer() {
        if (getCurrentPlayer() == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    @Override
    public void performAttack(CreatureCard[] creatures) {
        for (CreatureCard card : creatures) {
            card.tap();
        }
    }

    @Override
    public boolean isCreaturesAttackValid(CreatureCard[] attackingCreatures) {
        if (ArrayUtils.hasDuplicatesExceptNull(attackingCreatures)) {
            return false;
        }
        
        CreatureCard[] playerCreatures = getCurrentPlayer().getCreaturesOnTable(); 
        
        for (CreatureCard card : attackingCreatures) {
            if (card.isTapped()) {
                return false;
            }
            if (card.hasSummoningSickness()) {     
                return false;
            }
            if (!ArrayUtils.containsCard(card, playerCreatures)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean isCreaturesBlockValid(CreatureCard[] attackingCreatures, CreatureCard[] blockingCreatures) {
        if (attackingCreatures.length != blockingCreatures.length) {
            return false;
        }
        if (!isCreaturesAttackValid(attackingCreatures)) {
            return false;
        }
        if (ArrayUtils.hasDuplicatesExceptNull(blockingCreatures)) {
            return false;
        }
       
        CreatureCard[] secondPlayerCreatures = getSecondPlayer().getCreaturesOnTable(); 
        
        for (CreatureCard card : blockingCreatures) {
            if (card != null) {
                if (card.isTapped()) {
                    return false;
                }
                if (!ArrayUtils.containsCard(card, secondPlayerCreatures)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    @Override
    public void performBlockAndDamage(CreatureCard[] attackingCreatures, CreatureCard[] blockingCreatures) {
        for (int i = 0; i < attackingCreatures.length; i++) {
            CreatureCard attackCreature = attackingCreatures[i];
            CreatureCard blockCreature = blockingCreatures[i]; 
            if (blockCreature == null) {
                getSecondPlayer().subtractLives(attackCreature.getPower());
                continue;
            } 
            if (attackCreature.getPower() >= blockCreature.getToughness() && attackCreature.getPower() != 0) {
                getSecondPlayer().destroyCreature(blockCreature);
                continue;
            }
            if (blockCreature.getPower() >= attackCreature.getToughness() && blockCreature.getPower() != 0) {
                getCurrentPlayer().destroyCreature(attackCreature);
                continue;
            }
        }
    }
    
}

