/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals a given percent to the opponent's front unit
//per turn. Damage is done before healing.
//Used by Choco Knight and Ascended Choco Knight.
public class HealthyAttack extends Skill{
    
    private double percent;
    private int HPBefore;
    
    public HealthyAttack(Creature owner, double percent){
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
            enemyFormation.getFrontCreature().takeAOEDamage(Math.round(HPBefore * percent), enemyFormation);
        }
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new HealthyAttack(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "After attacking, deals damage equal to " + OtherThings.nicePercentString(percent) + " of health before";
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * (owner.getBaseAtt() + owner.getBaseHP() * percent*0.66));
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
}
