/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reflects a percentage of the direct damage back at attacker.
//used by season 5 heroes
public class Reflect extends SpecialAbility{
    
    private double multiplier;
    private long damageTakenThisRound;

    public Reflect(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageTakenThisRound = 0;
    }
    

    @Override
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation){
        if (owner == thisFormation.getFrontCreature()){
            damageTakenThisRound = damage;
        }
    }

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            Creature target = enemyFormation.getFrontCreature();
            if (target.isDead()){//if creature died from normal attack, reflect damage is saved for the next enemy
                enemyFormation.handleCreatureDeaths(thisFormation);
                if (enemyFormation.size() > 0){
                    enemyFormation.getFrontCreature().changeHP(-damageTakenThisRound*multiplier,enemyFormation);//new front creature
                }
            }
            else{
                target.changeHP(-damageTakenThisRound*multiplier,enemyFormation);//elemental damage boost and defence not considered.
            }
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Reflect(newOwner,multiplier);
    }
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Returns " + percent + "% of damage recieved";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+1.75*multiplier));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    
    
}
