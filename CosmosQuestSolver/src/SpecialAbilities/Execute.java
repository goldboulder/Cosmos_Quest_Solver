/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;

//After attacking, insta-kills an enemy if they have less than a given percentage
//of health.
//used by season 6 heroes
public class Execute extends SpecialAbility{
    
    private double percent;
    
    public Execute(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation){
        //long enemyFormationDamageTaken = enemyFormation.getDamageTaken();
        Creature victem = enemyFormation.getFrontCreature();
        
        long enemyHPBefore = victem.getCurrentHP();
        super.attack(thisFormation,enemyFormation);
        
        if (victem.shouldTakeExecute(percent)){
            victem.takeExecute(owner,enemyFormation,thisFormation,enemyHPBefore);
        }
        
    }
    

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
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
