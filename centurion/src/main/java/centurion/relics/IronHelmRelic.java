package centurion.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import centurion.cards.dualwield.*;
import centurion.cards.quest.*;
import centurion.cards.swordshield.*;
import centurion.cards.twohanded.*;
import centurion.cards.token.*;
import centurion.characters.Centurion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import centurion.util.TextureLoader;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static centurion.CenturionMod.makeRelicPath;

public class IronHelmRelic extends CustomRelic implements CustomSavable<Integer> {

    public static final String ID = centurion.CenturionMod.makeID(IronHelmRelic.class.getSimpleName());
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(IronHelmRelic.class.getSimpleName() + ".png"));

    private int experienceToGainThisFight = 0;
    private int currentRank = 0;

    public static final Logger logger = LogManager.getLogger(centurion.CenturionMod.class.getName());

    public IronHelmRelic() {
        super(ID, IMG, RelicTier.STARTER, LandingSound.SOLID);
        this.counter = 0;
    }


    @Override
    public void atBattleStart() {
        AbstractMonster.EnemyType enemyType = AbstractMonster.EnemyType.NORMAL;

        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) enemyType = AbstractMonster.EnemyType.ELITE;
            else if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) enemyType = AbstractMonster.EnemyType.BOSS;
        }

        experienceToGainThisFight = getXPForEnemyType(enemyType);
        if (experienceToGainThisFight + counter >= 10) {
            flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
        logger.info("Experience to gain is:" + experienceToGainThisFight);
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

        AbstractPlayer p = AbstractDungeon.player;

        Centurion.StanceType stance = ((Centurion)p).getStanceType();

        this.counter = this.counter + this.experienceToGainThisFight;
        if (counter >= 10) {
            counter = counter - 10;
            this.currentRank++;

            RewardItem defaultAward = new RewardItem();
            defaultAward.text = DESCRIPTIONS[1];
            addCardsToReward(defaultAward, new LightHeal(), new PowerUp());
            AbstractDungeon.getCurrRoom().rewards.add(0, defaultAward);

            RewardItem newRankAward = new RewardItem();
            if (this.currentRank == 1) {
                newRankAward.text = DESCRIPTIONS[2];
                addCardsToReward(newRankAward, new TwoHandedStance(), new DualWieldStance(), new SwordShieldStance());
            } else if (this.currentRank == 2 || this.currentRank == 5 || this.currentRank == 8) {
                newRankAward.text = DESCRIPTIONS[3];
                if (stance == Centurion.StanceType.TwoHanded) addCardsToReward(newRankAward, new StrengthUp());
                else if (stance == Centurion.StanceType.DualWield) addCardsToReward(newRankAward, new StatsUp());
                else if (stance == Centurion.StanceType.SwordShield) addCardsToReward(newRankAward, new DexterityUp());
            } else if (this.currentRank == 3 || this.currentRank == 6 || this.currentRank == 9) {
                newRankAward.text = DESCRIPTIONS[4];
                HealthUp c = new HealthUp();
                if (stance == Centurion.StanceType.TwoHanded) c.increase(2);
                else if (stance == Centurion.StanceType.DualWield) c.increase(1);;
                addCardsToReward(newRankAward, c);
            } else if (this.currentRank == 4) {
                newRankAward.text = DESCRIPTIONS[5];
                AddThreeQuestsForStance(newRankAward, stance);
            }

            AbstractDungeon.getCurrRoom().rewards.add(0, newRankAward);
            updateTips();
        }
    }

    private void addCardsToReward(RewardItem reward, AbstractCard... cards) {
        reward.cards.clear();
        for(AbstractCard c: cards) reward.cards.add(c);
    }

    private void AddThreeQuestsForStance(RewardItem reward, Centurion.StanceType stance) {
        reward.cards.clear();
        CardGroup quests = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        quests.addToTop(new Draw1000());
        quests.addToTop(new BloodDrive());
        if (stance == Centurion.StanceType.SwordShield) {
            quests.addToTop(new PunchBag());
        } else {
            quests.addToTop(new BeatDown());
        }
        for(int i = 0; i < 3; i++) {
            AbstractCard card = quests.getRandomCard(true);
            reward.cards.add(card);
            quests.removeCard(card);
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
