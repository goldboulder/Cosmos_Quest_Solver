/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;

//the most common special ability. Increases the attack and armor of all creatures
//of a specified element (everyone if element is null) (including the owner)
//while creature is alive
public class StatAura extends Skill{
    
    protected int attBoost;
    protected int armorBoost;
    protected Element element;

    public StatAura(Creature owner, int attBoost, int armorBoost, Element element) {//if elsment is null, apply to all creatures
        super(owner);
        this.attBoost = attBoost;
        this.armorBoost = armorBoost;
        this.element = element;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new StatAura(newOwner,attBoost,armorBoost,element);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addAttConstantBoost(attBoost);
                creature.addArmor(armorBoost);
            }
        }
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addAttConstantBoost(-attBoost);
                creature.addArmor(-armorBoost);
            }
        }
    }
    
    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        if (element == null){
            sb.append("All");
        }
        else{
            switch(element){//method for this?
                case AIR: sb.append("Air"); break;
                case WATER: sb.append("Water"); break;
                case EARTH: sb.append("Earth"); break;
                case FIRE: sb.append("Fire"); break;
                default: sb.append("All");
            }
        }
        sb.append(" creatures ");
        
        if (attBoost != 0 && armorBoost == 0){
            sb.append("have +").append(attBoost).append(" attack");
        }
        else if (attBoost == 0 && armorBoost != 0){
            sb.append("have +").append(armorBoost).append(" armor");
        }
        else if (attBoost != 0 && armorBoost != 0){
            sb.append("have +").append(attBoost).append(" attack and +").append(armorBoost).append(" armor");
        }
        else{//no boost
            return "";
        }
        return sb.toString();
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attBoost + " " + armorBoost + " " + Elements.getString(element);
    }
    
    @Override
    public int viability() {
        return (owner.getBaseAtt() + (Formation.MAX_MEMBERS/3) * attBoost) * (owner.getBaseHP() + (Formation.MAX_MEMBERS/3) *armorBoost);
    }

    @Override
    public int positionBias() {
        return -2;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
