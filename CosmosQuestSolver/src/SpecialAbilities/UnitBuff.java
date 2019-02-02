/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Monster;
import cosmosquestsolver.OtherThings;

//increases attack and strength by specified amounts for each unit behind
//owner. Used by Seethe
public class UnitBuff extends SpecialAbility{
    
    protected int attBoost;
    protected int HPBoost;

    public UnitBuff(Creature owner, int attBoost, int HPBoost) {
        super(owner);
        this.attBoost = attBoost;
        this.HPBoost = HPBoost;
    }

    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        int unitsBehind = unitsBehind(thisFormation);
        owner.setCurrentAtt(owner.getBaseAtt() + attBoost * unitsBehind);
        owner.setCurrentHP(owner.getBaseHP() + HPBoost * unitsBehind);
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
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new UnitBuff(newOwner,attBoost,HPBoost);
    }
    
    
    @Override
    public String getDescription() {
        return "Increaces attack by " + attBoost + " and HP by " + HPBoost + " per unit behind";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attBoost + " " + HPBoost;
    }
    
    @Override
    public int viability() {
        return (owner.getBaseHP() + HPBoost*Formation.MAX_MEMBERS/2) * (owner.getBaseAtt() + attBoost*Formation.MAX_MEMBERS/2);
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

