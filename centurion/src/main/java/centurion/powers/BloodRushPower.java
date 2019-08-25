package centurion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;

public class BloodRushPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(BloodRushPower.class.getSimpleName());

    public BloodRushPower(AbstractCreature owner) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, 0);
        this.canGoNegative = false;
        this.updateDescription();
        this.loadImages();
    }

    public void stackPower(int stackAmount) {
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount < 10) {
            if (this.amount == 0) this.flashWithoutSound();
            this.amount++;
            AbstractCreature p = this.owner;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FilterPower(p, 1), 1));
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.amount > 0) {
            AbstractCreature p = this.owner;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FilterPower(p, -amount), -amount));
            this.amount = 0;
        }
    }

}