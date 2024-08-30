package centurion.cards.twohanded;

import centurion.actions.DiscoverAction;
import centurion.cards.AbstractDynamicCard;
import centurion.cards.dualwield.Dagger;
import centurion.cards.dualwield.Mace;
import centurion.cards.dualwield.Sword;
import centurion.characters.Centurion;
import centurion.powers.RazorPower;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class TwoHandedStance extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(TwoHandedStance.class.getSimpleName());
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, TwoHandedStance.class.getSimpleName()));

    private static final int COST = 1;

    public TwoHandedStance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.isInnate = true;
        this.purgeOnUse = true;
        SoulboundField.soulbound.set(this, true);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        cards.addToTop(new Greatsword());
        cards.addToTop(new BattleAxe());
        //cards.addToTop(new Warhammer());
        AbstractDungeon.actionManager.addToBottom(
                new DiscoverAction(cards, cardStrings.EXTENDED_DESCRIPTION[0], this.upgraded ? DiscoverAction.CardOption.UPGRADE : DiscoverAction.CardOption.NONE));
    }

    // Upgraded stats.
    @Override
    public void upgrade() { this.defaultUpgrade(); }

    @Override
    public AbstractCard makeCopy()
    {
        return new TwoHandedStance();
    }
}