package centurion.cards.skill;

import centurion.actions.FilterAction;
import centurion.actions.FocusAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static centurion.CenturionMod.makeCardPath;

public class Focus extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(Focus.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, Focus.class.getSimpleName()));

    private static final int COST = -1;

    public Focus() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int bonus = this.upgraded ? 1 : 0;
        AbstractDungeon.actionManager.addToBottom(new FocusAction(p, bonus, this.freeToPlayOnce, this.energyOnUse, cardStrings.EXTENDED_DESCRIPTION[0]));
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }
}