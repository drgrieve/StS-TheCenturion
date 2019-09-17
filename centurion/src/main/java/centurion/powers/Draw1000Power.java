package centurion.powers;

import basemod.BaseMod;
import centurion.cards.quest.Draw1000;
import centurion.cards.skill.Burst;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class Draw1000Power extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(Draw1000Power.class.getSimpleName());

    public Draw1000Power(AbstractCreature owner, int amount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, amount);
        this.updateDescription();
        this.loadImages();
    }

    public void onCardDraw(AbstractCard card) {
        this.flashWithoutSound();
        this.amount--;
        if (this.amount == 0) {
            AbstractCard reward = new Burst();
            reward.upgrade();
            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(reward, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
            }
            else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(reward, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
            }
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(reward.makeStatEquivalentCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        adjustMasterQuestCard(Draw1000.ID, this.amount);
    }

    public void stackPower(int stackAmount) {
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

}