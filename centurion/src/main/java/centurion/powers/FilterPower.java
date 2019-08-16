package centurion.powers;

import centurion.actions.FilterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FilterPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(FilterPower.class.getSimpleName());

    public FilterPower(AbstractCreature owner, int newAmount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, newAmount);
        this.canGoNegative = false;
        this.updateDescription();
        this.loadImages();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount;
    }

    public void stackPower(int amount) {
        this.defaultStack(amount);
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.amount > 0) {
            AbstractDungeon.actionManager.addToTop(new FilterAction(this.amount));
        }
    }

}