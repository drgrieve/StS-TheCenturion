package centurion.powers;

import basemod.interfaces.CloneablePowerInterface;
import centurion.actions.BleedAction;
import centurion.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static centurion.CenturionMod.makePowerPath;

public class LoseThornsPower extends AbstractPower {
    public static final String POWER_ID = centurion.CenturionMod.makeID("LoseThornsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ThornsDown.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ThornsDownS.png"));

    private AbstractCreature source;
    private static boolean naturalclear = false;

    public LoseThornsPower(AbstractCreature owner, AbstractCreature source, int amount) {

        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = source;
        this.type = POWER_TYPE;
        this.amount = amount;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void atStartOfTurn() {
        flash();

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new ThornsPower(this.owner, -this.amount), -this.amount));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, LoseThornsPower.POWER_ID));
        this.naturalclear = true;
    }

    public void onRemove() {
        if (naturalclear) {
            if (this.owner.getPower(ThornsPower.POWER_ID).amount <= 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, ThornsPower.POWER_ID));
            }
        }
    }
}