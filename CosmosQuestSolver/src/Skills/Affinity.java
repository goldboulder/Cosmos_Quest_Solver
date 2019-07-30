/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import GUI.NodePanel;
import cosmosquestsolver.OtherThings;

//reduces damage against the same element
// used by 
public class Affinity extends Skill implements NodeSkill{
    
    private double multiplier;

    public Affinity(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Affinity(newOwner,multiplier);
    }
    
    @Override//rico???
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){
        if (attacker.getElement() == owner.getElement()){
            return (damage + owner.getArmor()) * (1-multiplier) - owner.getArmor();
        }
        else{
            return damage;
        }
    }
    
    @Override
    public String getDescription() {
        return "Takes " + OtherThings.nicePercentString(multiplier) + " less damage from the same element";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return (int)((owner.getBaseHP()/((1-multiplier) + 0.1)) * owner.getBaseAtt());
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }

    @Override
    public void addNodeFields(NodePanel panel) {
        panel.addDoubleTextField("multiplier",multiplier);
    }

    @Override
    public String getImageName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String getName() {
        return "Affinity";
    }
    
}
