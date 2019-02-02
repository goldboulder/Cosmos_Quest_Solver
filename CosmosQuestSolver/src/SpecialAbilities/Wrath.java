/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals damage to enemy on death.
//Used by Billy
public class Wrath extends SpecialAbility{
    
    private int damage;
    private boolean active = false;//needed?

    public Wrath(Creature owner, int damage) {
        super(owner);
        this.damage = damage;
    }
    

    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Wrath(newOwner,damage);
    }
    
    
    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        Creature target = findTarget(enemyFormation);
        if (target != null){
            target.changeHP(-damage,enemyFormation);
        }
    }
    
    
    private Creature findTarget(Formation enemyFormation){
        for (Creature c : enemyFormation){
            if (!c.isDead()){
                return c;
            }
        }
        return null;
    }
    
    @Override
    public String getDescription() {
        return "Deals " + damage + " damage after dying"; //amount?
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage;
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt()+damage/2);
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
