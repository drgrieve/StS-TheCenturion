package centurion.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch (
        clz = RestoreRetainedCardsAction.class,
        method = "update"
)
public class RestoreRetainedCardsActionPatch {

    //private static final Logger logger = LogManager.getLogger(RestoreRetainedCardsActionPatch.class.getName());

    @SpireInsertPatch(
        locator = Locator.class,
        localvars = {"e"}
    )

    public static void Insert(RestoreRetainedCardsAction __instance, AbstractCard e) {
        //logger.info("RestoreRetainedCardsActionPatch: " + e.cardID);
        if (!e.isEthereal) e.triggerOnEndOfPlayerTurn();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "addToTop");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}