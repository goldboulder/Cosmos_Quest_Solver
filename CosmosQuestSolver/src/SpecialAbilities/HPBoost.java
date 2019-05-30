/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//grants x% more health.
// used by 
public class HPBoost extends SpecialAbility{
    
    private double multiplier;

    public HPBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.setMaxHP((int)(owner.getMaxHP() * (1+multiplier)));
        owner.setCurrentHP(owner.getMaxHP());
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new HPBoost(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return " Grants " + OtherThings.nicePercentString(multiplier) + " more health";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+multiplier));
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
