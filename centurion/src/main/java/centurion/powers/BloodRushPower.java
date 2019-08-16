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
    private int filterDown = 0;

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
        if (this.filterDown == 0) this.flashWithoutSound();
        this.filterDown++;
        AbstractCreature p = this.owner;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FilterPower(p, 1), 1));
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.filterDown > 0) {
            AbstractCreature p = this.owner;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FilterPower(p, -filterDown), -filterDown));
            this.filterDown = 0;
        }
    }

}