/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//at the beginning of the fight, deal AOE to all units equal to the level times
//the number of units more the enemy formation has divided by the milestone.
//used by Yisus
public class ScaleableFlatField extends Skill{
    
    private double levelMilestone;
    private int damage;

    public ScaleableFlatField(Creature owner, int damage, double levelMilestone) {
        super(owner);
        this.damage = damage;
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableFlatField(newOwner,damage,levelMilestone);
    }
    
    @Override
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {
        double numExtra =  enemyFormation.size() - thisFormation.size();
        
        if (numExtra <= 0){
            return;
        }
        enemyFormation.takeAOEDamage(roundedScaleMilestone(owner,damage,levelMilestone) * numExtra);
    }

    
    
    @Override
    public String getDescription() {
        if (levelMilestone == 1){
            return "At start, deals " + damage + " AOE per number of units the enemy formation has more of per level";
        }
        return "At start, deals " + damage + " AOE per number of units the enemy formation has more of per " + levelMilestone + " levels " + roundedScaleMilestoneStr(owner,damage,levelMilestone);
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt() + roundedScaleMilestone(owner,damage,levelMilestone));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
}
