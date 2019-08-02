package centurion.cards;

import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.SetupAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Setup extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(Setup.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADE_REDUCED_COST = 0;

    // /STAT DECLARATION/

    public Setup() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.retain = true;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SetupAction());
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_REDUCED_COST);
            initializeDescription();
        }
    }
}