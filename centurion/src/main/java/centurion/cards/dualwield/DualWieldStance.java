package centurion.cards.dualwield;

import centurion.actions.DiscoverAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class DualWieldStance extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(DualWieldStance.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, DualWieldStance.class.getSimpleName()));

    private static final int COST = 1;

    public DualWieldStance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.isInnate = true;
        this.purgeOnUse = true;
        SoulboundField.soulbound.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        cards.addToTop(new Sword());
        cards.addToTop(new Dagger());
        cards.addToTop(new Mace());
        AbstractDungeon.actionManager.addToBottom(
                new DiscoverAction(cards, cardStrings.EXTENDED_DESCRIPTION[0], this.upgraded ? DiscoverAction.CardOption.UPGRADE : DiscoverAction.CardOption.NONE));
    }

    @Override
    public void upgrade() { this.defaultUpgrade(); }

    @Override
    public AbstractCard makeCopy()
    {
        return new DualWieldStance();
    }

}