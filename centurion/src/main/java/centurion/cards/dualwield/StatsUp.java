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

    public static final String ID = centurion.CenturionMod.makeID(StatsUp.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, StatsUp.class.getSimpleName()));

    private static final int COST = 0;

    public StatsUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.MAGIC_NUMBER = 1;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 1;
        this.SECOND_MAGIC_NUMBER = 1;
        this.setSecondaryValues();
        this.isInnate = true;
        FleetingField.fleeting.set(this, true);
    }

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

    @Override
    public void upgrade() { this.defaultUpgrade(); }
}