/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reflects a a set amount of damage back at all enemies when attacked.
//used by Mr. Cotton
public class ThornsAll extends SpecialAbility{
    
    private long damage;

    public ThornsAll(Creature owner, long damage) {
        super(owner);
        this.damage = damage;
    }
    
    

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            enemyFormation.takeAOEDamage(damage);
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ThornsAll(newOwner,damage);
    }
    
    
    
    @Override
    public String getDescription() {
        return "Returns " + damage + " damage to all enemies when attacked";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * (owner.getBaseAtt()) + (1.5*damage*Formation.MAX_MEMBERS/2));
    }

    @Override
    public int positionBias() {
        return 3;
    }
    
    @Override
    public boolean WBTryLessCreatures(){
        return true;
    }
    
}
