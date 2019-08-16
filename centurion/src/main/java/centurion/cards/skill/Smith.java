package centurion.cards.skill;

import centurion.actions.FilterAction;
import centurion.actions.PushOnAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Smith extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Smith.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Smith.class.getSimpleName()));

    private static final int COST = 0;


    public Smith() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 3;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 2;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FilterAction(this.magicNumber, cardStrings.EXTENDED_DESCRIPTION[0], FilterAction.OnDiscardAction.UPGRADE, FilterAction.OnNotDiscardAction.NONE));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Smith();
    }}
