package centurion.powers;

import centurion.actions.DiscoverAction;
import centurion.cards.quest.BloodDrive;
import centurion.cards.quest.Draw1000;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BloodDrivePower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(BloodDrivePower.class.getSimpleName());

    public BloodDrivePower(AbstractCreature owner, int amount) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, amount);
        this.updateDescription();
        this.loadImages();
    }

    public void onBleed(int damageAmount) {
        this.flashWithoutSound();
        this.amount-=damageAmount;
        if (this.amount == 0) {
            CardGroup pool = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            CardGroup stance = ((Centurion)AbstractDungeon.player).getStanceCardPool();
            pool.group.addAll(stance.group);
            pool.group.addAll(AbstractDungeon.rareCardPool.group);
            CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for(int i = 0; i < 3; i++) {
                AbstractCard c = pool.getRandomCard(true, AbstractCard.CardRarity.RARE);
                cards.addToTop(c);
                pool.removeCard(c);
            }
            AbstractDungeon.actionManager.addToBottom(new DiscoverAction(cards));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        adjustMasterQuestCard(BloodDrive.ID, this.amount);
    }

    public void stackPower(int stackAmount) {
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

}