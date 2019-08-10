package centurion.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractDynamicCard extends AbstractDefaultCard {

    // "How come DefaultCommonAttack extends CustomCard and not DynamicCard like all the rest?"

    // Well every card, at the end of the day, extends CustomCard.
    // Abstract Default Card extends CustomCard and builds up on it, adding a second magic number. Your card can extend it and
    // bam - you can have a second magic number in that card (Learn Java inheritance if you want to know how that works).
    // Abstract Dynamic Card builds up on Abstract Default Card even more and makes it so that you don't need to add
    // the NAME and the DESCRIPTION into your card - it'll get it automatically. Of course, this functionality could have easily
    // Been added to the default card rather than creating a new Dynamic one, but was done so to deliberately.

    public AbstractDynamicCard(final String id,
                               final String img,
                               final int cost,
                               final CardType type,
                               final CardColor color,
                               final CardRarity rarity,
                               final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);
    }

    protected int BLOCK;
    protected int UPGRADE_PLUS_BLOCK;
    protected int DAMAGE;
    protected int UPGRADE_PLUS_DMG;
    protected int MAGIC_NUMBER;
    protected int UPGRADE_PLUS_MAGIC_NUMBER;
    protected int SECOND_MAGIC_NUMBER;
    protected int UPGRADE_PLUS_SECOND_MAGIC_NUMBER;
    private CardStrings cardStrings;

    public void setSecondaryValues() {
        this.baseBlock = BLOCK;
        this.block = BLOCK;
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = MAGIC_NUMBER;
        this.magicNumber = MAGIC_NUMBER;
        this.defaultBaseSecondMagicNumber = SECOND_MAGIC_NUMBER;
        this.defaultSecondMagicNumber = SECOND_MAGIC_NUMBER;
        cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
    }

    public void defaultUpgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADE_PLUS_BLOCK != 0) upgradeBlock(UPGRADE_PLUS_BLOCK);
            if (UPGRADE_PLUS_DMG != 0) upgradeDamage(UPGRADE_PLUS_DMG);
            if (UPGRADE_PLUS_MAGIC_NUMBER != 0) upgradeMagicNumber(UPGRADE_PLUS_MAGIC_NUMBER);
            if (UPGRADE_PLUS_SECOND_MAGIC_NUMBER != 0) upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_SECOND_MAGIC_NUMBER);
            if (cardStrings.UPGRADE_DESCRIPTION != null) rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }

    }

    public static String makeImageName(AbstractCard.CardType type, String className) {
        return type.toString().toLowerCase() + "s/" + className + ".png";
    }


}

