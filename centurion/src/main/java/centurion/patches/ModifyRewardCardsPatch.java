package centurion.patches;

import centurion.characters.Centurion;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch (
        clz = AbstractDungeon.class,
        method = "getRewardCards"
)
public class ModifyRewardCardsPatch {

    private static final Logger logger = LogManager.getLogger(AbstractDungeon.class.getName());

    @SpireInsertPatch(
        locator = Locator.class,
        localvars = {"c"}
    )

    public static void Insert(AbstractCard c) {
        //logger.info("RestoreRetainedCardsActionPatch: " + e.cardID);
        if (AbstractDungeon.player instanceof Centurion) {
            Centurion centurion = (Centurion) AbstractDungeon.player;
            CardGroup stanceCards = centurion.getStanceCardPool();
            logger.info(("Before: " + c.cardID));
            c = centurion.modifyCardReward(c, stanceCards);
            logger.info(("After: " + c.cardID));
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "makeCopy");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}