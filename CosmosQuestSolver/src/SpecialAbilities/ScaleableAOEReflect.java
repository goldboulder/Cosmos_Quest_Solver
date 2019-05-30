/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;
import Formations.Hero;

//reflects a percentage of the direct damage back as AOE.
//used by Fir
public class ScaleableAOEReflect extends SpecialAbility{
    
    private double multiplier;
    private long damageTakenThisRound;

    public ScaleableAOEReflect(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageTakenThisRound = 0;
    }
    

    @Override
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation){//is this skill asymetric? (which side you're on matters)
        damageTakenThisRound += damage;
    }

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            enemyFormation.takeRawDamage(damageTakenThisRound * roundedScaleMilestoneDouble(owner,multiplier,1));
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAOEReflect(newOwner,multiplier);
    }
    
    
    
    @Override
    public String getDescription() {
        return "Returns " + OtherThings.nicePercentString(multiplier) + " of damage recieved as AOE every level (" + OtherThings.nicePercentString(roundedScaleMilestoneDouble(owner,multiplier,1)) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+1.5*Formation.MAX_MEMBERS*roundedScaleMilestoneDouble(owner,multiplier,1)));
    }

    @Override
    public int positionBias() {
        return 2;
    }
    
    
    
}
