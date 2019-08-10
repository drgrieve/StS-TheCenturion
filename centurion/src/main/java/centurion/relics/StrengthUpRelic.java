package centurion.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import centurion.util.TextureLoader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static centurion.CenturionMod.makeRelicOutlinePath;
import static centurion.CenturionMod.makeRelicPath;

public class StrengthUpRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = centurion.CenturionMod.makeID(StrengthUpRelic.class.getSimpleName());
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(StrengthUpRelic.class.getSimpleName() + ".png"));

    public StrengthUpRelic() {
        this(0);
    }

    public StrengthUpRelic(int amount) {
        super(ID, IMG, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = amount;
        updateTips();
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(p, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.counter), this.counter));
    }

    public void increaseCounter(int amount) {
        flash();
        counter += amount;
        updateTips();
    }

    private void updateTips() {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }
    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + this.counter + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new StrengthUpRelic();
    }

}
