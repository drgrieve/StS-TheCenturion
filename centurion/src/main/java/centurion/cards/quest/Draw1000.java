package centurion.cards.quest;

import centurion.actions.MoveCardsAction;
import centurion.cards.AbstractDynamicCard;
import centurion.cards.IsInnateCard;
import centurion.characters.Centurion;
import centurion.powers.Draw1000Power;
import centurion.powers.ReminiscePower;
import centurion.powers.ReminisceRandomPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static centurion.CenturionMod.makeCardPath;

public class Draw1000 extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Draw1000.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 0;

    public static final String IMG = makeCardPath(makeImageName(TYPE, Draw1000.class.getSimpleName()));

    public Draw1000() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 1000;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new Draw1000Power(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Draw1000();
    }

}