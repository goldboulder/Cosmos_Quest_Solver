/*

 */
package Formations;

import Formations.Elements.Element;
import GUI.CreatureDrawer;
import SpecialAbilities.Nothing;
import SpecialAbilities.SpecialAbility;
import java.awt.Graphics;


public class Hero extends Creature{

    
    
    private int lvl1Att;
    private int lvl1HP;

    private Rarity rarity;
    private int level = 1;
    
    private int promoteLevel = 0;
    private int promote1HP = 0;
    private int promote2Att = 0;
    private int promote4Stats = 0;
    public SpecialAbility promote5Ability;
    //public SpecialAbility promote6Ability;
    
    //max level obtainable by players. level 1000 heroes exist to fight, but
    //cannot be obtained
    public static final int MAX_NORMAL_LEVEL = 99;
    
    public static final int MAX_PROMOTE_LEVEL = 5;
    
    
    
    public static enum Rarity{COMMON,RARE,LEGENDARY,ASCENDED};
    
    
    
    protected Hero(){
        //used for copying
    }

    protected Hero(Element element, int baseAtt, int baseHP, Rarity rarity, int ID, SpecialAbility specialAbility, int promote1HP, int promote2Att, int promote4Stats, SpecialAbility promote5Ability){
        super(element,baseAtt,baseHP);
        this.specialAbility = specialAbility;
        this.rarity = rarity;
        lvl1Att = baseAtt;
        lvl1HP = baseHP;
        this.ID = ID;
        this.promote1HP = promote1HP;
        this.promote2Att = promote2Att;
        this.promote4Stats = promote4Stats;
        this.promote5Ability = promote5Ability;
        //hero is leveled initially in creature factory, others leveled in getCopy()
    }
    
    @Override
    public void attatchSpecialAbility() {
        specialAbility.setOwner(this);
        promote5Ability.setOwner(this);
    }
    
    @Override
    public void restore(){
        super.restore();
        promote5Ability.restore();
    }
    
    @Override
    public Creature getCopy() {
        Hero hero = new Hero();
        
        hero.element = element;
        hero.specialAbility = specialAbility.getCopyForNewOwner(hero);
        hero.rarity = rarity;
        hero.lvl1Att = lvl1Att;
        hero.lvl1HP = lvl1HP;
        hero.promote1HP = promote1HP;
        hero.promote2Att = promote2Att;
        hero.promote4Stats = promote4Stats;
        hero.promote5Ability = promote5Ability.getCopyForNewOwner(hero);
        hero.levelUp(level);
        hero.promote(promoteLevel);
        hero.currentAtt = currentAtt;//for sandbox
        hero.currentHP = currentHP;
        hero.baseHP = baseHP;
        hero.maxHP = maxHP;
        hero.baseAtt = baseAtt;
        
        hero.ID = ID;
        
        return hero;
    }
    
    @Override
    public int getID(){
        return -ID - 2;//hero IDs are <= -2
    }
    
    
    
    
    @Override
    public void levelUp(int level) {
        this.level = level;
        this.baseHP = HPForLevel();
        this.baseAtt = attForLevel();
        currentHP = baseHP;//heroes will never level up mid-battle
        maxHP = baseHP;
        currentAtt = baseAtt;
    }
    
    
    
    public void promote(int level){
        promoteLevel = level;
        levelUp(this.level);
        
    }
    
    @Override
    public int getPromoteLevel(){
        return promoteLevel;
    }
    
    @Override
    public int getPromote1HP(){
        return promote1HP;
    }
    
    @Override
    public int getPromote2Att(){
        return promote2Att;
    }
    
    @Override
    public int getPromote4Stats(){
        return promote4Stats;
    }
    
    @Override
    public SpecialAbility getSpecialAbility(){
        if (promoteLevel == MAX_PROMOTE_LEVEL){
            return promote5Ability;
        }
        else{
            return specialAbility;
        }
    }
    
    //possible levels in the game currently
    public static boolean validHeroLevel(int level) {
        return ((level > 0 && level <= MAX_NORMAL_LEVEL) || level == 1000);
    }
    
    public static boolean validPromoteLevel(int promoteLevel){
        return (promoteLevel >= 0 && promoteLevel <= MAX_PROMOTE_LEVEL);
    }
    
    @Override
    public Rarity getRarity() {
        return rarity;
    }
    
    @Override
    public int getLevel() {
        return level;
    }
    
    public int HPForLevel(){
        int hp = levelStat(level,rarity,lvl1HP,lvl1Att,promoteLevel);
        
        if (promoteLevel >= 1){
            hp += promote1HP;
        }
        if (promoteLevel >= 4){
            hp += promote4Stats;
        }
        
        return hp;
    }
    
    public int attForLevel(){
        int att = levelStat(level,rarity,lvl1Att,lvl1HP,promoteLevel);
        
        if (promoteLevel >= 2){
            att += promote2Att;
        }
        if (promoteLevel >= 4){
            att += promote4Stats;
        }
        
        return att;
    }
    
    public int levelStat(int level, Rarity rarity, int statWanted, int otherStat, int promoteLevel){
        double boost = ((rarityStatBoost() + promotionLevelStatBoost())*statWanted*(level-1)) / (double)(otherStat+statWanted);
        int intBoost = (int) (boost + 0.5);
        return statWanted + intBoost;
    }
    
    //determines how much stats a hero gains per level depending on its rarity
    public int rarityStatBoost(){
        switch(rarity){
            case COMMON: return 1;
            case RARE: return 2;
            case LEGENDARY: return 6;
            case ASCENDED: return 12;
            default: return 0;
        }
        
    }
    
    public int promotionLevelStatBoost(){
        if (promoteLevel >= 3){
            switch(rarity){
                case COMMON: return 1;
                case RARE: return 2;
                case LEGENDARY: return 3;
                case ASCENDED: return 4;
                default: return 0;
            }
        }
        else{
            return 0;
        }
    }
    
    public void setID(int ID){
        this.ID = ID;
    }
    
    @Override
    public int getLvl1Att() {
        return lvl1Att;
    }

    @Override
    public int getLvl1HP() {
        return lvl1HP;
    }
    
    @Override
    public long getFollowers() {
        return 0;
    }
    
    @Override
    public void draw(Graphics g) {
        CreatureDrawer.drawCreature(this, g);
    }
    
    @Override
    public String getImageAddress() {
        return "Creatures/Heroes/" + getName();
    }
    
    @Override
    public String toolTipText() {
        if (getSpecialAbility() instanceof Nothing){
            return "<html>" + getName() + "</html>";
        }
        return "<html>" + getName() + "<br>" + getSpecialAbility().getDescription() + "</html>";
    }
    
    @Override
    public String getFormationText(){
        StringBuilder sb = new StringBuilder(getNickName() + ":");
        
        if (level == 1000){
            sb.append("1k");
        }
        else{
            sb.append(level);
        }
            
        
        if (promoteLevel != 0){
            sb.append(".").append(promoteLevel);
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\tAtt: ");
        sb.append(currentAtt).append("\tHP: ");
        sb.append(currentHP).append("\tElement: ");
        sb.append(element).append("\tLevel: ");
        sb.append(level).append("\tPromoteLevel: ");
        sb.append(promoteLevel).append("\tID: ");
        sb.append(ID);
        
        return sb.toString();
    }
    
}
