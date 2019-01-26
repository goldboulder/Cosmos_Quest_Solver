/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;

//attacks a random enemy each turn (only one hit, enemy in first is not guarenteed to get hit)
//used by Quest heroes 21-24
public class ElementDamageBoost extends SpecialAbility{
    
    private double percentBoost;

    public ElementDamageBoost(Creature owner, double multiplier) {
        super(owner);
        this.percentBoost = multiplier;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ElementDamageBoost(newOwner,percentBoost);
    }
    

    
    
    @Override
    public String getDescription() {
        double elementalDamage = percentBoost + Elements.DAMAGE_BOOST;
        if (elementalDamage % 1 == 0){
            return "x " + ((int)elementalDamage) + " damage to elemental strength";
        }
        return "x " + (elementalDamage) + " damage to elemental strength";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percentBoost;
    }
    
    public double getElementDamageBoost() {
        return percentBoost;
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * (int)(1+percentBoost);
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
