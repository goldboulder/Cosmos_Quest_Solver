/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//multiplies the owner's base attack power by a specified amount every time
//the user attacks. Used by Aural, Geum,Ascended Geum, and subatomic heroes
public class Berserk extends Skill{
    
    private double multiplier;
    private int turns;
    private int countdown;
    private boolean frontOnly;

    public Berserk(Creature owner, double multiplier, int turns, boolean frontOnly) {
        super(owner);
        this.multiplier = multiplier;
        this.turns = turns;
        countdown = turns;
        this.frontOnly = frontOnly;
    }
    

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (!frontOnly || thisFormation.getFrontCreature() == owner){
            countdown --;
            if (countdown == 0){
                owner.setCurrentAtt((long)Math.round(owner.getCurrentAtt()*multiplier));
                countdown = turns;
                //right here, it seems damage boost from fairies gets added every turn too.
            }
        }
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Berserk(newOwner,multiplier,turns,frontOnly);
    }
    

    
    @Override
    public String getDescription() {
        String multiplierString = "";
        if (multiplier % 1 == 0){
            multiplierString = Integer.toString((int)multiplier);
        }
        else{
            multiplierString = Double.toString(multiplier);
        }
        String turnString = "";
        if (turns == 1 && frontOnly){
            turnString = "attack";
        }
        else if (turns == 1 && !frontOnly){
            turnString = "turn";
        }
        else if (turns != 1 && !frontOnly){
            turnString = Integer.toString(turns) + " turns";
        }
        else{
            turnString = Integer.toString(turns) + " attacks";
        }
        return "Attack multiplies by " + multiplierString + " every " + turnString;
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        //int highest = owner.getBaseHP() > owner.getBaseAtt() ? owner.getBaseHP() : owner.getBaseAtt();
        double frontMultiplier = frontOnly ? 1 : 1.2;
        return (int)(frontMultiplier * owner.getBaseHP() * owner.getBaseAtt() * Math.pow((multiplier-1)/turns + 1,1.3));
        
    }

    @Override
    public int positionBias() {
        return frontOnly ? 1 : -3;
    }
    
    
    
}
