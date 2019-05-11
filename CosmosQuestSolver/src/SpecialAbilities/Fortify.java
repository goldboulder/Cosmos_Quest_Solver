/*

 */
package SpecialAbilities;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import Formations.Monster;
import cosmosquestsolver.OtherThings;

//+ x armor per unit behind.
// used by Bortles
public class Fortify extends SpecialAbility{
    
    private int armorPerCreature;

    public Fortify(Creature owner, int armorPerCreature) {
        super(owner);
        this.armorPerCreature = armorPerCreature;
    }

    
    @Override
    public SpecialAbility getCopyForNewOwner(Creature newOwner) {
        return new Fortify(newOwner,armorPerCreature);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.addArmorBoost(unitsBehind(thisFormation) * armorPerCreature);
    }
    
    //@Override
    //public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {
        //super.takeHit(attacker,thisFormation,enemyFormation,hit - (unitsBehind(thisFormation) * armorPerCreature));//start of battle!
    //}
    
    protected int unitsBehind(Formation f){
        int numBehind = 0;
        boolean foundOwner = false;
        for (Creature creature : f){
            if (foundOwner){//isDead needed?
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
        return "+" + armorPerCreature + " armor to self for each unit behind";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + armorPerCreature;
    }
    
    @Override
    public int viability() {//normal viability, using average damage increace if fighting a creature of a random element
        return (int)((owner.getBaseHP()+(Formation.MAX_MEMBERS-1)*2.0) * owner.getBaseAtt());
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
