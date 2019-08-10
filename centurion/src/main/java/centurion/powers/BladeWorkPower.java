package centurion.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BladeWorkPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(BladeWorkPower.class.getSimpleName());

    public BladeWorkPower(AbstractCreature owner, int newAmount) {
        this.initializePower(POWER_ID, owner, newAmount);
        this.type = PowerType.BUFF;
        this.canGoNegative = false;
        this.isTurnBased = true;

        loadImages();
        this.updateDescription();
    }

    public void stackPower(int stackAmount) {
        this.defaultStack(stackAmount);
    }

    public void reducePower(int reduceAmount) {
        this.defaultStack(-reduceAmount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.amount));
            this.flash();
        }
    }

}
