/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import GUI.RunePanel;
import cosmosquestsolver.OtherThings;

//grants x% more health.
// used as a rune skill
public class HPBoost extends Skill implements RuneSkill{
    
    private double multiplier;

    public HPBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.setMaxHP((int)(owner.getMaxHP() * multiplier));
        owner.setCurrentHP(owner.getMaxHP());
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new HPBoost(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Initial HP times " + OtherThings.twoDecimalFormat(multiplier);
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
    
    @Override
    public void addRuneFields(RunePanel panel) {
        panel.addDoubleTextField("multiplier",multiplier);
    }
    
    @Override
    public String getImageName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String getName() {
        return "Tank";
    }
    
}
