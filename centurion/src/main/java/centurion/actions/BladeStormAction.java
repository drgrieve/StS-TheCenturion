package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import java.util.Iterator;
import java.util.UUID;

public class BladeStormAction extends AbstractXAction {

    private AbstractCard card = null;

    public BladeStormAction(AbstractPlayer p, AbstractCard card, int bonusEffect, boolean freeToPlayOnce, int energyOnUse) {
        super(p, bonusEffect, freeToPlayOnce, energyOnUse);
        this.card = card;
    }

    public boolean callback(int effect) {
        if (card != null) {
            for (int i = 0; i < effect; i++) {
                AbstractDungeon.actionManager.addToBottom(
                        new ReplayAttackAction(card, null,i == 0)
                );
            }
        }
        return true;
    }

}

