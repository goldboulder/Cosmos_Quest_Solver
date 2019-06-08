/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals a given damage of the owner's attack to the opponent's front unit
//per turn. Damage is done before healing.
//Used by R31 and 4sc3nd3d R31.
public class RangedAttack extends SpecialAbility{
    
    protected int damage;
    
    public RangedAttack(Creature owner, int damage){
        super(owner);
        this.damage = damage;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//anti-aoe?
        //enemyFormation.getCreature(enemyFormation.size()-1).changeHP(-Math.round(owner.getCurrentAtt() * damage * (1-enemyFormation.getAOEResistance())), enemyFormation);
        enemyFormation.getFrontCreature().takeAOEDamage(damage, enemyFormation);
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new RangedAttack(newOwner,damage);
    }

    @Override
    public String getDescription() {
        return "Deals " + damage + " damage to front unit every turn";
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * (owner.getBaseAtt() + damage * Formation.MAX_MEMBERS * 0.66));
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damage;
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
}
