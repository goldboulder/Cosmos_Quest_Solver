/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;
import cosmosquestsolver.OtherThings;

//gives all allies and self + x% attack.(before or after attConstantBoost?)
//used by season 8 heroes
public class AttackPercentAura extends SpecialAbility{
    
    protected double boost;

    public AttackPercentAura(Creature owner, double boost) {//if elsment is null, apply to all creatures
        super(owner);
        this.boost = boost;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new AttackPercentAura(newOwner,boost);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            //if (element == null || creature.getElement() == element){
                creature.addAttPercentBoost(boost);
                
            //}
        }
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        for (Creature creature : thisFormation){
            //if (element == null || creature.getElement() == element){
                creature.addAttPercentBoost(-boost);
                
            //}
        }
    }
    
    @Override
    public String getDescription() {
        /*
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
        */
        return "Freindly creatures have " + OtherThings.nicePercentString(boost) + " more attack";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + boost;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseAtt() * (boost*6+1) * owner.getBaseHP());
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
