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
        long enemyFormationDamageTaken = enemyFormation.getDamageTaken();
        Creature victem = enemyFormation.getFrontCreature();
        
        long enemyHPBefore = victem.getCurrentHP();
        super.attack(thisFormation,enemyFormation);
        long enemyHPAfter = victem.getCurrentHP();
        
        if (enemyHPAfter == 0){//set damage taken back to right amount. This skill cannot do overkill damage.
            long damage = enemyHPBefore + 1;
            enemyFormation.setDamageTaken(enemyFormationDamageTaken + damage);
            victem.recordDamageTaken(damage,enemyFormation,thisFormation);//overrides other damage taken
        }
        else{//finish off
            long hpToUse = victem.getBaseHP() > victem.getMaxHP() ? victem.getBaseHP() : victem.getMaxHP();//use base for lep, max for bunnies
            double percentHealth = (double)victem.getCurrentHP()/hpToUse;
            if (percentHealth < percent){//equal to?
                victem.takeHit(owner, enemyFormation, thisFormation, victem.getCurrentHP()+1);
                victem.recordDamageTaken(enemyHPBefore + 1,enemyFormation,thisFormation);
            }
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
