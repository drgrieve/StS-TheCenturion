package centurion.cards.dualwield;

import centurion.cards.AbstractDynamicCard;
import centurion.cards.UltimateStrike;
import centurion.cards.token.QuickStrike;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Patience extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(Patience.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(Patience.ID);
    public static final String NAME = cardStrings.NAME;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 1;

    // /STAT DECLARATION/

    public Patience() {
        this(0);
    }

    public Patience(int upgrades) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.timesUpgraded = upgrades;
        this.baseMagicNumber = 1;
        this.magicNumber = timesUpgraded + 1;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = new UltimateStrike();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(card, this.magicNumber, true, true));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = NAME + "+" + this.timesUpgraded;
        initializeTitle();
        upgradeMagicNumber(1);
        rawDescription = UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    public boolean canUpgrade() { return true; }

    @Override
    public AbstractCard makeCopy()
    {
        return new Patience(this.timesUpgraded);
    }

}