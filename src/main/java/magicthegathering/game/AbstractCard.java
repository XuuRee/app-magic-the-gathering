package magicthegathering.game;

/**
 * Abstract card implementing common card methods.
 *
 * @author Zuzana Wolfova
 */
public abstract class AbstractCard implements Card {

    private boolean tapped = false;
    private boolean isOnTable = false;

    @Override
    public void tap() {
        this.tapped = true;
    }

    @Override
    public void untap() {
        this.tapped = false;
    }

    @Override
    public boolean isTapped() {
        return this.tapped;
    }

    @Override
    public boolean isOnTable() {
        return this.isOnTable;
    }

    @Override
    public void putOnTable() {
        this.isOnTable = true;
    }

}
