/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements.Element;
import Formations.Formation;
import Formations.Hero;

//all heroes possess a special ability that affects them and/or their teammates
//in combat. Most special abilities have parameters, and many heroes share the same
//abilities, so these abilities are modular
public abstract class SpecialAbility {
    
    protected Creature owner;
    
    public SpecialAbility(Creature owner){
        this.owner = owner;
    }
    
    public void setOwner(Creature creature) {
        this.owner = creature;
    }
    

    //the following methods define what to do at each stage of a formation's attack cycle.
    //they are empty, but overwritten when needed
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        
    }
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation){
        
    }
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation){
        
    }
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        
    }
    public double extraDamage(Formation thisFormation, Formation enemyFormation){
        return 0;
    }
    public double getElementDamageBoost(Element element) {
        return 0;
    }
    public void attack(Formation thisFormation, Formation enemyFormation) {
        enemyFormation.takeHit(owner,thisFormation);
    }
    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {
        long longHit = (long)Math.ceil(hit);
        recordDamageTaken(longHit,thisFormation,enemyFormation);
        attacker.getSpecialAbility().recordDamageDealt(longHit,thisFormation,enemyFormation);
        owner.changeHP(-hit,thisFormation);
    }
    public double alterIncomingDamage(double hit, double initialHit, Formation thisFormation, Formation enemyFormation) {
        return hit;
    }
    public void recordDamageDealt(long damage, Formation thisFormation, Formation enemyFormation){//needed?
        
    }
    
    //if needed, set, not add, damage taken as a local variable
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation){
        
    }
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){//have 2 post actions? AOE then heal
    
    }
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation){//healing goes here
        
    }
    public void deathAction(Formation thisFormation, Formation enemyFormation){
        
    }
    
    public static int roundedScaleMilestone(Creature owner, double amount, double milestone){
        int level = owner.getLevel() > Hero.MAX_NORMAL_LEVEL ? Hero.MAX_NORMAL_LEVEL : owner.getLevel();
        return (int)((amount * (int)(level / milestone)));
    }
    
    public static double roundedScaleMilestoneDouble(Creature owner, double amount, double milestone){
        int level = owner.getLevel() > Hero.MAX_NORMAL_LEVEL ? Hero.MAX_NORMAL_LEVEL : owner.getLevel();
        return (amount * (int)(level / milestone));
    }
    
    public double levelPercent(double percent){
        double actualPercent = 0;
        actualPercent = owner.getLevel() * percent;
        return actualPercent;
    }
    
    
    
    public static String roundedScaleMilestoneStr(Creature owner, double amount, double milestone){
        return "(" + roundedScaleMilestone(owner,amount,milestone) + ")";
    }
    
    //heroes and special abilities have references to each other. This makes sure
    //there aren't any null pointers
    public abstract SpecialAbility getCopyForNewOwner(Creature newOwner);
    
    //currently not used because the tooltips blockes mouseListeners
    public abstract String getDescription();
    public abstract String getParseString();
    
    //special abilities can sometimes drastically increace a hero's effectiveness
    //in combat. This method provides a heuristic measurement for that effectiveness.
    //the base viability is attack * health
    public abstract int viability();
    public abstract int positionBias();//+ if unit is best used in front. - for back, 0 for don't care 1-3 depending on how useful it is to be in that position
    public boolean WBNHEasy(){
        return false;
    }
    public boolean WBTryLessCreatures(){
        return false;
    }

    
    
}
