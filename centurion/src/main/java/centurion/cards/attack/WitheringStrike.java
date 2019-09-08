package centurion.cards.attack;

import centurion.CenturionMod;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class WitheringStrike extends AbstractDynamicCard {

    public static final String ID = CenturionMod.makeID(WitheringStrike.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, WitheringStrike.class.getSimpleName()));

    private static final int COST = 2;

    public WitheringStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.DAMAGE = 10;
        this.UPGRADE_PLUS_DMG = 2;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int d = this.damage;
        int i = 0;

        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        while(d > 0) {
            if (i !=0 ) {
                switch (i % 3) {
                    case 0: effect = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL; break;
                    case 1: effect = AbstractGameAction.AttackEffect.SLASH_VERTICAL; break;
                    case 2: effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL; break;
                }
            }
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, d, damageTypeForTurn), effect));
            i++;
            d = d / 2;
        }
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        //TODO add to description to bottom of card
        super.calculateCardDamage(mo);
        int d = this.damage;
        this.baseMagicNumber = 0;
        this.defaultBaseSecondMagicNumber = 0;
        while (d > 0) {
            this.baseMagicNumber += d;
            this.defaultBaseSecondMagicNumber++;
            d = d / 2;
        }
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new WitheringStrike();
    }

}