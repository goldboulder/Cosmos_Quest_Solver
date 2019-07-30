/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;

//Increases the attack and armor of all creatures
//of a specified element (everyone if element is null) (including the owner)
//while creature is alive. Increases lineraly as level increaces. Used by
//Halloween heroes, quest heroes 13-20 and 25-32, Ascended Athos, Ascended Rei, and
//Ascended Bavah
public class ScaleableAttackBoostAura extends AttackBoostAura{
        
    private double levelMilestone;
    
    public ScaleableAttackBoostAura(Creature owner, int attBoost, Element element, double levelMilestone) {//if elsment is null, apply to all creatures
        super(owner,attBoost,element);
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAttackBoostAura(newOwner,attBoost,element,levelMilestone);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addAttConstantBoost(roundedScaleMilestone(owner,attBoost,levelMilestone));//round up?***
            }
        }
        
        
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        
            
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addAttConstantBoost(-roundedScaleMilestone(owner,attBoost,levelMilestone));//round up?***
            }
        }
        
    }

    
    
    @Override
    public String getDescription() {
        
        String elementStr = "";
        if (element == null){
            elementStr = "all";
        }
        else{
            switch(element){//method for this?
                case AIR: elementStr = "air"; break;
                case WATER: elementStr = "water"; break;
                case EARTH: elementStr = "earth"; break;
                case FIRE: elementStr = "fire"; break;
                default: elementStr = "all";
            }
        }
        
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
        
        
            return "+" + attBoost + " attack to " + elementStr + " creatures every " + milestoneStr + " " + roundedScaleMilestoneStr(owner,attBoost,levelMilestone);
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attBoost + " " + Elements.getString(element) + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        int actualAttBoost = (roundedScaleMilestone(owner,attBoost,levelMilestone));
        return (owner.getBaseAtt() + (Formation.MAX_MEMBERS/3) * actualAttBoost) * owner.getBaseHP();
    }
    
}
