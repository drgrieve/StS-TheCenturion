package centurion.actions;

import centurion.CenturionMod;
import centurion.util.DiscoveryCardScreen;
import com.badlogic.gdx.utils.Array;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DiscoverAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private CardGroup cards;
    private String bannerText;
    private ArrayList<CardOption> options;

    public enum CardOption {
        NONE,
        UPGRADE,
        COST_ZERO_PRE,
        COST_ZERO_POST
    }

    public DiscoverAction(CardGroup cards) {
        this(cards, null, CardOption.COST_ZERO_POST);
    }

    public DiscoverAction(CardGroup cards, String bannerText, CardOption... options) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.cards = cards;
        this.options = new ArrayList<CardOption>(Arrays.asList(options));
        this.bannerText = bannerText;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for(CardOption o: options) {
                for(AbstractCard c: cards.group) {
                    switch (o) {
                        case UPGRADE:
                            c.upgrade();
                            break;
                        case COST_ZERO_PRE:
                            c.setCostForTurn(0);
                            break;
                    }
                }
            }
            if (CenturionMod.discoveryCardScreen == null) CenturionMod.discoveryCardScreen = new DiscoveryCardScreen();
            CenturionMod.discoveryCardScreen.discoveryOpen(cards, bannerText);
            this.tickDuration();
        } else {
            if (!this.retrieveCard) {
                if (CenturionMod.discoveryCardScreen.discoveryCard != null) {
                    AbstractCard card = CenturionMod.discoveryCardScreen.discoveryCard.makeStatEquivalentCopy();
                    card.current_x = -1000.0F * Settings.scale;
                    if (AbstractDungeon.player.hand.size() < 10) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }

                    if (options.contains(CardOption.COST_ZERO_POST))
                        card.setCostForTurn(0);
                    CenturionMod.discoveryCardScreen.discoveryCard = null;
                }
                this.retrieveCard = true;
            }
            this.tickDuration();
        }
    }

}
