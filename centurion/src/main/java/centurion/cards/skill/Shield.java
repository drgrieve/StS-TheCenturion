package centurion.cards.skill;

import basemod.helpers.BaseModCardTags;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import centurion.tags.CustomTags;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Shield extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Shield.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Shield.class.getSimpleName()));

    private static final int COST = 1;

    public Shield() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.BLOCK = 4;
        this.UPGRADE_PLUS_BLOCK = 3;
        this.setSecondaryValues();
        this.tags.add(BaseModCardTags.BASIC_DEFEND);
        tags.add(CustomTags.SHIELD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() { this.defaultUpgrade(); }

    @Override
    public AbstractCard makeCopy()
    {
        return new Shield();
    }}
