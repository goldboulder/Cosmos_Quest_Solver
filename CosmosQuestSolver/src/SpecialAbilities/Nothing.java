/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//null object pattern.
//used by monsters
public class Nothing extends SpecialAbility{

    public Nothing(Creature owner) {
        super(owner);
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Nothing(newOwner);
    }
    
    
    @Override
    public String getDescription() {
        return "";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt();
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
