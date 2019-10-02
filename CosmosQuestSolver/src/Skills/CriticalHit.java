/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;

//seed based randomness determines if damage is multiplied by a certain amount.
//used by Pokerface
public class CriticalHit extends Skill{
    
    private double multiplier;
    private int turn = 0;
    private long seed = -1;

    public CriticalHit(Creature owner, double multiplier) {
        super(owner);
        this.multiplier = multiplier;
    }
    
    @Override
    public void restore(){
        turn = 0;
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new CriticalHit(newOwner,multiplier);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation) {
        //enemyFormation.getTurnSeed(enemyFormation, turn);//gets formation to generate seed at the beginning of the fight. seed might change if called mid-fight
        seed = enemyFormation.getSeed();
        
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation){
        turn ++;
    }
    
    @Override
    public double extraDamage(Formation thisFormation, Formation enemyFormation){//damage boost from other heroes stacks with crit
        //if (alwaysHit || thisFormation.getTurnSeed(enemyFormation,turn) % 2 == 0){
        //if (enemyFormation.getTurnSeed(Formation.STALEMATE_CUTOFF_POINT-1-turn) % 2 == 0){
        //do empty spaces count?
        if (Formation.getTurnSeed(seed,turn) % 2 == 0){
            //test this more with data?
            return (multiplier-1) * (owner.attWithBoosts());
        }
        else{
            return 0;
        }
    }
    
    //double damage = currentAtt + attBoost + specialAbility.extraDamage(enemyFormation,thisFormation);//change to currentAttack*** current att obsolete?
    //damage = damageFromElement(damage,target.element) - target.getArmor();
    /*
    @Override
    public void attack(Formation thisFormation, Formation enemyFormation) {
        if (Math.abs(thisFormation.getTurnSeed(enemyFormation,turn)) % 2 == 1){
            long originalAtt = owner.getCurrentAtt();
            owner.setCurrentAtt((long)(Math.ceil(originalAtt * multiplier)));
            super.attack(thisFormation,enemyFormation);
            owner.setCurrentAtt(originalAtt);
            //System.out.println("crit, seed = " + thisFormation.getTurnSeed(enemyFormation, turn));
        }
        else{
            super.attack(thisFormation,enemyFormation);
            //System.out.println(" no crit, seed = " + thisFormation.getTurnSeed(enemyFormation, turn));
        }
    }
    */
    
    //https://github.com/GizmoMar/C-Hero-Calc/commit/b9e0ecd3c9860d9dd0016ca6910b5f237c37affd
    
    @Override
    public String getDescription() {
        if ((int)multiplier == multiplier){
            return "Has a 50% chance to deal x" + (int)multiplier + " damage";
        }
        return "Has a 50% chance to deal x" + multiplier + " damage";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * multiplier * 0.75);// more than average of extra damage damage because calc can find when he crits
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
    
    
}
