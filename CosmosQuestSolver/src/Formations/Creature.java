/*

 */
package Formations;

import Formations.Elements.Element;
import SpecialAbilities.SpecialAbility;
import java.awt.Graphics;

//a creature is any unit that can fight. Fundamentally, they have
// HP, attack power, and a special ability. Including monsters, heroes,
//and world bosses.
public abstract class Creature implements Comparable<Creature>{
    
    protected Element element;
    protected int baseHP;//hp the creatue starts off with
    protected int maxHP;//cannot heal more than this
    protected int baseAtt;//attack the creature starts off with
    protected int attBoost = 0;
    protected int armor = 0;
    protected int currentHP;
    protected long currentAtt;
    public SpecialAbility specialAbility;
    protected boolean facingRight = true;//for GUI. put in GUI class instead?
    protected int ID;//for quicker copying. Copying names of heroes was not nessesary for fights. Names can be accesed through a method in CreatureFactory.
    //also used for rng skills
    
    protected boolean performedDeathAction = false;//put in specialAbility?

    

    
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
    
    public SpecialAbility getSpecialAbility(){
        return specialAbility;
    }
    
    public void attatchSpecialAbility() {
        specialAbility.setOwner(this);
    }
    
    public int getAttBoost(){
        return attBoost;
    }
    
    public int getArmor(){
        return armor;
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
    
    public void addAttBoost(int a){
        attBoost += a;
    }
    
    public void addArmorBoost(int a){
        armor += a;
    }
    
    public void resetBoosts(){
        attBoost = 0;
        armor = 0;
    }
    
    public char getElementChar(){
        return Elements.getElementChar(element);
    }
    
    public void attack(Formation thisFormation,Formation enemyFormation){
        getSpecialAbility().attack(thisFormation,enemyFormation);
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
        //    System.out.println(getSpecialAbility());
        //}
        return getSpecialAbility().viability();
        
    }
    

    
    
    //actually changes HP directly. Positive numbers means healing
    public void changeHP(double damage, Formation thisFormation){
        if (currentHP == 0){
            return;//once dead, cannot be revived. does damage still count torawds total?
        }
        
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
    
    
    public void takeAOEDamage(double damage, Formation formation) {//move to specialAbility for Thumper?
        double newDamage = getSpecialAbility().alterAOEDamage(damage,formation);
        
        if (newDamage > 1){
            newDamage = Math.floor(newDamage);
        }
        else{
            newDamage = Math.ceil(newDamage);
        }
        changeHP(-newDamage,formation);

    }
    
    public double determineDamage(Creature target, Formation thisFormation, Formation enemyFormation){
        double damage = currentAtt + attBoost + getSpecialAbility().extraDamage(enemyFormation,thisFormation);
        damage = Elements.damageFromElement(this,damage,target.element) - target.getArmor();
        
        if (damage < 0){
            damage = 0;
        }
        return damage;
    }
    
    public void prepareForFight(Formation thisFormation, Formation enemyFormation){
        getSpecialAbility().prepareForFight(thisFormation, enemyFormation);
    }
    
    public void startOfFightAction(Formation thisFormation, Formation enemyFormation) {
        getSpecialAbility().startOfFightAction(thisFormation, enemyFormation);
    }
    
    public void startOfFightAction2(Formation thisFormation, Formation enemyFormation) {
        getSpecialAbility().startOfFightAction2(thisFormation, enemyFormation);
    }
    
    public void preRoundAction(Formation thisFormation, Formation enemyFormation) {//reseting buffs done elsewhere
        getSpecialAbility().preRoundAction(thisFormation,enemyFormation);
    }
    
    public void takeHit(Creature attacker,  Formation thisFormation, Formation enemyFormation, double hit) {//future special ability?
        getSpecialAbility().takeHit(attacker, thisFormation, enemyFormation, hit);
    }
    
    public void recordDamageTaken(long damage, Formation thisFormation, Formation enemyFormation) {
        getSpecialAbility().recordDamageTaken(damage,thisFormation,enemyFormation);
    }

    public void postRoundAction(Formation thisFormation, Formation enemyFormation) {//AOE takes effect even when dead
        getSpecialAbility().postRoundAction(thisFormation,enemyFormation);
    }
    
    public void postRoundAction2(Formation thisFormation, Formation enemyFormation) {
        getSpecialAbility().postRoundAction2(thisFormation,enemyFormation);
    }
    
    public void actionOnDeath(Formation thisFormation, Formation enemyFormation) {//is this needed? put in each specialAbility class?
        if (!performedDeathAction){
            getSpecialAbility().deathAction(thisFormation, enemyFormation);
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
