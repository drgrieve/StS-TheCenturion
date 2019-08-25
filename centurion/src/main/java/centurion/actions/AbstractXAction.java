package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public abstract class AbstractXAction extends AbstractGameAction {

    protected boolean freeToPlayOnce = false;
    protected AbstractPlayer p;
    protected int energyOnUse = -1;
    protected int bonusEffect = 0;
    private float startingDuration;

    public AbstractXAction() {

    }

    public AbstractXAction(AbstractPlayer p, int bonusEffect, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.bonusEffect = bonusEffect;
    }

    public void update() {
        if (this.duration == this.startingDuration) {

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
                }
                this.isDone = this.callback(effect);
                if (!this.isDone) {
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
                    tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            this.callbackFinal();
            this.isDone = true;
        }
    }

    public boolean callback(int effect) {
        return true;
    }

    public void callbackFinal() {
    }
}

