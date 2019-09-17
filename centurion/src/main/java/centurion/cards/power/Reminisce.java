package centurion.cards.power;

import centurion.actions.MoveCardsAction;
import centurion.cards.AbstractDynamicCard;
import centurion.cards.IsInnateCard;
import centurion.characters.Centurion;
import centurion.powers.ReminiscePower;
import centurion.powers.ReminisceRandomPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static centurion.CenturionMod.makeCardPath;

public class Reminisce extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Reminisce.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 1;

    public static final String IMG = makeCardPath(makeImageName(TYPE, Reminisce.class.getSimpleName()));

    public Reminisce() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 1;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player.discardPile.size() > 0) {
            AbstractDungeon.actionManager.addToBottom(new MoveCardsAction(
                    p.drawPile,
                    p.discardPile,
                    new IsInnateCard(),
                    999));
        }
        AbstractPower power = this.upgraded ? new ReminiscePower(p, this.magicNumber) : new ReminisceRandomPower(p, this.magicNumber);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, power, this.magicNumber));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Reminisce();
    }

}