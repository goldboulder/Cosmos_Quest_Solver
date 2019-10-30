/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//deals a set amount of damage to all creatures in the enemy formation after
//every turn while the owner is alive, increasing every turn. 
//Damage is done before healing. Used by Adrian
public class IncreasingAOE extends Skill{
    
    protected int damage;
    protected int damageIncrease;
    protected int turn = 0;
    
    public IncreasingAOE(Creature owner, int damage, int damageIncrease){
        super(owner);
        this.damage = damage;
        this.damageIncrease = damageIncrease;
    }
    
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        turn ++;
        enemyFormation.takeAOEDamage(damage + damageIncrease*turn);
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new IncreasingAOE(newOwner,damage,damageIncrease);
    }

    @Override
    public String getDescription() {
        return "After every turn, deals " + damage + " aoe damage, increasing by " + damageIncrease + " per turn";
    }

    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * (damage + damageIncrease*5) * Formation.MAX_MEMBERS);
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage + " " + damageIncrease;
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
