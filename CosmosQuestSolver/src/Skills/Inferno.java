/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import cosmosquestsolver.OtherThings;

//deals percent of the owner's attack to all units (even friendlies) after every attack.
//per turn. Damage is done before healing.
//Used by dragons.
public class Inferno extends Skill{
    
    protected double percent;
    
    public Inferno(Creature owner, double percent){
        super(owner);
        this.percent = percent;
    }
    
    public double getPercent(){
        return percent;
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//anti-aoe?
        //super.attack(thisFormation,enemyFormation);
        if (owner == thisFormation.getFrontCreature()){
            enemyFormation.takeAOEDamage(Math.round(owner.getCurrentAtt() * percent));
            for (Creature c : thisFormation){
                if (c != owner){
                    c.changeHP(-Math.round(owner.getCurrentAtt() * percent), thisFormation);
                }
            }
        }
    }

    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Inferno(newOwner,percent);
    }

    @Override
    public String getDescription() {
        return "Deals " + OtherThings.nicePercentString(percent) + " damage to all units including own team but not self after every attack (" + (int)Math.round(owner.getCurrentAtt()*percent) + ")";
    }

    @Override
    public int viability() {
        return (int)(owner.getBaseHP() * owner.getBaseAtt() * 1.8);
    }

    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }

    @Override
    public int positionBias() {
        return -2;
    }
    
    @Override
    public boolean WBTryLessCreatures(){
        return true;
    }
    
    
    
}
