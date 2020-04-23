/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;

/**
 *
 * @author Nathan
 */
public class ReverseRicochet extends Ricochet{
    
    public ReverseRicochet(Creature owner, double multiplier, int numBounces) {
        super(owner, multiplier, numBounces);
    }
    
    @Override
    public void postAttackAction(Formation thisFormation, Formation enemyFormation) {
        
        int bouncesLeft = numBounces;
        double hitElementMultiplier = Elements.elementDamageMultiplier(owner,enemyFormation.getFrontCreature().getElement());
        
        for (int i = enemyFormation.size() - 1; i > 0; i --){//front creature was already attacked
            Creature creature = enemyFormation.getCreature(i);
            
            if (bouncesLeft > 0){
                double nextCreatureMultiplier = Elements.elementDamageMultiplier(owner,creature.getElement());
                
                owner.addAttPercentBoost(-tempBoost);
                double rictDamage = owner.determineDamageDealt(creature, thisFormation, enemyFormation) + creature.getArmor();//does percent armor count here?*******
                owner.addAttPercentBoost(tempBoost);
                
                rictDamage *= Math.pow(multiplier, enemyFormation.size() - i);
                rictDamage *= Math.pow((1-enemyFormation.getAOEResistance()), i);
                
                //patch to compensate for rico damage now not being individual to each units' element
                rictDamage *= (hitElementMultiplier / nextCreatureMultiplier);
                
                rictDamage = Math.round(rictDamage-creature.getArmor());
                creature.takeHit(owner,enemyFormation,thisFormation,rictDamage);
                bouncesLeft --;
            }
            else{
                break;
            }
        }
    }
    /*
    @Override
    public void postAttackAction(Formation thisFormation, Formation enemyFormation) {
        
        int bouncesLeft = numBounces;
        double hitElementMultiplier = Elements.elementDamageMultiplier(owner,enemyFormation.getFrontCreature().getElement());
        
        for (int i = enemyFormation.size() - 1; i > 0; i --){//front creature was already attacked
            bounce(thisFormation,enemyFormation,i,bouncesLeft,hitElementMultiplier);
            bouncesLeft--;
        }
    }
*/
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ReverseRicochet(newOwner,multiplier,numBounces);
    }
    
    
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        if (numBounces == 1){
            return "Ricochet " + percent + "% damage to last unit";
        }
        else{
            return "Ricochet " + percent + "% damage to back " + numBounces + " units";
        }
        
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier + " " + numBounces;
    }
    
}
