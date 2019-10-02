/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import GUI.RunePanel;
import cosmosquestsolver.OtherThings;

//attack boosts are x times as effective.
// used as a rune skill
public class ExtraArmorBoost extends Skill implements RuneSkill{
    
    private double multiplier;

    public ExtraArmorBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.addArmorConstantEffectivness(multiplier - 1);
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
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
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1 + multiplier*1.1));
    }

    @Override
    public int positionBias() {
        return 2;
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
        return "Armor+";
    }
}
