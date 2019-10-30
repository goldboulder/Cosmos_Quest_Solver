/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals a given percent as AOE
//per turn. Damage is done before healing.
//Used by Adam
public class HealthyAOE extends Skill{
    
    private double percent;
    private int HPBefore;
    
    public HealthyAOE(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {
        HPBefore = owner.getCurrentHP();
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){
            enemyFormation.takeAOEDamage(HPBefore * percent);
        }
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new HealthyAOE(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "After attacking, deals AOE equal to " + OtherThings.nicePercentString(percent) + " of health before";
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * (owner.getBaseAtt() + owner.getBaseHP() * percent*Formation.MAX_MEMBERS*0.50));
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
}
