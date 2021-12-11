package centurion.cards;

import basemod.abstracts.CustomCard;

public abstract class AbstractCenturionCard extends CustomCard {

    // Custom Abstract Cards can be a bit confusing. While this is a simple base for simply adding a second magic number,
    // if you're new to modding I suggest you skip this file until you know what unique things that aren't provided
    // by default, that you need in your own cards.

    // In this example, we use a custom Abstract Card in order to define a new magic number. From here on out, we can
    // simply use that in our cards, so long as we put "extends AbstractDynamicCard" instead of "extends CustomCard" at the start.
    // In simple terms, it's for things that we don't want to define again and again in every single card we make.

    public int defaultSecondMagicNumber;        // Just like magic number, or any number for that matter, we want our regular, modifiable stat
    public int defaultBaseSecondMagicNumber;    // And our base stat - the number in it's base state. It will reset to that by default.
    public boolean upgradedDefaultSecondMagicNumber; // A boolean to check whether the number has been upgraded or not.
    public boolean isDefaultSecondMagicNumberModified; // A boolean to check whether the number has been modified or not, for coloring purposes. (red/green)
    public int retainVariable;
    public int baseRetainVariable;
    public boolean isRetainVariableModified;
    public boolean upgradedRetainVariable;
    public int enhanceCount;
    public boolean reshuffleOnUse = false; //if true -> don't discard on next use, has to be reset in the "use" method (from witchmod)

    public AbstractCenturionCard(final String id,
                                 final String name,
                                 final String img,
                                 final int cost,
                                 final String rawDescription,
                                 final CardType type,
                                 final CardColor color,
                                 final CardRarity rarity,
                                 final CardTarget target) {

        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isDefaultSecondMagicNumberModified = false;
    }

    public void displayUpgrades() { // Display the upgrade - when you click a card to upgrade it
        super.displayUpgrades();
        if (upgradedDefaultSecondMagicNumber) { // If we set upgradedDefaultSecondMagicNumber = true in our card.
            defaultSecondMagicNumber = defaultBaseSecondMagicNumber; // Show how the number changes, as out of combat, the base number of a card is shown.
            isDefaultSecondMagicNumberModified = true; // Modified = true, color it green to highlight that the number is being changed.
        }
        if (upgradedRetainVariable) {
            retainVariable = baseRetainVariable;
            isRetainVariableModified = true;
        }
    }

    public void upgradeDefaultSecondMagicNumber(int amount) { // If we're upgrading (read: changing) the number. Note "upgrade" and NOT "upgraded" - 2 different things. One is a boolean, and then this one is what you will usually use - change the integer by how much you want to upgrade.
        defaultBaseSecondMagicNumber += amount; // Upgrade the number by the amount you provide in your card.
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber; // Set the number to be equal to the base value.
        upgradedDefaultSecondMagicNumber = true; // Upgraded = true - which does what the above method does.
    }

    public void upgradeRetainVariable(int amount) {
        baseRetainVariable += amount;
        retainVariable = baseRetainVariable;
        upgradedRetainVariable = true;
    }

    protected void retainLower() {
        this.retainVariable--;
        this.isRetainVariableModified = true;
    }

    public void enhance() {
        enhance(1);
    }

    public void enhance(int amount) {
        if (baseDamage > 0) baseDamage+=amount;
        if (baseBlock > 0) baseBlock+=amount;
        enhanceCount+= amount;
        this.name = this.originalName + "+" + enhanceCount;
        initializeTitle();
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = this.name + "+";
        if (this.enhanceCount > 0) this.name+= enhanceCount;
        this.initializeTitle();
    }

}