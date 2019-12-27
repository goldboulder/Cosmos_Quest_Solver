/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//increases attack and HP by specified amounts for each unit behind depending on level
//owner. Used by Hans
public class ScaleableHiddenCharger extends Skill{//milestone?
    
    private int attPerLevel;
    private int HPPerLevel;
    private int turns;
    private double levelMilestone;
    private int currentTurn = 0;
    
    public ScaleableHiddenCharger(Creature owner, int attPerLevel, int HPPerLevel, int turns, double levelMilestone) {
        super(owner);
        this.attPerLevel = attPerLevel;
        this.HPPerLevel = HPPerLevel;
        this.turns = turns;
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableHiddenCharger(newOwner,attPerLevel,HPPerLevel,turns,levelMilestone);
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        currentTurn ++;
        if (currentTurn % turns == 0){
            int unitsInFront = unitsInFront(thisFormation);
            int attBoost = roundedScaleMilestone(owner,attPerLevel,levelMilestone);
            int HPBoost = roundedScaleMilestone(owner,HPPerLevel,levelMilestone);
            owner.setCurrentAtt(owner.getCurrentAtt() + attBoost * unitsInFront);
            owner.setCurrentHP(owner.getCurrentHP() + HPBoost * unitsInFront);
            owner.setMaxHP(owner.getMaxHP() + HPBoost);
        }
    }
    
    private int unitsInFront(Formation f){
        int numBehind = 0;
        boolean foundOwner = false;
        for (Creature creature : f){
            if (foundOwner && !creature.isDead()){//isDead needed?
                numBehind ++;
            }
            
            if (creature == owner){//this goes last in case a monster has this ability(not possible currently)
                foundOwner = true;
            }
        }
        
        return f.size() - 1 - numBehind;
    }

    
    
    
        
    @Override
    public String getDescription() {
        String pluralTurns = turns == 1 ? "turn" : turns + " turns";
        if (levelMilestone == 1){
            return "Increaces attack by " + attPerLevel + " and HP by " + HPPerLevel + " per unit in front per level per " + pluralTurns + " " +  roundedScaleMilestoneStr(owner,attPerLevel,levelMilestone);
        }
        else{
            return "Increaces attack by " + attPerLevel + " and HP by " + HPPerLevel + " per unit in front per " + levelMilestone + " levels per " + pluralTurns + " " + roundedScaleMilestoneStr(owner,attPerLevel,levelMilestone);
        }
        
    }
    
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attPerLevel + " " + HPPerLevel;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() + roundedScaleMilestone(owner,HPPerLevel,levelMilestone)*Formation.MAX_MEMBERS/2/turns) * (owner.getBaseAtt() + roundedScaleMilestone(owner,attPerLevel,levelMilestone)*Formation.MAX_MEMBERS/2/turns);
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
}
