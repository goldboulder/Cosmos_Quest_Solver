/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;
import Formations.Hero;

//takes x% less damage from attacks.
//used by Mecha Mary and Annie
public class ScaleableOneTimeAbsorb extends Skill{
    
    private double percentLessDamage;
    private boolean activated = false;
    private double milestone;
    
    public ScaleableOneTimeAbsorb(Creature owner, double percent, double milestone) {
        super(owner);
        this.percentLessDamage = percent;
        this.milestone = milestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableOneTimeAbsorb(newOwner,percentLessDamage,milestone);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.addArmorPercentBoost(1-roundedScaleMilestoneDouble(owner,percentLessDamage,milestone));
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){
        if (!activated){
            owner.addArmorPercentBoost(1/(1-roundedScaleMilestoneDouble(owner,percentLessDamage,1)));
            activated = true;
        }
    }
    
    @Override
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){
        return Math.round(damage);
    }
    
    @Override
    public String getDescription() {
        if (milestone == 1){
            return "Absorbs " + OtherThings.nicePercentString(percentLessDamage) + " of direct damage on first turn every level (" + OtherThings.nicePercentString(roundedScaleMilestoneDouble(owner,percentLessDamage,milestone)) + ")";
        }
        return "Absorbs " + OtherThings.nicePercentString(percentLessDamage) + " of direct damage on first turn every " + OtherThings.intOrNiceDecimalFormat(milestone) + " levels (" + OtherThings.nicePercentString(roundedScaleMilestoneDouble(owner,percentLessDamage,milestone)) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percentLessDamage;
    }
    
    @Override
    public int viability() {//divide by 0?
        return (int)(owner.getBaseAtt() * owner.getBaseHP() / (1-roundedScaleMilestoneDouble(owner,percentLessDamage,milestone)));
    }

    @Override
    public int positionBias() {
        return 3;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
/*

package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.CanUseSkills;
import cosmosquestsolver.OtherThings;

//takes x% less damage from attacks.
//used by Frosty
public class ScaleableAbsorbPercent extends Skill{
    
    private double percentLessDamage;
    private double levelMilestone;
    
    public ScaleableAbsorbPercent(Creature owner, double percent, double levelMilestone) {
        super(owner);
        this.percentLessDamage = percent;
        this.levelMilestone = levelMilestone;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAbsorbPercent(newOwner,percentLessDamage,levelMilestone);
    }
    
    @Override
    public double alterIncomingDamage(double hit, double initialHit, Formation thisFormation, Formation enemyFormation) {
        double actualPercent = 0;
        
        actualPercent = roundedScaleMilestoneDouble((CanUseSkills)owner,percentLessDamage,levelMilestone);
        
        if (owner == thisFormation.getFrontCreature()){
            double damageIntercepted = initialHit * actualPercent;//how does it work when stacked with Intercept?
            return Math.round(hit - damageIntercepted);
        }
        else{
            return hit;
        }
    }
    
    
    @Override
    public String getDescription() {
        String s = levelMilestone == 1 ? "" : "s";
        return "Absorbs " + OtherThings.nicePercentString(percentLessDamage) + " of direct damage every " + OtherThings.intOrNiceDecimalFormat(levelMilestone) + " level" + s + " (" + OtherThings.nicePercentString(roundedScaleMilestoneDouble((CanUseSkills)owner,percentLessDamage,levelMilestone)) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percentLessDamage;
    }
    
    @Override
    public int viability() {//divide by 0?
        return (int)(owner.getBaseAtt() * owner.getBaseHP() / (1-roundedScaleMilestoneDouble((CanUseSkills)owner,percentLessDamage,levelMilestone)));
    }
    
}

*/