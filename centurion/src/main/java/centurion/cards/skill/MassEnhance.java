package centurion.cards.skill;

import centurion.actions.EnhanceAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static centurion.CenturionMod.makeCardPath;

public class MassEnhance extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(MassEnhance.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, MassEnhance.class.getSimpleName()));

    private static final int COST = 0;


    public MassEnhance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
        this.exhaust = true;
        this.EXHAUSTIVE_AMT = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardGroup cards = p.drawPile;
        cards.group.addAll(p.discardPile.group);
        cards.group.addAll(p.hand.group);
        ArrayList<CardType> types = new ArrayList<AbstractCard.CardType>();
        types.add(AbstractCard.CardType.ATTACK);
        types.add(AbstractCard.CardType.SKILL);
        AbstractDungeon.actionManager.addToBottom(new EnhanceAction(cards, 0, types, EnhanceAction.EnhanceActionType.ALL).mustBeTagged());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.exhaust = false;
            ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE_AMT);
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new MassEnhance();
    }}
