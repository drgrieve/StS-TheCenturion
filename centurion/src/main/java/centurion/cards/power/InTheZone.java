package centurion.cards.power;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.EnergyPower;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class InTheZone extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(InTheZone.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, InTheZone.class.getSimpleName()));

    private static final int COST = 5;

    public InTheZone() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.UPGRADE_REDUCE_COST_BY = 1;
        this.setSecondaryValues();
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, 1), 1));
    }

    public void triggerOnEndOfPlayerTurn() {
        if (!this.isEthereal) super.triggerOnEndOfPlayerTurn();
        if (this.retain) {
            this.modifyCostForCombat(-1);
        }
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new InTheZone();
    }
}