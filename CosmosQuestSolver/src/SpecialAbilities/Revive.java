/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//revives a creature to x% health when killed. only done once
// used by 
public class Revive extends SpecialAbility{
    
    private boolean activated;
    private double percent;

    public Revive(Creature owner, double multiplier) {
        super(owner);
        this.percent = multiplier;
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Revive(newOwner,percent);
    }
    
    @Override
    public void takeHit(Creature attacker, Formation thisFormation, Formation enemyFormation, double hit) {//melf bugs it. have revive be a field in creature instead, handle in formation.remove dead
            super.takeHit(attacker, thisFormation, enemyFormation, hit);
            if (owner.isDead() && !activated){
                owner.setCurrentHP((int)(owner.getMaxHP() * percent));
                activated = true;
            }
    }

    
    @Override
    public String getDescription() {
        return "Comes back to life at " + OtherThings.nicePercentString(percent) + " health when killed the first time";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }
    
    @Override
    public int viability() {
        return (int)((owner.getBaseHP() * (percent + 2)) * owner.getBaseAtt());
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
