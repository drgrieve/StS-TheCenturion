package centurion.cards.power;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.EnhancementPower;
import centurion.powers.EnhancementRandomPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static centurion.CenturionMod.makeCardPath;

public class Enhancement extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Enhancement.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 2;

    public static final String IMG = makeCardPath(makeImageName(TYPE, Enhancement.class.getSimpleName()));

    public Enhancement() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower power = this.upgraded ? new EnhancementPower(p) : new EnhancementRandomPower(p);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, power, 1));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Enhancement();
    }

}