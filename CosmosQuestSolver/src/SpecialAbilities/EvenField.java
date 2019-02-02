/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//at the beginning of the fight, cuts a percentage of each enemy's hp by a certain
//percent. This percent is determined by how many creatures each formation has.
//if the hero is alone against a formation of 6, the ability reduces each
//enemy's hp to 1/6th its original value. has no effect if the user's formation
//has more creatures than the enemy's formation. Used by Leprechaun
public class EvenField extends SpecialAbility{//enemies cannot heal back to full health*
    
    private int numExtra; //adds invisible units to the enemy formation for the attack

    public EvenField(Creature owner, int numExtra) {
        super(owner);
        this.numExtra = numExtra;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new EvenField(newOwner,numExtra);
    }
    
    @Override
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {
        double percentDamage = 1-((double) thisFormation.size() / (enemyFormation.size()+numExtra));
        if (percentDamage <= 0){
            return;
        }
        for (Creature creature : enemyFormation){
            double damageDelt = creature.getBaseHP() * percentDamage  * (1 - enemyFormation.getAOEResistance());
            creature.changeHP(-damageDelt,enemyFormation);//rounding?
            //creature.setMaxHP(creature.getCurrentHP());//units cannot heal past the new HP cap-- this doesn't appear to be true anymore
        }
    }

    
    
    @Override
    public String getDescription() {
        return "At start, cuts enemy hp by a percent if outnumbered";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
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
