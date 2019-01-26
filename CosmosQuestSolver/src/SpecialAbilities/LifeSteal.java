/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//combines AOE and heal. not used, but the scaleable version is
public class LifeSteal extends SpecialAbility{
    
    protected int amount;
    protected boolean deadOnStart;
    
    public LifeSteal(Creature owner, int amount){
        super(owner);
        this.amount = amount;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {
        deadOnStart = owner.isDead();
    }

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart){
            enemyFormation.takeAOEDamage(amount);
        }
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new LifeSteal(newOwner,amount);
    }
    

    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart){
            thisFormation.AOEHeal(amount, enemyFormation);
        }
    }
    
    @Override
    public String getDescription() {
        return "";//todo
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * amount * Formation.MAX_MEMBERS * 2);
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
    
    
}
