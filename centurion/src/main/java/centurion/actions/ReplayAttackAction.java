package centurion.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReplayAttackAction extends AbstractGameAction {

    private AbstractCard card;
    private AbstractCreature target;
    private boolean isSlow;

    public ReplayAttackAction(AbstractCard card, AbstractCreature primaryTarget, boolean isSlow) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.isSlow = isSlow;
        this.target = primaryTarget;
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.card = card.makeSameInstanceOf();
        } else {
            this.isDone = true;
        }
    }

    public void update() {

        if (this.target == null || this.target.isDeadOrEscaped()) {
            target = AbstractDungeon.getMonsters().getRandomMonster(true);
        }
        if (this.target == null || this.target.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }

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
                new CardQueueItem(card, (AbstractMonster)target)
        );

        if (isSlow) {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        } else {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }

        this.isDone = true;
    }

}