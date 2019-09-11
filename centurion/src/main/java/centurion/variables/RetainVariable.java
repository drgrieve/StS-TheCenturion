package centurion.variables;

import basemod.abstracts.DynamicVariable;
import centurion.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static centurion.CenturionMod.makeID;

public class RetainVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return makeID("RETAIN");
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return ((AbstractDefaultCard) card).isRetainVariableModified;
    }
   
    @Override
    public int value(AbstractCard card)
    {
        return ((AbstractDefaultCard) card).retainVariable;
    }
    
    @Override
    public int baseValue(AbstractCard card)
    {
        return ((AbstractDefaultCard) card).baseRetainVariable;
    }
    
    @Override
    public boolean upgraded(AbstractCard card)
    {
        return ((AbstractDefaultCard) card).upgradedRetainVariable;
    }
}