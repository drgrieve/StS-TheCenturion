package centurion.cards;

import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import static centurion.CenturionMod.makeCardPath;

public class Anticipate extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Anticipate.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Anticipate.class.getSimpleName()));

    private static final int COST = 1;

    // /STAT DECLARATION/


    public Anticipate() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        this.BLOCK = 3;
        this.UPGRADE_PLUS_BLOCK = 2;
        this.setSecondaryValues();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));

        int monsterCount = 0;
        int attackingCount = 0;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (!mon.isDeadOrEscaped()) {
                monsterCount++;
                if (mon.getIntentBaseDmg() >= 0) attackingCount++;
            }
        }
        if (hasBlockNextTurn())
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, this.block * 2), this.block * 2));

        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    private boolean hasBlockNextTurn() {
        int monsterCount = 0;
        int attackingCount = 0;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (!mon.isDeadOrEscaped()) {
                monsterCount++;
                if (mon.getIntentBaseDmg() >= 0) attackingCount++;
            }
        }
        return (attackingCount < monsterCount);
    }

    @Override
    public void applyPowers() {
        this.baseBlock = BLOCK;
        if (this.upgraded) this.baseBlock += UPGRADE_PLUS_BLOCK;
        super.applyPowers();
        if (hasBlockNextTurn()) {
            this.baseMagicNumber = this.block * 2;
            this.magicNumber = this.block * 2;
        }
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Anticipate();
    }}
