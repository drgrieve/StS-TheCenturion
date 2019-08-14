package centurion.cards;

import centurion.actions.PushOnAction;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static centurion.CenturionMod.makeCardPath;

public class SlowBurn extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(SlowBurn.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, SlowBurn.class.getSimpleName()));

    private static final int COST = 1;


    public SlowBurn() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.UPGRADE_REDUCE_COST_BY = 1;
        this.setSecondaryValues();
        this.exhaust = true;
        this.retain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToTop(new GainEnergyAction(2));
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        if (!this.isEthereal) super.triggerOnEndOfPlayerTurn();
        if (this.retain) {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedPower(p, 1), 1));
        }
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new SlowBurn();
    }}
