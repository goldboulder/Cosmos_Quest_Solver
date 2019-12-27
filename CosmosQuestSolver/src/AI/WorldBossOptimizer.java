/*

 */
package AI;

import Formations.Elements;
import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Elements.Element;
import Formations.Formation;
import Formations.Hero;
import Formations.Monster;
import GUI.HeroCustomizationPanel.Priority;
import GUI.WorldBossOptimizerFrame;
import Skills.Nothing;
import Skills.Skill;
import cosmosquestsolver.OtherThings;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

//given the users assets and a world boss, calculates the maximum damage a user
//can deal to that world boss and the formation used to deal that damage.
//runs on a seperate thread
public class WorldBossOptimizer extends AISolver{
    
    protected WorldBossOptimizerFrame frame;
    protected Formation bossFormation;
    protected Hero[] prioritizedHeroes;
    protected Skill[] yourRunes;
    protected boolean hasRunes = false;
    protected boolean containsRandomBoss;
    private boolean NHWBEasy;
    
    public WorldBossOptimizer(WorldBossOptimizerFrame frame){
        this.frame = frame;
        
        triedCombinations = new HashSet<>();
    }
    //reference: https://pastebin.com/u/pebl
    
    //get parameters for the problem and performs search algorithm.
    //will not start a new search if one is already going
    @Override
    protected void search() {
        
        obtainProblem();
        if (prioritizedHeroes.length > maxCreatures){
            frame.recieveProgressString("Too many prioritized creatures");
            return;
        }
        bestComboPermu();
        if (searching){
            frame.recieveDone();
            searching = false;
        }
    }
    
    //asks the frame for the parameters of the problem
    private void obtainProblem(){
        followers = frame.getFollowers();
        followers = Math.min(followers, CreatureFactory.getStrongestMonster().getFollowers() * Formation.MAX_MEMBERS);
        maxCreatures = frame.getMaxCreatures();
        heroes = frame.getHeroes();//is this field needed?
        prioritizedHeroes = frame.getHeroes(Priority.ALWAYS);
        //create boss formation
        LinkedList<Creature> list = new LinkedList<>();
        list.add(frame.getBoss());
        bossFormation = new Formation(list);
        containsRandomBoss = bossFormation.containsRandomHeroes();
        yourRunes = frame.getYourRunes();
        for (int i = 0; i < Formation.MAX_MEMBERS; i++){
            //System.out.println(yourRunes[i]);
            if (!(yourRunes[i] instanceof Nothing)){
                hasRunes = true;
                //System.out.println("hasRunes");
                break;
            }
        }
        
        NHWBEasy = heroes.length == 0 && prioritizedHeroes.length == 0 && frame.getBoss().getMainSkill().WBNHEasy() && !hasRunes && !containsRandomBoss;
        
    }

    
    protected void bestComboPermu(){
        LinkedList<Creature> creatureList = getCreatureList(frame,this);
        pruneSubOptimalMonsters(creatureList);
        sendCreatureList(creatureList);
        //testList(creatureList);
        tryCombinations(creatureList);
        
    }
    
    private void pruneSubOptimalMonsters(LinkedList<Creature> list){
        Element bossElement = frame.getBoss().getElement();
        if (bossElement == Element.VOID){
            return;
        }
        int previousID = -1;
        ListIterator<Creature> iterator = list.listIterator();
        while (iterator.hasNext()){
            Creature c = iterator.next();
            if (c instanceof Monster){
                Monster m = (Monster) c;
                if (!Elements.hasAdvantage(m.getElement(), bossElement) && ((m.getFollowers() > followers*0.2 && m.getFollowers() > 100000) || m.getID() == previousID)){
                    iterator.remove();
                }
                
            }
            else{//only prune for no heroes
                return;
            }
            previousID = c.getID();
        }
        
    }
    
    private void sortCreatureList(LinkedList<Creature> creatureList){
        //int numStrongerHeroes = numHeroesStronger();//than average monster? 
        //if (numStrongerHeroes < maxCreatures-1){
            //long averageFollowers = followers/(maxCreatures-numStrongerHeroes);
            //Collections.sort(creatureList, (Creature c1, Creature c2) -> followerMinderViability(c2,averageFollowers)-followerMinderViability(c1,averageFollowers));
        //}
        //else{
            Collections.sort(creatureList, (Creature c1, Creature c2) -> strengthViability(c2)-strengthViability(c1));
        //}
    }
    
    protected void tryCombinations(LinkedList<Creature> creatureList){//same as in QuestSolver?
        
        
        int numCombinationCreatures = maxCreatures - prioritizedHeroes.length;
        long i = 0;//debug, for seeing what step of the list it's on
        int listNum = 1;//debug
        long nCrNum = OtherThings.nCr(listNum, numCombinationCreatures);//debug
        
        boolean strengthMode = !mindFollowers();//don't need to check for duplicates if on strength mode
        
        Iterator<LinkedList<Creature>> combinations = new CombinationIterator(creatureList,numCombinationCreatures);
        
        
        while(combinations.hasNext() && searching){
            i ++;//debug
            if (i > nCrNum){//debug
                //System.out.println(listNum + " " + creatureList.get(listNum));//debug
                progressReport(listNum,creatureList);
                listNum ++;//debug
                nCrNum = OtherThings.nCr(listNum, numCombinationCreatures);
            }//debug
            
            
            LinkedList<Creature> combo = combinations.next();
            for (Hero hero : prioritizedHeroes){
                combo.add(hero);
            }
            
            
            if (canAffordMonsters(combo) && !usingWeakMonstersOnNH(combo)){
                if (strengthMode){
                    tryPermutations(combo);
                }
                else if (!isDuplicateCombination(combo)){
                    tryPermutations(combo);
                    addToTriedCombos(combo);
                }
            }
        }
    }
    
    private boolean usingWeakMonstersOnNH(LinkedList<Creature> combo){
        final long cutoff = (long)(followers*0.8);
        if (heroes.length != 0 || prioritizedHeroes.length != 0){// only for no heroes
            return false;
        }
        
        long sum = 0;
        for (Creature c : combo){
            sum += c.getFollowers();
        }
        //if (sum < cutoff){
            //System.out.println(sum);
        //}
        return sum < cutoff && sum > 50000;
    }
    
    protected void progressReport(int listNum, LinkedList<Creature> creatureList) {
        frame.recieveProgressString("Search now including creature " + (listNum+1) + ": " + creatureList.get(listNum).getName());
    }
    
    protected void sendCreatureList(LinkedList<Creature> creatureList) {
        frame.recieveCreatureList(creatureList);
    }
    
    //functions similarly to the quest solver. if a formation that deals a higher amount of damage to the boss
    //is found, update the frame, but still keep searching
    private void tryPermutations(LinkedList<Creature> list) {
        if (NHWBEasy){
            //no runes here
            testFormation(new Formation(list));
            
        }
        else{
            
            PermutationIterator<Creature> permutations = new PermutationIterator(list);
            while(permutations.hasNext()){
                //if(maxCreatures != Formation.MAX_MEMBERS && (hasRunes || containsRandomBoss)){
                if(maxCreatures != Formation.MAX_MEMBERS && (hasRunes || containsRandomBoss)){
                    
                    LinkedList<Creature> currentPermutation = permutations.next();
                    testFormation(maxWithShufflingBlankSpaces(currentPermutation));
                }
                else{
                    //System.out.println("No Shuffle");
                    Formation f = new Formation(permutations.next());
                    
                    if (hasRunes){
                        Formation runeF = f.getCopy();
                        runeF.addRuneSkills(yourRunes);
                        testFormation(runeF);
                    }
                    else{
                        testFormation(f);
                    }
                }
                //System.out.println("testing the long way" + maxCreatures);
            }
        }
        
    }
    
    private void testFormation(Formation f){
        long damage = Formation.damageDealt(f.getCopy(), bossFormation.getCopy());//what if bosses aren't alone?
            
            if (damage > frame.getBestDamage()){
                frame.recieveDamageOfBattle(damage);//do this first to avoid race condition with multiple threads
                if (maxCreatures != Formation.MAX_MEMBERS && (hasRunes || containsRandomBoss)){
                    frame.recieveBlankSpaceSolution(f.getMembers(), f.getBlankSpaces(), hasRunes);
                }
                else{
                    frame.recieveSolution(f);
                }
                
            }
    }
    
    protected Formation maxWithShufflingBlankSpaces(LinkedList<Creature> creatureList){
        //System.out.println("PassedWithShufflingBlankSpaces");
        LinkedList<Integer> nums = new LinkedList<>();
        for (int i = 0; i < Formation.MAX_MEMBERS; i++){
            nums.add(i);
        }
        
        Iterator<LinkedList<Integer>> combinations = new CombinationIterator(nums,Formation.MAX_MEMBERS - maxCreatures);
        
        long damage = 0;
        Formation currentBest = new Formation();
        while(combinations.hasNext()){
            //make formation with blank spaces
            LinkedList<Integer> blankSpaces = combinations.next();
            Creature[] creatureArray = Formation.listBlankSpacesToArray(creatureList, blankSpaces);
            //get copy
            Creature[] copyArray = new Creature[creatureArray.length];
            for (int i = 0; i < creatureArray.length; i++){
                if (creatureArray[i] != null){
                    copyArray[i] = creatureArray[i].getCopy();
                }
            }
            
            
            Formation f = new Formation(copyArray);
            Formation runeF = f.getCopy();
            if (hasRunes){
                runeF.addRuneSkills(yourRunes);
            }
            
            long tempDamage = Formation.damageDealt(runeF.getCopy(), bossFormation.getCopy());
            //System.out.println(passed);
            if (tempDamage > damage){
                damage = tempDamage;
                currentBest = runeF;
                if (hasRunes){
                    currentBest.addRuneSkills(yourRunes);
                }
            }
        }
        
        return currentBest;
    }
    
    //since the element of the world boss is known and constant, give monsters strong against
    //that element a higher priority
    @Override
    protected int strengthViability(Creature c){
        int baseViability = super.strengthViability(c);
        if (Elements.elementDamageMultiplier(c,bossFormation.getFrontCreature().getElement()) > 1){
            baseViability  = (int)(baseViability * (double)Elements.DAMAGE_BOOST * 3);
        }
        if (Elements.elementDamageMultiplier(bossFormation.getFrontCreature(),c.getElement()) > 1){
            baseViability  = (int)(baseViability * (double)Elements.DAMAGE_BOOST / 3);
        }
        return baseViability;
    }
    /*
    @Override//don't need
    protected int followerMinderViability(Creature c, long averageFollowers){
        int baseViability = super.followerMinderViability(c, averageFollowers);
        if (Elements.elementDamageMultiplier(c,bossFormation.getFrontCreature().getElement()) > 1){
            baseViability  = (int)(baseViability * (double)Elements.DAMAGE_BOOST * 20);//buff strong followers instead?
        }
        if (Elements.elementDamageMultiplier(bossFormation.getFrontCreature(),c.getElement()) > 1){
            baseViability  = (int)(baseViability * (double)Elements.DAMAGE_BOOST / 3);
        }
        return baseViability;
    }
    */
    
    /*
    private void test(){
        System.out.println("Followers: " + followers);
        System.out.println("Max Creatures: " + maxCreatures);
        System.out.println("Heroes: ");
        for (Hero hero: heroes){
            System.out.println("\t" + hero);
        }
        System.out.println("Boss: " + bossFormation);
    }

    private void testList(List<Creature> creatureList) {
        for (Creature c : creatureList){
            System.out.println(c);
        }
    }
    */
    
    
    
}
