package centurion.cards.skill;

import centurion.actions.FilterAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Burst extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Burst.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Burst.class.getSimpleName()));

    private static final int COST = 0;

    public Burst() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        this.MAGIC_NUMBER = 2;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 1;
        this.setSecondaryValues();

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FilterAction(this.magicNumber, cardStrings.EXTENDED_DESCRIPTION[0], FilterAction.OnDiscardAction.ENERGY, FilterAction.OnNotDiscardAction.DRAW));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

}