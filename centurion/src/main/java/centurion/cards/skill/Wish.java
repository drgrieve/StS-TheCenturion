package centurion.cards.skill;

import centurion.actions.WishAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Wish extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Wish.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Wish.class.getSimpleName()));

    private static final int COST = -1;

    public Wish() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WishAction(p, this.freeToPlayOnce, this.energyOnUse, this.upgraded));
    }

    public boolean canPlay(AbstractCard card) {
        int maximumCost = this.upgraded ? 5 : 6;
        if (this.energyOnUse > maximumCost) {
            this.cantUseMessage = this.cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return true;
    }

    public void upgrade() {
        if (!this.upgraded) upgradeName();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Wish();
    }

}