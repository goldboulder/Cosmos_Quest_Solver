/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Elements.Element;

//increases attack and strength by specified amounts for each unit behind depending on level
//owner. Used by Ascended Seethe
public class ScaleableUnitBuff extends Skill{
    
    private double attPerLevel;
    private double HPPerLevel;
    
    public ScaleableUnitBuff(Creature owner, double attPerLevel, double HPPerLevel) {//if elsment is null, apply to all creatures
        super(owner);
        this.attPerLevel = attPerLevel;
        this.HPPerLevel = HPPerLevel;
    }
    
    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableUnitBuff(newOwner,attPerLevel,HPPerLevel);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        int unitsBehind = unitsBehind(thisFormation);
        owner.setCurrentAtt(owner.getBaseAtt() + Math.round(attPerLevel*owner.getLevel()) * unitsBehind);
        owner.setCurrentHP(owner.getBaseHP() + (int)Math.round(HPPerLevel*owner.getLevel()) * unitsBehind);
        owner.setMaxHP(owner.getCurrentHP());
    }
    
    protected int unitsBehind(Formation f){
        int numBehind = 0;
        boolean foundOwner = false;
        for (Creature creature : f){
            if (foundOwner && !creature.isDead()){//isDead needed?
                numBehind ++;
            }
            
            if (creature == owner){//this goes last in case a monster has this ability(not possible currently)
                foundOwner = true;
            }
        }
        
        return numBehind;
    }

    
    
    
        
    @Override
    public String getDescription() {
        
        return "Increaces attack by " + attPerLevel + " and HP by " + HPPerLevel + " per unit behind per level (" + Math.round(attPerLevel*owner.getLevel()) + "," + Math.round(HPPerLevel*owner.getLevel()) + ")";
        
        
    }
    
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attPerLevel + " " + HPPerLevel;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() + (int)Math.round(HPPerLevel*owner.getLevel())*Formation.MAX_MEMBERS/2) * (owner.getBaseAtt() + (int)Math.round(attPerLevel*owner.getLevel())*Formation.MAX_MEMBERS/2);
    }

    @Override
    public int positionBias() {
        return 3;
    }
    
}
