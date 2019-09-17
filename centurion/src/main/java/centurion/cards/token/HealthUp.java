package centurion.cards.token;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class HealthUp extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(HealthUp.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, HealthUp.class.getSimpleName()));

    private static final int COST = 0;

    public HealthUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 3;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 1;
        this.setSecondaryValues();
        this.isInnate = true;
        FleetingField.fleeting.set(this, true);
    }

    public void increase(int amount) {
        this.magicNumber+= amount;
        this.baseMagicNumber+= amount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.increaseMaxHp(this.magicNumber, true);
    }

    @Override
    public void upgrade() { this.defaultUpgrade(); }

    @Override
    public AbstractCard makeCopy()
    {
        return new HealthUp();
    }

}