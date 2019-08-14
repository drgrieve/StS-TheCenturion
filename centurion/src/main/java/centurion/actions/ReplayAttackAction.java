package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReplayAttackAction extends AbstractGameAction {

    private AbstractCard card;
    private AbstractPlayer p;
    private boolean isSlow;

    public ReplayAttackAction(AbstractCard card, boolean isSlow) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.p = AbstractDungeon.player;
        this.isSlow = isSlow;
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.card = card;
        } else {
            this.isDone = true;
        }
    }

    public void update() {
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);

        AbstractDungeon.player.limbo.group.add(card);
        card.current_x = (Settings.WIDTH / 2.0F);
        card.current_y = (Settings.HEIGHT / 2.0F);
        card.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
        card.target_y = (Settings.HEIGHT / 2.0F);
        card.freeToPlayOnce = true;
        card.purgeOnUse = true;
        card.targetAngle = 0.0F;
        card.drawScale = 0.12F;
        card.applyPowers();
        AbstractDungeon.actionManager.currentAction = null;
        AbstractDungeon.actionManager.addToTop(this);
        AbstractDungeon.actionManager.cardQueue.add(
                new CardQueueItem(card, target)
        );

        if (isSlow) {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        } else {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }

        this.isDone = true;
    }

}