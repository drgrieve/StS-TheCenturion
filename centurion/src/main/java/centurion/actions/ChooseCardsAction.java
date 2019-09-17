package centurion.actions;

import basemod.BaseMod;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChooseCardsAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private CardGroup source;
    protected CardGroup destination;
    private Predicate<AbstractCard> predicate;
    private Consumer<List<AbstractCard>> callback;
    private boolean sortCards;
    private boolean isRandom;

    public ChooseCardsAction(CardGroup source, Predicate<AbstractCard> predicate, int amount, Consumer<List<AbstractCard>> callback) {
        this.sortCards = false;
        this.p = AbstractDungeon.player;
        this.source = source;
        this.predicate = predicate;
        this.callback = callback;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public ChooseCardsAction(CardGroup source, Predicate<AbstractCard> predicate, Consumer<List<AbstractCard>> callback) {
        this(source, predicate, 1, callback);
    }

    public ChooseCardsAction(CardGroup source, int amount, Consumer<List<AbstractCard>> callback) {
        this(source, (c) -> {
            return true;
        }, amount, callback);
    }

    public ChooseCardsAction(CardGroup source, Consumer<List<AbstractCard>> callback) {
        this(source, (c) -> {
            return true;
        }, 1, callback);
    }

    public ChooseCardsAction(CardGroup source, Predicate<AbstractCard> predicate, int amount) {
        this(source, predicate, amount, (Consumer)null);
    }

    public ChooseCardsAction(CardGroup source, Predicate<AbstractCard> predicate) {
        this(source, predicate, 1);
    }

    public ChooseCardsAction(CardGroup source, int amount) {
        this(source, (c) -> {
            return true;
        }, amount);
    }

    public ChooseCardsAction(CardGroup source) {
        this(source, (c) -> {
            return true;
        }, 1);
    }

    public ChooseCardsAction isRandom(boolean isRandom) {
        this.isRandom = isRandom;
        return this;
    }

    public ChooseCardsAction sort(boolean sortCards) {
        this.sortCards = sortCards;
        return this;
    }

    public void update() {
        ArrayList callbackList;
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var5 = this.source.group.iterator();

            while(var5.hasNext()) {
                AbstractCard c = (AbstractCard)var5.next();
                if (this.predicate.test(c)) {
                    if (this.source == this.p.drawPile) {
                        tmp.addToRandomSpot(c);
                    } else {
                        tmp.addToTop(c);
                    }
                }
            }

            if (tmp.size() == 0) {
                this.isDone = true;
            } else if (tmp.size() == 1) {
                card = tmp.getTopCard();
                if (this.source == this.p.exhaustPile) {
                    card.unfadeOut();
                }

                if (this.destination != null) {
                    if (this.destination == this.p.hand && this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                        this.source.moveToDiscardPile(card);
                        this.p.createHandIsFullDialog();
                    } else {
                        card.untip();
                        card.unhover();
                        card.lighten(true);
                        card.setAngle(0.0F);
                        card.drawScale = 0.12F;
                        card.targetDrawScale = 0.75F;
                        card.current_x = CardGroup.DRAW_PILE_X;
                        card.current_y = CardGroup.DRAW_PILE_Y;
                        this.source.removeCard(card);
                        this.destination.addToTop(card);
                        AbstractDungeon.player.hand.refreshHandLayout();
                        AbstractDungeon.player.hand.applyPowers();
                    }
                }

                callbackList = new ArrayList();
                callbackList.add(card);
                if (this.callback != null) {
                    this.callback.accept(callbackList);
                }

                this.isDone = true;
            } else if (tmp.size() > this.amount && !this.isRandom) {
                if (this.sortCards) {
                    tmp.sortAlphabetically(true);
                    tmp.sortByRarityPlusStatusCardType(true);
                }

                if (this.amount == 1) {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
                } else {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[1], false);
                }

                this.tickDuration();
            } else {
                callbackList = new ArrayList();

                if (this.isRandom && tmp.size() <= this.amount) this.isRandom = false;
                for(int i = 0; i < tmp.size() && i < amount; ++i) {
                    if (isRandom) {
                        card = tmp.getRandomCard(true);
                        tmp.removeCard(card);
                    } else {
                        card = tmp.getNCardFromTop(i);
                    }
                    callbackList.add(card);
                    if (this.source == this.p.exhaustPile) {
                        card.unfadeOut();
                    }

                    if (this.destination != null) {
                        if (this.destination == this.p.hand && this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                            this.source.moveToDiscardPile(card);
                            this.p.createHandIsFullDialog();
                        } else {
                            card.untip();
                            card.unhover();
                            card.lighten(true);
                            card.setAngle(0.0F);
                            card.drawScale = 0.12F;
                            card.targetDrawScale = 0.75F;
                            card.current_x = CardGroup.DRAW_PILE_X;
                            card.current_y = CardGroup.DRAW_PILE_Y;
                            this.source.removeCard(card);
                            this.destination.addToTop(card);
                            this.p.hand.refreshHandLayout();
                            this.p.hand.applyPowers();
                        }
                    }
                }

                if (this.callback != null) {
                    this.callback.accept(callbackList);
                }

                this.isDone = true;
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                callbackList = new ArrayList();
                Iterator var3 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var3.hasNext()) {
                    card = (AbstractCard)var3.next();
                    callbackList.add(card);
                    card.untip();
                    card.unhover();
                    if (this.source == this.p.exhaustPile) {
                        card.unfadeOut();
                    }

                    if (this.destination != null) {
                        if (this.destination == this.p.hand && this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
                            this.source.moveToDiscardPile(card);
                            this.p.createHandIsFullDialog();
                        } else {
                            this.source.removeCard(card);
                            this.destination.addToTop(card);
                        }
                    }

                    this.p.hand.refreshHandLayout();
                    this.p.hand.applyPowers();
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
                if (this.callback != null) {
                    this.callback.accept(callbackList);
                }
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");
        TEXT = uiStrings.TEXT;
    }
}
