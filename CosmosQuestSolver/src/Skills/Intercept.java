/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//takes a percentage of the front creature's damage.
//used by Neil
public class Intercept extends Skill{
    
    private double interceptPercent;

    public Intercept(Creature owner, double interceptPercent) {
        super(owner);
        this.interceptPercent = interceptPercent;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Intercept(newOwner,interceptPercent);
    }
    
    @Override
    public double alterIncomingDamage(double hit, double initialHit, Formation thisFormation, Formation enemyFormation) {
        if (owner != thisFormation.getFrontCreature() && !owner.isDead()){
            double damageIntercepted = hit * interceptPercent;
            owner.takeHit(owner, thisFormation, enemyFormation, Math.round(damageIntercepted));
            return Math.round(hit - damageIntercepted);
        }
        else{
            return hit;
        }
    }
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(interceptPercent * 100));
        return "Absorbs " + percent + "% direct damage from first unit";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + interceptPercent;
    }
    
    @Override
    public int viability() {
        return (int)(interceptPercent * owner.getBaseHP() * owner.getBaseHP() * 2);
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
