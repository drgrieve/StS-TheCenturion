package centurion.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import centurion.cards.dualwield.*;
import centurion.cards.swordshield.*;
import centurion.cards.twohanded.*;
import centurion.cards.token.*;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import centurion.util.TextureLoader;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

import static centurion.CenturionMod.makeRelicPath;

public class IronHelmRelic extends CustomRelic implements CustomSavable<Integer> {

    // ID, images, text.
    public static final String ID = centurion.CenturionMod.makeID(IronHelmRelic.class.getSimpleName());
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(IronHelmRelic.class.getSimpleName() + ".png"));

    private int experienceToGainThisFight = 0;
    private int currentRank = 0;

    public IronHelmRelic() {
        super(ID, IMG, RelicTier.STARTER, LandingSound.SOLID);
        this.counter = 0;
    }


    @Override
    public void atBattleStart() {
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        AbstractMonster.EnemyType enemyType = AbstractMonster.EnemyType.NORMAL;
        for(int i = 0; i < monsters.size(); i++) {
            if (monsters.get(0).type == AbstractMonster.EnemyType.BOSS) {
                enemyType = AbstractMonster.EnemyType.BOSS;
                break;
            } else if (monsters.get(0).type == AbstractMonster.EnemyType.ELITE) {
                enemyType = AbstractMonster.EnemyType.ELITE;
            }
        }
        experienceToGainThisFight = getXPForEnemyType(enemyType);
        if (experienceToGainThisFight + counter >= 10) {
            flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public int getCurrentXP() {
        int experience = 1 * CardCrawlGame.monstersSlain;
        experience += 5 * (CardCrawlGame.elites1Slain + CardCrawlGame.elites2Slain + CardCrawlGame.elites3Slain);
        experience += 10 * AbstractDungeon.bossCount;
        return experience;
    }

    public int getXPForEnemyType(AbstractMonster.EnemyType enemyType) {
        switch(enemyType) {
            case NORMAL: return 1;
            case ELITE: return 5;
            case BOSS: return 10;
        }
        return 0;
    }

    public void onTrigger() {
        this.counter = this.counter + this.experienceToGainThisFight;
        if (counter >= 10) {
            counter = counter - 10;
            this.currentRank++;

            RewardItem defaultAward = new RewardItem();
            defaultAward.text = DESCRIPTIONS[1];
            addCardsToReward(defaultAward, new String[] {LightHeal.ID, PowerUp.ID });
            AbstractDungeon.getCurrRoom().rewards.add(0, defaultAward);

            AbstractPlayer p = AbstractDungeon.player;
            boolean hasTwoHandedStance = p.masterDeck.findCardById(TwoHandedStance.ID) != null;
            boolean hasDualWieldStance = p.masterDeck.findCardById(DualWieldStance.ID) != null;
            boolean hasSwordShieldStance = p.masterDeck.findCardById(SwordShieldStance.ID) != null;

            RewardItem newRankAward = new RewardItem();
            if (this.currentRank == 1) {
                newRankAward.text = DESCRIPTIONS[2];
                addCardsToReward(newRankAward, new String[] { TwoHandedStance.ID, DualWieldStance.ID, SwordShieldStance.ID });
            } else if (this.currentRank == 2 || this.currentRank == 5 || this.currentRank == 8) {
                newRankAward.text = DESCRIPTIONS[3];
                if (hasTwoHandedStance) addCardToReward(newRankAward, StrengthUp.ID);
                else if (hasDualWieldStance) addCardToReward(newRankAward, StatsUp.ID);
                else if (hasSwordShieldStance) addCardToReward(newRankAward, DexterityUp.ID);
            }
            AbstractDungeon.getCurrRoom().rewards.add(0, newRankAward);
            updateTips();
        }
    }

    private void addCardToReward(RewardItem reward, String cardId) {
        addCardsToReward(reward, new String[] { cardId });
    }

    private void addCardsToReward(RewardItem reward, String[] cardIds) {
        reward.type = RewardItem.RewardType.CARD;
        reward.cards.clear();
        for(int i = 0; i < cardIds.length; i++) {
            AbstractCard c = CardLibrary.getCard(cardIds[i]);
            reward.cards.add(c.makeCopy());
        }
    }

    private void updateTips() {
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + this.currentRank + ".";
    }

    public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
        return new IronHelmRelic();
    }

    public Integer onSave() {
        return this.currentRank;
    }

    public void onLoad(Integer rank) {
        this.currentRank = rank;
        updateTips();
    }
}
