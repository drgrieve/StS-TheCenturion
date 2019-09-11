package centurion.cards.skill;

import centurion.cards.AbstractDynamicCard;
import centurion.cards.attack.MarkOfDeath;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Sow extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Sow.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Sow.class.getSimpleName()));

    private static final int COST = 1;

    public Sow() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int inDiscardPile = 5;
        int inDrawPile = 0;
        AbstractCard c = new MarkOfDeath();
        if (this.upgraded) {
            c.upgrade();
            inDrawPile = AbstractDungeon.cardRng.random(1, 4);
            inDiscardPile = 5 - inDrawPile;
        }
        if (inDrawPile > 0)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, inDrawPile, true, true));
        if (inDiscardPile > 0)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(c, inDiscardPile));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Sow();
    }}
