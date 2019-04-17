/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;

//at the beggining of the battle, adjests the stats of the owner as if its level
// was multiplied by a specified amount. Used by Easter event 1 heroes
public class StatLevelBoost extends SpecialAbility{
    
    protected double multiplier;//edit stat gain on rarity type function instead?

    public StatLevelBoost(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new StatLevelBoost(newOwner,multiplier);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {//adjusts stats
        
        int[] newStats = getNewStats();
        //owner.setBaseAtt(newStats[0]);
        owner.setMaxHP(newStats[1]);
        owner.setCurrentAtt(newStats[0]);
        owner.setCurrentHP(owner.getMaxHP());
        
    }
    
    //slight rounding error on flynn.5 and leaf.5. 
    protected int[] getNewStats(){
        if (owner instanceof Hero){
            Hero h = (Hero) owner;
            int att = levelStat(h.getLevel(),h,h.getLvl1Att(),h.getLvl1HP());
            int hp = levelStat(h.getLevel(),h,h.getLvl1HP(),h.getLvl1Att());
            
            if (h.getPromoteLevel() >= 1){
                hp += h.getPromote1HP();
            }
            if (h.getPromoteLevel() >= 2){
                att += h.getPromote2Att();
            }
            if (h.getPromoteLevel() >= 4){
                hp += h.getPromote4Stats();
                att += h.getPromote4Stats();
            }
            
            
            return new int[]{att,hp};
        }
        else{
            return new int[]{owner.getLvl1Att(),owner.getLvl1HP()};
        }
    }
    
    
    
    protected int levelStat(int level, Hero h, int statWanted, int otherStat){
            double boost = ((h.rarityStatBoost()*multiplier + h.promotionLevelStatBoost())*statWanted*(level-1)) / (double)(otherStat+statWanted);
            int intBoost = (int) (boost + 0.5);
            return statWanted + intBoost;
        
    }
    
    @Override
    public String getDescription() {
        String multString = "";
        if (multiplier % 1 == 0){
            multString = Integer.toString((int)multiplier);
        }
        else{
            multString = Double.toString(multiplier);
        }
        
        
        int[] newStats = getNewStats();
        return "Stats gained per level x" + multString + " (" + newStats[0] + "," + newStats[1] + ")";
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {//returns viability at the owner's effective level
        int[] newStats = getNewStats();
        return newStats[0] * newStats[1];
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
