package centurion.actions;

import centurion.CenturionMod;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import javax.smartcardio.Card;

public class WishAction extends AbstractXAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("BetterToHandAction").TEXT;
    private boolean isUpgraded;
    private int cost;

    public WishAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse, boolean isUpgraded) {
        super(p,0, freeToPlayOnce, energyOnUse);
        this.isUpgraded = isUpgraded;
    }

    public boolean callback(int effect) {
        effect--;
        if (effect < 0) return true;

        this.cost = effect;
        CardGroup cards = getCandidateCards();

        if (cards.size() > 1) {
            AbstractDungeon.gridSelectScreen.open(cards, 1, TEXT[0], false);
            return false;
        } else if (cards.size() == 1) {
            addCardToHand(cards.getBottomCard());
        }
        return true;
    }

    public void callbackFinal() {
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            addCardToHand(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
        }
    }

    private CardGroup getCandidateCards() {
        CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(AbstractCard c: AbstractDungeon.srcCommonCardPool.group) addCardIfValid(c, cards);
        for(AbstractCard c: AbstractDungeon.srcUncommonCardPool.group) addCardIfValid(c, cards);
        for(AbstractCard c: AbstractDungeon.srcRareCardPool.group) addCardIfValid(c, cards);
        Centurion centurion = (Centurion)p;
        if (centurion != null)
            for(AbstractCard c: centurion.getStanceCardPool().group) addCardIfValid(c, cards);

        while (cards.size() > 10) {
            AbstractCard c = cards.getRandomCard(true);
            cards.removeCard(c);
        }

        cards.sortAlphabetically(true);
        cards.sortByRarity(false);
        return cards;
   }

    private void addCardIfValid(AbstractCard c, CardGroup cards) {
        AbstractCard tempCard = c.makeCopy();
        if (isUpgraded) tempCard.upgrade();
        if (!tempCard.hasTag(AbstractCard.CardTags.HEALING) && tempCard.cost == cost)
            cards.addToBottom(tempCard);
    }

    private void addCardToHand(AbstractCard c) {
        if (p.hand.size() < 10) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        } else {
            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        }
        c.freeToPlayOnce = true;
    }

}

