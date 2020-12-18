/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//at the beggining of the battle, increases attack and health by x for each other
//horseman in the formation. Used by Halloween 4 heroes
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
        int numOther = numOtherHorsemen(thisFormation);
        if (numOther > 0){
            owner.setMaxHP(owner.getMaxHP() + boost*numOther);
            owner.setCurrentHP(owner.getCurrentHP() + boost*numOther);
            owner.setCurrentAtt(owner.getCurrentAtt() + boost*numOther);
        }
    }
    
    private int numOtherHorsemen(Formation formation){
        int amount = 0;
        String name = owner.getName();
        for (Creature c : formation){
            if (c.getName().equals("Stench") && !name.equals("Stench")) amount ++;
            if (c.getName().equals("Rumble") && !name.equals("Rumble")) amount ++;
            if (c.getName().equals("Vermin") && !name.equals("Vermin")) amount ++;
            if (c.getName().equals("Reaper") && !name.equals("Reaper")) amount ++;
        }
        return amount;
    }
    
    @Override
    public String getDescription() {
        
        return "Increases stats by " + boost + " for each other horseman in formation";
        
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
