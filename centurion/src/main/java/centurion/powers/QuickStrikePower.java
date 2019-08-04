package centurion.powers;

import centurion.cards.token.QuickStrike;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.FlashOfSteel;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class QuickStrikePower extends AbstractDefaultPower {
    public static final String POWER_ID = centurion.CenturionMod.makeID(QuickStrikePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    private boolean isUpgraded = false;

    public QuickStrikePower(AbstractCreature owner, int bladeAmt, boolean isUpgraded) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = bladeAmt;
        this.type = POWER_TYPE;
        this.isUpgraded = isUpgraded;
        loadImages();
        this.updateDescription();
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            AbstractCard c = new QuickStrike();
            if (this.isUpgraded) c.upgrade();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, this.amount, false));
        }
    }

    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
    }

    public void updateDescription() {
        if (this.amount > 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }

    }

}