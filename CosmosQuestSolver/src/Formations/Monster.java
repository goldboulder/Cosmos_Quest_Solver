/*

 */
package Formations;

import Formations.Elements.Element;
import GUI.CreatureDrawer;
import Skills.Nothing;
import Skills.Skill;
import cosmosquestsolver.OtherThings;
import java.awt.Graphics;


public class Monster extends Creature {

    public static final int TOTAL_UNIQUE_TIERS = 15;
    public static final String[] REPEAT_TIER_STRING = new String[]{"","Furious ","Raging "};
    public static final int TOTAL_TIERS = TOTAL_UNIQUE_TIERS * REPEAT_TIER_STRING.length;
    
    private long followers;// number of followers needed to use the monster
    private int tier;
    
    protected Monster(Element element, int baseAtt, int baseHP, int tier, long followers, Skill skill){
        super(element,baseAtt,baseHP);
        this.followers = followers;
        this.skill = skill;
        this.tier = tier;
    }

    @Override
    public Monster getCopy() {
        Monster m = new Monster(element,baseAtt,baseHP,tier,followers,skill);
        m.setID(ID);
        m.currentAtt = currentAtt;
        m.currentHP = currentHP;
        m.skill = skill.getCopyForNewOwner(m);
        m.runeSkill = runeSkill.getCopyForNewOwner(m);
        return m;
    }
    
    public int getTier(){
        return tier;
    }
    
    @Override
    public String getNickName() {
        return Character.toLowerCase(Elements.getElementChar(element)) + "" + getTier();
    }
    
    public static boolean validTier(int tier){
        return (tier > 0 && tier <= TOTAL_TIERS);
    }
    
    public static int getNumTimesRepeat(){
        return REPEAT_TIER_STRING.length;
    }
    
    // monsters' id is determined by their tier and element, so
    // they don't need to store an ID field
    @Override
    public int getID(){
        int elementNum = 0;
        switch(element){
            case AIR: elementNum = 0;break;
            case EARTH: elementNum = 1;break;
            case FIRE: elementNum = 2;break;
            case WATER: elementNum = 3;break;
        }
        return (tier-1) * Elements.MONSTER_ELEMENTS.length + elementNum;
    }
    
    @Override
    public long getFollowers(){
        return followers;
    }
    
    @Override
    public String getImageAddress() {
        int strIndex = (tier-1)/TOTAL_UNIQUE_TIERS;
        
        return "Creatures/Monsters/" + getName().substring(REPEAT_TIER_STRING[strIndex].length());//remove prefix
        
    }
    
    @Override
    public String toolTipText() {
        if (skill instanceof Nothing){
            return "<html>" + getName() + "<br>Tier: " + tier + "<br>Followers: " + OtherThings.intToCommaString(followers) + "</html>";
        }
        else{
            return "<html>" + getName() + "<br>Tier: " + tier + "<br>" + skill.getDescription() + "<br>Followers: " + OtherThings.intToCommaString(followers) + "</html>";
        }
    }
    
    public int sortingValue(){
        return element.ordinal()*TOTAL_TIERS + tier;
    }
    
    @Override
    public Hero.Rarity getRarity() {
        return null;
    }

    @Override
    public void draw(Graphics g) {
        CreatureDrawer.drawCreature(this, g);
    }

    
    
}
