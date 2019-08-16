package centurion.cards.power;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class BrightBeginning extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(BrightBeginning.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, BrightBeginning.class.getSimpleName()));

    private static final int COST = 3;

    public BrightBeginning() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.isInnate = true;
        this.setSecondaryValues();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) this.exhaust = true;

        for (AbstractCard c : p.hand.group) {
            if (c.isInnate && c.uuid != this.uuid) c.setCostForTurn(-9);
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BrightBeginning();
    }
}