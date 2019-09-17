package centurion.cards.skill;

import basemod.abstracts.CustomCard;
import centurion.CenturionMod;
import centurion.actions.FilterAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class ShiftingPlan extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(ShiftingPlan.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, ShiftingPlan.class.getSimpleName()));

    private static final int COST = 0;
    private AbstractCard transformedCard;
    private AbstractCard originalCard;

    public ShiftingPlan() {
        this(true);
    }

    public ShiftingPlan(boolean setOriginal) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.isInnate = true;
        AlwaysRetainField.alwaysRetain.set(this, true);
        if (setOriginal) this.originalCard = new ShiftingPlan(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.transformedCard != null) {
            transformedCard.applyPowers();
            transformedCard.use(p, m);
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (super.canUse(p, m)) {
            return transformedCard != null;
        }
        return true;
    }

    @Override
    public void triggerOnExhaust() {
        this.transformedCard = this.originalCard;
        applyTransform(false);
        this.transformedCard = null;
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        if (!this.isEthereal) super.triggerOnEndOfPlayerTurn();
        if (this.retain) {
            CardType cardType = AbstractDungeon.cardRng.randomBoolean() ? CardType.SKILL : CardType.ATTACK;
            AbstractCard c = null;
            while(c == null || c.cardID == ShiftingPlan.ID)
                c = AbstractDungeon.returnTrulyRandomCardInCombat(cardType);
            Centurion centurion = (Centurion) AbstractDungeon.player;
            CardGroup stanceCards = centurion.getStanceCardPool().getCardsOfType(cardType);
            c = centurion.modifyCardReward(c, stanceCards);
            transformedCard = c.makeCopy();
            applyTransform(true);
        }
    }

    private void applyTransform(boolean isShifting) {
        if (this.upgraded) transformedCard.upgrade();
        this.target = transformedCard.target;
        this.type = transformedCard.type;
        this.cost = transformedCard.cost;
        this.costForTurn = transformedCard.cost;
        this.name = transformedCard.name + (isShifting ? "?" : "");
        this.baseDamage = transformedCard.baseDamage;
        this.baseBlock = transformedCard.baseBlock;
        this.baseMagicNumber = transformedCard.baseMagicNumber;
        this.magicNumber = transformedCard.baseMagicNumber;
        this.defaultBaseSecondMagicNumber = SECOND_MAGIC_NUMBER;
        this.exhaust = transformedCard.exhaust;
        ExhaustiveVariable.setBaseValue(this, ExhaustiveField.ExhaustiveFields.exhaustive.get(this.transformedCard));
        if (transformedCard instanceof AbstractDynamicCard) {
            this.defaultBaseSecondMagicNumber = ((AbstractDynamicCard)transformedCard).defaultBaseSecondMagicNumber;
            this.defaultSecondMagicNumber = ((AbstractDynamicCard)transformedCard).defaultSecondMagicNumber;
        }
        this.rawDescription = transformedCard.rawDescription;
        this.initializeDescription();
        this.loadCardImage(((CustomCard)transformedCard).textureImg);
        this.applyPowers();
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new ShiftingPlan();
    }

}