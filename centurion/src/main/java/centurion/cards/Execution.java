package centurion.cards;

import centurion.actions.ExecuteAction;
import centurion.characters.Centurion;
import com.badlogic.gdx.Game;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;

import static centurion.CenturionMod.makeCardPath;

public class Execution extends AbstractHeavyCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(Execution.class.getSimpleName());
    public static final String IMG = makeCardPath("Strike_Centurion.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 3;
    private static final int DAMAGE = 20;
    private static final int MAGIC_NUMBER = 2;
    private static final int UPGRADE_MAGIC_NUMBER = 0;
    private static final int ADDITIONAL_HEAVY_NUMBER = 1;
    private static final int UPGRADE_ADDITIONAL_HEAVY_NUMBER = 1;

    // /STAT DECLARATION/


    public Execution() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.baseMagicNumber = MAGIC_NUMBER;
        this.magicNumber = MAGIC_NUMBER;
        this.defaultBaseSecondMagicNumber = ADDITIONAL_HEAVY_NUMBER;
        this.defaultSecondMagicNumber = ADDITIONAL_HEAVY_NUMBER;
        this.retain = true;
        this.exhaust = true;
    }

    // Actions the card should do.
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ExecuteAction(m,
                        new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        this.defaultSecondMagicNumber,
                        this.uuid)
        );
    }

    //Upgraded stats.
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeDefaultSecondMagicNumber(UPGRADE_ADDITIONAL_HEAVY_NUMBER);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new Execution();
    }

}