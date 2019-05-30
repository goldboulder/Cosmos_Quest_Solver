/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reduces damage against the same element
// used by 
public class Affinity extends SpecialAbility{
    
    private double multiplier;

    public Affinity(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Affinity(newOwner,multiplier);
    }
    
    @Override//rico???
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){
        if (attacker.getElement() == owner.getElement()){
            return (damage + owner.getArmor()) * multiplier - owner.getArmor();
        }
        else{
            return damage;
        }
    }
    
    @Override
    public String getDescription() {
        return "Takes " + OtherThings.nicePercentString(1 - multiplier) + " damage from the same element";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return (int)((owner.getBaseHP()/(multiplier + 0.1)) * owner.getBaseAtt());
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
