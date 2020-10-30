/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//transfers x% overkill damage to next unit
//used by Lady Maligryn
public class DamageRecycle extends Skill{
    
    private double percent;
    private long enemyHPBefore;
    private Creature firstTarget;
    
    public DamageRecycle(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation){
        firstTarget = enemyFormation.getFrontCreature();
        enemyHPBefore = firstTarget.getCurrentHP();
    }
    
    @Override
    public void postAttackAction(Formation thisFormation, Formation enemyFormation){
        double overkillDamage = owner.determineDamageDealt(firstTarget, thisFormation, enemyFormation) + firstTarget.getArmor() - enemyHPBefore;
        if (enemyFormation.size() > 1){
            enemyFormation.getCreature(1).takeAOEDamage(overkillDamage * percent, thisFormation);
        }
        
    }
    

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new DamageRecycle(newOwner,percent);
    }

    @Override
    public String getDescription() {
        String percentStr = Integer.toString((int)(percent * 100));
        return "Transfers " + percentStr + "% overkill damage to next unit";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * (1+0.75*percent));
    }

    @Override
    public int positionBias() {
        return 1;
    }
    
    
    
}
