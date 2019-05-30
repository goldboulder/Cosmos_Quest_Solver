/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//grants x% more attack.
// used by 
public class AttackBoost extends SpecialAbility{
    
    private double multiplier;

    public AttackBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.setCurrentAtt((int)(owner.getCurrentAtt() * (1+multiplier)));
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new AttackBoost(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return " Grants " + OtherThings.nicePercentString(multiplier) + " more attack";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+multiplier));
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
