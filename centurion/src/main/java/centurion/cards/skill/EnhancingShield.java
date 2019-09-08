package centurion.cards.skill;

import centurion.actions.EnhanceAction;
import centurion.actions.FilterAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class EnhancingShield extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(EnhancingShield.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, EnhancingShield.class.getSimpleName()));

    private static final int COST = 1;


    public EnhancingShield() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.BLOCK = 3;
        this.UPGRADE_PLUS_BLOCK = 3;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        this.baseMagicNumber++;
        this.baseBlock++;
        this.applyPowers();
        if (baseMagicNumber == 1) {
            this.rawDescription = this.cardStrings.DESCRIPTION + this.cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
        AbstractDungeon.actionManager.addToBottom(new EnhanceAction(p.drawPile, this.baseMagicNumber, CardType.SKILL));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new EnhancingShield();
    }}
