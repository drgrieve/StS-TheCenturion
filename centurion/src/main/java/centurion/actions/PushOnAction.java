package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class PushOnAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
    public static final String[] TEXT = uiStrings.TEXT;

    public PushOnAction() {
        setValues(AbstractDungeon.player, AbstractDungeon.player, -1);
        this.actionType = ActionType.DISCARD;
    }

    public void update() {
        if (this.duration == 0.5F){
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved){
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()){
                int count = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
                AbstractDungeon.actionManager.addToTop(new GainEnergyAction(count));

                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group){
                    AbstractDungeon.player.hand.moveToDiscardPile(c);
                    GameActionManager.incrementDiscard(false);
                    c.triggerOnManualDiscard();
                }
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}
