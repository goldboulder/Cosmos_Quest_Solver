/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//After attacking, insta-kills an enemy if they have less than a given percentage
//of health.
//used by season 6 heroes
public class Execute extends Skill{
    
    private double percent;
    private Creature victem;
    private long enemyHPBefore;
    
    public Execute(Creature owner, double percent){
        super(owner);
        this.percent = percent;
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
        
        if (victem.shouldTakePercentExecute(percent)){
            victem.takeExecute(owner,enemyFormation,thisFormation,enemyHPBefore);
        }
        
    }
    

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Execute(newOwner,percent);
    }

    @Override
    public String getDescription() {
        String percentStr = Integer.toString((int)(percent * 100));
        return "After attacking, insta-kills if enemy is under " + percentStr + "% health";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+(2.5*percent)));
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
