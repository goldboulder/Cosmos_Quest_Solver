/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;


//deals AOE damage when the owner kills an enemy depending on level
//used by Ascended Defile
public class ScaleableBloodBomb extends SpecialAbility{
        
    private double levelMilestone;
    private int damage;
        
    public ScaleableBloodBomb(Creature owner, int damage, double levelMilestone) {
        super(owner);
        this.damage = damage;
        this.levelMilestone = levelMilestone;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        //super.attack(thisFormation,enemyFormation);
        if (enemyFormation.getFrontCreature().isDead()){
            //enemyFormation.takeAOEDamage(roundedScaleMilestone(owner,damage,levelMilestone));//bubbles does not work with this skill
            enemyFormation.takeRawDamage(roundedScaleMilestone(owner,damage,levelMilestone));
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableBloodBomb(newOwner,damage,levelMilestone);
    }
    
    
    
    @Override
    public String getDescription() {
        
        String milestoneStr = "";
        if (levelMilestone % 1 == 0){
            milestoneStr = Integer.toString((int)levelMilestone);
        }
        else{
            milestoneStr = Double.toString(levelMilestone);
        }
        
        if (levelMilestone == 1){
            return "After killing, deals " + damage + " aoe every level " + roundedScaleMilestoneStr(owner,damage,levelMilestone);
        }
        else{
            return "After killing, deals " + damage + " aoe every " + milestoneStr + " levels " + roundedScaleMilestoneStr(owner,damage,levelMilestone);
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        int actualDamage = roundedScaleMilestone(owner,damage,levelMilestone);
        return (owner.getBaseHP() * owner.getBaseAtt()) + (actualDamage * actualDamage * Formation.MAX_MEMBERS);
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
}
