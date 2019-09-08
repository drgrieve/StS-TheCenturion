package centurion.powers;

import centurion.actions.FetchAction;
import centurion.cards.IsInnateCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.List;
import java.util.function.Consumer;

public abstract class ReminisceAbstractPower extends AbstractDefaultPower {

    private boolean isRandom;

    public ReminisceAbstractPower(String powerId, AbstractCreature owner, int amount, boolean isRandom) {
        this.initializePower(powerId, PowerType.BUFF, owner, amount);
        this.canGoNegative = false;
        this.isRandom = isRandom;
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
    public void atStartOfTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new FetchAction(
                p.drawPile,
                new IsInnateCard(),
                this.amount,
                new MyCallBack(this))
                .isRandom(isRandom));
    }

    private class MyCallBack implements Consumer<List<AbstractCard>> {

        private ReminisceAbstractPower power;
        public MyCallBack(ReminisceAbstractPower power) {
            this.power = power;
        }

        @Override
        public void accept(List<AbstractCard> cards) {
            if (cards != null && cards.size() > 0) power.flash();
        }
    }

}
