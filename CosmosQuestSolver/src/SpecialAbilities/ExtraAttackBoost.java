/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//attack boosts are x times as effective.
// used by 
public class ExtraAttackBoost extends SpecialAbility{
    
    private double multiplier;

    public ExtraAttackBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation) {
        return owner.getAttBoost() * (multiplier-1);
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ExtraAttackBoost(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Attack boost is " + OtherThings.intOrNiceDecimalFormat(multiplier) + " times as effective";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return owner.getBaseHP() * (owner.getBaseAtt() + 4);
    }

    @Override
    public int positionBias() {
        return 2;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
