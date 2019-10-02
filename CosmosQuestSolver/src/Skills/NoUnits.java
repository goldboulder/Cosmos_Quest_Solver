/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import GUI.RunePanel;

//
public class NoUnits extends Skill implements RuneSkill{

    public NoUnits(Creature owner) {
        super(owner);
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new NoUnits(newOwner);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        owner.setCurrentHP(0);//leprecaun?*** won't be removed before lep does his skill
    }
    
    
    @Override
    public String getDescription() {
        return "No units can be placed here";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int viability() {
        return 0;
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
        //nothing
    }

    @Override
    public String getImageName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getName() {
        return "Block";
    }
    
}
