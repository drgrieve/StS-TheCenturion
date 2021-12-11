package centurion.actions;

import centurion.CenturionMod;
import centurion.cards.AbstractCenturionCard;
import centurion.tags.CustomTags;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class EnhanceAction extends AbstractGameAction {

    public enum EnhanceActionType {
        RANDOM,
        CHOOSE,
        ALL
    }

    public static final String ACTION_ID = CenturionMod.makeID(EnhanceAction.class.getSimpleName());
    private static final UIStrings uiStrings =  CardCrawlGame.languagePack.getUIString(ACTION_ID);
    public static final String[] TEXT = uiStrings.TEXT;
    private float startingDuration;
    private CardGroup cards;
    private ArrayList<AbstractCard.CardType> cardTypes;
    private EnhanceActionType enhanceActionType;
    private boolean mustBeTagged;

    public EnhanceAction(CardGroup cards, int amount, AbstractCard.CardType cardType) {
        this(cards, amount, new ArrayList<AbstractCard.CardType>(), EnhanceActionType.RANDOM);
        this.cardTypes.add(cardType);
    }

    public EnhanceAction(CardGroup cards, int amount, ArrayList<AbstractCard.CardType> cardTypes, EnhanceActionType enhanceActionType) {
        this.cards = cards;
        this.amount = amount;
        this.cardTypes = cardTypes;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        this.enhanceActionType = enhanceActionType;
    }

    public EnhanceAction mustBeTagged() {
        this.mustBeTagged = true;
        return this;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            if (cards.isEmpty()) {
                this.isDone = true;
                return;
            }

            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for(AbstractCard c: cards.group) {
                boolean passesTag = !mustBeTagged || (c.hasTag(AbstractCard.CardTags.STRIKE) || c.hasTag(CustomTags.SHIELD));
                if (passesTag && cardTypes.contains(c.type) && ((c.baseBlock > 0) || c.baseDamage > 0)) {
                    tmpGroup.addToTop(c);
                }
            }

            if (this.amount >= tmpGroup.size()) this.enhanceActionType = EnhanceActionType.ALL;

            if (this.enhanceActionType == EnhanceActionType.RANDOM || this.enhanceActionType == EnhanceActionType.ALL) {
                if (this.enhanceActionType == EnhanceActionType.ALL) this.amount = tmpGroup.size();
                CardCrawlGame.sound.play("CARD_POWER_IMPACT");
                for(int i = 0; i < Math.min(this.amount, tmpGroup.size()); ++i) {
                    AbstractCard c = this.enhanceActionType == EnhanceActionType.RANDOM ? tmpGroup.getRandomCard(true) : tmpGroup.getNCardFromTop(i);
                    enhance(c);
                    if (i <= 20) {
                        float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
                        float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;
                        AbstractGameEffect effect = new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y);
                        if (i == 0) effect = new ShowCardBrieflyEffect(c.makeStatEquivalentCopy());
                        AbstractDungeon.effectList.add(effect);
                    }
                    if (this.enhanceActionType == EnhanceActionType.RANDOM) tmpGroup.removeCard(c);
                }
                this.isDone = true;
                return;
            } else
            {
                AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, false, TEXT[0]);
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
                tickDuration();
                return;
            }

        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            CardCrawlGame.sound.play("CARD_POWER_IMPACT");
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards){
                enhance(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        this.isDone = true;
    }

    private void enhance(AbstractCard c) {
        if (c instanceof AbstractCenturionCard) {
            ((AbstractCenturionCard)c).enhance();
        } else {
            if (c.baseDamage > 0) c.baseDamage++;
            if (c.baseBlock > 0) c.baseBlock++;
        }
    }

}

