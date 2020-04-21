/*

 */
package Formations;

import Formations.Elements.Element;
import Skills.Skill;
import java.awt.Graphics;

//a creature is any unit that can fight. Fundamentally, they have
// HP, attack power, and a special ability. Includes monsters, heroes,
//and world bosses.
public abstract class Creature implements Comparable<Creature>{//refresh instead of copy for performance improvement? textbox to input lineup for quest? put in enemyFormationMakerPanel
    
    protected Element element;
    protected int baseHP;//hp the creatue starts off with
    protected int maxHP;//cannot heal more than this
    protected int baseAtt;//attack the creature starts off with
    protected int attConstantBoost = 0;
    protected double attPercentBoost = 1;
    protected int armorConstant = 0;//subtracts damage
    protected double armorPercent = 1;//1 = take normalDamage
    protected double attConstantBoostEffectiveness = 1;
    protected double armorConstantEffectiveness = 1;
    protected double healEffectiveness = 1;
    protected int currentHP;
    protected long currentAtt;
    protected Skill skill;
    protected Skill runeSkill;
    
    protected boolean facingRight = true;//for GUI. put in GUI class instead?
    protected int ID;//for quicker copying. Copying names of heroes was not nessesary for fights. Names can be accesed through a method in CreatureFactory.
    //also used for rng skills
    
    protected boolean performedDeathAction = false;//delete if the revive skill does not make the on-death skill work a second time

    

    
    //public static enum Elements {AIR,WATER,EARTH,FIRE}//should this and ELEMENT_DAMAGE_BOOST be in its own class?
    //public static final double ELEMENT_DAMAGE_BOOST = 1.5;
    
    protected Creature(){
        //used for copying
    }
    
    protected Creature(Element element, int baseAtt, int baseHP){
        this.element = element;
        this.baseHP = baseHP;
        this.maxHP = baseHP;
        this.currentHP = baseHP;
        this.baseAtt = baseAtt;
        this.currentAtt = baseAtt;
    }
    
    public void restore(){
        maxHP = baseHP;
        attConstantBoost = 0;
        attPercentBoost = 1;
        armorConstant = 0;
        armorPercent = 1;
        attConstantBoostEffectiveness = 1;
        armorConstantEffectiveness = 1;
        healEffectiveness = 1;
        currentHP = baseHP;
        currentAtt = baseAtt;
        skill.restore();
        runeSkill.restore();
        performedDeathAction = false;
    }
    public abstract Creature getCopy();
    public abstract Hero.Rarity getRarity();// only used by heroes, defensive programming: monsters might have abilities in the future
    public abstract long getFollowers();
    public abstract void draw(Graphics g);
    public abstract String getImageAddress();
    public abstract String toolTipText();
    
    public String getName() {
        return CreatureFactory.getCreatureName(getID());
    }
    
    public String getNickName() {
        return CreatureFactory.getCreatureNickName(getID());
    }
    
    public int getID(){
        return ID;
    }
    
    public int getRawID(){
        return ID;
    }
    
    public void setID(int ID){
        this.ID = ID;
    }
    
    public Skill getMainSkill(){
        return skill;
    }
    
    
    public Skill getRuneSkill(){
        return runeSkill;
    }
    
    public void setRuneSkill(Skill runeSkill){
        this.runeSkill = runeSkill;
    }
    
    
    public void attatchSkill() {
        skill.setOwner(this);
    }
    
    public int getAttConstantBoost(){
        return (int)(attConstantBoost * attConstantBoostEffectiveness);
    }
    
    public double getAttPercentBoost(){
        return attPercentBoost;
    }
    
    public double attWithBoosts(){
        return (currentAtt + attConstantBoost * attConstantBoostEffectiveness) * attPercentBoost;
    }
    
    public double hitAfterDefend(Creature attacker, Formation thisFormation, Formation enemyFormation, double damage){
        double newDamage = damage * getArmorPercent() - getArmor();//armor last?
        newDamage = getMainSkill().hitAfterDefend(attacker,thisFormation,enemyFormation,newDamage);
        newDamage = getRuneSkill().hitAfterDefend(attacker, thisFormation, enemyFormation, newDamage);
        return newDamage;
    }
    
    public int getArmor(){
        return (int)(armorConstant * armorConstantEffectiveness);
    }
    
    public double getArmorPercent(){
        return armorPercent;
    }
    
    public boolean isFacingRight(){
        return facingRight;
    }
    
    public void setFacingRight(boolean facingRight){
        this.facingRight = facingRight;
    }
    
    public int getBaseHP() {
        return baseHP;
    }
    
    public int getMaxHP() {
        return maxHP;
    }
    
    public int getCurrentHP() {
        return currentHP;
    }
    
    public Element getElement() {
        return element;
    }
    
    public long getCurrentAtt() {
        return currentAtt;
    }
    
    public int getBaseAtt() {
        return baseAtt;
    }
    
    public void setBaseAtt(int att){
        baseAtt = att;
    }
    
    public void setBaseHP(int hp){
        baseHP = hp;
    }
    
    public void setMaxHP(int hp){
        maxHP = hp;
    }
    
    public void setCurrentAtt(long att) {
        currentAtt = att;
    }

    public void setCurrentHP(int HP) {
        currentHP = HP;
    }
    
    public void addAttConstantBoost(int a) {
        attConstantBoost += a;
    }
    
    public void addAttPercentBoost(double a){//add or multiply?** example input would be 1.15
        attPercentBoost += a;
        //System.out.println(a + ", attPercentBoost now " + attPercentBoost);
    }
    
    
    public void addArmor(int a) {
        armorConstant += a;
    }
    
    public void addArmorPercentBoost(double a){
        armorPercent *= a;//p6 skill?
    }
    
    public void resetBoosts(){
        attConstantBoost = 0;
        attPercentBoost = 1;
        armorConstant = 0;
        armorPercent = 1;
    }
    
    public void addArmorConstantEffectivness(double e){
        armorConstantEffectiveness += e;
    }
    
    public void addAttConstantBoostEffectiveness(double e){
        attConstantBoostEffectiveness += e;
    }
    
    public void addHealEffectivness(double e){
        healEffectiveness += e;
    }
    
    public double getArmorConstantEffectivness(){
        return armorConstantEffectiveness;
    }
    
    public double getAttConstantBoostEffectivness(){
        return attConstantBoostEffectiveness;
    }
    
    public double getHealEffectivness(){
        return healEffectiveness;
    }
    
    public char getElementChar(){
        return Elements.getElementChar(element);
    }
    
    public void attack(Formation thisFormation,Formation enemyFormation){
        enemyFormation.takeHit(this,thisFormation,getMainSkill().chooseTarget(thisFormation,enemyFormation));//rune skill unlikely to matter here
        //getMainSkill().attack(thisFormation,enemyFormation);
        getMainSkill().postAttackAction(thisFormation, enemyFormation);
        runeSkill.postAttackAction(thisFormation, enemyFormation);
    }
    
    public boolean isDead(){
        return currentHP <= 0;
    }
    
    // the game's interpretation of creature strength. does not take into account
    // special abilities
    public int strength(){
        return (int) Math.ceil(Math.pow((baseAtt*baseHP), 1.5));
    }
    
    // a somewhat more accurate measurement of a creature's viability in battle
    public int viability(){
       // System.out.println(getName());
       // if (getName().equals("Arshen")){
        //    System.out.println(getMainSkill());
        //}
        return getMainSkill().viability() + runeSkill.viability() - baseHP*baseAtt;
        
    }
    

    
    
    //actually changes HP directly. Positive numbers means healing
    public void changeHP(double damage, Formation thisFormation){
        
        int num;//Geum?
        
        if (damage < 0){
            num = (int)Math.round(damage-0.0001);
            thisFormation.addDamageTaken(-(long)Math.round(damage-0.0001));
        }
        else{
            num = (int) Math.ceil(damage);
        }
        
        if (num < 0 || currentHP != 0){//once dead, cannot be revived. does damage still count torawds total?
            currentHP += num;
        }
        
        if (currentHP < 0){
            currentHP = 0;
        }
        if (currentHP > maxHP){
            currentHP = maxHP;
        }
    }
    
    //for fairies
    public void changeHPNoDeathCheck(double damage, Formation thisFormation){
        
        int num;//Geum?
        if (damage < 0){//round away from 0
            num = (int)Math.floor(damage);
            thisFormation.addDamageTaken(-(long)Math.floor(damage));
        }
        else{
            num = (int) Math.ceil(damage);
        }
        
        
        currentHP += num;
        if (currentHP < 0){
            currentHP = 0;
        }
        if (currentHP > maxHP){
            currentHP = maxHP;
        }
    }
    
    
    public void takeAOEDamage(double damage, Formation formation) {//modify for runes?
        double newDamage = getMainSkill().alterAOEDamage(damage,formation);
        
        if (newDamage > 1){
            newDamage = Math.floor(newDamage);
        }
        else{
            newDamage = Math.ceil(newDamage);
        }
        changeHP(-newDamage,formation);

    }
    
    public boolean shouldTakePercentExecute(double percent) {//modify for runes?
        return getMainSkill().shouldTakePercentExecute(percent);
    }
    
    public boolean shouldTakeConstantExecute(int hp) {//modify for runes?
        return getMainSkill().shouldTakeConstantExecute(hp);
    }
    
    public void takeExecute(Creature attacker, Formation thisFormation, Formation enemyFormation, long enemyHPBefore) {
        takeHit(attacker, thisFormation, enemyFormation, getCurrentHP()+1);
        //recordDamageTaken(enemyHPBefore + 1,thisFormation,enemyFormation);
    }
    
    public double determineDamageDealt(Creature target, Formation thisFormation, Formation enemyFormation){//modify for runes?
        double damage = attWithBoosts() + getMainSkill().extraDamage(enemyFormation,thisFormation);
        damage *= runeSkill.moreDamage(thisFormation, enemyFormation);
        damage = Elements.damageFromElement(this,damage,target.element);//percent damage reduction here?
        damage = target.hitAfterDefend(this,enemyFormation,thisFormation,damage);
        
        if (damage < 0){
            damage = 0;
        }
        return damage;
    }
    
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        getMainSkill().prepareForFight(thisFormation, enemyFormation);
        runeSkill.prepareForFight(thisFormation, enemyFormation);
    }
    
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {//modify for runes?
        getMainSkill().startOfFightAction(thisFormation, enemyFormation);
    }
    
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {//modify for runes?
        getMainSkill().startOfFightAction2(thisFormation, enemyFormation);
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {//modify for runes?
        getMainSkill().preRoundAction(thisFormation,enemyFormation);
    }
    
    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {//future skill?
        //getMainSkill().takeHit(attacker, thisFormation, enemyFormation, hit);
        
        if (hit < 0){
            hit = 0;
        }
        
        long longHit = (long)Math.round(hit);
        recordDamageTaken(longHit,thisFormation,enemyFormation);
        attacker.recordDamageDealt(longHit,thisFormation,enemyFormation);
        changeHP(-hit,thisFormation);
    }
    
    public void takeHeal(double amount, Formation thisFormation) {
        changeHP(Math.round(amount * healEffectiveness), thisFormation);
    }
    
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation) {//modify for runes?
        getMainSkill().recordDamageTaken(damage,thisFormation,enemyFormation);
    }
    
    public void recordDamageDealt(long damage, Formation thisFormation, Formation enemyFormation){//modify for runes?
        getMainSkill().recordDamageDealt(damage,thisFormation,enemyFormation);
    }
    
    public void postRoundAction0(Formation thisFormation, Formation enemyFormation) {
        getMainSkill().postRoundAction0(thisFormation,enemyFormation);//modify for runes?
    }

    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//AOE takes effect even when dead
        getMainSkill().postRoundAction(thisFormation,enemyFormation);//modify for runes?
    }
    
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        getMainSkill().postRoundAction2(thisFormation,enemyFormation);
        runeSkill.postRoundAction2(thisFormation, enemyFormation);
    }
    
    public void postRoundAction3(Formation thisFormation, Formation enemyFormation) {
        getMainSkill().postRoundAction3(thisFormation,enemyFormation);
        runeSkill.postRoundAction3(thisFormation, enemyFormation);
    }
    
    public void actionOnDeath(Formation thisFormation, Formation enemyFormation) {//is this needed? put in each skill class?
        if (!performedDeathAction){//change for revive?
            getMainSkill().deathAction(thisFormation, enemyFormation);
            performedDeathAction = true;
        }
    }
    
    @Override
    public int compareTo(Creature c) {
        return c.getID() - getID();
    }
    
    public boolean isSameCreature(Creature c){
        return (c.getClass() == getClass() && c.getID() == getID());
    }
    
    public String getFormationText(){
        return getNickName();
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":\tAtt: ");
        sb.append(currentAtt).append("\tHP: ");
        sb.append(currentHP).append("\tElement: ");
        sb.append(element);
        
        return sb.toString();
    }
    
    
    //having these useless methods for monsters avoid having to cast for special abiilities
    public void levelUp(int level) {
        
    }
    public int getPromoteLevel(){
        return 0;
    }
    public int getPromote1HP(){
        return 0;
    }
    public int getPromote2Att(){
        return 0;
    }
    public int getPromote4Stats(){
        return 0;
    }
    public int getLevel() {
        return 0;
    }
    public int getLvl1Att() {
        return baseAtt;
    }
    public int getLvl1HP() {
        return baseHP;
    }

    

    

    

    

    

    

    
    
}
