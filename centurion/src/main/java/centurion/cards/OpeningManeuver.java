package centurion.cards;

import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import basemod.helpers.ModalChoiceCard;
import basemod.helpers.TooltipInfo;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static centurion.CenturionMod.makeCardPath;

public class OpeningManeuver extends AbstractDynamicCard implements ModalChoice.Callback {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(OpeningManeuver.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final Logger logger = LogManager.getLogger(centurion.CenturionMod.class.getName());

    private static final int COST = 1;
    private ModalChoice modal;
    private Axe axeCard;

    // /STAT DECLARATION/

    public OpeningManeuver() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.isInnate = true;
        this.exhaust = true;

        modal = new ModalChoiceBuilder()
            .setTitle("Choose an Axe or a Shield")
            .setCallback(this) // Sets callback of all the below options to this
            .setColor(Centurion.Enums.COLOR_GRAY) // Sets color of any following cards to red
            .addOption(new Axe())
            .addOption(new Shield())
            .create();
    }

    // Uses the titles and descriptions of the option cards as tooltips for this card
    @Override
    public List<TooltipInfo> getCustomTooltips()
    {
        return modal.generateTooltips();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.upgraded) {
            modal.getCard(0).upgrade();
            modal.getCard(1).upgrade();
        }
        modal.open();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new OpeningManeuver();
    }

    @Override
    public void optionSelected(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster, int i) {

    }
}