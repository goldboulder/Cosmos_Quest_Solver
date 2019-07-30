/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;

//Increases the attack of all creatures
//of a specified element (everyone if element is null) (including the owner)
//while creature is alive
public class AttackBoostAura extends Skill{
    
    protected int attBoost;
    protected Element element;

    public AttackBoostAura(Creature owner, int attBoost, Element element) {//if elsment is null, apply to all creatures
        super(owner);
        this.attBoost = attBoost;
        this.element = element;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new AttackBoostAura(newOwner,attBoost,element);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addAttConstantBoost(attBoost);
            }
        }
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addAttConstantBoost(-attBoost);
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
        sb.append(" creatures have +").append(attBoost).append(" attack");
        
        return sb.toString();
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attBoost + " " + Elements.getString(element);
    }
    
    @Override
    public int viability() {
        return (owner.getBaseAtt() + (Formation.MAX_MEMBERS/3) * attBoost) * owner.getBaseHP();
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
