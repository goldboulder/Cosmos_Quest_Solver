/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import GUI.RunePanel;
import cosmosquestsolver.OtherThings;

//heals are x times as effective.
// used as a rune skill
public class ExtraHeal extends Skill implements RuneSkill{
    
    private double multiplier;

    public ExtraHeal(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.addHealEffectivness(multiplier - 1);
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ExtraHeal(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Heals are " + OtherThings.intOrNiceDecimalFormat(multiplier) + " times as effective";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * 1.15);
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
        return "Heal+";
    }
    
}
