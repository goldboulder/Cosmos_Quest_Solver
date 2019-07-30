
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import GUI.NodePanel;
import GUI.NodeSelecterPanel;
import cosmosquestsolver.OtherThings;

//grants x% more attack.
// used by 
public class AttackPercentBoost extends Skill implements NodeSkill{
    
    private double multiplier;

    public AttackPercentBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.setCurrentAtt((int)(owner.getCurrentAtt() * (multiplier)));
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new AttackPercentBoost(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Initial attack times " + OtherThings.intOrNiceDecimalFormat(multiplier);
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
    public void addNodeFields(NodePanel panel) {
        panel.addDoubleTextField("multiplier",multiplier);
    }
    
    @Override
    public String getImageName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String getName() {
        return "Att% Up";
    }
    
}
