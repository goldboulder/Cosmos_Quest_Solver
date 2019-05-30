/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//attack boosts are x times as effective.
// used by 
public class ExtraArmorBoost extends SpecialAbility{
    
    private double multiplier;

    public ExtraArmorBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void addArmorBoost(int a) {
        owner.addRawArmorBoost((int)(multiplier * a));
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ExtraArmorBoost(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Armor is " + OtherThings.intOrNiceDecimalFormat(multiplier) + " times as effective";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return owner.getBaseHP() * (owner.getBaseAtt() + 6);
    }

    @Override
    public int positionBias() {
        return 2;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
