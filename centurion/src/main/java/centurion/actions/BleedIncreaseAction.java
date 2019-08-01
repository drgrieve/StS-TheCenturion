package centurion.actions;

import centurion.powers.BleedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BleedIncreaseAction extends AbstractGameAction {
    private float startingDuration;
    private int percentage;

    public BleedIncreaseAction(AbstractCreature target, AbstractCreature source, int percentage) {
        this.target = target;
        this.source = source;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DEBUFF;
        this.attackEffect = AttackEffect.FIRE;
        this.duration = this.startingDuration;
        this.percentage = percentage;
    }

    public void update() {
        if (this.duration == this.startingDuration && this.target != null && this.target.hasPower(BleedPower.POWER_ID)) {
            int currentBleed = this.target.getPower(BleedPower.POWER_ID).amount;
            int increaseAmount = (int)Math.floor(currentBleed * (percentage / 100.0));
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(this.target, this.source,
                            new BleedPower(this.target, this.source, increaseAmount), increaseAmount));
        }

        this.tickDuration();
    }
}

