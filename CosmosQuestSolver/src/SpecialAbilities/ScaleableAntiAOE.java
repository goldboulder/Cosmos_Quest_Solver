/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;

//decreaces AOE ability damage on formation by a specified percentage based on
//level. Used by Bubbles
public class ScaleableAntiAOE extends AntiAOE{
    
    
    public ScaleableAntiAOE(Creature owner, double amount) {
        super(owner,amount);
    }
    
    

    
    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new ScaleableAntiAOE(newOwner,percent);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
            double AOEResistance = roundedScaleMilestoneDouble(owner,percent,1);
            if (AOEResistance > 1){
                AOEResistance = 1;
            }
            thisFormation.setAOEResistance(AOEResistance);
    }

    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation) {
            thisFormation.setAOEResistance(thisFormation.getAOEResistance() - roundedScaleMilestoneDouble(owner,percent,1));
    }

    
    
    @Override
    public String getDescription() {
        
        String percentStr = "";
        double times100 = percent * 100;
        if (times100 > 100){
                times100 = 100;
            }
        if (times100 % 1 == 0){
            percentStr = Integer.toString((int) times100);
        }
        else{
            percentStr = Double.toString(times100);
        }
        
        String percentLevelStr = "";
        
        double times100Level = 100 * roundedScaleMilestoneDouble(owner,percent,1);
        if (times100Level > 100){
                times100Level = 100;
            }
        if (times100Level % 1 == 0){
            percentLevelStr = Integer.toString((int) times100Level);
        }
        else{
            percentLevelStr = Double.toString(times100Level);
        }
        
        
        return "Formation takes" + percentStr + "% less damage per level from AOE skills (" + percentLevelStr + "%)";
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }
    
    
    
    @Override
    public int viability() {
        
        int level = owner.getLevel() > Hero.MAX_NORMAL_LEVEL ? Hero.MAX_NORMAL_LEVEL : owner.getLevel();
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+(percent*level*0.01)));
    }
    
}
