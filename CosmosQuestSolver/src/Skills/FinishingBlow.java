/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//After attacking, insta-kills an enemy if they have less than a given percentage
//of health.
//used by 4tz4r
public class FinishingBlow extends Skill{
    
    private int threshold;
    private Creature victem;
    private long enemyHPBefore;
    
    public FinishingBlow(Creature owner, int threshold){
        super(owner);
        this.threshold = threshold;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        victem = enemyFormation.getFrontCreature();
        enemyHPBefore = victem.getCurrentHP();
    }
    
    @Override
    public void postAttackAction(Formation thisFormation, Formation enemyFormation){
        //long enemyFormationDamageTaken = enemyFormation.getDamageTaken();
        
        //super.attack(thisFormation,enemyFormation);
        
        if (victem.shouldTakeConstantExecute(threshold)){
            victem.takeExecute(owner,enemyFormation,thisFormation,enemyHPBefore);
        }
        
    }
    

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new FinishingBlow(newOwner,threshold);
    }

    @Override
    public String getDescription() {
        return "After attacking, insta-kills if enemy is under " + threshold + " health";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + threshold;
    }

    @Override
    public int viability() {
        return owner.getBaseHP() * (owner.getBaseAtt() + threshold/2);
    }

    @Override
    public int positionBias() {
        return -1;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
    
    
}
