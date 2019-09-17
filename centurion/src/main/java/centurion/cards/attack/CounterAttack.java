package centurion.cards.attack;

import centurion.CenturionMod;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class CounterAttack extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(CounterAttack.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, CounterAttack.class.getSimpleName()));

    private static final int COST = 2;

    public CounterAttack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.DAMAGE = 9;
        this.UPGRADE_PLUS_DMG = 4;
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int damageMultiplier = damageMultiplier(m);
        AbstractGameAction.AttackEffect effect = damageMultiplier == 1 ? AbstractGameAction.AttackEffect.SLASH_HORIZONTAL : AbstractGameAction.AttackEffect.SLASH_HEAVY;
        AbstractDungeon.actionManager.addToBottom(
            new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
    }

    private int damageMultiplier(AbstractMonster m) {
        CenturionMod.logger.info("intent damage:" + m.getIntentBaseDmg());
        return m.getIntentBaseDmg() > 0 ? 2 : 1;
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int baseDamage = this.baseDamage;
        this.baseDamage *= damageMultiplier(mo);
        super.calculateCardDamage(mo);
        this.baseDamage = baseDamage;
    }

    @Override
    public void upgrade() { this.defaultUpgrade(); }

    @Override
    public AbstractCard makeCopy()
    {
        return new CounterAttack();
    }

}