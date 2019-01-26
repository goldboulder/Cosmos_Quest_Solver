/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Formation;
import Formations.Monster;
import cosmosquestsolver.OtherThings;

//multiplies attack by a specified amount for every living monster behind the
//owner (multiplicative). Used by Geror and Ascended Geror
public class MonsterBuff extends SpecialAbility{
    
    private double multiplier;

    public MonsterBuff(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }

    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.setCurrentAtt((long)(Math.ceil(owner.getBaseAtt() * Math.pow(multiplier, monstersBehind(thisFormation)))));
    }
    
    private int monstersBehind(Formation f){
        int numBehind = 0;
        boolean foundOwner = false;
        for (Creature creature : f){
            if (foundOwner && creature instanceof Monster && !creature.isDead()){
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
        return new MonsterBuff(newOwner,multiplier);
    }
    
    
    @Override
    public String getDescription() {
        return "Monsters behind multiply attack by " + multiplier;
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        int att = (int)(owner.getBaseAtt() * Math.pow(multiplier, Formation.MAX_MEMBERS/3.0));
        return owner.getBaseHP() * att;
    }

    @Override
    public int positionBias() {
        return 3;
    }
    
    
    
}

