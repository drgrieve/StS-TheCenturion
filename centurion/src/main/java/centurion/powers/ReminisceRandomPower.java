package centurion.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReminisceRandomPower extends ReminisceAbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = centurion.CenturionMod.makeID(ReminisceRandomPower.class.getSimpleName());

    public ReminisceRandomPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, true);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ReminisceRandomPower(owner, this.amount);
    }
}

