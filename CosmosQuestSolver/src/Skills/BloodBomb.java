/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//deals AOE damage when the owner kills an enemy
//used by Defile
public class BloodBomb extends Skill{
    
    protected int damage;
    
    public BloodBomb(Creature owner, int damage){
        super(owner);
        this.damage = damage;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        //super.attack(thisFormation,enemyFormation);
        if (enemyFormation.getFrontCreature().isDead() && thisFormation.getFrontCreature() == owner){
            //enemyFormation.takeAOEDamage(damage);//bubbles does not work with this skill
            enemyFormation.takeRawDamage(damage);
        }
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new BloodBomb(newOwner,damage);
    }

    @Override
    public String getDescription() {
        return "After killing, deals " + damage + " aoe";
    }
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage;
    }

    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (damage * damage * Formation.MAX_MEMBERS);
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
    
    
    
    
}
