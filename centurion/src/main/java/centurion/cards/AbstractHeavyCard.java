package centurion.cards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractHeavyCard extends AbstractDefaultCard {

    public AbstractHeavyCard(final String id,
                             final String img,
                             final int cost,
                             final CardType type,
                             final CardColor color,
                             final CardRarity rarity,
                             final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);

    }

    public void applyPowers() {
        AbstractPower strength = AbstractDungeon.player.getPower("Strength");
        if (strength != null) {
            strength.amount *= this.magicNumber;
        }
        super.applyPowers();
        if (strength != null) {
            strength.amount /= this.magicNumber;
        }
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPower strength = AbstractDungeon.player.getPower("Strength");
        if (strength != null) {
            strength.amount *= this.magicNumber;
        }
        super.calculateCardDamage(mo);
        if (strength != null) {
            strength.amount /= this.magicNumber;
        }
    }

}