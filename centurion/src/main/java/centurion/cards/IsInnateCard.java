package centurion.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.function.Predicate;

public class IsInnateCard implements Predicate<AbstractCard> {

    @Override
    public boolean test(AbstractCard c) {
        return c.isInnate;
    }
}
