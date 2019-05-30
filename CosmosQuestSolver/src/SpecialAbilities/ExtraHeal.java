/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//heals are x times as effective.
// used by 
public class ExtraHeal extends SpecialAbility{
    
    private double multiplier;

    public ExtraHeal(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public double healBoost(double base) {//formation variable for p6 skills?
        return base * multiplier;
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ExtraHeal(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Heals are " + OtherThings.nicePercentString(multiplier) + " more effective";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * 1.15);
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
