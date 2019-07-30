/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Elements.Element;
import Formations.Formation;

//additively boosts damage with element damage.
//used by djinns and drifter heroes
public class ElementDamageBoost extends Skill{
    
    private double percentBoost;
    private Element element;

    public ElementDamageBoost(Creature owner, Element element, double multiplier) {
        super(owner);
        this.element = element;
        this.percentBoost = multiplier;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
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
        double baseElementBoost = owner.getElement() == Element.VOID ? 1.0 : Elements.DAMAGE_BOOST;
        
        if ((percentBoost*100) % 1 == 0){
            return "+" + ((int)(100*percentBoost)) + "% elemental damage to " + elementStr + " creatures (x " + (baseElementBoost + percentBoost) + ")";
        }
        return "+" + (100*percentBoost) + "% elemental damage to " + elementStr + " creatures (x " + (baseElementBoost + percentBoost) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percentBoost + " " + Elements.getString(element);
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
            return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+percentBoost));
        }
        else{
            return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+(percentBoost/Elements.numElements())));
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
