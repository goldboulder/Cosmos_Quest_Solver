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
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {
        if (enemyFormation.getFrontCreature().isDead() && thisFormation.getFrontCreature() == owner){
            owner.setCurrentAtt(owner.getCurrentAtt() + attGain);
            owner.setMaxHP(owner.getMaxHP() + HPGain);
            owner.setCurrentHP(owner.getCurrentHP() + HPGain);
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
