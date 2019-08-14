package centurion.cards.dualwield;

import centurion.actions.BladeStormAction;
import centurion.cards.AbstractDynamicCard;
import centurion.cards.UltimateStrike;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class BladeStorm extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(BladeStorm.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, BladeStorm.class.getSimpleName()));

    private static final int COST = -1;
    private static AbstractCard lastAttack = null;

    public BladeStorm() {
        super(ID, IMG, COST, TYPE, COLOR, CardRarity.SPECIAL, TARGET);
        this.setSecondaryValues();
    }

    public BladeStorm(boolean setStanceRarity) {
        this();
        if (setStanceRarity) this.rarity = RARITY;
    }

    public void applyPowers() {
        lastAttack = null;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == CardType.ATTACK) {
                lastAttack = c;
            }
        }
        if (lastAttack == null) {
            this.rawDescription = getDescription() + cardStrings.EXTENDED_DESCRIPTION[2];
            initializeDescription();
        } else {
            this.rawDescription =
                    getDescription() + cardStrings.EXTENDED_DESCRIPTION[0] + lastAttack.name + cardStrings.EXTENDED_DESCRIPTION[1];
            initializeDescription();
        }
    }

    public void onMoveToDiscard() {
        this.rawDescription = getDescription();
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bonus = this.upgraded ? 1 : 0;
        AbstractCard card = null;
        if (lastAttack != null) {
            card = lastAttack.makeStatEquivalentCopy();
            if (card.costForTurn >= 0) {
                card.setCostForTurn(0);
            }
        }
        AbstractDungeon.actionManager.addToBottom(new BladeStormAction(p, card, bonus, this.freeToPlayOnce, this.energyOnUse));
    }

    public void upgrade() {
        if (!this.upgraded) upgradeName();
    }

    private String getDescription() {
        return upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BladeStorm();
    }

}