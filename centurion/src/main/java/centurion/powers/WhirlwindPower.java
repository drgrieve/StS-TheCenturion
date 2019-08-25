package centurion.powers;

import basemod.interfaces.CloneablePowerInterface;
import centurion.actions.ReplayAttackAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WhirlwindPower extends AbstractDefaultPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = centurion.CenturionMod.makeID(WhirlwindPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int attacksDoubledThisTurn = 0; //Count attacks triggered so not counted in total

    public WhirlwindPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.source = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        this.updateDescription();
        this.loadImages();
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }

    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && card.type == AbstractCard.CardType.ATTACK && this.hasCreditsLeft()) {
            this.flash();
            attacksDoubledThisTurn++;
            AbstractDungeon.actionManager.addToBottom(new ReplayAttackAction(card, action.target, false));
        }

    }

    public void atStartOfTurn() { this.attacksDoubledThisTurn = 0; }

    private boolean hasCreditsLeft() {
        int attacks = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == AbstractCard.CardType.ATTACK) {
                attacks++;
                if (attacks - attacksDoubledThisTurn > this.amount) return false;
            }
        }
        return true;
    }

    @Override
    public AbstractPower makeCopy() {
        return new WhirlwindPower(owner, amount);
    }
}
