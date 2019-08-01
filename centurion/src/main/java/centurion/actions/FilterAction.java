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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import org.lwjgl.opengl.EXTAbgr;

import java.util.Iterator;

public class FilterAction extends AbstractGameAction {

    public static final String ACTION_ID = centurion.CenturionMod.makeID(FilterAction.class.getSimpleName());
    private static final UIStrings uiStrings =  CardCrawlGame.languagePack.getUIString(ACTION_ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private float startingDuration;

    public FilterAction(int numCards) {
        this(numCards, null);
    }

    public FilterAction(int numCards, String customUIText) {
        this.amount = numCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        if (customUIText != null) TEXT[0] = customUIText;
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
            }

            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator cards = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
                while (cards.hasNext()) {
                    AbstractCard c = (AbstractCard) cards.next();
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.tickDuration();
    }

}

