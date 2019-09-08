package centurion.powers;

import centurion.actions.EnhanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class EnhancementRandomPower extends AbstractDefaultPower {

    public static final String POWER_ID = centurion.CenturionMod.makeID(EnhancementRandomPower.class.getSimpleName());

    public EnhancementRandomPower(AbstractCreature owner) {
        this.initializePower(POWER_ID, PowerType.BUFF, owner, 1);
        this.updateDescription();
        loadImages();
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
    
    public void atStartOfTurnPostDraw() {
        this.flash();
        ArrayList<AbstractCard.CardType> types = new ArrayList<AbstractCard.CardType>();
        types.add(AbstractCard.CardType.ATTACK);
        types.add(AbstractCard.CardType.SKILL);
        AbstractDungeon.actionManager.addToBottom(new EnhanceAction(AbstractDungeon.player.hand, this.amount, types, EnhanceAction.EnhanceActionType.RANDOM));
    }
}