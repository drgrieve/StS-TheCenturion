package centurion.cards.dualwield;

import centurion.cards.AbstractDynamicCard;
import centurion.cards.token.QuickStrike;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static centurion.CenturionMod.makeCardPath;

public class Feint extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(Feint.class.getSimpleName());
    public static final String IMG = makeCardPath("Strike_Centurion.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 0;

    // /STAT DECLARATION/

    public Feint() {
        super(ID, IMG, COST, TYPE, COLOR, CardRarity.SPECIAL, TARGET);

        this.DAMAGE = 3;
        this.UPGRADE_PLUS_DMG = 1;
        this.MAGIC_NUMBER = 1;
        this.UPGRADE_PLUS_MAGIC_NUMBER = 1;
        this.setSecondaryValues();
    }

    public Feint(boolean setStanceRarity) {
        this();
        if (setStanceRarity) this.rarity = RARITY;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Feint();
    }

}