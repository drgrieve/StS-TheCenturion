package centurion.cards.twohanded;

import centurion.cards.AbstractHeavyCard;
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

public class HeavyBlow extends AbstractHeavyCard {

    public static final String ID = centurion.CenturionMod.makeID(HeavyBlow.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, HeavyBlow.class.getSimpleName()));

    private static final int COST = 2;
    private static final int ADDITIONAL_HEAVY_NUMBER = 1;
    private static final int UPGRADE_ADDITIONAL_HEAVY_NUMBER = 1;

    public HeavyBlow() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, 1, 0);

        this.DAMAGE = 14;
        this.SECOND_MAGIC_NUMBER = ADDITIONAL_HEAVY_NUMBER;
        this.UPGRADE_PLUS_SECOND_MAGIC_NUMBER = UPGRADE_ADDITIONAL_HEAVY_NUMBER;
        this.setSecondaryValues();

        AlwaysRetainField.alwaysRetain.set(this, true);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction.AttackEffect effect = this.damage >= 20 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        if (!this.isEthereal) super.triggerOnEndOfPlayerTurn();
        if (this.retain) {
            this.upgradeMagicNumber(this.defaultSecondMagicNumber);
        }
    }

    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new HeavyBlow();
    }

}