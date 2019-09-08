package centurion.cards.attack;

import centurion.CenturionMod;
import centurion.actions.EnhanceAction;
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

public class EnhancingStrike extends AbstractDynamicCard {

    public static final String ID = CenturionMod.makeID(EnhancingStrike.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, EnhancingStrike.class.getSimpleName()));

    private static final int COST = 1;

    public EnhancingStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.DAMAGE = 4;
        this.UPGRADE_PLUS_DMG = 3;
        this.setSecondaryValues();
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.baseMagicNumber++;
        this.baseDamage++;
        this.applyPowers();
        if (baseMagicNumber == 1) {
            this.rawDescription = this.cardStrings.DESCRIPTION + this.cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
        AbstractDungeon.actionManager.addToBottom(new EnhanceAction(p.drawPile, this.baseMagicNumber, CardType.ATTACK));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new EnhancingStrike();
    }

}