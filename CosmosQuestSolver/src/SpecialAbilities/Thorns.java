/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reflects a a set amount of damage back at attacker.
//used by Murphy
public class Thorns extends SpecialAbility{
    
    private long damage;

    public Thorns(Creature owner, long damage) {
        super(owner);
        this.damage = damage;
    }
    
    

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            Creature target = enemyFormation.getFrontCreature();
            if (target.isDead()){//if creature died from normal attack, reflect damage is saved for the next enemy
                //target.postRoundAction(enemyFormation, thisFormation);
                //target.postRoundAction2(enemyFormation, thisFormation);
                //enemyFormation.handleCreatureDeaths(thisFormation);
                target = findNextTarget(enemyFormation);
                if (enemyFormation.size() > 0 && target != null){
                    target.changeHP(-damage,enemyFormation);//new front creature
                }
                
            }
            else{
                target.changeHP(-damage,enemyFormation);//elemental damage boost and defence not considered.
            }
        }
    }
    
    private Creature findNextTarget(Formation enemyFormation){
        for (Creature c : enemyFormation){
            if (!c.isDead()){
                return c;
            }
        }
        return null;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Thorns(newOwner,damage);
    }
    
    
    
    @Override
    public String getDescription() {
        return "Returns " + damage + " damage when attacked";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * (owner.getBaseAtt()) + (1.5*damage));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    
    
}
