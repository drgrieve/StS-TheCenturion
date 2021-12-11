package centurion.powers;

import centurion.CenturionMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class MacePower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(MacePower.class.getSimpleName());
    private int weakAmount = 0;

    public MacePower(AbstractCreature owner, int weakAmount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, 0);
        this.weakAmount = weakAmount;
        this.isTurnBased = true;

        this.updateDescription();
        this.loadImages();
    }

    public void stackPower(int stackAmount) {
        this.weakAmount += stackAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1] + this.weakAmount + DESCRIPTIONS[2];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.amount++;
            if (action.target != null) CenturionMod.logger.info(action.target.name);
            if (this.amount == 2) {
                this.flash();
                if (action.target != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(action.target, this.owner, new WeakPower(action.target, weakAmount, false ), weakAmount));
                }
                else if (card.target == AbstractCard.CardTarget.ALL_ENEMY) {
                    for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this.owner, new WeakPower(mo, weakAmount, false), weakAmount, true, AbstractGameAction.AttackEffect.NONE));
                    }
                }
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