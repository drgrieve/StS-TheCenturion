package centurion.cards.skill;

import centurion.actions.DiscoverAction;
import centurion.cards.AbstractDynamicCard;
import centurion.cards.attack.Strike_Centurion;
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
        this.MAGIC_NUMBER = 3;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 3;
        this.setSecondaryValues();
        this.isInnate = true;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Strike_Centurion strike = new Strike_Centurion();
        Shield shield = new Shield();
        shield.enhance(this.magicNumber);
        strike.enhance(this.magicNumber);

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