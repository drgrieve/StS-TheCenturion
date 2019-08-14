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
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

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

        /*
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(int i = 0; i < monsters.size(); i++) {
            if (monsters.get(0).type == AbstractMonster.EnemyType.BOSS) {
                enemyType = AbstractMonster.EnemyType.BOSS;
                break;
            } else if (monsters.get(0).type == AbstractMonster.EnemyType.ELITE) {
                enemyType = AbstractMonster.EnemyType.ELITE;
            }
        }
         */

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

        StanceType stance = StanceType.None;
        if (p.masterDeck.findCardById(TwoHandedStance.ID) != null) stance = StanceType.TwoHanded;
        else if (p.masterDeck.findCardById(DualWieldStance.ID) != null) stance = StanceType.DualWield;
        else if (p.masterDeck.findCardById(SwordShieldStance.ID) != null) stance = StanceType.SwordShield;
        modifyStanceRewardCard(stance);

        this.counter = this.counter + this.experienceToGainThisFight;
        if (counter >= 10) {
            counter = counter - 10;
            this.currentRank++;

            RewardItem defaultAward = new RewardItem();
            defaultAward.text = DESCRIPTIONS[1];
            addCardsToReward(defaultAward, new String[] {LightHeal.ID, PowerUp.ID });
            AbstractDungeon.getCurrRoom().rewards.add(0, defaultAward);

            RewardItem newRankAward = new RewardItem();
            if (this.currentRank == 1) {
                newRankAward.text = DESCRIPTIONS[2];
                addCardsToReward(newRankAward, new String[] { TwoHandedStance.ID, DualWieldStance.ID, SwordShieldStance.ID });
            } else if (this.currentRank == 2 || this.currentRank == 5 || this.currentRank == 8) {
                newRankAward.text = DESCRIPTIONS[3];
                if (stance == StanceType.TwoHanded) addCardToReward(newRankAward, StrengthUp.ID);
                else if (stance == StanceType.DualWield) addCardToReward(newRankAward, StatsUp.ID);
                else if (stance == StanceType.SwordShield) addCardToReward(newRankAward, DexterityUp.ID);
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

    private void modifyStanceRewardCard(StanceType stance) {
        logger.info(("Current stance:" + stance));
        if (stance == StanceType.None) return;

        //Find existing card rewards
        RewardItem cardReward = null;
        Iterator rewards = AbstractDungeon.getCurrRoom().rewards.iterator();
        while (rewards.hasNext()) {
            RewardItem reward = (RewardItem) rewards.next();
            logger.info(("Existing reward:" + reward.type));
            if (reward.type == RewardItem.RewardType.CARD && reward.cards.size() > 0) cardReward = reward;
        }
        if (cardReward == null) return;
        logger.info("Found card reward. First card is:" + cardReward.cards.get(0).cardID);

        ArrayList<AbstractCard> stanceCards = loadStanceCards(stance);
        Iterator cards = cardReward.cards.iterator();
        int i = 0;
        while (cards.hasNext()) {
            AbstractCard card = (AbstractCard) cards.next();
            Float nextFloat = AbstractDungeon.cardRng.random();
            logger.info("Stance rng:" + nextFloat);
            //if (AbstractDungeon.cardRng.randomBoolean(0.17f)) {
            if(nextFloat < 0.17F) {
                AbstractCard replacementCard = getStanceReplacementCard(card, stanceCards);
                if (replacementCard != null) {
                    logger.info("Replace card:" + replacementCard.cardID);
                    cardReward.cards.set(i, replacementCard);
                }
            }
            i++;
        }
    }

    private ArrayList<AbstractCard> loadStanceCards(StanceType stance) {
        ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();

        if (stance == StanceType.TwoHanded) {

        } else if (stance == StanceType.DualWield) {
            cards.add(new Caution(true));
            cards.add(new DoubleStrike(true));
            cards.add(new Feint(true));
            cards.add(new Probe(true));
            cards.add(new Patience(true));
            cards.add(new SlashAndParry(true));
            cards.add(new Quicksilver(true));
            cards.add(new MurderAllTheThings(true));
            cards.add(new BladeWork(true));
        } else {

        }
        return cards;
    }

    private AbstractCard getStanceReplacementCard(AbstractCard card, ArrayList<AbstractCard> stanceCards) {
        ArrayList<AbstractCard> candidates = new ArrayList<AbstractCard>();
        for(int i = 0; i < stanceCards.size(); i++) {
            if (stanceCards.get(i).rarity == card.rarity) candidates.add(stanceCards.get(i));
        }
        AbstractCard candidate = null;
        if (candidates.size() > 0) {
            if (candidates.size() == 1) candidate = candidates.get(0);
            int index = AbstractDungeon.cardRng.random(0, candidates.size());
            candidate = candidates.get(index);
        }
        if (candidate == null) return null;
        stanceCards.remove(candidate);
        candidate = candidate.makeCopy();
        if (card.upgraded) candidate.upgrade();
        return candidate;
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

    public enum StanceType {
        None,
        TwoHanded,
        DualWield,
        SwordShield
    }
}
