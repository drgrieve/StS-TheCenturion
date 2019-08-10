package centurion.cards.dualwield;

import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.relics.DexterityUpRelic;
import centurion.relics.StrengthUpRelic;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static centurion.CenturionMod.makeCardPath;

public class StatsUp extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(StatsUp.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPGRADE_PLUS_MAGIC_NUMBER = 1;
    private static final int SECOND_MAGIC_NUMBER = 1;

    // /STAT DECLARATION/


    public StatsUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.isInnate = true;
        FleetingField.fleeting.set(this, true);
        this.baseMagicNumber = MAGIC_NUMBER;
        this.magicNumber = MAGIC_NUMBER;
        this.defaultBaseSecondMagicNumber = SECOND_MAGIC_NUMBER;
        this.defaultSecondMagicNumber = SECOND_MAGIC_NUMBER;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasRelic(StrengthUpRelic.ID)) {
            StrengthUpRelic relic = (StrengthUpRelic)p.getRelic(StrengthUpRelic.ID);
            relic.increaseCounter(magicNumber);
        } else {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new StrengthUpRelic(magicNumber));
        }

        if (p.hasRelic(DexterityUpRelic.ID)) {
            DexterityUpRelic relic = (DexterityUpRelic)p.getRelic(DexterityUpRelic.ID);
            relic.increaseCounter(defaultSecondMagicNumber);
        } else {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new DexterityUpRelic(defaultSecondMagicNumber));
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, this.defaultSecondMagicNumber), this.defaultSecondMagicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC_NUMBER);
            initializeDescription();
        }
    }
}