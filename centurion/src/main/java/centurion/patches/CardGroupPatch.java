package centurion.patches;

import centurion.cards.AbstractCenturionCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.CardGroup",
        method="moveToDiscardPile"
)
public class CardGroupPatch {
    public static SpireReturn Prefix(CardGroup __instance, AbstractCard c) {
        if (c instanceof AbstractCenturionCard) {
            AbstractCenturionCard card = (AbstractCenturionCard) c;
            if (card.reshuffleOnUse) { //this is set by the "use" method of the card
                __instance.moveToDeck(card, true);
                card.reshuffleOnUse = false;
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }
}
