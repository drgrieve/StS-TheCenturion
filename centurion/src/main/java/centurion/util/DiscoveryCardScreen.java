//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package centurion.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.mainMenu.HorizontalScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.FtueTip.TipType;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import de.robojumper.ststwitch.TwitchPanel;
import de.robojumper.ststwitch.TwitchVoteListener;
import de.robojumper.ststwitch.TwitchVoteOption;
import de.robojumper.ststwitch.TwitchVoter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiscoveryCardScreen implements ScrollBarListener {
    private static final Logger logger = LogManager.getLogger(DiscoveryCardScreen.class.getName());
    private static UIStrings uiStrings;
    public static String[] TEXT;
    private static final float PAD_X;
    private static final float CARD_TARGET_Y;
    public ArrayList<AbstractCard> rewardGroup;
    public AbstractCard codexCard = null;
    public AbstractCard discoveryCard = null;
    public boolean onCardSelect = true;
    public boolean hasTakenAll = false;
    public boolean cardOnly = false;
    public RewardItem rItem = null;
    private boolean codex = false;
    private boolean draft = false;
    private boolean discovery = false;
    private SkipCardButton skipButton = new SkipCardButton();
    private SingingBowlButton bowlButton = new SingingBowlButton();
    private final int SKIP_BUTTON_IDX = 0;
    private final int BOWL_BUTTON_IDX = 1;
    private int draftCount = 0;
    private static final int MAX_CARDS_ON_SCREEN = 4;
    private static final int MAX_CARDS_BEFORE_SCROLL = 5;
    private static final float START_X;
    private boolean grabbedScreen = false;
    private float grabStartX = 0.0F;
    private float scrollX;
    private float targetX;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private HorizontalScrollBar scrollBar;
    public ConfirmButton confirmButton;
    private AbstractCard touchCard;
    private boolean isVoting;
    private boolean mayVote;

    public DiscoveryCardScreen() {
        this.scrollX = START_X;
        this.targetX = this.scrollX;
        this.scrollLowerBound = (float)Settings.WIDTH - 300.0F * Settings.scale;
        this.scrollUpperBound = 2400.0F * Settings.scale;
        this.confirmButton = new ConfirmButton();
        this.touchCard = null;
        this.isVoting = false;
        this.mayVote = false;
        this.scrollBar = new HorizontalScrollBar(this, (float)Settings.WIDTH / 2.0F, 50.0F * Settings.scale + HorizontalScrollBar.TRACK_H / 2.0F, (float)Settings.WIDTH - 256.0F * Settings.scale);
    }

    public void update() {
        if (Settings.isTouchScreen) {
            this.confirmButton.update();
            if (this.confirmButton.hb.clicked && this.touchCard != null) {
                this.confirmButton.hb.clicked = false;
                this.confirmButton.hb.clickStarted = false;
                this.confirmButton.isDisabled = true;
                this.confirmButton.hide();
                this.touchCard.hb.clicked = false;
                this.skipButton.hide();
                this.bowlButton.hide();
                if (this.codex) {
                    this.codexCard = this.touchCard;
                } else if (this.discovery) {
                    this.discoveryCard = this.touchCard;
                } else {
                    this.acquireCard(this.touchCard);
                }

                this.takeReward();
                AbstractDungeon.closeCurrentScreen();
                this.touchCard = null;
            }
        }

        this.skipButton.update();
        this.bowlButton.update();
        this.updateControllerInput();
        if (!this.scrollBar.update()) {
            this.updateScrolling();
        }

        this.cardSelectUpdate();
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.player.viewingRelics) {
            int index = 0;
            boolean anyHovered = false;

            for(Iterator var3 = this.rewardGroup.iterator(); var3.hasNext(); ++index) {
                AbstractCard c = (AbstractCard)var3.next();
                if (c.hb.hovered) {
                    anyHovered = true;
                    break;
                }
            }

            if (!anyHovered) {
                index = 0;
                Gdx.input.setCursorPosition((int)((AbstractCard)this.rewardGroup.get(index)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.rewardGroup.get(index)).hb.cY);
            } else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                    ++index;
                    if (index > this.rewardGroup.size() - 1) {
                        index = 0;
                    }

                    Gdx.input.setCursorPosition((int)((AbstractCard)this.rewardGroup.get(index)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.rewardGroup.get(index)).hb.cY);
                }
            } else {
                --index;
                if (index < 0) {
                    index = this.rewardGroup.size() - 1;
                }

                Gdx.input.setCursorPosition((int)((AbstractCard)this.rewardGroup.get(index)).hb.cX, Settings.HEIGHT - (int)((AbstractCard)this.rewardGroup.get(index)).hb.cY);
            }

        }
    }

    private void updateScrolling() {
        int x = InputHelper.mX;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.targetX += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.targetX -= Settings.SCROLL_SPEED;
            }

            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartX = (float)(-x) - this.targetX;
            }
        } else if (InputHelper.isMouseDown) {
            this.targetX = (float)(-x) - this.grabStartX;
        } else {
            this.grabbedScreen = false;
        }

        this.scrollX = MathHelper.scrollSnapLerpSpeed(this.scrollX, this.targetX);
        this.resetScrolling();
        this.updateBarPosition();
    }

    private void resetScrolling() {
        if (this.targetX < this.scrollLowerBound) {
            this.targetX = MathHelper.scrollSnapLerpSpeed(this.targetX, this.scrollLowerBound);
        } else if (this.targetX > this.scrollUpperBound) {
            this.targetX = MathHelper.scrollSnapLerpSpeed(this.targetX, this.scrollUpperBound);
        }

    }

    private void cardSelectUpdate() {
        AbstractCard hoveredCard = null;
        Iterator var2 = this.rewardGroup.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            c.update();
            c.updateHoverLogic();
            if (c.hb.justHovered) {
                CardCrawlGame.sound.playV("CARD_OBTAIN", 0.4F);
            }

            if (c.hb.hovered) {
                hoveredCard = c;
            }
        }

        if (hoveredCard != null && InputHelper.justClickedLeft) {
            hoveredCard.hb.clickStarted = true;
        }

        if (hoveredCard != null && (InputHelper.justClickedRight || CInputActionSet.proceed.isJustPressed())) {
            InputHelper.justClickedRight = false;
            CardCrawlGame.cardPopup.open(hoveredCard);
        }

        if (hoveredCard != null && CInputActionSet.select.isJustPressed()) {
            hoveredCard.hb.clicked = true;
        }

        if (hoveredCard != null && hoveredCard.hb.clicked) {
            hoveredCard.hb.clicked = false;
            if (!Settings.isTouchScreen) {
                this.skipButton.hide();
                this.bowlButton.hide();
                if (this.codex) {
                    this.codexCard = hoveredCard;
                } else if (this.discovery) {
                    this.discoveryCard = hoveredCard;
                } else {
                    this.acquireCard(hoveredCard);
                }

                this.takeReward();
                AbstractDungeon.closeCurrentScreen();
                this.draftCount = 0;
            } else if (!this.confirmButton.hb.clicked) {
                this.touchCard = hoveredCard;
                this.confirmButton.show();
                this.confirmButton.isDisabled = false;
            }
        }

        if (InputHelper.justReleasedClickLeft && Settings.isTouchScreen && hoveredCard == null && !this.confirmButton.isDisabled && !this.confirmButton.hb.hovered) {
            this.confirmButton.hb.clickStarted = false;
            this.confirmButton.hide();
            this.touchCard = null;
        }

    }

    private void acquireCard(AbstractCard hoveredCard) {
        this.recordMetrics(hoveredCard);
        InputHelper.justClickedLeft = false;
        AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
    }

    private void recordMetrics(AbstractCard hoveredCard) {
        HashMap<String, Object> choice = new HashMap();
        ArrayList<String> notpicked = new ArrayList();
        Iterator var4 = this.rewardGroup.iterator();

        while(var4.hasNext()) {
            AbstractCard card = (AbstractCard)var4.next();
            if (card != hoveredCard) {
                notpicked.add(card.getMetricID());
            }
        }

        choice.put("picked", hoveredCard.getMetricID());
        choice.put("not_picked", notpicked);
        choice.put("floor", AbstractDungeon.floorNum);
        CardCrawlGame.metricData.card_choices.add(choice);
    }

    private void recordMetrics(String pickText) {
        HashMap<String, Object> choice = new HashMap();
        ArrayList<String> notpicked = new ArrayList();
        Iterator var4 = this.rewardGroup.iterator();

        while(var4.hasNext()) {
            AbstractCard card = (AbstractCard)var4.next();
            notpicked.add(card.getMetricID());
        }

        choice.put("picked", pickText);
        choice.put("not_picked", notpicked);
        choice.put("floor", AbstractDungeon.floorNum);
        CardCrawlGame.metricData.card_choices.add(choice);
    }

    private void takeReward() {
        if (this.rItem != null) {
            AbstractDungeon.combatRewardScreen.rewards.remove(this.rItem);
            AbstractDungeon.combatRewardScreen.positionRewards();
            if (AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                AbstractDungeon.combatRewardScreen.hasTakenAll = true;
                AbstractDungeon.overlayMenu.proceedButton.show();
            }
        }

    }

    private void renderTwitchVotes(SpriteBatch sb) {
        if (this.isVoting) {
            if (this.getVoter().isPresent()) {
                TwitchVoter twitchVoter = (TwitchVoter)this.getVoter().get();
                TwitchVoteOption[] options = twitchVoter.getOptions();
                int voteCountOffset = this.bowlButton.isHidden() ? 1 : 2;
                int sum = (Integer)Arrays.stream(options).map((cx) -> {
                    return cx.voteCount;
                }).reduce(0, Integer::sum);

                for(int i = 0; i < this.rewardGroup.size(); ++i) {
                    AbstractCard c = (AbstractCard)this.rewardGroup.get(i);
                    StringBuilder cardVoteText = (new StringBuilder("#")).append(i + voteCountOffset).append(": ").append(options[i + voteCountOffset].voteCount);
                    if (sum > 0) {
                        cardVoteText.append(" (").append(options[i + voteCountOffset].voteCount * 100 / sum).append("%)");
                    }

                    FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, cardVoteText.toString(), c.target_x, c.target_y - 200.0F * Settings.scale, Color.WHITE.cpy());
                }

                StringBuilder skipVoteText = (new StringBuilder("#0: ")).append(options[0].voteCount);
                if (sum > 0) {
                    skipVoteText.append(" (").append(options[0].voteCount * 100 / sum).append("%)");
                }

                if (this.bowlButton.isHidden()) {
                    FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, skipVoteText.toString(), (float)Settings.WIDTH / 2.0F, 150.0F * Settings.scale, Color.WHITE.cpy());
                } else {
                    FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, skipVoteText.toString(), (float)Settings.WIDTH / 2.0F - 100.0F, 150.0F * Settings.scale, Color.WHITE.cpy());
                    StringBuilder bowlVoteText = (new StringBuilder("#1: ")).append(options[1].voteCount);
                    if (sum > 0) {
                        bowlVoteText.append(" (").append(options[1].voteCount * 100 / sum).append("%)");
                    }

                    FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, bowlVoteText.toString(), (float)Settings.WIDTH / 2.0F + 100.0F, 150.0F * Settings.scale, Color.WHITE.cpy());
                }

                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, TEXT[3] + twitchVoter.getSecondsRemaining() + TEXT[4], (float)Settings.WIDTH / 2.0F, AbstractDungeon.dynamicBanner.y - 70.0F * Settings.scale, Color.WHITE.cpy());
            }

        }
    }

    public void render(SpriteBatch sb) {
        this.confirmButton.render(sb);
        this.skipButton.render(sb);
        this.bowlButton.render(sb);
        this.renderCardReward(sb);
        if (this.shouldShowScrollBar()) {
            this.scrollBar.render(sb);
        }

        if (!this.codex && !this.discovery) {
            this.renderTwitchVotes(sb);
        }

        if (this.codex || this.discovery) {
            AbstractDungeon.overlayMenu.combatDeckPanel.render(sb);
            AbstractDungeon.overlayMenu.discardPilePanel.render(sb);
            AbstractDungeon.overlayMenu.exhaustPanel.render(sb);
        }

    }

    private void renderCardReward(SpriteBatch sb) {
        Iterator var6;
        AbstractCard c;
        if (this.rewardGroup.size() > 5) {
            int maxPossibleStartingIndex = this.rewardGroup.size() - 4;
            int indexToStartAt = Math.max(Math.min((int)((float)(maxPossibleStartingIndex + 1) * MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollX)), maxPossibleStartingIndex), 0);

            for(Iterator var4 = this.rewardGroup.iterator(); var4.hasNext(); c.target_y = (float)Settings.HEIGHT / 2.0F) {
                c = (AbstractCard)var4.next();
                if (this.rewardGroup.indexOf(c) >= indexToStartAt && this.rewardGroup.indexOf(c) < indexToStartAt + 4) {
                    c.target_x = (float)Settings.WIDTH / 2.0F + ((float)(this.rewardGroup.indexOf(c) - indexToStartAt) - 1.5F) * (AbstractCard.IMG_WIDTH + PAD_X);
                } else if (this.rewardGroup.indexOf(c) < indexToStartAt) {
                    c.target_x = (float)(-Settings.WIDTH) * 0.25F;
                } else {
                    c.target_x = (float)Settings.WIDTH * 1.25F;
                }
            }
        } else {
            for(var6 = this.rewardGroup.iterator(); var6.hasNext(); c.target_y = CARD_TARGET_Y) {
                c = (AbstractCard)var6.next();
                c.target_x = (float)Settings.WIDTH / 2.0F + ((float)this.rewardGroup.indexOf(c) - (float)(this.rewardGroup.size() - 1) / 2.0F) * (AbstractCard.IMG_WIDTH + PAD_X);
            }
        }

        var6 = this.rewardGroup.iterator();

        while(var6.hasNext()) {
            c = (AbstractCard)var6.next();
            c.render(sb);
        }

        var6 = this.rewardGroup.iterator();

        while(var6.hasNext()) {
            c = (AbstractCard)var6.next();
            c.renderCardTip(sb);
        }

    }

    public void reopen() {
        this.confirmButton.hideInstantly();
        this.touchCard = null;
        AbstractDungeon.screen = Enum.DISCOVERY;
        this.skipButton.hide();
        this.bowlButton.hide();

        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.dynamicBanner.appear(TEXT[0]);
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void discoveryOpen(CardGroup cards) {

        if (this.uiStrings == null) {
            uiStrings = CardCrawlGame.languagePack.getUIString("centurion:DiscoveryCardScreen");
            TEXT = uiStrings.TEXT;
        }

        this.confirmButton.hideInstantly();
        this.touchCard = null;
        this.rItem = null;
        this.codex = false;
        this.discovery = true;
        this.discoveryCard = null;
        this.draft = false;
        this.codexCard = null;
        this.bowlButton.hide();
        this.skipButton.hide();
        this.onCardSelect = true;
        AbstractDungeon.topPanel.unhoverHitboxes();

        this.rewardGroup = cards.group;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = Enum.DISCOVERY;
        AbstractDungeon.dynamicBanner.appear(TEXT[0]);
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.placeCards((float)Settings.WIDTH / 2.0F, CARD_TARGET_Y);
        Iterator var6 = this.rewardGroup.iterator();

        for(AbstractCard c: cards.group) UnlockTracker.markCardAsSeen(c.cardID);
    }

    private void placeCards(float x, float y) {
        int maxPossibleStartingIndex = this.rewardGroup.size() - 4;
        int indexToStartAt = (int)((float)(maxPossibleStartingIndex + 1) * MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollX));
        if (indexToStartAt > maxPossibleStartingIndex) {
            indexToStartAt = maxPossibleStartingIndex;
        }

        AbstractCard c;
        for(Iterator var5 = this.rewardGroup.iterator(); var5.hasNext(); c.current_y = y) {
            c = (AbstractCard)var5.next();
            c.drawScale = 0.75F;
            c.targetDrawScale = 0.75F;
            if (this.rewardGroup.size() > 5) {
                if (this.rewardGroup.indexOf(c) < indexToStartAt) {
                    c.current_x = (float)(-Settings.WIDTH) * 0.25F;
                } else if (this.rewardGroup.indexOf(c) >= indexToStartAt + 4) {
                    c.current_x = (float)Settings.WIDTH * 1.25F;
                } else {
                    c.current_x = x;
                }
            } else {
                c.current_x = x;
            }
        }

    }

    public void onClose() {
        AbstractDungeon.overlayMenu.cancelButton.hide();
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.isScreenUp = false;
        AbstractDungeon.overlayMenu.hideBlackScreen();
        AbstractDungeon.overlayMenu.showCombatPanels();
        if (Settings.isControllerMode) {
            Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
        }

    }

    public void reset() {
        this.draftCount = 0;
        this.codex = false;
        this.discovery = false;
        this.draft = false;
    }

    public void skippedCards() {
        this.recordMetrics("SKIP");
    }

    public void closeFromBowlButton() {
        this.recordMetrics("Singing Bowl");
    }

    private Optional<TwitchVoter> getVoter() {
        return TwitchPanel.getDefaultVoter();
    }

    public void scrolledUsingBar(float newPercent) {
        this.scrollX = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.targetX = this.scrollX;
        this.updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollX);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar() {
        return this.rewardGroup.size() > 5;
    }

    static {
        PAD_X = 40.0F * Settings.scale;
        CARD_TARGET_Y = (float)Settings.HEIGHT * 0.45F;
        START_X = (float)Settings.WIDTH - 300.0F * Settings.scale;
    }

    public static class Enum {
        @SpireEnum
        public static CurrentScreen DISCOVERY;

        public Enum() {
        }
    }
}
