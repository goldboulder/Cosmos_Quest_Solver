/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Monster;
import cosmosquestsolver.OtherThings;

//+ x armor per unit behind.
// used by 4th05
public class SelfArmor extends SpecialAbility{
    
    private int armor;

    public SelfArmor(Creature owner, int armor) {
        super(owner);
        this.armor = armor;
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new SelfArmor(newOwner,armor);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.addArmorBoost(armor);
    }
    
    
    @Override
    public String getDescription() {
        return "+" + armor + " armor to self";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + armor;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return (int)((owner.getBaseHP()+armor*2.0) * owner.getBaseAtt());
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
