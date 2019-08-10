package centurion.powers;

import centurion.actions.BleedAction;
import centurion.cards.BleedOut;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static centurion.CenturionMod.makePowerPath;

public class RazorPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(RazorPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int baseBleed = 1;
    private int currentBleed = baseBleed;

    public RazorPower(AbstractCreature owner, int newAmount) {
        this(owner, newAmount, 0, 0);
    }

    public RazorPower(AbstractCreature owner, int newAmount, int increaseBaseBleedAmount, int increaseCurrentBleedAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = newAmount;
        this.type = PowerType.BUFF;
        this.canGoNegative = true;
        this.isTurnBased = true;
        this.baseBleed = baseBleed + increaseBaseBleedAmount;
        this.currentBleed = currentBleed + increaseCurrentBleedAmount;

        loadImages();
        this.updateDescription();
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            this.amount = 0;
        }

        if (this.amount >= 999) {
            this.amount = 999;
        }
    }

    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.amount = 0;
        }

        if (this.amount >= 999) {
            this.amount = 999;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.currentBleed + DESCRIPTIONS[1];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && this.amount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, this.owner, new BleedPower(target, this.owner, this.currentBleed), this.currentBleed, true));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new RazorPower(this.owner, -1), -1));

            if (card.cardID == BleedOut.ID) {
                int applications = card.upgraded ? 2 : 1;
                for(int i = 0; i < applications; i++) {
                    AbstractDungeon.actionManager.addToBottom(new BleedAction(action.target, this.owner));
                }

            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            if (this.amount <= 1) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            } else  {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new RazorPower(this.owner, -1), -1));
                if (this.currentBleed > this.baseBleed) {
                    this.currentBleed = this.currentBleed - 1;
                    updateDescription();
                }
            }
        }
    }

    public void increaseCurrentBleed(int amount) {
        this.currentBleed = this.currentBleed + amount;
    }

    public void increaseBaseBleed(int amount) {
        this.baseBleed = this.baseBleed + amount;
        if (this.baseBleed > this.currentBleed) this.currentBleed = this.baseBleed;
    }

}
