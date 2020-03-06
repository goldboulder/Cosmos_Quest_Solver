/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;

//multiplies the damage done to a creature by a specified amount if the creature
//and the owner have the same element. Damage is multiplied before attack boosts
//are added to it. Used by Pontus, Atzar, Rigr, Dagda, and their ascended counterparts
public class Purity extends Skill{
    
    private double multiplier;

    public Purity(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public double moreDamage(Formation thisFormation, Formation enemyFormation) {
        if (enemyFormation.getFrontCreature().getElement() == owner.getElement()){
            return multiplier;
            //return (owner.attWithBoosts()) * (multiplier - 1);
        }
        else{
            return 1;
        }
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Purity(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        String numStr = "";
        if (multiplier == 2){
            numStr = "Double";
        }
        else if (multiplier == 3){
            numStr = "Triple";
        }
        else{
            numStr = "x " + multiplier;
        }
        return numStr + " attack against same element";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return owner.getBaseHP() * owner.getBaseAtt() * (int)(1 + (multiplier-1)/Elements.numElements());
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
