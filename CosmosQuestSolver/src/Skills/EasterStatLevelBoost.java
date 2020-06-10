/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//at the beggining of the battle, adjests the stats of the owner as if its level
// was multiplied by a specified amount, but only if other Easter heroes are
// in the formation. Used by Daisy
public class EasterStatLevelBoost extends StatLevelBoost{
    
    public EasterStatLevelBoost(Creature owner, double multiplier) {
        super(owner, multiplier);
    }
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new EasterStatLevelBoost(newOwner,multiplier);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {//adjusts stats
        if (hasOtherEasterHeroes(thisFormation)){
            super.prepareForFight(thisFormation, enemyFormation);
        }
    }
    
    private boolean hasOtherEasterHeroes(Formation formation){
        for (Creature c : formation){
            switch (c.getName()){
                case "Sparks": case "Leaf": case "Flynn": case "Willow": case "Gizmo": case "Thumper": case "Mysterious Egg": case "Baby Pyros": case "Young Pyros": case "King Pyros":
                    return true;
                default:
            }
        }
        return false;
    }
    
    @Override
    public String getDescription() {
        
        
        int[] newStats = getNewStats();
        return "Stats gained per level x" + OtherThings.intOrNiceDecimalFormat(multiplier) + " if with another Easter hero " + " (" + newStats[0] + "," + newStats[1] + ")";
        
    }
    
    @Override
    public int viability() {//returns viability at the owner's effective level
        int[] newStats = getNewStats();
        int max = newStats[0] * newStats[1];
        int min = owner.getBaseHP() * owner.getBaseAtt();
        return (int)(min + (max-min)*multiplier * 0.2);
    }
    
}
