/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;
import Formations.Hero;
import GUI.RunePanel;

//
//
public class NoHeroes extends Skill implements RuneSkill{

    public NoHeroes(Creature owner) {
        super(owner);
    }

    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new NoHeroes(newOwner);
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        if (owner instanceof Hero){
            owner.setCurrentHP(0);
        }
    }
    
    
    @Override
    public String getDescription() {
        return "No heroes can be placed here";
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int viability() {
        if (owner instanceof Hero){
            return 0;
        }
        else{
            return owner.getBaseAtt() * owner.getBaseHP();
        }
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
        return "Hero Block";
    }
    
}
