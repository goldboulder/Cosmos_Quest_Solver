/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals a given percent of the owner's attack to the opponent's back unit
//per turn. Damage is done before healing.
//Used by B-Day.
public class Simmer extends Skill{
    
    protected double percent;
    protected double damageThisTurn = 0;//remember value if fairies change it
    
    public Simmer(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {
        damageThisTurn = Math.round(owner.getCurrentAtt() * percent);
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//anti-aoe?
        //enemyFormation.getCreature(enemyFormation.size()-1).changeHP(-Math.round(owner.getCurrentAtt() * percent * (1-enemyFormation.getAOEResistance())), enemyFormation);
        enemyFormation.getCreature(enemyFormation.size()-1).takeAOEDamage(damageThisTurn, enemyFormation);
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Simmer(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "Deals " + OtherThings.nicePercentString(percent) + " damage to back unit every turn (" + (int)Math.round(owner.getCurrentAtt()*percent) + ")";
    }

    @Override
    public int viability() {
        return (int)((owner.getBaseHP() * owner.getBaseAtt()) * (1+percent*Formation.MAX_MEMBERS*(owner.getCurrentAtt()/owner.getCurrentHP())));
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
    @Override
    public boolean WBTryLessCreatures(){
        return true;
    }
    
    
    
}
