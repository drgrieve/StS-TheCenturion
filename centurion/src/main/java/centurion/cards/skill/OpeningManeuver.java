package centurion.cards.skill;

import centurion.actions.DiscoverAction;
import centurion.cards.AbstractDynamicCard;
import centurion.cards.Strike_Centurion;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class OpeningManeuver extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(OpeningManeuver.class.getSimpleName());
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, OpeningManeuver.class.getSimpleName()));

    private static final int COST = 1;

    public OpeningManeuver() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.isInnate = true;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard strike = new Strike_Centurion();
        AbstractCard shield = new Shield();
        if (this.upgraded) {
            strike.upgrade();
            shield.upgrade();
        }
        shield.baseBlock +=3;
        strike.baseDamage +=3;

        CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        cards.addToTop(strike);
        cards.addToTop(shield);
        AbstractDungeon.actionManager.addToBottom(new DiscoverAction(cards));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new OpeningManeuver();
    }

}