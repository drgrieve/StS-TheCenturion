package centurion.cards.twohanded;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.MacePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static centurion.CenturionMod.makeCardPath;

public class Greatsword extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Greatsword.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Greatsword.class.getSimpleName()));

    private static final int COST = 0;

    public Greatsword() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 2;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 1;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Greatsword();
    }

}