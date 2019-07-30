/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//regenerates a given amount of damage taken.
//Used by Sharkjellyn.
public class Recover extends Skill{
    
    private int amount;
    
    public Recover(Creature owner, int amount){
        super(owner);
        this.amount = amount;
    }
    
    
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//anti-aoe?
        if (thisFormation.getFrontCreature() == owner){
            owner.takeHeal(Math.round(amount), thisFormation);
        }
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Recover(newOwner,amount);
    }

    @Override
    public String getDescription() {
        return "Heals " + amount + " after being attacked";
    }

    @Override
    public int viability() {
        double limitedPercent = (1 - amount);
        if (limitedPercent <= 0.3){
            limitedPercent = 0.3;
        }
        return (int)((owner.getBaseHP() + amount) * owner.getBaseAtt());
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount;
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
    
    
}
