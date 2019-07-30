/*

 */
package Formations;

import Formations.Elements.Element;
import GUI.AssetPanel;
import GUI.CreatureDrawer;
import GUI.ImageFactory;
import Skills.Skill;
import java.awt.Graphics;


public class WorldBoss extends Creature{
    
    private long damageTaken;//yes, this has to be long. Darn Geum...
    
    protected WorldBoss(){
        //usedForCopying
    }
    
    protected WorldBoss(Element element, int baseAtt, int ID, Skill skill){
        super(element,baseAtt,Integer.MAX_VALUE);
        this.skill = skill;
        this.ID = ID;
    }
    
    @Override
    public void restore(){
        super.restore();
        damageTaken = 0;
    }

    @Override
    public Creature getCopy() {
        WorldBoss wb = new WorldBoss();
        wb.element = element;
        wb.baseHP = baseHP;
        wb.baseAtt = baseAtt;
        wb.skill = skill.getCopyForNewOwner(wb);
        wb.nodeSkill = nodeSkill.getCopyForNewOwner(wb);
        wb.currentHP = baseHP;//needed?
        wb.currentAtt = baseAtt;
        wb.ID = ID;
        
        //wb.level = level;//levels mattered in the past
        
        return wb;
    }
    /*
    //world bosses don't have hp, instead, record damage taken
    @Override
    public void takeHit(Creature creature,  Formation thisFormation, Formation enemyFormation) {
        double hit = creature.determineDamageDealt(this,thisFormation,enemyFormation);
        thisFormation.addDamageTaken((long)Math.ceil(hit));
        damageTaken = thisFormation.getDamageTaken();//duplicate info? bosses may not always be alone?
    }//damage refection on bosses? override changeHP instead?
    */
    
    //world boss should implement battleCreature, not extend creature
    @Override
    public void changeHP(double damage, Formation thisFormation){
        
        long num;//Geum
        if (damage < 0){//round away from 0
            num = (long)Math.floor(damage);
            thisFormation.addDamageTaken(-num);
        }
        else{//this shouldn't happen. Bosses have no use for healing
            num = (long) Math.ceil(damage);
        }
        
        damageTaken -= num;
        
    }
    
    
    @Override
    public void draw(Graphics g) {
        CreatureDrawer.drawCreature(this, g);
    }
    
    @Override
    public String getImageAddress() {
        return "Creatures/World Bosses/" + getName();
    }
    
    @Override
    public String toolTipText() {
        return "<html>" + getName() + "<br>Attack: " + getBaseAtt() + "<br>" + getMainSkill().getDescription() + "</html>";
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\tAtt: ");
        sb.append(currentAtt).append("\tDamage Taken: ");
        sb.append(damageTaken).append("\tElement: ");
        sb.append(element);
        
        return sb.toString();
    }

    @Override
    public Hero.Rarity getRarity() {
        return null;
    }
    
    @Override
    public long getFollowers() {
        return 0;
    }

    @Override
    public boolean isSameCreature(Creature c) {
        if (!(c instanceof WorldBoss)){
            return false;
        }
        return getName().equals(c.getName());
    }

    @Override
    public int getID(){
        return -ID - 2;//world bosses are mixed in with heroes
    }
    
    
    
    
}
