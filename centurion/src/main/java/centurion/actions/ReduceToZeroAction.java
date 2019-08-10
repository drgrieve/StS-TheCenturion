package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

public class ReduceToZeroAction extends AbstractGameAction {

    public static final String ACTION_ID = centurion.CenturionMod.makeID(ReduceToZeroAction.class.getSimpleName());
    private static final UIStrings uiStrings =  CardCrawlGame.languagePack.getUIString(ACTION_ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private static final int SET_COST_TO = 0;
    private float startingDuration;
    private AbstractCard.CardType cardType;
    private ArrayList<AbstractCard> invalidCards = new ArrayList();
    private ArrayList<AbstractCard> validCards = new ArrayList();
    private AbstractPlayer p;

    public ReduceToZeroAction(AbstractPlayer p, int numCards, AbstractCard.CardType cardType) {
        this.setValues(p, p, numCards);
        this.p = p;
        this.cardType = cardType;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update() {
        Iterator iterator;
        AbstractCard c;

        if (this.duration == Settings.ACTION_DUR_FAST) {
            iterator = this.p.hand.group.iterator();
            while(iterator.hasNext()) {
                c = (AbstractCard)iterator.next();
                if (this.isValidCard(c)) this.validCards.add(c);
                else this.invalidCards.add(c);
            }

            if (this.validCards.size() == 0) {
                this.isDone = true;
                return;
            }

            if (this.validCards.size() <= this.amount) {
                iterator = this.validCards.iterator();
                while(iterator.hasNext()) {
                    c = (AbstractCard)iterator.next();
                    c.setCostForTurn(SET_COST_TO);
                }
                this.isDone = true;
                return;
            }

            this.p.hand.group.removeAll(this.invalidCards);
            if (this.p.hand.group.size() > this.amount) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, false, false, false);
                this.tickDuration();
                return;
            }

            if (this.p.hand.group.size() == this.amount) {
                for(int i = 0; i < this.amount; ++i) {
                    this.p.hand.getTopCard().setCostForTurn(SET_COST_TO);
                }

                this.returnCards();
                this.isDone = true;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            iterator = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while(iterator.hasNext()) {
                c = (AbstractCard)iterator.next();
                c.setCostForTurn(SET_COST_TO);
                this.p.hand.addToTop(c);
            }

            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }

        this.tickDuration();
    }

    private boolean isValidCard(AbstractCard card) {
        return card.type.equals(this.cardType) && card.cost != SET_COST_TO && card.costForTurn != SET_COST_TO;
    }

    private void returnCards() {
        Iterator iterator = this.invalidCards.iterator();
        while(iterator.hasNext()) {
            AbstractCard c = (AbstractCard)iterator.next();
            this.p.hand.addToTop(c);
        }
        this.p.hand.refreshHandLayout();
    }

}

