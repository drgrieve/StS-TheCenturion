package centurion.cards;

import centurion.characters.Centurion;
import centurion.powers.RazorPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static centurion.CenturionMod.makeCardPath;

public class StrategicCut extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(StrategicCut.class.getSimpleName());
    public static final String IMG = makeCardPath("Strike_Centurion.png");
    public static final Logger logger = LogManager.getLogger(centurion.CenturionMod.class.getName());

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int MAGIC_NUMBER = 1;
    private static final int UPGRADE_PLUS_MAGIC_NUMBER = 0;
    private static final int ADDITIONAL_BLEED_NUMBER = 1;
    private static final int UPGRADE_ADDITIONAL_BLEED_NUMBER = 1;

    // /STAT DECLARATION/

    public StrategicCut() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = MAGIC_NUMBER;
        this.magicNumber = MAGIC_NUMBER;
        this.defaultBaseSecondMagicNumber = ADDITIONAL_BLEED_NUMBER;
        this.defaultSecondMagicNumber = ADDITIONAL_BLEED_NUMBER;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        if (p.hasPower(RazorPower.POWER_ID)) {
            ((RazorPower)p.getPower(RazorPower.POWER_ID)).increaseCurrentBleed(this.defaultSecondMagicNumber);
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new RazorPower(p, this.magicNumber), this.magicNumber));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new RazorPower(p, this.magicNumber, 0, this.defaultSecondMagicNumber), this.magicNumber));
        }

        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            //upgradeMagicNumber(UPGRADE_MAGIC_NUMBER);
            upgradeDefaultSecondMagicNumber(UPGRADE_ADDITIONAL_BLEED_NUMBER);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new StrategicCut();
    }

}