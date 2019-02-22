/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Elements.Element;
import Formations.Formation;

//additively boosts damage with element damage.
//used by Quest heroes 21-24 and drifter heroes
public class ElementDamageBoost extends SpecialAbility{
    
    private double percentBoost;
    private Element element;

    public ElementDamageBoost(Creature owner, Element element, double multiplier) {
        super(owner);
        this.element = element;
        this.percentBoost = multiplier;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ElementDamageBoost(newOwner,element,percentBoost);
    }
    

    
    
    @Override
    public String getDescription() {
        String elementStr;
        if (element == null){
            elementStr = "all non-void";
        }
        else{
            elementStr = Elements.getString(element).toLowerCase();
        }
        
        if ((percentBoost*100) % 1 == 0){
            return "+" + ((int)(100*percentBoost)) + "% damage to " + elementStr + " creatures";
        }
        return "+" + (100*percentBoost) + "% damage to " + elementStr + " creatures";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percentBoost;
    }
    
    @Override
    public double getElementDamageBoost(Element elementAttacked) {//rounding?
        if (elementAttacked == this.element ||(owner.getElement() == Element.VOID && !(elementAttacked == Element.VOID))){
            return percentBoost;
        }
        else{
            return 0;
        }
    }
    
    @Override
    public int viability() {
        if (element == null){
            return owner.getBaseHP() * owner.getBaseAtt() * (int)(1+percentBoost);
        }
        else{
            return owner.getBaseHP() * owner.getBaseAtt() * (int)(1+(percentBoost/Elements.numElements()));
        }
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
