/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;
import cosmosquestsolver.OtherThings;

//Increases the attack of all creatures.
// every turn while creature is alive. Increases lineraly as level increaces.
//used by Yeti the Postman
public class ScaleableGrowingAttAura extends Skill{
    
    private double attBoost;
    private int turns;
    private double levelMilestone;
    private int currentTurn = 0;
    
    public ScaleableGrowingAttAura(Creature owner, double attBoost, int turns, double levelMilestone) {//if elsment is null, apply to all creatures
        super(owner);
        this.attBoost = attBoost;
        this.turns = turns;
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableGrowingAttAura(newOwner,attBoost,turns,levelMilestone);
    }
    /*
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        
        for (Creature c : thisFormation){
            c.setCurrentAtt(c.getCurrentAtt() + roundedScaleMilestone(owner,attBoost,levelMilestone));
            //creature.addAttConstantBoost(roundedScaleMilestone(owner,attBoost,levelMilestone));//round up?***
        }
        
        
    }
    */
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        
        currentTurn ++;
        if (currentTurn % turns == 0){
            for (Creature c : thisFormation){
                c.setCurrentAtt(c.getCurrentAtt() + Math.round(attBoost*owner.getLevel()/levelMilestone));
                //creature.addAttConstantBoost(roundedScaleMilestone(owner,attBoost,levelMilestone));//round up?***
            }
        }
        
    }
/*
    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        
            
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addAttConstantBoost(-roundedScaleMilestone(owner,attBoost,levelMilestone));//round up?***
            }
        }
        
    }
*/
    
    
    @Override
    public String getDescription() {
        
        String milestoneStr = "";
        if (levelMilestone % 1 == 0){
            if (levelMilestone == 1){
                milestoneStr = "level";
            }
            else{
                milestoneStr = Integer.toString((int)levelMilestone) + " levels";
            }
        }
        else{
            if (levelMilestone == 1){
                milestoneStr = "level";
            }
            else{
                milestoneStr = Double.toString(levelMilestone) + " levels";
            }
        }
        int actualBoost = (int)Math.round(attBoost*owner.getLevel()/levelMilestone);
        if (turns == 1){
            return "+" + OtherThings.intOrNiceDecimalFormat(attBoost) + " attack to all creatures every " + milestoneStr + " per turn (" + actualBoost + ")";
        }
        else{
            return "+" + OtherThings.intOrNiceDecimalFormat(attBoost) + " attack to all creatures every " + milestoneStr + " per " + turns + " turns (" + actualBoost + ")";
        }
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attBoost + " " + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        int actualAttBoost = (roundedScaleMilestone(owner,attBoost,levelMilestone));
        return (owner.getBaseAtt() + (Formation.MAX_MEMBERS*2/turns) * actualAttBoost) * owner.getBaseHP();
    }
    
    @Override
    public int positionBias() {
        return -3;
    }
    
}
