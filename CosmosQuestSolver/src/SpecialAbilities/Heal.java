/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//heals all creatures in owner's formation by a set amount every turn.
//heals are done after any AOE damage and will not revive a dead creature.
//used by Auri and Aeris
public class Heal extends SpecialAbility{
    
    protected int amount;
    protected boolean deadOnStart;

    public Heal(Creature owner, int amount) {
        super(owner);
        this.amount = amount;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {
        deadOnStart = owner.isDead();
        //System.out.println(owner.getName() + " has " + owner.getCurrentHP());
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Heal(newOwner,amount);
    }
    

    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart){
            thisFormation.AOEHeal(amount, enemyFormation);
        }
    }
    
    @Override
    public String getDescription() {
        return "Heals formation " + amount + " HP after every turn";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * amount * Formation.MAX_MEMBERS/2);
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
