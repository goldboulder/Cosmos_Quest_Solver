/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//at the beggining of the battle, increases attack and health by x if another
//horseman is in the formation. Used by Halloween 4 heroes
public class HorsemenBrotherhood extends Skill{
    
    int boost;
    
    public HorsemenBrotherhood(Creature owner, int boost) {
        super(owner);
        this.boost = boost;
    }
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new HorsemenBrotherhood(newOwner,boost);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {//adjusts stats
        if (hasOtherHorsemen(thisFormation)){
            owner.setMaxHP(owner.getMaxHP() + boost);
            owner.setCurrentHP(owner.getCurrentHP() + boost);
            owner.setCurrentAtt(owner.getCurrentAtt() + boost);
        }
    }
    
    private boolean hasOtherHorsemen(Formation formation){
        String name = owner.getName();
        for (Creature c : formation){
            if (c.getName().equals("Stench") && !name.equals("Stench")) return true;
            if (c.getName().equals("Rumble") && !name.equals("Rumble")) return true;
            if (c.getName().equals("Vermin") && !name.equals("Vermin")) return true;
            if (c.getName().equals("Reaper") && !name.equals("Reaper")) return true;
        }
        return false;
    }
    
    @Override
    public String getDescription() {
        
        return "Increases stats by " + boost + " if with another horseman";
        
    }
    
    @Override
    public int viability() {//returns viability at the owner's effective level
        return (owner.getBaseHP() + boost / 3) * (owner.getBaseAtt() + boost / 3);
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + boost;
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
}
