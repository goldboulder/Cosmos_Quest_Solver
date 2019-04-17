/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//attack boosts are x times as effective.
// used by Nerissa
public class Absorb extends SpecialAbility{
    
    private double multiplier;

    public Absorb(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Absorb(newOwner,multiplier);
    }
    
    @Override
    public double alterIncomingDamage(double hit, double initialHit, Formation thisFormation, Formation enemyFormation) {
        return (hit + owner.getArmor()) * multiplier - owner.getArmor();
    }
    
    @Override
    public String getDescription() {
        return "Absorbs " + OtherThings.nicePercentString(1 - multiplier) + " direct damage";
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
