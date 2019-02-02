/*

 */
package Formations;

import SpecialAbilities.BloodBomb;
import SpecialAbilities.CriticalHit;
import SpecialAbilities.Ricochet;
import SpecialAbilities.ScaleableAOE;
import SpecialAbilities.ScaleableStartingDamage;
import SpecialAbilities.SpecialAbility;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

//holds a list of creatures representing a battle line. Also simulates battles
public class Formation implements Iterable<Creature>{

    

    
    
    private long totalDamageTaken = 0;// for tiebreakers in pvp
    public static final int MAX_MEMBERS = 6;//current max creatures possible in one line
    private LinkedList<Creature> members;
    private double AOEResistance = 0;//1 is invincible
    
    //in rare circumstances, armor might outweigh attack power, resulting in an
    //infinite loop. After a set amount of rounds, end the battle
    public static final int STALEMATE_CUTOFF_POINT = 100;
    //private long seed = -1;//used for random skills. newSeed should be positive. if newSeed is needed, generate it
    

    public void removeMonsters() {
        for (int i = 0; i < members.size(); i ++){
            if (members.get(i) instanceof Monster){
                members.remove(i);
                i --;
            }
        }
    }

    public boolean containsLepHeroes() {
        for (int i = 0; i < members.size(); i ++){
            SpecialAbility s = members.get(i).getSpecialAbility();
            if (s instanceof Ricochet || s instanceof ScaleableAOE || s instanceof ScaleableStartingDamage || s instanceof BloodBomb){
                return true;
            }
        }
        return false;
    }

    //{"setup":[-103,-67,-102,-122,-71,-120],"shero":{"120":88,"118":99,"101":99,"100":99,"69":99,"65":99},"player":[-28,119,117,117,118],"phero":{"26":1000}}
    public String idStr() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        for (int i = MAX_MEMBERS - 1; i >= 0; i--){
            try{
                sb.append(members.get(i).getID());
            }
            catch(IndexOutOfBoundsException e){
                sb.append(-1);
            }
            
            if (i != 0){
                sb.append(",");
            }
        }
        
        sb.append("]");
        return sb.toString();
    }

    public String heroLevelStr() {
        List<Creature> sortedHeroList = getSortedHeroList();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        //set up hash map string for hero levels. The game still has legacy code
        //for world bosses that allows them to be leveled, but those levels don't matter
        for (int i = sortedHeroList.size()-1; i >= 0; i--){
            if (sortedHeroList.get(i) instanceof Hero){
                Hero h = (Hero) sortedHeroList.get(i);
                sb.append("\"").append(sortedHeroList.get(i).getRawID()).append("\":").append(h.getLevel());
            }
            else if (sortedHeroList.get(i) instanceof WorldBoss){
                sb.append("\"").append(sortedHeroList.get(i).getRawID()).append("\":1");
            }
            
            if (i != 0){
                sb.append(",");
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    public String heroPromoStr() {
        List<Creature> sortedHeroList = getSortedHeroList();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        //set up hash map string for hero promotions.
        for (int i = sortedHeroList.size()-1; i >= 0; i--){
            if (sortedHeroList.get(i) instanceof Hero){
                Hero h = (Hero) sortedHeroList.get(i);
                sb.append("\"").append(sortedHeroList.get(i).getRawID()).append("\":").append(h.getPromoteLevel());
            }
            
            if (i != 0){
                sb.append(",");
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    private LinkedList<Creature> getSortedHeroList(){
        LinkedList<Creature> list= new LinkedList<>();
        for(Creature c: members){
            if (c instanceof Hero || c instanceof WorldBoss){
                list.add(c);
            }
        }
        Collections.sort(list, (Creature c1, Creature c2) -> c2.getRawID()-c1.getRawID());
        return list;
    }
    
    //shifts certain heroes to the back or the front of the formation depending on their ability
    public void positionBias() {
        
        HashMap<Creature,Integer> map = new HashMap<>();
        for (int i = 0; i < members.size(); i++){
            int position = (int)(100*(i + 1.1*Math.random()*members.get(i).getSpecialAbility().positionBias()));
            map.put(members.get(i), position);
        }
        Collections.sort(members, (Creature c1, Creature c2) -> map.get(c2)-map.get(c1));
        
    }
    
    //returns a short-hand representation of the units in the formation
    //in the format used by the chat
    public String shortHandText(){
        
        if (members.isEmpty()){
            return "Nothing";
        }
        
        StringBuilder sb = new StringBuilder("");
        /*
        for (int i = 0; i < members.size(); i++){
            sb.append(members.get(i).getFormationText());
            if (i != members.size()-1){
                sb.append(",");
            }
        }
        */
        //reverse order
        for (int i = members.size()-1; i >=0; i--){
            sb.append(members.get(i).getFormationText());
            if (i != 0){
                sb.append(", ");
            }
        }
        
        return sb.toString();
    }

    public boolean isBossFormation() {
        return members.size() == 1 && members.getFirst() instanceof WorldBoss;
    }

    
    
    public static enum VictoryCondition{WIN,DRAW,LOSE};
    
    //empty formation
    public Formation(){
        members = new LinkedList<>();
    }
    
    public Formation(List<Creature> creatures){
        
        members = new LinkedList<>();
        
        for (Creature creature: creatures){
            members.add(creature);
        }
    }
    
    public Formation getCopy() {
        LinkedList list = new LinkedList<>();
        
        for(Creature c : members){
            list.add(c.getCopy());
        }
        Formation f = new Formation(list);
        f.totalDamageTaken = totalDamageTaken;
        f.AOEResistance = AOEResistance;
        return f;
    }
    
    public LinkedList<Creature> getMembers() {
        return members;
    }
    
    /*
    public long getSeed() {
        if (seed == -1){//lazy evaluation
            seed = generateSeed();
        }
        return seed;
    }
    */
    
    public long getSeed(){//recalculates every time
        long newSeed = 1;
        
        for (int i = size()-1; i >= 0; i--){
            newSeed = (newSeed * Math.abs((int)(members.get(i).getID())) +1) % Integer.MAX_VALUE;
        }
        //newSeed = (16807 * newSeed) % Integer.MAX_VALUE;
        //empty slots (id=abs(-1))
        newSeed += (MAX_MEMBERS - size());// battle code replays don't seem to do this step
        //System.out.println(newSeed);
        
        return newSeed;
    }
    
    /*
    // Add monster to the back of the army
        void add(const MonsterIndex m) {
            this->monsters[monsterAmount] = m;
            this->followerCost += monsterReference[m].cost;
            this->monsterAmount++;
            strength += pow(monsterReference[m].hp * monsterReference[m].damage, 1.5);

            // Seed takes into account empty spaces with lane size 6, recalculated each time monster is added
            // Any empty spaces are considered to be contiguous and frontmost as they are in DQ and quests
            int64_t newSeed = 1;
            for (int i = monsterAmount - 1; i >= 0; i--) {
                newSeed = newSeed * abs(monsterReference[monsters[i]].index) + 1;
            }
            // Simplification of loop for empty monsters (id: -1) contiguous and frontmost
            newSeed += 6 - monsterAmount;
            this->seed = newSeed;
        }
    */
    
    //gets a pseudo-random number that skills with random componens each turn can use
    public long getTurnSeed(int turn){
        return getTurnSeed(getSeed(),turn);
        //long enemySeed = otherFormation.getSeed();
        //long ans = (enemySeed + (long)Math.pow(101-turnNumber,3)) % ((long)Math.round((double)enemySeed/(101 - turnNumber) + (101 - turnNumber)*(101 - turnNumber)));
        //return Math.abs(ans);
    }
    
    public static long getTurnSeed(long seed,int turn){
        for (int i = 0; i < turn; ++i){
                seed = (16807 * seed) % Integer.MAX_VALUE;
        }
        return seed;
    }
    
    public void addDamageTaken(long hit) {
        totalDamageTaken += hit;
    }
    
    public long getDamageTaken(){
        return totalDamageTaken;
    }
    
    public void setDamageTaken(long damage) {
        totalDamageTaken = damage;
    }
    
    public void attack(Formation enemyFormation){
        members.getFirst().attack(this,enemyFormation);//only the first creature damages foes directly
    }
    
    public Creature getFrontCreature(){
        try{//is this needed?
            return members.getFirst();
        }
        catch (NoSuchElementException e){
            return null;
        }
    }
    
    public Creature getCreature(int position) {
        return members.get(position);
    }
    
    public void addCreature(Creature c) {
        if (members.size() < MAX_MEMBERS){
            members.add(c);
        }
    }
    
    public Creature getEntry(int i) {
        try{
            return members.get(i);
        }
        catch (Exception e){
            return null;
        }
    }
    
    public boolean isEmpty() {
        return members.isEmpty();
    }
    
    public boolean containsMonsters(){
        for (Creature creature : members){
            if (creature instanceof Monster){
                return true;
            }
        }
        return false;
    }
    
    //for identifying repeat formations in the combination iterator.
    //there can be more than one of each monster, but the combination
    //iterator treats the same type of monsters as different creatures
    //return true if the two foamations have the same combination of creatures
    public boolean containsSameCreaturesForCombination(Formation formation) {
        if (members.size() != formation.members.size()){
            return false;
        }
        LinkedList<Creature> thisList = getMembers();
        LinkedList<Creature> otherList = formation.getMembers();
        
        Collections.sort(thisList, (Creature c1, Creature c2) -> c2.getID()-c1.getID());
        Collections.sort(otherList, (Creature c1, Creature c2) -> c2.getID()-c1.getID());
        for (int i = 0; i < thisList.size(); i++){
            if (!thisList.get(i).isSameCreature(otherList.get(i))){
                return false;
            }
        }
        return true;
        
        
    }
    
    //returns true if the formation contains two or more of the same creature
    public boolean contains(Creature c) {
        for (Creature creature : members){
            if (c.getClass() == creature.getClass() && c.getID() == creature.getID()){
                return true;
            }
        }
        return false;
    }
    
    //returns a list of all monsters in the formation
    public LinkedList<Monster> getMonsters(){
        LinkedList<Monster> list = new LinkedList<>();
        for (Creature creature : members){
            if (creature instanceof Monster){
                Monster m = (Monster)creature;
                list.add(m);
            }
        }
        return list;
    }

    public long getFollowers() {
        long sum = 0;
        for (Creature c : members){
            sum += c.getFollowers();
        }
        return sum;
    }
    
    //returns the game's definition of a formation's strength
    //the sum of each creature's strength
    public int strength(){
        int sum = 0;
        for (Creature creature : members){
            sum += creature.strength();
        }
        return sum;
    }
    
    public void prepareForFight(Formation enemyFormation){
       for (Creature creature : members){
            creature.prepareForFight(this,enemyFormation);
        } 
    }
    
    //activates each creature's special ability's start of fight action
    public void startOfFightAction(Formation enemyFormation){
        for (Creature creature : members){
            creature.startOfFightAction(this,enemyFormation);
        }
    }
    
    //activates each creature's special ability's start of fight action
    public void startOfFightAction2(Formation enemyFormation){
        for (Creature creature : members){
            creature.startOfFightAction2(this,enemyFormation);
        }
    }
    
    
    public void preRoundAction(Formation enemyFormation){
        for (int i = members.size() - 1; i >= 0; i--){
            members.get(i).preRoundAction(this,enemyFormation);
        }
    }
    
    public void postRoundAction(Formation enemyFormation){
        for (int i = members.size() - 1; i >= 0; i--){
            members.get(i).postRoundAction(this,enemyFormation);
        }
    }
    
    public void postRoundAction2(Formation enemyFormation){//healing
        for (int i = members.size() - 1; i >= 0; i--){
            members.get(i).postRoundAction2(this,enemyFormation);
        }
    }
    
    // at the end of each round, delete dead creatures
    // the creatures behind, if any, will take their place at the front
    public boolean handleCreatureDeaths(Formation enemyFormation){//iterator efficiency?
        boolean killedSomething = false;
        Iterator<Creature> iterator = members.iterator();
        while (iterator.hasNext()) {
            Creature c = iterator.next();
            if (c.isDead()) {
                c.actionOnDeath(this,enemyFormation);
                iterator.remove();
                killedSomething = true;
            }
        }
        return killedSomething;
        
    }
    
    //after every turn, remove all dead units. Some creatures have abiilities
    // that can kill other units when they die, so it checks agaain until it doesn't remove anybody
    private static void handleDeaths(Formation thisFormation, Formation enemyFormation){
        boolean leftDead = false;
        boolean rightDead = false;
        do{
            leftDead = thisFormation.handleCreatureDeaths(enemyFormation);
            rightDead = enemyFormation.handleCreatureDeaths(thisFormation);
        }
        while(leftDead || rightDead);
    }
    
    public boolean hasDeadUnits(){
        for (Creature c : members){
            if (c.isDead()){
                return true;
            }
        }
        return false;
    }
    
    public void takeHit(Creature attacker, Formation enemyFormation) {
        takeHit(attacker,enemyFormation,0);//creature in front usually takes the main hit
    }
    
    public void takeHit(Creature attacker, Formation enemyFormation,int position) {//position: which creature to take the hit
        double hit = attacker.determineDamage(members.get(position),this,enemyFormation);//check for array out of bounds?
        hit = alterIncomingDamage(hit,enemyFormation);//if abilities ever have %reduction armor, do it either ^ or V depending on what gets reduced first. prefer ^
        members.get(position).takeHit(attacker,this,enemyFormation,hit);
    }
    
    //for abilities like Niel's, where hits can be affected by abiilities other than armor
    private double alterIncomingDamage(double hit, Formation enemyFormation){
        double startingDamage = hit;
        for (Creature c : members){
            hit = c.getSpecialAbility().alterIncomingDamage(hit,startingDamage,this,enemyFormation);
        }
        return hit;
    }
    
    public void takeAOEDamage(double damage){
        double newDamage = damage * (1 - AOEResistance);
        for (Creature creature : members){
            creature.takeAOEDamage(newDamage,this);
        }
    }
    
    public void AOEHeal(double amount, Formation enemyFormation){//anti AOE rounding?
        double newAmount = amount * (1 - enemyFormation.getAOEResistance());
        for (Creature creature : members){
            creature.changeHP(newAmount,this);
        }
    }
    
    public double getAOEResistance() {
        return AOEResistance;
    }
    
    public void setAOEResistance(double m){
        AOEResistance = m;
    }

    @Override
    public Iterator<Creature> iterator() {
        return new FormationIterator();
    }
    
    public void setFacingRight(boolean facingRight) {
        for (Creature creature : members){
            creature.setFacingRight(facingRight);
        }
    }

    public int size() {
        return members.size();
    }

    //for iterating each creature inf the formation
    private class FormationIterator implements Iterator<Creature>{
        
        Iterator<Creature> iterator;
        
        public FormationIterator(){
            iterator = members.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Creature next() {
            return iterator.next();
        }
        
    }
    
    
    
    
    public static VictoryCondition determineOutcome(Formation thisFormation, Formation enemyFormation){//should these be in Formation class?
        battle(thisFormation,enemyFormation);
        
        boolean thisDead = thisFormation.isEmpty();
        boolean enemyDead = enemyFormation.isEmpty();
        
        if (thisDead && !enemyDead){
            return VictoryCondition.LOSE;
        }
        else if (thisDead && enemyDead){
            return VictoryCondition.DRAW;
        }
        else if (!thisDead && enemyDead){
            return VictoryCondition.WIN;
        }
        else{//if both sides still have followers, something went wrong
            return VictoryCondition.DRAW;//damage based win?
        }
    }
    
    public static int wonInPvP(Formation thisFormation, Formation enemyFormation){//0 for lose, 2 for win, 1 for tie
        battle(thisFormation,enemyFormation);
        
        boolean thisDead = thisFormation.isEmpty();
        boolean enemyDead = enemyFormation.isEmpty();
        
        
        if (thisDead && !enemyDead){
            return 0;
        }
        else if (thisDead && enemyDead){
            if (thisFormation.getDamageTaken() < enemyFormation.getDamageTaken()){
                return 2;
            }
            else return 0;
        }
        else if (!thisDead && enemyDead){
            return 2;
        }
        else{//if there's a stalemate
            return 1;//damage based win?
        }
    }
    
    public static boolean passed(Formation thisFormation, Formation enemyFormation){//for quests, where draws don't count
        VictoryCondition v = determineOutcome(thisFormation,enemyFormation);
        return v == VictoryCondition.WIN;
    }
    
    public static long damageDealt(Formation thisFormation, Formation enemyFormation){//for world bosses
        battle(thisFormation,enemyFormation);
        return enemyFormation.getDamageTaken();
    }
    
    
    //primary method for simulating battles
    public static void battle(Formation thisFormation, Formation enemyFormation){
        doBattlePrep(thisFormation,enemyFormation);
        
        handleDeaths(thisFormation,enemyFormation);
        
        
        int roundNumber = 0;
        
        
        while(!(thisFormation.isEmpty() || enemyFormation.isEmpty()) && roundNumber < STALEMATE_CUTOFF_POINT){
            roundNumber ++;
            doOneRound(thisFormation,enemyFormation);
        }
    }
    
    //simulates a battle and returns the battle log
    public static LinkedList<BattleState> getBattleSim(Formation thisFormation, Formation enemyFormation){
        LinkedList<BattleState> states = new LinkedList<>();
        
        Formation.doBattlePrep(thisFormation,enemyFormation);
        handleDeaths(thisFormation,enemyFormation);
        
        
        int roundNumber = 0;
        states.add(new BattleState(thisFormation.getCopy(),enemyFormation.getCopy(),roundNumber));
        
        while(!(thisFormation.isEmpty() || enemyFormation.isEmpty()) && roundNumber < Formation.STALEMATE_CUTOFF_POINT){
            roundNumber ++;
            doOneRound(thisFormation,enemyFormation);
            states.add(new BattleState(thisFormation.getCopy(),enemyFormation.getCopy(),roundNumber));
        }
        return states;
    }
    
    public static void doBattlePrep(Formation thisFormation, Formation enemyFormation){
        thisFormation.prepareForFight(enemyFormation);
        enemyFormation.prepareForFight(thisFormation);
        thisFormation.startOfFightAction(enemyFormation);
        enemyFormation.startOfFightAction(thisFormation);
        thisFormation.startOfFightAction2(enemyFormation);
        enemyFormation.startOfFightAction2(thisFormation);
    }
    
    public static void doOneRound(Formation thisFormation, Formation enemyFormation){
        thisFormation.preRoundAction(enemyFormation);
        enemyFormation.preRoundAction(thisFormation);
        
        thisFormation.attack(enemyFormation);
        enemyFormation.attack(thisFormation);
        
        thisFormation.postRoundAction(enemyFormation);
        enemyFormation.postRoundAction(thisFormation);
        
        thisFormation.postRoundAction2(enemyFormation);//for healing
        enemyFormation.postRoundAction2(thisFormation);
        
        
        handleDeaths(thisFormation,enemyFormation);
        //thisFormation.handleCreatureDeaths(enemyFormation);
        //enemyFormation.handleCreatureDeaths(thisFormation);
    }
    
    
    
    //prints a text log of the battle for debugging
    private static void printBattleStatus(Formation thisFormation, Formation enemyFormation, int roundNumber){
        if (roundNumber == 0){
            System.out.println("Start");
        }
        else{
            System.out.println("After Round " + roundNumber + ":");
        }
        System.out.println("    Left Formation\n" + thisFormation);
        System.out.println("    Right Formation\n" + enemyFormation);
        System.out.println("\n\n");
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("\t___________________________________________________________________\n");
        for (Creature creature : members){
            sb.append("\t").append(creature.toString()).append("\n");
        }
        sb.append("\t___________________________________________________________________");
        
        return sb.toString();
    }
    
}
