package centurion.actions;

import centurion.powers.BleedPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BleedAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;
    public static final Logger logger = LogManager.getLogger(centurion.CenturionMod.class.getName());

    public BleedAction(AbstractCreature target, AbstractCreature source) {

        int amount = 0;
        if (target.hasPower(BleedPower.POWER_ID)) {
            amount = target.getPower(BleedPower.POWER_ID).amount;
        }

        this.setValues(target, source, amount);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = AttackEffect.POISON;
        this.duration = 0.33F;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            if (this.duration == 0.33F && this.target.currentHealth > 0) {
                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.CHARTREUSE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.HP_LOSS));
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }

        }
    }
}
