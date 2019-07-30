/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//combines AOEDamage and heal and damages self
//used by Kedari
public class ScaleableSacrifice extends Skill{
    
    private double AOEDamage;
    private double healAmount;
    private double sacrificeAmount;
    
    public ScaleableSacrifice(Creature owner, double AOEDamage, double healAmount, double sacrificeAmount){
        super(owner);
        this.AOEDamage = AOEDamage;
        this.healAmount = healAmount;
        this.sacrificeAmount = sacrificeAmount;
    }
    

    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//weird order: heal taken before reflect in game
        
        owner.changeHP(-Math.floor(roundedScaleMilestoneDouble(owner,sacrificeAmount,1)), thisFormation);
        enemyFormation.takeAOEDamage(Math.floor(roundedScaleMilestoneDouble(owner,AOEDamage,1)));
    }
    
    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        //does not heal self
            
        double heal = Math.floor(roundedScaleMilestoneDouble(owner,healAmount,1));
        for (Creature c : thisFormation){
            if (c != owner){
                double newAmount = heal * (1 - enemyFormation.getAOEResistance());
                //have individual creature heal method in Creature?
                c.takeHeal(newAmount,thisFormation);
                
            }
        }
    }
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new ScaleableSacrifice(newOwner,AOEDamage,healAmount,sacrificeAmount);
    }
    
    @Override
    public String getDescription() {
        
        int actualSac = 0;
        int actualDam = 0;
        int actualH = 0;
        
        
        actualSac = roundedScaleMilestone(owner,sacrificeAmount,1);
        actualDam = roundedScaleMilestone(owner,AOEDamage,1);
        actualH = roundedScaleMilestone(owner,healAmount,1);
        
        
        return "Sacrifices " + sacrificeAmount + " HP to deal " + AOEDamage + " AOE and heal teammates for " + healAmount + " every level " + "(" + actualSac + "," + actualDam + "," + actualH + ")";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + AOEDamage + " " + healAmount + " " + sacrificeAmount;
    }
    
    @Override
    public int viability() {
        int actualSac = 0;
        int actualDam = 0;
        int actualH = 0;
        
        
        actualSac = roundedScaleMilestone(owner,sacrificeAmount,1);
        actualDam = roundedScaleMilestone(owner,AOEDamage,1);
        actualH = roundedScaleMilestone(owner,healAmount,1);
        
        //System.out.println(actualSac);
        //System.out.println((owner.getBaseHP() * owner.getBaseAtt()) + (owner.getBaseHP() * (Formation.MAX_MEMBERS *(2 * (actualDam + actualH) - actualSac))));
        
        return (owner.getBaseHP() * owner.getBaseAtt()) + (Formation.MAX_MEMBERS * 70 *(2 * (actualDam + actualH) - actualSac));//work on?
        
    }

    @Override
    public int positionBias() {
        return -3;
    }
    
    
    
}
