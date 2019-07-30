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
public class ArmorAura extends Skill{
    
    protected int armorBoost;
    protected Element element;

    public ArmorAura(Creature owner, int armorBoost, Element element) {//if elsment is null, apply to all creatures
        super(owner);
        this.armorBoost = armorBoost;
        this.element = element;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ArmorAura(newOwner,armorBoost,element);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
                creature.addArmor(armorBoost);
            }
        }
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            if (element == null || creature.getElement() == element){
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
        sb.append(" creatures have +").append(armorBoost).append(" armor");
        
        return sb.toString();
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + armorBoost + " " + Elements.getString(element);
    }
    
    @Override
    public int viability() {
        return owner.getBaseAtt() * (owner.getBaseHP() + (Formation.MAX_MEMBERS/3) *armorBoost);
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
