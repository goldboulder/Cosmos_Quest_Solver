/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import java.util.ArrayList;
import java.util.Collections;

//attacks enemies for random damages in addition to normal attack
//used by Tetra
public class WildCard extends Skill{

    private int turn = 0;
    private long seed = -1;
    private double percent;
    
    /*
    6 types of attacks:
    
    0: 25% damage to 4 targets
    1: 75% to 1, 25% to next
    2: 25% to 1, 75% to next
    3: 50% to 2 targets
    4: 25% to 1, 50% to next, 25% to next
    5: 100% to 1 target
    
    the frontmost creatures are the targets
    */
    private int attackType = 0;
    
    public WildCard(Creature owner, double percent) {
        super(owner);
        this.percent = percent;
    }
    
    @Override
    public void restore(){
        turn = 0;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new WildCard(newOwner,percent);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        //enemyFormation.getTurnSeed(enemyFormation, turn);//gets formation to generate seed at the beginning of the fight. seed might change if called mid-fight
        seed = enemyFormation.getSeed();
        
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        //determine attack type
        attackType = (int)(Formation.getTurnSeed(seed,turn) % 6);//change?
        turn ++;
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation){
        double baseDamage = owner.attWithBoosts();
        switch (attackType){
            case 1: //25% damage to 4 targets
                return baseDamage * 0.25;
            case 2: //75% to 1, 25% to next
                return baseDamage * 0.75;
            case 3: //25% to 1, 75% to next
                return baseDamage * 0.25;
            case 5: //50% to 2 targets
                return baseDamage * 0.5;
            case 4: //25% to 1, 50% to next, 25% to next
                return baseDamage * 0.25;
            case 0: //100% to 1 target
                return baseDamage;
            default: 
                return 0;
                
        }
        
    }
    
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){
        if (thisFormation.getFrontCreature() != owner){// only after attacking
            return;
        }
        int enemies = enemyFormation.size();
        //System.out.println("enemies: " + enemies + "   attack: " + attackType);
        switch (attackType){
            case 1: //25% damage to 4 targets
                //wildAttack(thisFormation,enemyFormation,0,0.25);
                if (enemies > 1){
                    wildAttack(thisFormation,enemyFormation,1,0.25);
                }
                if (enemies > 2){
                    wildAttack(thisFormation,enemyFormation,2,0.25);
                }
                if (enemies > 3){
                    wildAttack(thisFormation,enemyFormation,3,0.25);
                }
                
                
                break;
            case 2: //75% to 1, 25% to next
                //wildAttack(thisFormation,enemyFormation,0,0.75);
                if (enemies > 1){
                    wildAttack(thisFormation,enemyFormation,1,0.25);
                }
                
                break;
            case 3: //25% to 1, 75% to next
                //wildAttack(thisFormation,enemyFormation,0,0.25);
                if (enemies > 1){
                    wildAttack(thisFormation,enemyFormation,1,0.75);
                }
                
                break;
            case 5: //50% to 2 targets
                //wildAttack(thisFormation,enemyFormation,0,0.5);
                if (enemies > 1){
                    wildAttack(thisFormation,enemyFormation,1,0.5);
                }
                
                break;
            case 4: //25% to 1, 50% to next, 25% to next
                //wildAttack(thisFormation,enemyFormation,0,0.25);
                if (enemies > 1){
                    wildAttack(thisFormation,enemyFormation,1,0.5);
                }
                if (enemies > 2){
                    wildAttack(thisFormation,enemyFormation,2,0.25);
                }
                
                break;
            case 0: //100% to 1 target
                //wildAttack(thisFormation,enemyFormation,0,1.0);
                
                break;
            default:
                
        }
        
    }
    
    private void wildAttack(Formation thisFormation, Formation enemyFormation, int index, double percent){
        Creature c = enemyFormation.getCreature(index);
        double rictDamage = owner.attWithBoosts() * percent;
        rictDamage *= Elements.elementDamageMultiplier(owner, c.getElement());
        
        
        rictDamage *= Math.pow((1-enemyFormation.getAOEResistance()), index);
        rictDamage -= c.getArmor();
        
        c.takeHit(owner,enemyFormation,thisFormation,rictDamage);
        
    }
    
    @Override
    public String getDescription() {
        return "After attacking, attacks front units, splitting damage amongst them randomly";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int viability() {
        return owner.getBaseHP() * owner.getBaseAtt() * 2;
    }
    

    @Override
    public int positionBias() {
        return 2;
    }
    
    
    
}
