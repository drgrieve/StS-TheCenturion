package centurion.cards.dualwield;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.BladeWorkPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class BladeWork extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(BladeWork.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 1;

    public static final String IMG = makeCardPath(makeImageName(TYPE, BladeWork.class.getSimpleName()));

    // /STAT DECLARATION/

    public BladeWork() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 2;
        this.setSecondaryValues();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BladeWorkPower(p, this.magicNumber), this.magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        this.defaultUpgrade();
        if (upgraded) this.isInnate = true;
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BladeWork();
    }

}