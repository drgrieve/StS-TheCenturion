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

public class BladeStormAction extends AbstractGameAction {

    private AbstractCard card = null;
    private boolean freeToPlayOnce = false;
    private AbstractPlayer p;
    private int energyOnUse = -1;
    private int bonusEffect = 0;

    public BladeStormAction(AbstractPlayer p, AbstractCard card, int bonusEffect, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.card = card;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.bonusEffect = bonusEffect;
    }

    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        effect += bonusEffect;

        if (effect > 0) {
            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
                if (card != null) {
                    for (int i = 0; i < effect; i++) {
                        AbstractDungeon.actionManager.addToBottom(
                                new ReplayAttackAction(card, i == 0)
                        );
                    }
                }
            }
            this.isDone = true;
        }
    }
}

