package centurion.cards.stance;

import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import centurion.cards.AbstractDynamicCard;
import centurion.cards.dualwield.Caution;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class DualWieldStance extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(DualWieldStance.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private ModalChoice modal;

    // /STAT DECLARATION/


    public DualWieldStance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.isInnate = true;
        this.purgeOnUse = true;
        SoulboundField.soulbound.set(this, true);

        modal = new ModalChoiceBuilder()
            .setTitle(cardStrings.EXTENDED_DESCRIPTION[0])
            .addOption(new Sword())
            .addOption(new Dagger())
            .addOption(new Mace())
            .create();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        checkStanceUnlock();
        if (this.upgraded) {
            modal.getCard(0).upgrade();
            modal.getCard(1).upgrade();
            modal.getCard(2).upgrade();
        }
        modal.open();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    private void checkStanceUnlock() {
        if (CardLibrary.getCard(Caution.ID).rarity == CardRarity.SPECIAL) {
            CardLibrary.getCard(Caution.ID).rarity = CardRarity.COMMON;
        }
    }
}