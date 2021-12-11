package centurion.actions;

import centurion.CenturionMod;
import centurion.powers.BleedPower;
import centurion.powers.RepurposePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import org.lwjgl.opengl.EXTAbgr;

import java.util.Iterator;

public class FilterAction extends AbstractGameAction {

    public enum OnDiscardAction {
        NONE,
        ENERGY,
        UPGRADE,
        MULTI,
        EXHAUST
    }

    public enum OnNotDiscardAction {
        NONE,
        DRAW,
        PLAY
    }

    public static final String ACTION_ID = centurion.CenturionMod.makeID(FilterAction.class.getSimpleName());
    private static final UIStrings uiStrings =  CardCrawlGame.languagePack.getUIString(ACTION_ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private float startingDuration;
    private OnDiscardAction onDiscardAction;
    private OnNotDiscardAction onNotDiscardAction;
    private int cardsDiscarded;

    public FilterAction(int numCards) {
        this(numCards, null, OnDiscardAction.NONE, OnNotDiscardAction.NONE);
    }

    public FilterAction(int numCards, String customUIText, OnDiscardAction discardAction, OnNotDiscardAction notDiscardAction) {
        this.amount = numCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        if (customUIText != null) TEXT[0] = customUIText;
        onDiscardAction = discardAction;
        onNotDiscardAction = notDiscardAction;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for(int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
                tmpGroup.addToTop((AbstractCard)AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
            }

            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            int attacks = 0; int skills = 0; int powers = 0;
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards){
                if (this.onDiscardAction == OnDiscardAction.EXHAUST)
                    AbstractDungeon.player.drawPile.moveToExhaustPile(c);
                else
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                if (onDiscardAction == OnDiscardAction.UPGRADE && c.canUpgrade()) c.upgrade();
                switch(c.type) {
                    case ATTACK: attacks++; break;
                    case SKILL: skills++; break;
                    case POWER: powers++; break;
                }
                cardsDiscarded++;
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            if (onDiscardAction == OnDiscardAction.ENERGY) AbstractDungeon.player.gainEnergy(cardsDiscarded);
            else if (onDiscardAction == OnDiscardAction.MULTI) {
                if (attacks > 0)  AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(attacks * 2, true), DamageInfo.DamageType.NORMAL, AttackEffect.FIRE));
                if (skills > 0) AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, skills * 2));
                if (powers > 0)  AbstractDungeon.player.gainEnergy(powers);
            }
            if (AbstractDungeon.player.hasPower(RepurposePower.POWER_ID))
                ((RepurposePower)AbstractDungeon.player.getPower(RepurposePower.POWER_ID)).onTrigger(cardsDiscarded);
        }
        int cardsNotDiscarded = this.amount - cardsDiscarded;
        if (cardsNotDiscarded > 0 && onNotDiscardAction != OnNotDiscardAction.NONE) {
            if (onNotDiscardAction == OnNotDiscardAction.DRAW) {
                AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, cardsNotDiscarded));
            }
            else if (onNotDiscardAction == OnNotDiscardAction.PLAY)
            {
                for(int i = 0; i < cardsNotDiscarded; i++)
                    AbstractDungeon.actionManager.addToBottom(new PlayTopCardAction(
                        (AbstractDungeon.getCurrRoom()).monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
            }
        }
        //CenturionMod.logger.info("FilterAction" + duration);
        //CenturionMod.logger.info("FilterAction" + AbstractDungeon.gridSelectScreen.selectedCards.size());
        //CenturionMod.logger.info("FilterAction" + cardsDiscarded);
        this.isDone = true;
    }

}

