package centurion.cards.skill;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.RazorPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class SharpenBlade extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(SharpenBlade.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, SharpenBlade.class.getSimpleName()));

    private static final int COST = 0;

    public SharpenBlade() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 3;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 2;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new RazorPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SharpenBlade();
    }
}