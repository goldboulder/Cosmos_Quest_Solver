/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import cosmosquestsolver.OtherThings;

//reduces reflect damage recieved by x% per level
//used by GAMES heroes
public class ScaleableAntiReflect extends Skill{
    
    private double amountPerLevel;
    
    public ScaleableAntiReflect(Creature owner, double amountPerLevel) {
        super(owner);
        this.amountPerLevel = amountPerLevel;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAntiReflect(newOwner,amountPerLevel);
    }
    
    @Override
    public void takeReflectDamage(double damage, Formation thisFormation, Formation enemyFormation){
        owner.changeHP(-damage * (1 - roundedScaleMilestoneDouble(owner, amountPerLevel, 1)),thisFormation);
    }
    
    
    @Override
    public String getDescription() {
        return "Takes " + amountPerLevel + "% less damage per level from reflects (" + OtherThings.nicePercentString(Skill.roundedScaleMilestoneDouble(owner, amountPerLevel, 1)) + ")";
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amountPerLevel;
    }
    
    
    
    @Override
    public int viability() {
        
        int level = owner.getLevel() > Hero.MAX_NORMAL_LEVEL ? Hero.MAX_NORMAL_LEVEL : owner.getLevel();
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+(amountPerLevel*level*0.01)));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
}
