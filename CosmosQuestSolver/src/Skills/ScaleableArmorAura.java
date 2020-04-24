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
//Halloween heroes, quest heroes 13-20 and 25-32, and Ascended Athos
public class ScaleableArmorAura extends ArmorAura{
        
    private double levelMilestone;
    
    public ScaleableArmorAura(Creature owner, int armorBoost, Element element, double levelMilestone) {//if elsment is null, apply to all creatures
        super(owner,armorBoost,element);
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableArmorAura(newOwner,armorBoost,element,levelMilestone);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addArmor(roundedScaleMilestone(owner,armorBoost,levelMilestone));
            }
        }
        
        
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        
            
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addArmor(-roundedScaleMilestone(owner,armorBoost,levelMilestone));
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
        
        
        return "+" + armorBoost + " armor to " + elementStr + " creatures every " + milestoneStr + " " + roundedScaleMilestoneStr(owner,armorBoost,levelMilestone);
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + armorBoost + " " + Elements.getString(element) + " " + levelMilestone;
    }
    
    @Override
    public int viability() {
        int actualArmorBoost = (roundedScaleMilestone(owner,armorBoost,levelMilestone));
        return owner.getBaseAtt() * (owner.getBaseHP() + (Formation.MAX_MEMBERS/3) * actualArmorBoost);
    }
    
}
