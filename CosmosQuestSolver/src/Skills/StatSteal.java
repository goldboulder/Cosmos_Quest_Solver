/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import Formations.WorldBoss;
import GUI.RunePanel;
import cosmosquestsolver.OtherThings;

//steals a percentage of a unit's stats. Which unit depends on the hero's starting position.
//if there are less units on enemy line and hero is last, there will be no effect.
//Used by Season 11 heroes.
public class StatSteal extends Skill {
    
    protected double percent;
    
    public StatSteal(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    
    @Override
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation){
        
        int position = thisFormation.positionOfCreature(owner);
        if (position < enemyFormation.size()){
            Creature c = enemyFormation.getCreature(position);
            
            if (c instanceof WorldBoss) return;
            int HPToSteal = (int)Math.round(c.getBaseHP() * percent);
            int attToSteal = (int)Math.round(c.getBaseAtt() * percent);
            c.setCurrentHP(c.getCurrentHP() - HPToSteal);
            c.setCurrentAtt(c.getCurrentAtt() - attToSteal);
            owner.setMaxHP(owner.getMaxHP() + HPToSteal);
            owner.setCurrentHP(owner.getMaxHP());
            owner.setCurrentAtt(owner.getCurrentAtt() + attToSteal);
        }
    }
    

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new StatSteal(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "Steals " + OtherThings.nicePercentString(percent) + " stats from enemy in same position";
    }

    @Override
    public int viability() {
        return (int)((owner.getBaseHP() * owner.getBaseAtt()) * 5);
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return 1;
    }

    
    
    
    
}
