package centurion.cards.attack;

import centurion.CenturionMod;
import centurion.actions.DamageAsBlockAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class OpportuneStrike extends AbstractDynamicCard {

    public static final String ID = CenturionMod.makeID(OpportuneStrike.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, OpportuneStrike.class.getSimpleName()));

    private static final int COST = 2;

    public OpportuneStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.DAMAGE = 16;
        this.UPGRADE_PLUS_DMG = 4;
        this.setSecondaryValues();
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.damage > 0) {
            AbstractGameAction.AttackEffect effect = this.damage >= 20 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
            AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
        }
        if (this.magicNumber > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.magicNumber));
        }
    }

    public void applyPowers() {
        calculateCardDamage(null);
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        int beforeDamage = this.damage;
        this.damage = calculateDamage();
        if (beforeDamage - this.damage > 0) {
            this.baseMagicNumber = beforeDamage - this.damage;
            this.magicNumber = this.baseMagicNumber;
        }
        else {
            this.baseMagicNumber = 0;
            this.magicNumber = 0;
        }
    }

    public int calculateDamage() {
        int incomingDamage = -AbstractDungeon.player.currentBlock;
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (!mon.isDeadOrEscaped()) {
                if (mon.getIntentDmg() >= 0) incomingDamage+= mon.getIntentDmg();
            }
        }
        if (incomingDamage > 0) {
            if (this.damage - incomingDamage <= 0) return 0;
            return this.damage - incomingDamage;
        }
        return this.damage;
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new OpportuneStrike();
    }

}