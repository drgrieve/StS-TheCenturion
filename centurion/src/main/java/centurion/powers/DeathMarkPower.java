package centurion.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public class DeathMarkPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(DeathMarkPower.class.getSimpleName());

    public DeathMarkPower(AbstractCreature owner, int newAmount) {
        this.initializePower(POWER_ID, PowerType.DEBUFF, owner, newAmount);
        this.canGoNegative = false;
        this.isTurnBased = false;
        this.updateDescription();
        loadImages();
    }

    public void stackPower(int stackAmount) {
        this.defaultStack(stackAmount, false);
    }

    public void reducePower(int reduceAmount) {
        this.defaultStack(-reduceAmount, false);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
