/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//heals front creature by a set amount every turn.
//heals are done after any AOE damage and will not revive a dead creature.
//used by Mother
public class HealFirst extends SpecialAbility{
    
    protected int amount;

    public HealFirst(Creature owner, int amount) {
        super(owner);
        this.amount = amount;
    }
    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new HealFirst(newOwner,amount);
    }
    

    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        thisFormation.getFrontCreature().takeHeal(amount * (1 - enemyFormation.getAOEResistance()), enemyFormation);
    }
    
    @Override
    public String getDescription() {
        return "Heals front creature " + amount + " HP after every turn";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * amount * 2);
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
