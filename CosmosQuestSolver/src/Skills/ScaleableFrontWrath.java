/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals AOE damage to front x units upon death. Used by space heroes
public class ScaleableFrontWrath extends Skill{
    
    private int damagePerLevel;
    private int targets;

    public ScaleableFrontWrath(Creature owner, int damagePerLevel, int targets) {
        super(owner);
        this.damagePerLevel = damagePerLevel;
        this.targets = targets;
    }
    

    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableFrontWrath(newOwner,damagePerLevel,targets);
    }
    

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        for (int i = 0; i < targets && i < enemyFormation.size(); i++){
            enemyFormation.getCreature(i).changeHP(-damagePerLevel * owner.getLevel(),enemyFormation);;
        }
        
    }
    
    @Override
    public String getDescription() {
        if (targets == 1){
            return "Deals " + damagePerLevel + " damage per level to front unit on death (" + Integer.toString(damagePerLevel * owner.getLevel()) + ")";
        }
        else{
            return "Deals " + damagePerLevel + " damage per level to front " + targets + " units on death (" + roundedScaleMilestone(owner,damagePerLevel,1) + ")";
        }
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + damagePerLevel + " " + targets;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + roundedScaleMilestone(owner,damagePerLevel,1) * targets;
    }

    @Override
    public int positionBias() {
        return 2;
    }
    
}
