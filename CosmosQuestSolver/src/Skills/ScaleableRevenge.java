/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//attack increaces by x% of damage recieved
//used by the eternal heroes
public class ScaleableRevenge extends Skill{
    
    private double percentPerLevel;
    private int damageStorage;//attack only increases at end of turn
    
    public ScaleableRevenge(Creature owner, double percentPerLevel) {
        super(owner);
        this.percentPerLevel = percentPerLevel;
    }
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableRevenge(newOwner,percentPerLevel);
    }
    
    @Override
    public void recordAnyDamageTaken(long damage, Formation thisFormation, Formation enemyFormation){
        damageStorage += (int)(damage * roundedScaleMilestoneDouble(owner,this.percentPerLevel,1) + 0.5);
        
    }
    
    @Override
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation){//lep damage
        owner.setCurrentAtt((int)(owner.getCurrentAtt() + damageStorage));
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {
        damageStorage = 0;
    }
    
    @Override
    public void postRoundAction3(Formation thisFormation, Formation enemyFormation) {
        owner.setCurrentAtt((int)(owner.getCurrentAtt() + damageStorage));
    }
    
    @Override
    public String getDescription() {
        return "Attack increaces by " + OtherThings.nicePercentString(percentPerLevel) + " of damage recieved per level (" + OtherThings.nicePercentString(roundedScaleMilestoneDouble(owner,percentPerLevel,1)) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percentPerLevel;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1 + roundedScaleMilestoneDouble(owner,this.percentPerLevel,1)));
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
}
