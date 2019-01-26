/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//heals a percentage of damage dealt.
//used by Sanqueen
public class Vampyrism extends SpecialAbility{ //rounding?
    
    private double percent;
    private long damageDealtThisRound;
    protected boolean deadOnStart;

    public Vampyrism(Creature owner, double multiplier) {
        super(owner);
        this.percent = multiplier;
    }
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Vampyrism(newOwner,percent);
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageDealtThisRound = 0;
        deadOnStart = owner.isDead();
    }
    
    @Override
    public void recordDamageDealt(long damage, Formation thisFormation, Formation enemyFormation){
        damageDealtThisRound = damage;
    }

    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        if (!deadOnStart){
            owner.changeHP(damageDealtThisRound * percent, enemyFormation);
        }
    }
    
    
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(this.percent * 100));
        return "Heals for " + percent + "% of damage dealt";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+percent));
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
