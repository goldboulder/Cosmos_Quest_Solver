/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

//increases att and hp when the owner kills an enemy
//used by season 9 heroes
public class BloodLust extends Skill{
    
    protected int attGain;
    protected int HPGain;
    
    public BloodLust(Creature owner, int attGain, int HPGain){
        super(owner);
        this.attGain = attGain;
        this.HPGain = HPGain;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//take glitch into account?
        if (!owner.isDead() && enemyFormation.getFrontCreature().isDead() && thisFormation.getFrontCreature() == owner){
            owner.setCurrentAtt(owner.getCurrentAtt() + attGain);
            owner.setMaxHP(owner.getMaxHP() + HPGain);
            owner.setCurrentHP(owner.getCurrentHP() + HPGain);
        }
        //game bug quick patch
        if (owner.isDead() && enemyFormation.getFrontCreature().isDead() && thisFormation.getFrontCreature() == owner){
            //transfer stats to unit behind ** this buff is ignored by fairy's skills
            if (thisFormation.size() > 1){
                Creature c = thisFormation.getCreature(1);
            
                c.setCurrentAtt(c.getCurrentAtt() + attGain);
                c.setMaxHP(c.getMaxHP() + HPGain);
                c.setCurrentHP(c.getCurrentHP() + HPGain);
            }
        }
        
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new BloodLust(newOwner,attGain,HPGain);
    }

    @Override
    public String getDescription() {
        return "Gains " + attGain + " attack and " + HPGain + " HP after killing";
    }
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + attGain;
    }

    @Override
    public int viability() {
        return ((owner.getBaseHP() + HPGain/3) * (owner.getBaseAtt() + attGain/3));
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    
    
    
    
}
