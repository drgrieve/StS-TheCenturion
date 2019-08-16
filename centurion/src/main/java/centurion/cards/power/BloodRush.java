package centurion.cards.power;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.BloodRushPower;
import centurion.powers.ThornsUpPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class BloodRush extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(BloodRush.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 1;

    public static final String IMG = makeCardPath(makeImageName(TYPE, BloodRush.class.getSimpleName()));

    // /STAT DECLARATION/

    public BloodRush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.UPGRADE_REDUCE_COST_BY = 1;
        this.setSecondaryValues();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BloodRushPower(p), 0));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BloodRush();
    }

}