/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import Formations.WorldBoss;
import GUI.RunePanel;
import cosmosquestsolver.OtherThings;

//lowers enemy team's attack by x%
//used by Antoinette
public class AttackDrain extends Skill {
    
    protected double percent;
    
    public AttackDrain(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    
    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation){
        if (enemyFormation.getFrontCreature() instanceof WorldBoss) return;
        
        for (Creature c : enemyFormation.getMembers()){
            c.setCurrentAtt(c.getCurrentAtt() - (int)(c.getBaseAtt() * percent));
        }
    }
    

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new AttackDrain(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "Lowers all enemy's attack by " + OtherThings.nicePercentString(percent) + " at start";
    }

    @Override
    public int viability() {
        return (int)((owner.getBaseHP() * owner.getBaseAtt()) / Math.pow(1-percent,2.5));
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return 0;
    }

    
    
    
    
}
