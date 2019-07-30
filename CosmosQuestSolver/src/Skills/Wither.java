/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//halves the hp of the owner every attack. Used by GaiaByte
public class Wither extends Skill{

    private double amount;
    
    public Wither(Creature owner, double amount) {
        super(owner);
        this.amount = amount;
    }

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (thisFormation.getFrontCreature() == owner){
            int newHP = (int)(owner.getCurrentHP()/amount);
            owner.changeHP(-owner.getCurrentHP()+newHP,thisFormation);//does this count as damage dealt for other team?***
        }
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Wither(newOwner,amount);
    }
    
    @Override
    public String getDescription() {
        if (amount == 2){
            return "Halves hp after every attack";
        }
        return "Divides hp by " + amount + " after every attack";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + amount;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1.5/amount));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    
    
}
