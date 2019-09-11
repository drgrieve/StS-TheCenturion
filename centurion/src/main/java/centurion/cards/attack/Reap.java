package centurion.cards.attack;

import centurion.CenturionMod;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.DeathMarkPower;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Reap extends AbstractDynamicCard {

    public static final String ID = CenturionMod.makeID(Reap.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Reap.class.getSimpleName()));

    private static final int COST = 1;

    public Reap() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.DAMAGE = 4;
        this.UPGRADE_PLUS_DMG = 1;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int marks = numberOfMarks(m);

        AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        switch (marks) {
            case 1: effect = AbstractGameAction.AttackEffect.SLASH_VERTICAL; break;
            case 2: effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL; break;
            case 3: effect = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL; break;
        }
        //TODO something special on 5 or more (same for repeating strike).
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));

        if (marks == 0) this.exhaust = true;
        else AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DeathMarkPower(m, -1), -1));
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int baseDamage = this.baseDamage;
        this.baseDamage *= Math.pow(2, numberOfMarks(mo));
        super.calculateCardDamage(mo);
        this.baseDamage = baseDamage;
    }

    private int numberOfMarks(AbstractMonster mo) {
        if (mo.hasPower(DeathMarkPower.POWER_ID)) {
            return mo.getPower(DeathMarkPower.POWER_ID).amount;
        }
        return 0;
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Reap();
    }

}