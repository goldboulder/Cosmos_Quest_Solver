/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//Nullifies damage above a given amount.
//used by Doyenne
public class DamageDodge extends Skill{
    
    private long damageCap;

    public DamageDodge(Creature owner, long damageCap) {
        super(owner);
        this.damageCap = damageCap;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new DamageDodge(newOwner,damageCap);
    }
    
    
    @Override
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){
        if((long)Math.ceil(damage) > damageCap){
            return 0;
        }
        else{
            return damage;
        }
    }
    
    
    
    
    @Override
    public String getDescription() {
        return "Ignores attacks over " + damageCap + " damage";
    }
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damageCap;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * (owner.getBaseHP()/damageCap) * owner.getBaseAtt());
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
