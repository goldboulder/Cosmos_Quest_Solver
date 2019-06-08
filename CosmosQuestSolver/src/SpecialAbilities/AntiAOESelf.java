/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//reduces the effect of AOE attacks for the user only.
//Used by Thumper
public class AntiAOESelf extends SpecialAbility{
    
    protected double percent;
    
    public AntiAOESelf(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    @Override
    public double alterAOEDamage(double damage, Formation formation){
        return super.alterAOEDamage(damage, formation) * (1 - percent);
    }
    
    @Override
    public boolean shouldTakePercentExecute(double percent) {
        return super.shouldTakePercentExecute(percent*(1-this.percent));
    }
    
    @Override
    public boolean shouldTakeConstantExecute(int hp) {
        return super.shouldTakeConstantExecute((int)(percent*hp));
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new AntiAOESelf(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "Takes " + OtherThings.nicePercentString(percent) + " less damage from AOE skills";
    }

    @Override
    public int viability() {
        double limitedPercent = (1 - percent);
        if (limitedPercent <= 0.3){
            limitedPercent = 0.3;
        }
        return (int)((owner.getBaseHP() * owner.getBaseAtt()) / limitedPercent);
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return -1;
    }
    
    
    
}
