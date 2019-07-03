/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//deals a set amount of damage to all creatures in the enemy formation after
//every turn while the owner is alive. Damage is done before healing. Used by
//Alpha, TR0N1X, and Lord of Chaos
public class AOE extends SpecialAbility{
    
    protected int damage;
    
    public AOE(Creature owner, int damage){
        super(owner);
        this.damage = damage;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeAOEDamage(damage);
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new AOE(newOwner,damage);
    }

    @Override
    public String getDescription() {
        return "After every turn, deals " + damage + " aoe damage";
    }

    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * damage * Formation.MAX_MEMBERS);
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage;
    }

    @Override
    public int positionBias() {
        return -3;
    }

    
    
    @Override
    public boolean WBTryLessCreatures(){
        return true;
    }
    
    
    
}
