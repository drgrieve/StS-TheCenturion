package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import java.util.Iterator;
import java.util.UUID;

public class DamageAsBlockAction extends AbstractGameAction {
    private DamageInfo damageInfo;
    private AttackEffect attackEffect;

    public DamageAsBlockAction(AbstractCreature target, DamageInfo damageInfo, AttackEffect attackEffect) {
        this.damageInfo = damageInfo;
        this.setValues(target, damageInfo);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.attackEffect = attackEffect;
    }

    public void update() {
        if ((this.duration == 0.1F) && (this.target != null)) {
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(
                            this.target.hb.cX,
                            this.target.hb.cY,
                            this.attackEffect)
            );

            AbstractMonster mon = (AbstractMonster) this.target;

            int monsterBeforeDamageHealth = mon.currentHealth;

            this.target.damage(this.damageInfo);

            int block;

            if (mon.isDying) {
                block = monsterBeforeDamageHealth;
            } else {
                block = monsterBeforeDamageHealth - mon.currentHealth;
            }

            if (block > 0) {
                AbstractDungeon.player.addBlock(block);
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        tickDuration();
    }
}

