package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import java.util.Iterator;
import java.util.UUID;

public class ExecuteAction extends AbstractGameAction {
    private int increaseAmount;
    private DamageInfo damageInfo;
    private UUID uuid;

    public ExecuteAction(AbstractCreature target, DamageInfo info, int increaseAmount, UUID targetUUID) {
        this.damageInfo = info;
        this.setValues(target, info);
        this.increaseAmount = increaseAmount;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.uuid = targetUUID;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {

            AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
            if (damageInfo.base > 40) {
                AbstractDungeon.effectList.add(new VerticalImpactEffect(this.target.hb.cX + this.target.hb.width / 4.0F, this.target.hb.cY - this.target.hb.height / 4.0F));
                attackEffect = AttackEffect.BLUNT_HEAVY;
            }

            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, attackEffect));

            this.target.damage(this.damageInfo);
            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead) {

                Iterator cards = AbstractDungeon.player.masterDeck.group.iterator();
                AbstractCard c;
                while(cards.hasNext()) {
                    c = (AbstractCard)cards.next();
                    if (c.uuid.equals(this.uuid)) {
                        c.baseMagicNumber += this.increaseAmount;
                        break;
                    }
                }

                cards = GetAllInBattleInstances.get(this.uuid).iterator();
                while(cards.hasNext()) {
                    c = (AbstractCard)cards.next();
                    c.baseMagicNumber += this.increaseAmount;
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}

