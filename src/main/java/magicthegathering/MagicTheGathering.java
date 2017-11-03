package magicthegathering;

import magicthegathering.game.Card;
import magicthegathering.game.CreatureCard;
import magicthegathering.game.Game;
import magicthegathering.game.LandCard;
import magicthegathering.game.Player;
import magicthegathering.impl.GameImpl;
import magicthegathering.impl.PlayerImpl;

import java.util.Arrays;
import java.util.Scanner;

/**
 * This is the main class containing text user interface and logic for the game.
 *
 * @author Zuzana Wolfova
 */
public class MagicTheGathering {

    private static final int SKIP = -42;
    private static final int NULL_CREATURE = -1;

    private static final Scanner READER = new Scanner(System.in);
    private static Game game;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Welcome to Magic the Gathering!");
        System.out.println("Empty line means skipping the action.");

        Player[] players = getPlayersName();
        game = new GameImpl(players[0], players[1]);
        game.initGame();

        while (!isPlayerDead()) {
            playTurn();
            game.changePlayer();
        }

        System.out.println("Player " + game.getSecondPlayer() + " has won.");
    }

    private static Player[] getPlayersName() {
        Player[] players = new Player[2];
        for (int i = 0; i < players.length; i++) {
            System.out.println("Enter name for player " + (i + 1));
            players[i] = new PlayerImpl(READER.nextLine());
        }
        return players;
    }

    private static boolean isPlayerDead() {
        return game.getCurrentPlayer().isDead();
    }

    private static void playTurn() {

        System.out.println();
        System.out.println("Turn of Player " + game.getCurrentPlayer().getName() + " with life "
                + game.getCurrentPlayer().getLife() + " begins.");
        System.out.println("===========================================================");
        System.out.println();
        game.prepareCurrentPlayerForTurn();

        System.out.println("Your hand:");
        System.out.println();
        Arrays.stream(game.getCurrentPlayer().getCardsInHand()).forEach(System.out::println);
        System.out.println();

        System.out.println("Your table:");
        System.out.println();
        Arrays.stream(game.getCurrentPlayer().getCardsOnTable()).forEach(System.out::println);
        System.out.println();

        putLandOnTable();
        buyCreatures();
        performAttackAndBlock();
    }

    private static void putLandOnTable() {
        System.out.println("Phase 1 - putting land on the table");
        System.out.println("===================================");
        System.out.println();

        LandCard[] landsInHand = game.getCurrentPlayer().getLandsInHand();
        printCardsWithIndexes(landsInHand);

        System.out.println("Which land you want to put on the table?");

        int landIndex = readIntLine();
        if (landIndex < 0 || landIndex >= landsInHand.length) {
            System.out.println("Invalid index, skipping.");
            return;
        }

        game.getCurrentPlayer().putLandOnTable(landsInHand[landIndex]);
    }

    private static void buyCreatures() {
        System.out.println("Phase 2 - putting creatures on the table");
        System.out.println("========================================");
        System.out.println();

        CreatureCard[] creaturesInHand = game.getCurrentPlayer().getCreaturesInHand();

        System.out.println("Lands on table:");
        Arrays.stream(game.getCurrentPlayer().getLandsOnTable()).forEach(System.out::println);
        System.out.println();
        System.out.println("Creatures in hand:");
        printCardsWithIndexes(creaturesInHand);

        while (true) {
            System.out.println("Type creature number you want to put on the table:");
            int creatureIndex = readIntLine();

            if (creatureIndex < 0 || creatureIndex >= creaturesInHand.length) break;

            boolean put = game.getCurrentPlayer().putCreatureOnTable(creaturesInHand[creatureIndex]);

            if (put) {
                System.out.println("Creature was put on table.");
            } else {
                System.out.println("Creature could not be put on table!");
            }
        }
    }

    private static void performAttackAndBlock() {
        CreatureCard[] attackingCreatures = performAttack();
        performBlock(attackingCreatures);
    }

    private static CreatureCard[] performAttack() {
        System.out.println("Phase 3.1 - choosing which creatures will attack");
        System.out.println("================================================");
        System.out.println();

        CreatureCard[] attackingCreatures = pickCreaturesForAttack();
        while (!game.isCreaturesAttackValid(attackingCreatures)) {
            System.out.println("Invalid attack, try again.");
            attackingCreatures = pickCreaturesForAttack();
        }

        game.performAttack(attackingCreatures);
        return attackingCreatures;
    }

    private static void performBlock(CreatureCard[] attackingCreatures) {
        System.out.println("Phase 3.2 - choosing which creatures will block");
        System.out.println("Note: -1 represents that creature is not blocked");
        System.out.println("================================================");
        System.out.println();

        if (attackingCreatures.length == 0) {
            System.out.println("Skipping action.");
            return;
        }

        CreatureCard[] blockingCreatures = pickCreaturesForBlock(attackingCreatures);
        while (!game.isCreaturesBlockValid(attackingCreatures, blockingCreatures)) {
            System.out.println("Invalid block, try again.");
            blockingCreatures = pickCreaturesForBlock(attackingCreatures);
        }

        printBlock(attackingCreatures, blockingCreatures);
        game.performBlockAndDamage(attackingCreatures, blockingCreatures);
    }

    private static void printBlock(CreatureCard[] attackingCreatures, CreatureCard[] blockingCreatures) {
        System.out.println("Block: ");
        for (int i = 0; i < attackingCreatures.length; i++) {
            String blockingOne = blockingCreatures[i] == null ? "PLAYER" : blockingCreatures[i].toString();
            System.out.println(attackingCreatures[i] + " -> " + blockingOne);
        }
    }

    private static CreatureCard[] pickCreaturesForAttack() {
        return pickCreaturesFor(true);
    }

    private static CreatureCard[] pickCreaturesForBlock(CreatureCard[] attackingCreatures) {
        CreatureCard[] blockCreatures = pickCreaturesFor(false);
        if (blockCreatures.length == 0) {
            blockCreatures = new CreatureCard[attackingCreatures.length];
        }
        return blockCreatures;
    }

    private static CreatureCard[] pickCreaturesFor(boolean isAttack) {
        String action = isAttack ? "attack" : "block";
        Player playersCreatures = isAttack ? game.getCurrentPlayer() : game.getSecondPlayer();

        CreatureCard[] creaturesOnTable = playersCreatures.getCreaturesOnTable();

        if (creaturesOnTable.length == 0) {
            System.out.println("Skipping action.");
            return new CreatureCard[0];
        }

        return processActionInput(creaturesOnTable, action);
    }

    private static CreatureCard[] processActionInput(CreatureCard[] creaturesOnTable, String action) {
        while (true) {
            System.out.println("Choose creatures for " + action + " (whitespace separated numbers):");
            printCardsWithIndexes(creaturesOnTable);

            String answer = READER.nextLine();

            String[] creaturesForAction = answer.split("\\s+");
            CreatureCard[] creatures = new CreatureCard[creaturesForAction.length];
            int i;
            for (i = 0; i < creaturesForAction.length; i++) {
                int index = parseInt(creaturesForAction[i]);
                if (index >= 0 && index < creaturesOnTable.length) {
                    creatures[i] = creaturesOnTable[index];
                } else {
                    if (index == SKIP) {
                        return new CreatureCard[0];
                    } else if (index == NULL_CREATURE) {
                        creatures[i] = null;
                    } else {
                        System.out.println("Invalid index " + index + ", try again");
                        break;
                    }
                }
            }
            if (i == creaturesForAction.length) {
                return creatures;
            }
        }
    }

    private static int readIntLine() throws NumberFormatException {
        return parseInt(READER.nextLine());

    }

    private static int parseInt(String inputString) throws NumberFormatException {
        if (inputString.trim().isEmpty()) return SKIP;
        return Integer.parseInt(inputString);
    }

    private static void printCardsWithIndexes(Card[] cards) {
        for (int i = 0; i < cards.length; i++) {
            System.out.println(i + ": " + cards[i]);
        }
        System.out.println();
    }

}
