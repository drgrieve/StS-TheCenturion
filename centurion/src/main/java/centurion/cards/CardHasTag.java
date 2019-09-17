package centurion.cards;

import basemod.helpers.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.function.Predicate;

public class CardHasTag implements Predicate<AbstractCard> {

    private AbstractCard.CardTags tag;

    public CardHasTag(AbstractCard.CardTags tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(AbstractCard c) {
        return c.hasTag(this.tag);
    }

}
