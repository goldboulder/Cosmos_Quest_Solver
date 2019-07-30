/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//attack boosts are x times as effective.
// used by Nerissa and Bornag
public class Absorb extends Skill{
    
    private double multiplier;

    public Absorb(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Absorb(newOwner,multiplier);
    }
    /*
    @Override//ricochet attacks are affected!
    public void takeHit(Creature attacker, Formation thisFormation, Formation enemyFormation, double hit) {
            
            super.takeHit(attacker, thisFormation, enemyFormation,(hit + owner.getArmor()) * multiplier - owner.getArmor());
    }
*/
    
    @Override
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){
        return Math.round(damage);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.addArmorPercentBoost(multiplier);
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
