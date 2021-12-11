package centurion.variables;

import basemod.abstracts.DynamicVariable;
import centurion.cards.AbstractCenturionCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

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
        return ((AbstractCenturionCard) card).isRetainVariableModified;
    }
   
    @Override
    public int value(AbstractCard card)
    {
        return ((AbstractCenturionCard) card).retainVariable;
    }
    
    @Override
    public int baseValue(AbstractCard card)
    {
        return ((AbstractCenturionCard) card).baseRetainVariable;
    }
    
    @Override
    public boolean upgraded(AbstractCard card)
    {
        return ((AbstractCenturionCard) card).upgradedRetainVariable;
    }
}