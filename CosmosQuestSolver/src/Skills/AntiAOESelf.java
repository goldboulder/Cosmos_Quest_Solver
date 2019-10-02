/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import GUI.RunePanel;
import cosmosquestsolver.OtherThings;

//reduces the effect of AOE attacks for the user only.
//Used by Thumper and as a rune skill
public class AntiAOESelf extends Skill implements RuneSkill{
    
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
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new AntiAOESelf(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "Takes " + OtherThings.nicePercentString(percent) + " less damage from AOE skills";
    }

    @Override
    public int viability() {
        double limitedPercent = (1 - percent/2.5);
        if (limitedPercent <= 0.5){
            limitedPercent = 0.5;
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

    @Override
    public void addRuneFields(RunePanel panel) {
        panel.addDoubleTextField("percent",percent);
    }

    @Override
    public String getImageName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String getName() {
        return "Antimagic";
    }
    
    
    
}
