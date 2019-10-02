/*

 */
package Formations;

import Formations.Elements.Element;
import GUI.CreatureDrawer;
import Skills.Nothing;
import Skills.Skill;
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
    public Skill promote5Skill;
    public Skill promote6Skill;
    
    //max level obtainable by players. level 1000 heroes exist to fight, but
    //cannot be obtained
    public static final int MAX_NORMAL_LEVEL = 99;
    
    public static final int MAX_PROMOTE_LEVEL = 6;
    
    
    
    public static enum Rarity{COMMON,RARE,LEGENDARY,ASCENDED};
    
    
    
    protected Hero(){
        //used for copying
    }

    protected Hero(Element element, int baseAtt, int baseHP, Rarity rarity, int ID, Skill skill, int promote1HP, int promote2Att, int promote4Stats, Skill promote5Skill, Skill promote6Skill){
        super(element,baseAtt,baseHP);
        this.skill = skill;
        this.rarity = rarity;
        lvl1Att = baseAtt;
        lvl1HP = baseHP;
        this.ID = ID;
        this.promote1HP = promote1HP;
        this.promote2Att = promote2Att;
        this.promote4Stats = promote4Stats;
        this.promote5Skill = promote5Skill;
        this.promote6Skill = promote6Skill;
        //hero is leveled initially in creature factory, others leveled in getCopy()
    }
    
    @Override
    public void attatchSkill() {
        skill.setOwner(this);
        promote5Skill.setOwner(this);
        promote6Skill.setOwner(this);
    }
    
    @Override
    public void restore(){
        super.restore();
        promote5Skill.restore();
    }
    
    @Override
    public Creature getCopy() {
        Hero hero = new Hero();
        
        hero.element = element;
        hero.skill = skill.getCopyForNewOwner(hero);
        hero.runeSkill = runeSkill.getCopyForNewOwner(hero);
        hero.rarity = rarity;
        hero.lvl1Att = lvl1Att;
        hero.lvl1HP = lvl1HP;
        hero.promote1HP = promote1HP;
        hero.promote2Att = promote2Att;
        hero.promote4Stats = promote4Stats;
        hero.promote5Skill = promote5Skill.getCopyForNewOwner(hero);
        if (promote6Skill != null)//*********temperary
            hero.promote6Skill = promote6Skill.getCopyForNewOwner(hero);
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
    public Skill getMainSkill(){
        if (promoteLevel >= 5){
            return promote5Skill;
        }
        else{
            return skill;
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
    
    //**************************************************************************
    // p6 skills
    //**************************************************************************
    
    
    @Override
    public void attack(Formation thisFormation,Formation enemyFormation){
        super.attack(thisFormation, enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.postAttackAction(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public int viability(){
        int num = getMainSkill().viability() + runeSkill.viability() - getBaseHP()*getBaseAtt();
        if (promoteLevel >= 6){
            num += promote6Skill.viability() - baseHP*baseAtt;
        }
        return num;
    }
    
    @Override
    public void takeAOEDamage(double damage, Formation formation) {
        double newDamage = getMainSkill().alterAOEDamage(damage,formation);//runes?
        if (promoteLevel >= 6){
            newDamage = promote6Skill.alterAOEDamage(newDamage, formation);
        }
        
        if (newDamage > 1){
            newDamage = Math.floor(newDamage);
        }
        else{
            newDamage = Math.ceil(newDamage);
        }
        changeHP(-newDamage,formation);

    }
    
    @Override
    public boolean shouldTakePercentExecute(double percent) {//runes?
        if (promoteLevel < 6){
            return getMainSkill().shouldTakePercentExecute(percent);
        }
        else{
            return getMainSkill().shouldTakePercentExecute(percent) && promote6Skill.shouldTakePercentExecute(percent);
        }
    }
    
    @Override
    public double determineDamageDealt(Creature target, Formation thisFormation, Formation enemyFormation){//runes?
        double damage = attWithBoosts() + getMainSkill().extraDamage(enemyFormation,thisFormation);
        
        if (promoteLevel >= 6){
            damage += promote6Skill.extraDamage(enemyFormation,thisFormation);
        }
        
        damage = Elements.damageFromElement(this,damage,target.element);
        damage = target.hitAfterDefend(this,enemyFormation,thisFormation,damage);
        
        if (damage < 0){
            damage = 0;
        }
        return damage;
    }
    
    @Override
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        getMainSkill().prepareForFight(thisFormation, enemyFormation);
        runeSkill.prepareForFight(thisFormation, enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.prepareForFight(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {//runes?
        getMainSkill().startOfFightAction(thisFormation, enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.startOfFightAction(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {//runes?
        getMainSkill().startOfFightAction2(thisFormation, enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.startOfFightAction2(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {//runes?
        getMainSkill().preRoundAction(thisFormation,enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.preRoundAction(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){//addative or multiplicitive?
        double newDamage = damage * getArmorPercent() - getArmor();
        newDamage = getMainSkill().hitAfterDefend(attacker,thisFormation,enemyFormation,newDamage);
        if (promoteLevel >= 6){
            newDamage = promote6Skill.hitAfterDefend(attacker,thisFormation,enemyFormation,newDamage);
        }
        newDamage = runeSkill.hitAfterDefend(attacker, thisFormation, enemyFormation, newDamage);
        return newDamage;
    }
    
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation) {//runes?
        super.recordDamageTaken(damage, thisFormation, enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.recordDamageTaken(damage,thisFormation,enemyFormation);
        }
    }
    
    public void recordDamageDealt(long damage, Formation thisFormation, Formation enemyFormation){//runes?
        super.recordDamageDealt(damage, thisFormation, enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.recordDamageDealt(damage,thisFormation,enemyFormation);
        }
    }
    
    @Override
    public void postRoundAction0(Formation thisFormation, Formation enemyFormation) {//runes?
        getMainSkill().postRoundAction0(thisFormation,enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.postRoundAction0(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//runes?
        getMainSkill().postRoundAction(thisFormation,enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.postRoundAction(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        getMainSkill().postRoundAction2(thisFormation,enemyFormation);
        runeSkill.postRoundAction2(thisFormation,enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.postRoundAction2(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public void postRoundAction3(Formation thisFormation, Formation enemyFormation) {
        getMainSkill().postRoundAction3(thisFormation,enemyFormation);
        runeSkill.postRoundAction3(thisFormation,enemyFormation);
        if (promoteLevel >= 6){
            promote6Skill.postRoundAction3(thisFormation, enemyFormation);
        }
    }
    
    @Override
    public void actionOnDeath(Formation thisFormation, Formation enemyFormation) {//runes?
        if (!performedDeathAction){//change for revive?
            getMainSkill().deathAction(thisFormation, enemyFormation);
            if (promoteLevel >= 6){
                promote6Skill.deathAction(thisFormation, enemyFormation);
            }
            performedDeathAction = true;
        }
    }
    
    //**************************************************************************
    //  end p6 skills
    //**************************************************************************
    
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
        if (getMainSkill() instanceof Nothing && !showPromote6Text()){
            return "<html>" + getName() + "</html>";
        }
        if (getMainSkill() instanceof Nothing && showPromote6Text()){
            return "<html>" + getName() + "<br>" + promote6Skill.getDescription() + "</html>";
        }
        if (!(getMainSkill() instanceof Nothing) && !showPromote6Text()){
            return "<html>" + getName() + "<br>" + getMainSkill().getDescription() + "</html>";
        }
        //if (!(getMainSkill() instanceof Nothing) && showPromote6Text()){
            return "<html>" + getName() + "<br>" + getMainSkill().getDescription() + "<br>" + promote6Skill.getDescription() + "</html>";
        //}
    }
    
    private boolean showPromote6Text(){
        return !(promote6Skill instanceof Nothing) && promoteLevel >= 6;
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
