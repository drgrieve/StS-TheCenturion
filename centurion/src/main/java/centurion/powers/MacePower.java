package centurion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.WeakPower;

public class MacePower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(MacePower.class.getSimpleName());
    private int weakAmount = 0;

    public MacePower(AbstractCreature owner, int weakAmount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, 0);
        this.weakAmount = weakAmount;
        this.isTurnBased = true;

        this.updateDescription();
        this.loadImages();
    }

    public void stackPower(int stackAmount) {
        this.weakAmount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1] + this.weakAmount + DESCRIPTIONS[2];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.flash();
            this.amount++;
            if (this.amount == 2) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(action.target, this.owner, new WeakPower(action.target, weakAmount, false ), weakAmount));
                this.amount = 0;
            }
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.amount > 0) {
            this.flash();
            this.amount = 0;
        }
    }

}