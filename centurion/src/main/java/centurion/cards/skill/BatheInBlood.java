package centurion.cards.skill;

import centurion.actions.FilterAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.powers.BleedPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class BatheInBlood extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(BatheInBlood.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, BatheInBlood.class.getSimpleName()));

    private static final int COST = 2;

    public BatheInBlood() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.UPGRADE_REDUCE_COST_BY = 1;
        this.setSecondaryValues();
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bleed = 0;
        for(AbstractMonster monster: AbstractDungeon.getMonsters().monsters) {
            if(!monster.isDeadOrEscaped() && monster.hasPower(BleedPower.POWER_ID))
                bleed+=monster.getPower((BleedPower.POWER_ID)).amount;
        }
        if (bleed > 0)
            AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, bleed));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BatheInBlood();
    }

}