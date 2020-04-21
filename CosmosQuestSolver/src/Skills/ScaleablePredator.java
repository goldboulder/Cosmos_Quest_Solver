/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;


//deal a set amount damage at start of fight to enemy with least HP that scales with level.
//used by Galla
public class ScaleablePredator extends Skill{
    
    private int amount;
    private double levelMilestone;
    
    public ScaleablePredator(Creature owner, int amount, double levelMilestone){
        super(owner);
        this.amount = amount;
        this.levelMilestone = levelMilestone;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleablePredator(newOwner,amount,levelMilestone);
    }
    
    @Override
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {
        if (!enemyFormation.isEmpty()){
            enemyFormation.getCreature(leastHPPosition(enemyFormation)).changeHP(-roundedScaleMilestone(owner,amount,1),enemyFormation);
        }
    }
    
    private int leastHPPosition(Formation enemyFormation){//empty line?
        int mostHPIndex = 0;
        long greatestHP = Long.MAX_VALUE;
        
        for (int i = 0; i < enemyFormation.size(); i++){
            Creature c = enemyFormation.getCreature(i);
            long cHP = c.getCurrentHP();//currentHP,two units have the same HP- select first in line
            
            if (cHP < greatestHP){
                greatestHP = cHP;
                mostHPIndex = i;
            }
        }
        return mostHPIndex;
    }
    
    @Override
    public String getDescription() {
        
        if (levelMilestone == 1){
            return "At start, deal " + amount + " damage per level to unit with least HP " + roundedScaleMilestoneStr(owner,amount,levelMilestone);
        }
        else{
            return "At start, deal " + amount + " damage per" + levelMilestone + " + levels to unit with least HP " + roundedScaleMilestoneStr(owner,amount,levelMilestone);
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt() + roundedScaleMilestone(owner,amount,levelMilestone)/2);
    }
    
    @Override
    public int positionBias() {
        return 0;
    }
    
}
