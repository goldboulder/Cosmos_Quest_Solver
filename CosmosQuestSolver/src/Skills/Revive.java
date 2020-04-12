/*

 */
package Skills;

import Formations.Creature;
import Formations.Elements;
import Formations.Formation;
import GUI.RunePanel;
import cosmosquestsolver.OtherThings;

//revives a creature to x% health when killed. only done once.
// used as a rune skill
public class Revive extends Skill implements RuneSkill{
    
    private boolean activated;
    private double percent;

    public Revive(Creature owner, double multiplier) {
        super(owner);
        this.percent = multiplier;
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new Revive(newOwner,percent);
    }
    
    @Override//post roundAction instead? before or after other things? after heal? same time as heal, as if its dead, heal won't work anyway heal berore/after?
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {//melf bugs it if onAttack. have revive be a field in creature instead, handle in formation.remove dead
            if (owner.isDead() && !activated){
                owner.setCurrentHP((int)(owner.getMaxHP() * percent));
                //actionOnDeath here?
                activated = true;
            }
    }

    
    @Override
    public String getDescription() {
        return "Comes back to life at " + OtherThings.nicePercentString(percent) + " health when killed the first time";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + percent;
    }
    
    @Override
    public int viability() {
        return (int)((owner.getBaseHP() * (percent + 2)) * owner.getBaseAtt());
    }

    @Override
    public int positionBias() {
        return 0;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
    @Override
    public void addRuneFields(RunePanel panel) {
        panel.addDoubleTextField("percent",percent);
    }
    
    @Override
    public String getImageName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String getName() {
        return "Angel";
    }
    
}
