/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;


//deal a set amount damage at start of fight that scales with level. Used by Dr Hawking
public class ScaleableStartingDamage extends StartingDamage{
    
    private double levelMilestone;
    
    public ScaleableStartingDamage(Creature owner, int amount, double levelMilestone){
        super(owner,amount);
        this.levelMilestone = levelMilestone;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableStartingDamage(newOwner,amount,levelMilestone);
    }
    
    @Override
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeAOEDamage(roundedScaleMilestone(owner,amount,1));
    }
    
    @Override
    public String getDescription() {
        
        if (levelMilestone == 1){
            return "At start, deal " + amount + " aoe per level " + roundedScaleMilestoneStr(owner,amount,levelMilestone);
        }
        else{
            return "At start, deal " + amount + " aoe every " + levelMilestone + " levels " + roundedScaleMilestoneStr(owner,amount,levelMilestone);
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt() + Formation.MAX_MEMBERS * (roundedScaleMilestone(owner,amount,1)));
    }
    
}
