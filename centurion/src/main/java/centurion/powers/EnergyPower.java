package centurion.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

public class EnergyPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(EnergyPower.class.getSimpleName());

    public EnergyPower(AbstractCreature owner, int amount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, amount);
        this.updateDescription();
        loadImages();
    }

    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(powerStrings.DESCRIPTIONS[0]);

        for(int i = 0; i < this.amount; ++i) {
            sb.append("[E] ");
        }

        sb.append(LocalizedStrings.PERIOD);
        this.description = sb.toString();
    }

    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));
        this.flash();
    }
}