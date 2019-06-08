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
        return "Freindly creatures have " + OtherThings.nicePercentString(boost) + " more attack";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + boost;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseAtt() * (boost*Formation.MAX_MEMBERS+1) * owner.getBaseHP());
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
