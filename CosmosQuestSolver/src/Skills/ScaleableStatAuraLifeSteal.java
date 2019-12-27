/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;

//Combines ScaleableStatArua and ScaleableLifeSteal
//used by Emily
public class ScaleableStatAuraLifeSteal extends StatAuraLifeSteal{
        
    private double levelMilestone;
    
    public ScaleableStatAuraLifeSteal(Creature owner, int amount, double levelMilestone) {//if elsment is null, apply to all creatures
        super(owner,amount);
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableStatAuraLifeSteal(newOwner,amount,levelMilestone);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        
        for (Creature creature : thisFormation){
            creature.addAttConstantBoost(roundedScaleMilestone(owner,amount,levelMilestone));//round up?***
            creature.addArmor(roundedScaleMilestone(owner,amount,levelMilestone));
        }
        
        
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeAOEDamage(roundedScaleMilestone(owner,amount,levelMilestone));
    }
    
    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        thisFormation.AOEHeal(roundedScaleMilestone(owner,amount,levelMilestone), enemyFormation);
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            creature.addAttConstantBoost(-roundedScaleMilestone(owner,amount,levelMilestone));//round up?***
            creature.addArmor(-roundedScaleMilestone(owner,amount,levelMilestone));
        }
    }

    
    
    @Override
    public String getDescription() {
        String plural = levelMilestone == 1 ? "level" : "levels";
        return "+ " + amount + " attack, armor, AOE, and healing to all creatures per " + levelMilestone +" " + plural + " (" + roundedScaleMilestone(owner,amount,levelMilestone) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        int actualAmount = (roundedScaleMilestone(owner,amount,levelMilestone));
        return (owner.getBaseAtt() + (Formation.MAX_MEMBERS/3) * actualAmount) * (owner.getBaseHP() + (Formation.MAX_MEMBERS/3) * actualAmount)  + (owner.getBaseHP() * actualAmount * Formation.MAX_MEMBERS * 2);
    }
    
}
