package centurion.cards;

import basemod.devcommands.deck.DeckAdd;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import basemod.helpers.TooltipInfo;
import centurion.characters.Centurion;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import sun.util.resources.cldr.ar.CalendarData_ar_DZ;

import javax.smartcardio.Card;
import java.util.List;

import static centurion.CenturionMod.makeCardPath;

public class RankUp extends AbstractDynamicCard implements ModalChoice.Callback {

    // TEXT DECLARATION

    public static final String ID = centurion.CenturionMod.makeID(RankUp.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Centurion.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private ModalChoice modal;

    // /STAT DECLARATION/

    public RankUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.isInnate = true;
        FleetingField.fleeting.set(this, true);

        //TODO move descriptions to card strings
        modal = new ModalChoiceBuilder()
            .setTitle("Choose a Stance")
            .setCallback(this)
                .setColor(CardColor.RED)
                .addOption("Two-Handed", "Smash your foes and see them cower in front of you.", CardTarget.NONE)
                .setColor(CardColor.GREEN)
                .addOption("Dual Wield","Learn multi-hit attacks and whirlwind defense.", CardTarget.NONE)
                .setColor(CardColor.BLUE)
                .addOption("Sword & Shield","Improve your defensive abilities.", CardTarget.NONE)
                .create();
    }

    public List<TooltipInfo> getCustomTooltips()
    {
        return modal.generateTooltips();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        modal.open();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    public void optionSelected(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster, int i) {
        AbstractCard card;
        switch (i) {
            case 0:
                card = CardLibrary.getCard(RankUp.ID).makeCopy();
                break;
            case 1:
                card = CardLibrary.getCard(RankUp.ID).makeCopy();
                break;
            case 2:
                card = CardLibrary.getCard(RankUp.ID).makeCopy();
                break;
            default:
                return;
        }
        AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(card, card.current_x, card.current_y));
    }
}
