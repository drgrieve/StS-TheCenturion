package centurion.powers;

import centurion.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static centurion.CenturionMod.getModID;
import static centurion.CenturionMod.makePowerPath;

public abstract class AbstractDefaultPower extends AbstractPower {

    protected PowerStrings powerStrings;
    private String NAME;
    protected String[] DESCRIPTIONS;

    public AbstractDefaultPower() {

    }

    protected void initializePower(String powerId, PowerType type, AbstractCreature owner, int newAmount) {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(powerId);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        this.ID = powerId;
        this.name = NAME;
        this.type = type;
        this.owner = owner;
        this.amount = newAmount;
    }

    protected void loadImages() {
        int modIDLength = (getModID() + ":").length();
        int powerLength = "Power".length();
        String baseImageName = this.ID.substring(modIDLength, this.ID.length() - powerLength);
        Texture tex84 = TextureLoader.getTexture(makePowerPath(baseImageName + ".png"));
        Texture tex32 = TextureLoader.getTexture(makePowerPath(baseImageName + "Small.png"));
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 96, 96);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }

    protected void defaultStack(int amount) {
        this.fontScale = 8.0F;
        this.amount += amount;
        if (this.amount <= 0) {
            this.amount = 0;
        }

        if (this.amount >= 999) {
            this.amount = 999;
        }
    }
}
