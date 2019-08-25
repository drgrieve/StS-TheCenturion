package centurion.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FocusAction extends AbstractXAction {

    private String customUIText;

    public FocusAction(AbstractPlayer p, int bonusEffect, boolean freeToPlayOnce, int energyOnUse, String customUIText) {
        super(p, bonusEffect, freeToPlayOnce, energyOnUse);
        this.customUIText = customUIText;
    }

    public boolean callback(int effect) {
        AbstractDungeon.actionManager.addToBottom(new FilterAction(effect * 4, customUIText, FilterAction.OnDiscardAction.MULTI, FilterAction.OnNotDiscardAction.NONE));
        return true;
    }

}

