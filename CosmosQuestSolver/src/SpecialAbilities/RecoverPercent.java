/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//regenerates a given percent of damage taken.
//Used by Willow.
public class RecoverPercent extends SpecialAbility{
    
    private double percent;
    private long damageTakenThisRound;
    
    public RecoverPercent(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        damageTakenThisRound = 0;
    }
    
    @Override
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation){
        if (owner == thisFormation.getFrontCreature()){
            damageTakenThisRound += damage;
        }
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//anti-aoe?
        owner.takeHeal(Math.round(damageTakenThisRound * percent), thisFormation);
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new RecoverPercent(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "Heals " + OtherThings.nicePercentString(percent) + " of damage taken";
    }

    @Override
    public int viability() {
        double limitedPercent = (1 - percent);
        if (limitedPercent <= 0.3){
            limitedPercent = 0.3;
        }
        return (int)((owner.getBaseHP() * owner.getBaseAtt()) / limitedPercent);
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    
    
}
