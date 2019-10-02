/*

 */
package Skills;

import Formations.Creature;
import Formations.Formation;

// upon death, grants remaining units a given percent of the unit's health and attack
// used by season 7 heroes
public class PartingGift extends Skill{
    
    private double multiplier;
    private boolean activated = false;

    public PartingGift(Creature owner, double multiplier) {//how will this work with geum?
        super(owner);
        this.multiplier = multiplier;
    }
    

    
    
    @Override
    public Skill getCopyForNewOwner(Creature newOwner) {
        return new PartingGift(newOwner,multiplier);
    }
    
    @Override
    public void deathAction(Formation thisFormation, Formation enemyFormation){
        if (!activated){
            grantBoost(thisFormation);
        }
    }

    @Override
    public void postRoundAction0(Formation thisFormation, Formation enemyFormation) {//triggers before reflect
        if (owner.isDead() && !activated){
            grantBoost(thisFormation);
        }
    }
    
    private void grantBoost(Formation thisFormation){
        int attBoost = (int) Math.round(owner.getCurrentAtt() * multiplier);
        int HPBoost = (int) Math.round(owner.getMaxHP() * multiplier);
        for (Creature c : thisFormation){
            if (owner != c){
                c.setMaxHP(c.getMaxHP() + HPBoost);
                //c.changeHPNoDeathCheck(HPBoost, thisFormation);//this currently revives dead units. The game does this too. if changed: do changeHP()
                c.changeHP(HPBoost, thisFormation);
                c.setCurrentAtt(c.getCurrentAtt() + attBoost);
            }
        }
        activated = true;
    }
    
    @Override
    public String getDescription() {
        String percent = Integer.toString((int)(multiplier * 100));
        return "Grants remaining units " + percent + "% attack and HP after dying (" + Integer.toString((int)Math.round(owner.getCurrentAtt() * multiplier)) + "," + Integer.toString((int)Math.round(owner.getMaxHP() * multiplier)) + ")"; //amount?
    }
    
    @Override
    public String getParseString() {
        return this.getClass().getSimpleName() + " " + multiplier;
    }
    
    @Override
    public int viability() {
        int HP = (int)(owner.getBaseHP() * (1+multiplier*Formation.MAX_MEMBERS*0.8));
        int att = (int)(owner.getBaseAtt() * (1+multiplier*Formation.MAX_MEMBERS*0.8));
        return att * HP;
    }

    @Override
    public int positionBias() {
        return 2;
    }
    
    @Override
    public boolean WBNHEasy() {
        return true;
    }
    
}
