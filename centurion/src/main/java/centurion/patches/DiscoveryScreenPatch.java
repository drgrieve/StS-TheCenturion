package centurion.patches;

import centurion.CenturionMod;
import centurion.util.DiscoveryCardScreen.Enum;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class DiscoveryScreenPatch {

    private static final Logger logger = LogManager.getLogger(AbstractDungeon.class.getName());

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon",
            method = "openPreviousScreen"
    )
    public static class OpenPreviousScreen {
        public OpenPreviousScreen() {
        }

        public static void Postfix(AbstractDungeon.CurrentScreen s) {
            if (s == Enum.DISCOVERY) {
                CenturionMod.discoveryCardScreen.reopen();
            }
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon",
            method = "closeCurrentScreen"
    )
    public static class CloseCurrentScreen {
        public CloseCurrentScreen() {
        }

        @SpireInsertPatch(
                rloc = 0,
                localvars = {"screen"}
        )
        public static void Insert(AbstractDungeon.CurrentScreen screen) {
            if (screen == Enum.DISCOVERY) {
                CenturionMod.discoveryCardScreen.onClose();
            }
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon",
            method = "render"
    )
    public static class Render {
        public Render() {
        }

        @SpireInsertPatch(
                locator = DiscoveryScreenPatch.Render.Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SpriteBatch sb) {
            if (AbstractDungeon.screen == Enum.DISCOVERY) {
                CenturionMod.discoveryCardScreen.render(sb);
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher("com.megacrit.cardcrawl.dungeons.AbstractDungeon", "screen");
                return LineFinder.findInOrder(ctBehavior, new ArrayList(), finalMatcher);
            }
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.dungeons.AbstractDungeon",
            method = "update"
    )
    public static class Update {
        public Update() {
        }

        @SpireInsertPatch(
                locator = DiscoveryScreenPatch.Update.Locator.class
        )
        public static void Insert(AbstractDungeon __instance) {
            if (AbstractDungeon.screen == Enum.DISCOVERY) {
                CenturionMod.discoveryCardScreen.update();
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher("com.megacrit.cardcrawl.dungeons.AbstractDungeon", "screen");
                return LineFinder.findInOrder(ctBehavior, new ArrayList(), finalMatcher);
            }
        }
    }
}