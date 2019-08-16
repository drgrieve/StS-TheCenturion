package centurion.cards.attack;

import centurion.CenturionMod;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static centurion.CenturionMod.makeCardPath;

public class RepeatingStrike extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(RepeatingStrike.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, RepeatingStrike.class.getSimpleName()));

    private static final int COST = 0;

    public RepeatingStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.DAMAGE = 3;
        this.UPGRADE_PLUS_DMG = 3;
        this.setSecondaryValues();
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        switch  (numberPlayed()) {
            case 1: effect = AbstractGameAction.AttackEffect.SLASH_VERTICAL; break;
            case 2: effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL; break;
            case 3: effect = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL; break;
        }
        //TODO something special on 5 or more.
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));

        for (AbstractCard c : p.hand.group) {
            if (c.cardID == this.cardID && c.uuid != this.uuid) c.calculateCardDamage(null);
            c.initializeDescription();
        }
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int baseDamage = this.baseDamage;
        this.baseDamage *= Math.pow(2, numberPlayed());
        super.calculateCardDamage(mo);
        this.baseDamage = baseDamage;
    }

    private int numberPlayed() {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.cardID == this.cardID && c.uuid != this.uuid) count++;
        }
        return count;
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new RepeatingStrike();
    }

}