package centurion.powers;

import centurion.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static centurion.CenturionMod.getModID;
import static centurion.CenturionMod.makePowerPath;

public abstract class AbstractDefaultPower extends AbstractPower {

    public AbstractDefaultPower() {

    }

    public void loadImages() {
        int modIDLength = (getModID() + ":").length();
        int powerLength = "Power".length();
        String baseImageName = this.ID.substring(modIDLength, this.ID.length() - powerLength);
        Texture tex84 = TextureLoader.getTexture(makePowerPath(baseImageName + ".png"));
        Texture tex32 = TextureLoader.getTexture(makePowerPath(baseImageName + "Small.png"));
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }
}
