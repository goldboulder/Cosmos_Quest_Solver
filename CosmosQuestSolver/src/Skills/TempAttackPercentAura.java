/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//gives all allies and self + x% attack for y turns.
//used by season 10 heroes
public class TempAttackPercentAura extends Skill{
    
    private double boost;
    private int turns;
    private int turnsLeft;
    private boolean done = false;
    
    public TempAttackPercentAura(Creature owner, double boost, int turns){
        super(owner);
        this.boost = boost;
        this.turnsLeft = turns;
        this.turns = turns;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new TempAttackPercentAura(newOwner,boost,turns);
    }
    
    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
        if (!done){
            for (Creature creature : thisFormation){
                creature.addAttPercentBoost(-boost);
                //creature.getMainSkill().ricochetNerf(-boost);
            }
            done = true;
        }
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        if (turns != 0){
        for (Creature creature : thisFormation){
                creature.addAttPercentBoost(boost);
                //System.out.println("added boost");
                //creature.getMainSkill().ricochetNerf(boost);
            }
        }
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        turnsLeft --;
        if (turnsLeft == 0 && !done){
            for (Creature creature : thisFormation){
                creature.addAttPercentBoost(-boost);
                //creature.getMainSkill().ricochetNerf(-boost);
            }
            done = true;
        }
    }
    
    @Override
    public String getDescription() {
        return "Freindly creatures have " + OtherThings.nicePercentString(boost) + " more attack for " + turns + " turns";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + boost + " " + turns;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseAtt() * (1.5*boost*Math.min(Formation.MAX_MEMBERS,turns)+1) * owner.getBaseHP());
    }
    
    @Override
    public int positionBias() {
        return -2;
    }
    
    @Override
    public boolean WBNHEasy() {
        return false;
    }
    
}
