package centurion.cards.quest;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.BeatDownPower;
import centurion.powers.Draw1000Power;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class BeatDown extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(BeatDown.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 0;

    public static final String IMG = makeCardPath(makeImageName(TYPE, BeatDown.class.getSimpleName()));

    public BeatDown() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 1000;
        this.UPGRADE_PLUS_MAGIC_NUMBER = -250;
        this.setSecondaryValues();
        this.isInnate = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BeatDownPower(p, this.baseMagicNumber), this.baseMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC_NUMBER);
            initializeDescription();
        }    
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BeatDown();
    }

}