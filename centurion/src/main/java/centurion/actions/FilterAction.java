package centurion.actions;

import centurion.powers.BleedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import org.lwjgl.opengl.EXTAbgr;

import java.util.Iterator;

public class FilterAction extends AbstractGameAction {

    public static final String ACTION_ID = centurion.CenturionMod.makeID(FilterAction.class.getSimpleName());
    private static final UIStrings uiStrings =  CardCrawlGame.languagePack.getUIString(ACTION_ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private float startingDuration;
    private int cardsToChooseFromCount = 0;
    private int cardsDiscardedCount = 0;
    private ExtraAction discardedAction;
    private ExtraAction notDiscardedAction;

    public static enum ExtraAction {
        NONE,
        DRAW,
        ENERGY
    }

    public FilterAction(int numCards) {
        this(numCards, ExtraAction.NONE, ExtraAction.NONE, null);
    }

    public FilterAction(int numCards, ExtraAction onDiscarded, ExtraAction onNotDiscarded, String customUIText) {
        this.amount = numCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        if (customUIText != null) TEXT[0] = customUIText;
        this.discardedAction = onDiscarded;
        this.notDiscardedAction = onNotDiscarded;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for(int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
                tmpGroup.addToTop((AbstractCard)AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
                this.cardsToChooseFromCount++;
            }

            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var3 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                while (var3.hasNext()) {
                    cardsDiscardedCount++;
                    AbstractCard c = (AbstractCard) var3.next();
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
            int gainEnergy = 0;
            int drawCards = 0;
            if (cardsDiscardedCount > 0 && this.discardedAction != ExtraAction.NONE) {
                if (this.discardedAction == ExtraAction.DRAW) drawCards += cardsDiscardedCount;
                if (this.discardedAction == ExtraAction.ENERGY) gainEnergy += cardsDiscardedCount;
            }
            int cardsNotDiscardedCount = this.cardsToChooseFromCount - cardsDiscardedCount;
            if (cardsNotDiscardedCount > 0 && this.notDiscardedAction != ExtraAction.NONE) {
                if (this.notDiscardedAction == ExtraAction.DRAW) drawCards += cardsNotDiscardedCount;
                if (this.notDiscardedAction == ExtraAction.ENERGY) gainEnergy += cardsNotDiscardedCount;
            }
            if (drawCards > 0) AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, drawCards));
            if (gainEnergy > 0) AbstractDungeon.player.gainEnergy(gainEnergy);
        }

        this.tickDuration();
    }

}

