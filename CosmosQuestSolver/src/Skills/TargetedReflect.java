/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reflects a percentage of the direct damage back at creature with most HP.
//used by Guy
public class TargetedReflect extends Skill{
    
    private double multiplier;
    private long damageTakenThisRound;

    public TargetedReflect(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageTakenThisRound = 0;
    }
    

    @Override
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation){
        if (owner == thisFormation.getFrontCreature()){
            damageTakenThisRound += damage;
        }
        
    }

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){//can only reflect direct damage while in front
            enemyFormation.getCreature(mostHPPosition(enemyFormation)).changeHP(-damageTakenThisRound*multiplier,enemyFormation);
        }
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new TargetedReflect(newOwner,multiplier);
    }
    
    private int mostHPPosition(Formation enemyFormation){//empty line?
        int mostHPIndex = 0;
        long greatestHP = 0;
        
        for (int i = 0; i < enemyFormation.size(); i++){
            Creature c = enemyFormation.getCreature(i);
            long cHP = c.getCurrentHP();//currentHP,two units have the same HP- select first in line
            
            if (cHP > greatestHP){
                greatestHP = cHP;
                mostHPIndex = i;
            }
        }
        return mostHPIndex;
    }
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Returns " + percent + "% of damage recieved to enemy with most HP";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+20*multiplier)) ;
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
    
    
}
