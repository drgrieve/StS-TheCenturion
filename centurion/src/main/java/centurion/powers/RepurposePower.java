package centurion.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RepurposePower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(RepurposePower.class.getSimpleName());

    public RepurposePower(AbstractCreature owner, int newAmount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, newAmount);
        this.canGoNegative = false;
        this.updateDescription();
        this.loadImages();
    }

    public void onTrigger(int cardsDiscarded) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, cardsDiscarded * amount));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void stackPower(int amount) {
        this.defaultStack(amount, true);
    }

    public void reducePower(int amount) {
        this.defaultStack(-amount, true);
    }

}