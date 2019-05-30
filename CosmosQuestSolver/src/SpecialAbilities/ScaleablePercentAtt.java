/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import static SpecialAbilities.SpecialAbility.roundedScaleMilestoneDouble;
import cosmosquestsolver.OtherThings;
import Formations.Hero;

//
//used by 5-12-6
public class ScaleablePercentAtt extends SpecialAbility{
    
    private double percent;
    
    public ScaleablePercentAtt(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    
    

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleablePercentAtt(newOwner,percent);
    }
    /*
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation){//does not stack with element boost
        return enemyFormation.getFrontCreature().getMaxHP() * roundedScaleMilestoneDouble(owner,percent,1);
    }
    */
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {//post/attack action for p6 compatability?
        Creature victem = enemyFormation.getFrontCreature();
        //long enemyHPBefore = victem.getCurrentHP();
        super.attack(thisFormation, enemyFormation);
        
        //double extraHit = victem.getMaxHP() * roundedScaleMilestoneDouble(owner,percent,1);
        victem.takeHit(owner,enemyFormation,thisFormation,victem.getMaxHP() * roundedScaleMilestoneDouble(owner,percent,1));
        //victem.recordDamageTaken(victem.getMainSkill().getDamageTaken() + extraHit,thisFormation,enemyFormation);
        
    }
    
    @Override
    public String getDescription() {
        return "Deals extra damage equalt to " + OtherThings.nicePercentString(percent) + " of of enemy HP every level (" + OtherThings.nicePercentString(roundedScaleMilestoneDouble(owner,percent,1)) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+(5.5*roundedScaleMilestoneDouble(owner,percent,1))));
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

/*

package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.CanUseSkills;
import static SpecialAbilities.SpecialAbility.roundedScaleMilestoneDouble;
import cosmosquestsolver.OtherThings;

//
//used by 5-12-6
public class ScaleablePercentAtt extends SpecialAbility{
    
    private double percent;
    private double levelMilestone;
    
    public ScaleablePercentAtt(Creature owner, double percent, double levelMilestone){
        super(owner);
        this.percent = percent;
        this.levelMilestone = levelMilestone;
    }
    
    
    

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleablePercentAtt(newOwner,percent,levelMilestone);
    }

    public void attack(Formation thisFormation, Formation enemyFormation) {
        Creature c = enemyFormation.getFrontCreature();
        c.takeHit(owner, enemyFormation, thisFormation, c.getMaxHP() * roundedScaleMilestoneDouble((CanUseSkills)owner,percent,levelMilestone));//max or current? before or after attack?***
        enemyFormation.takeHit(owner,thisFormation);
    }
    
    @Override
    public String getDescription() {
        String s = levelMilestone == 1 ? "" : "s";
        return "Adds " + OtherThings.nicePercentString(percent) + " of of enemy HP as attack every " + OtherThings.intOrNiceDecimalFormat(levelMilestone) + " level" + s + " (" + OtherThings.nicePercentString(roundedScaleMilestoneDouble((CanUseSkills)owner,percent,levelMilestone)) + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+(5.5*roundedScaleMilestoneDouble((CanUseSkills)owner,percent,levelMilestone))));
    }
    
    
    
}

*/