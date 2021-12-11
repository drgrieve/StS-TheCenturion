package centurion.cards.skill;

import centurion.actions.FilterAction;
import centurion.cards.AbstractDynamicCard;
import centurion.characters.Centurion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import static centurion.CenturionMod.makeCardPath;

public class BringItOn extends AbstractDynamicCard {

    public static final String ID = centurion.CenturionMod.makeID(BringItOn.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;
    public static final String IMG = makeCardPath(makeImageName(TYPE, BringItOn.class.getSimpleName()));

    private static final int COST = 0;

    public BringItOn() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.setSecondaryValues();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = countAliveMonsters(AbstractDungeon.getMonsters());
        AbstractDungeon.actionManager.addToBottom(new FilterAction(count));
        if (this.upgraded) AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, count));
    }

    private int countAliveMonsters(MonsterGroup monsters) {
        int count = 0;
        for(AbstractMonster m: monsters.monsters)
            if (!m.isDead && !m.escaped) count++;
        return count;
    }

    @Override
    public void upgrade() {
        this.defaultUpgrade();
    }
}