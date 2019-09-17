package centurion.cards.skill;

import centurion.actions.EnhanceAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.tags.CustomTags;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class ReadyShield extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(ReadyShield.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, ReadyShield.class.getSimpleName()));

    private static final int COST = 1;


    public ReadyShield() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.BLOCK = 9;
        this.UPGRADE_PLUS_BLOCK = 3;
        this.setSecondaryValues();
        this.exhaust = true;
        AlwaysRetainField.alwaysRetain.set(this, true);
        tags.add(CustomTags.SHIELD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    public void triggerOnEndOfPlayerTurn() {
        if (!this.isEthereal) super.triggerOnEndOfPlayerTurn();
        if (this.retain) {
            this.baseBlock--;
        }
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new ReadyShield();
    }}
