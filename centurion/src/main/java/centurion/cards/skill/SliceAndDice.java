package centurion.cards.skill;

import centurion.cards.AbstractDynamicCard;
import centurion.cards.token.Dice;
import centurion.cards.token.Doom;
import centurion.cards.token.Gloom;
import centurion.cards.token.Slice;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class SliceAndDice extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(SliceAndDice.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, SliceAndDice.class.getSimpleName()));

    private static final int COST = 0;

    public SliceAndDice() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard c = new Slice();
        if (this.upgraded) c.upgrade();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, 1, false));
        c = new Dice();
        if (this.upgraded) c.upgrade();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, 1, false));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SliceAndDice();
    }}
