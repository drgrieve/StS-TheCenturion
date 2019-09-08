package centurion.powers;

import basemod.interfaces.CloneablePowerInterface;
import centurion.actions.FetchAction;
import centurion.cards.IsInnateCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Predicate;

public class ReminiscePower extends ReminisceAbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = centurion.CenturionMod.makeID(ReminiscePower.class.getSimpleName());

    public ReminiscePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, false);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ReminiscePower(owner, this.amount);
    }
}

