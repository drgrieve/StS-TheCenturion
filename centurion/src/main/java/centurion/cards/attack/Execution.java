package centurion.cards.attack;

import centurion.actions.ExecuteAction;
import centurion.cards.AbstractHeavyCard;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Execution extends AbstractHeavyCard {

    public static final String ID = centurion.CenturionMod.makeID(Execution.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Execution.class.getSimpleName()));

    private static final int COST = 3;
    private static final int HEAVY = 2;
    private static final int ADDITIONAL_HEAVY_NUMBER = 1;
    private static final int UPGRADE_ADDITIONAL_HEAVY_NUMBER = 1;

    public Execution() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, HEAVY, 0);

        this.DAMAGE = 20;
        this.setSecondaryValues();

        this.SECOND_MAGIC_NUMBER = ADDITIONAL_HEAVY_NUMBER;
        this.UPGRADE_PLUS_SECOND_MAGIC_NUMBER = UPGRADE_ADDITIONAL_HEAVY_NUMBER;

        AlwaysRetainField.alwaysRetain.set(this, true);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ExecuteAction(m,
                        new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        this.defaultSecondMagicNumber,
                        this.uuid)
        );
    }

    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Execution();
    }

}