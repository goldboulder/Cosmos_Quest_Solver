/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//Nullifies damage above a given amount.
//used by Doyenne
public class DamageDodge extends SpecialAbility{
    
    private long damageCap;

    public DamageDodge(Creature owner, long damageCap) {
        super(owner);
        this.damageCap = damageCap;
    }
    
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new DamageDodge(newOwner,damageCap);
    }
    
    @Override
    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {
        if((long)Math.ceil(hit) > damageCap){
            super.takeHit(attacker, thisFormation, enemyFormation, 0);
        }
        else{
            super.takeHit(attacker, thisFormation, enemyFormation, hit);
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
