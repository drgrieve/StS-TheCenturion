package centurion.cards;

import centurion.actions.FilterAction;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Experience extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(Experience.class.getSimpleName());
    public static final String IMG = makeCardPath("Defend.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 0;

    // /STAT DECLARATION/


    public Experience() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        int experience = 3 * CardCrawlGame.monstersSlain;
        experience += 9 * (CardCrawlGame.elites1Slain + CardCrawlGame.elites2Slain + CardCrawlGame.elites3Slain);
        experience += 15 * AbstractDungeon.bossCount;
        this.baseMagicNumber = experience;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Experience();
    }}
