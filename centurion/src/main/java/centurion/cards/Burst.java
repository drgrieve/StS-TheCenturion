package centurion.cards;

import centurion.actions.FilterAction;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Burst extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(Burst.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int MAGIC_NUMBER = 2;
    private static final int UPGRADE_PLUS_MAGIC_NUMBER = 1;

    private int cardsDiscarded;

    // /STAT DECLARATION/

    public Burst() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = MAGIC_NUMBER;
        this.magicNumber = MAGIC_NUMBER;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        cardsDiscarded = p.discardPile.size();
        AbstractDungeon.actionManager.addToBottom(new FilterAction(this.magicNumber, EXTENDED_DESCRIPTION[0]));
    }

    @Override
    public void triggerOnExhaust() {
        cardsDiscarded = AbstractDungeon.player.discardPile.size() - cardsDiscarded;
        if (cardsDiscarded > 0) AbstractDungeon.player.gainEnergy(cardsDiscarded);
        int cardsNotDiscarded = this.magicNumber - cardsDiscarded;
        if (cardsNotDiscarded > 0) AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, cardsNotDiscarded));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC_NUMBER);
            initializeDescription();
        }
    }
}