/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals AOE damage to enemy formation equal to a specified percentage of the
//user's base attack upon death. Used by season 2 heroes and Ruin
public class WrathPercentAtt extends Skill{
    
    private double multiplier;

    public WrathPercentAtt(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    

    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new WrathPercentAtt(newOwner,multiplier);
    }
    

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        //if (thisFormation.getFrontCreature() == owner){
            //enemyFormation.takeAOEDamage(Math.round(owner.getBaseAtt() * multiplier));
        //}
        enemyFormation.takeRawDamage(Math.round(owner.getBaseAtt() * multiplier));
        //for (Creature c : enemyFormation){
            //c.changeHP(-Math.round(owner.getBaseAtt() * multiplier), enemyFormation);//right now, bubbles doesn't seem to work on this ability in-game
        //}
    }
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Deals " + percent + "% attack as aoe after dying (" + Integer.toString((int)(Math.round(owner.getBaseAtt() * multiplier))) + ")"; //amount?
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() * owner.getBaseAtt()) + (int)(owner.getBaseAtt() * multiplier * Formation.MAX_MEMBERS);
    }

    @Override
    public int positionBias() {
        return 3;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
