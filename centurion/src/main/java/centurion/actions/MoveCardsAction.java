package centurion.actions;

import basemod.BaseMod;
import com.google.gson.internal.$Gson$Preconditions;
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

public class MoveCardsAction extends ChooseCardsAction {

    public MoveCardsAction(CardGroup destination, CardGroup source, Predicate<AbstractCard> predicate, int amount, Consumer<List<AbstractCard>> callback) {
        super(source, predicate, amount, callback);
        this.destination = destination;
    }

    public MoveCardsAction(CardGroup destination, CardGroup source, Predicate<AbstractCard> predicate, Consumer<List<AbstractCard>> callback) {
        this(destination, source, predicate, 1, callback);
    }

    public MoveCardsAction(CardGroup destination, CardGroup source, int amount, Consumer<List<AbstractCard>> callback) {
        this(destination, source, (c) -> {
            return true;
        }, amount, callback);
    }

    public MoveCardsAction(CardGroup destination, CardGroup source, Consumer<List<AbstractCard>> callback) {
        this(destination, source, (c) -> {
            return true;
        }, 1, callback);
    }

    public MoveCardsAction(CardGroup destination, CardGroup source, Predicate<AbstractCard> predicate, int amount) {
        this(destination, source, predicate, amount, (Consumer)null);
    }

    public MoveCardsAction(CardGroup destination, CardGroup source, Predicate<AbstractCard> predicate) {
        this(destination, source, predicate, 1);
    }

    public MoveCardsAction(CardGroup destination, CardGroup source, int amount) {
        this(destination, source, (c) -> {
            return true;
        }, amount);
    }

    public MoveCardsAction(CardGroup destination, CardGroup source) {
        this(destination, source, (c) -> {
            return true;
        }, 1);
    }

}
