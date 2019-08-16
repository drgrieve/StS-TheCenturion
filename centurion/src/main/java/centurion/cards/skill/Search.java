package centurion.cards.skill;

import centurion.actions.FilterAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Search extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Search.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Search.class.getSimpleName()));

    private static final int COST = 1;

    public Search() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 4;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 2;
        this.EXHAUSTIVE_AMT = 2;
        this.setSecondaryValues();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FilterAction(this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }
}