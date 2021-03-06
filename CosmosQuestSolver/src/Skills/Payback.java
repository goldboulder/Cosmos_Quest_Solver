/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//stores a % of damage recieved and releases it to front enemy on death
//used by Easter 3 heroes
public class Payback extends Skill{
    
    private double multiplier;
    private long damageTaken;

    public Payback(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    

    @Override
    public void recordAnyDamageTaken(long damage, Formation thisFormation, Formation enemyFormation){
        damageTaken += damage;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Payback(newOwner,multiplier);
    }
    
    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {//does reflect damage count too? if it doesn't die?***
        Creature target = Formation.findFirstTarget(enemyFormation);
        if (target != null){
            target.changeHP(-damageTaken*multiplier,enemyFormation);
            //if (target.getMainSkill() instanceof Payback){
                //target.getMainSkill().recordDirectDamageTaken((long)(Math.ceil(damageTaken*multiplier)),thisFormation,enemyFormation);
            //}
        }
    }
    
    @Override
    public String getDescription() {
        return "Stores " + OtherThings.nicePercentString(multiplier) + " of damage recieved and releases it to front enemy on death";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+6*multiplier));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    
    
}
