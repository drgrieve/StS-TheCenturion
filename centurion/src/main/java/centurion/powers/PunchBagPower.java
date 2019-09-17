package centurion.powers;

import centurion.actions.ChooseCardsAction;
import centurion.cards.CardHasTag;
import centurion.cards.UltimateStrike;
import centurion.cards.quest.BeatDown;
import centurion.cards.skill.Shield;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.List;
import java.util.function.Consumer;

public class PunchBagPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(PunchBagPower.class.getSimpleName());

    public PunchBagPower(AbstractCreature owner, int amount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, amount);
        this.updateDescription();
        this.loadImages();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {

        //TODO
        if (info.base > 0 && this.amount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            this.flashWithoutSound();
            this.amount-=info.base;

            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToBottom(new ChooseCardsAction(
                    AbstractDungeon.player.masterDeck,
                    new CardHasTag(AbstractCard.CardTags.STRIKE),
                    1,
                    new MyCallback(this)
                ));
            }
            adjustMasterQuestCard(BeatDown.ID, this.amount);
        }
    }

    public void stackPower(int stackAmount) {
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    private class MyCallback implements Consumer<List<AbstractCard>> {

        private AbstractDefaultPower power;

        public MyCallback(AbstractDefaultPower power) {
            this.power = power;
        }

        @Override
        public void accept(List<AbstractCard> cards) {
            if (cards.size() > 0)
            {
                AbstractPlayer p = AbstractDungeon.player;
                AbstractCard c = cards.get(0);

                AbstractCard reward = new Shield(); //TODO new UltimateShield();
                if (c.upgraded) reward.upgrade();

                replaceCardIfFound(c, reward, p.drawPile, p.exhaustPile, p.discardPile, p.hand);

                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                p.masterDeck.removeCard(c);

                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(reward, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(power.owner, power.owner, power.ID));
            }
        }

        private void replaceCardIfFound(AbstractCard find, AbstractCard replace, CardGroup... piles) {
            for(CardGroup pile: piles) {
                AbstractCard c = pile.getSpecificCard(find);
                if (c != null) {
                    pile.removeCard(c);
                    pile.addToRandomSpot(replace);
                    break;
                }
            }
        }
    }

}