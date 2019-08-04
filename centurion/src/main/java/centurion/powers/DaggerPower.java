package centurion.powers;

import centurion.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import static centurion.CenturionMod.makePowerPath;

public class DaggerPower extends AbstractDefaultPower {
    public static final String POWER_ID = centurion.CenturionMod.makeID(DaggerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    private int razorAmount = 0;

    public DaggerPower(AbstractCreature owner, int razorAmount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = 0;
        this.razorAmount = razorAmount;
        this.type = POWER_TYPE;
        this.updateDescription();
        this.loadImages();
    }

    public void stackPower(int stackAmount) {
        this.razorAmount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1] + this.razorAmount + DESCRIPTIONS[2];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.flash();
            this.amount++;
            if (this.amount == 2) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new RazorPower(this.owner, razorAmount), razorAmount));
                this.amount = 0;
            }
        }
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.amount > 0) {
            this.flash();
            this.amount = 0;
        }
    }

}