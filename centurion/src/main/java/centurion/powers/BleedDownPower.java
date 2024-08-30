package centurion.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class BleedDownPower extends AbstractDefaultPower {
    public static final String POWER_ID = centurion.CenturionMod.makeID(ThornsDownPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;

    private static boolean naturalclear = false;

    public BleedDownPower(AbstractCreature owner, int amount) {

        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = POWER_TYPE;
        this.amount = amount;

        loadImages();
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void atStartOfTurn() {
        flash();

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new BleedUpPower(this.owner, -this.amount), -this.amount));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, BleedDownPower.POWER_ID));
        this.naturalclear = true;
    }

    public void onRemove() {
        if (naturalclear) {
            if (this.owner.getPower(BleedUpPower.POWER_ID).amount <= 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, BleedUpPower.POWER_ID));
            }
        }
    }
}