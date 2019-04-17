/*

 */
package AI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Formation;
import Formations.Hero;
import Formations.Monster;
import GUI.QuestSolverFrame;
import cosmosquestsolver.OtherThings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//contatins algorithm to search for a solution to beat a given formation.
//runs on a seperate thread
public class QuestSolver extends AISolver{
    
    protected QuestSolverFrame frame;//if a solution is found, this feild lets the frame know and sends the solution to it
    protected Formation enemyFormation;//the formation the solver wants to beat
    protected Hero[] prioritizedHeroes;// heroes that must be in the solution
    protected boolean containsRandomHeroes;
    
    public QuestSolver(QuestSolverFrame frame){
        this.frame = frame;
        triedCombinations = new HashSet<>();
    }
    
    //get parameters for the problem and performs search algorithm.
    //will not start a new search if one is already going
    @Override
    public void search(){
        obtainProblem();
        if (prioritizedHeroes.length > maxCreatures){
            tooManyPrioritizedCreaturesMessage();
            return;
        }
        
        bestComboPermu();
        if (searching){
            finished();
            searching = false;
        }
    }
    
    //asks the frame for the parameters of the problem
    protected void obtainProblem(){
        followers = frame.getFollowers();
        maxCreatures = frame.getMaxCreatures();
        heroes = frame.getHeroesWithoutPrioritization();
        prioritizedHeroes = frame.getPrioritizedHeroes();
        enemyFormation = frame.getEnemyFormation();
        containsRandomHeroes = enemyFormation.containsRandomHeroes();
        
    }

    //gets a list of all creatures available, sorts them by viability, and
    //tests each possible combination until list is exhausted or solution is found.
    //tests formations most likeley to win (by heuristic) first
    protected void bestComboPermu(){
        LinkedList<Creature> creatureList = getCreatureList();
        sortCreatureList(creatureList);
        sendCreatureList(creatureList);
        if (creatureList.size() < maxCreatures){
            maxCreatures = creatureList.size();
        }
        tryCombinations(creatureList);
    }
    
    //sorts the list of creatures so that the combination iterator can select the combinations most
    //likely to produce a solution first. If hero viability outweighs monster viability, sorts by viability.
    protected void sortCreatureList(LinkedList<Creature> creatureList){//debug for not all heroes being first over monsters*******
        int numStrongerHeroes = numHeroesStronger();//than average monster? 
        if (mindFollowers()){
            long averageFollowers = followers/(maxCreatures-numStrongerHeroes);
            Collections.sort(creatureList, (Creature c1, Creature c2) -> followerMinderViability(c2,averageFollowers)-followerMinderViability(c1,averageFollowers));
        }
        else{
            Collections.sort(creatureList, (Creature c1, Creature c2) -> strengthViability(c2)-strengthViability(c1));
        }
    }
    
    
    
    // for every combination of creatures provided by the combination iterator, test all permutations of that combination.
    // skip the combination if you don't have enough followers to use all the monsters, or that combination has been tried
    // before. Also checks if the user canceled the search each combination and stops if this is the case
    // this algorithm is designed to find a solution the user can afford, not nessesarily the most efficient solution.
    protected void tryCombinations(LinkedList<Creature> creatureList){
        
        int numCombinationCreatures = maxCreatures - prioritizedHeroes.length;
        long i = 0;//counter for seeing what step of the list it's on
        int listNum = 1;//debug
        //calculates the number of combinations tried when it's time to include a new creature
        //int the search
        long nCrNum = OtherThings.nCr(listNum, numCombinationCreatures);//debug
        
        boolean strengthMode = !mindFollowers();//don't need to check for duplicates if on strength mode since there are none
        
        Iterator<LinkedList<Creature>> combinations = new CombinationIterator(creatureList,numCombinationCreatures);
        while(combinations.hasNext() && searching){
            i ++;
            if (i > nCrNum){
                //at this point, the solver has included the next most powerful
                //creature in its search. Updates the GUI and calculates how many
                // combinations are left until the next creature is included
                progressReport(listNum,creatureList);
                
                listNum ++;
                nCrNum = OtherThings.nCr(listNum, numCombinationCreatures);
            }
            
            //add prioritized heroes to the list
            LinkedList<Creature> combo = combinations.next();
            for (Hero hero : prioritizedHeroes){
                combo.add(hero);
            }
            
            //if user has not chosen to stop the search, try all the permutations
            // against the enemy formation
            if (proceedWithPermutations(combo)){
                tryPermutationsAndMindDuplicates(strengthMode,combo);
            }
            
        }
        
    }
    
    //only tries all permutations if you can afford the monsters and that combination wasn't already
    //tried. If duplicates are possible, record it for later
    protected void tryPermutationsAndMindDuplicates(boolean strengthMode, LinkedList<Creature> combo){
        if (canAffordMonsters(combo)){// did not notice a performance boost (memory saving?)
            if (strengthMode){
                tryPermutations(combo);
            }
            else if (!isDuplicateCombination(combo)){
                tryPermutations(combo);
                addToTriedCombos(combo);
            }
        }
    }

    //constructs a formation for every possible permutation of creatures in the given list
    //and see if any win against the enemy formation. If one is found, send the solution to the 
    //frame and stop
    protected void tryPermutations(LinkedList<Creature> list) {
        LinkedList<Creature> currentPermutation;
        PermutationIterator<Creature> permutations = new PermutationIterator(list);
        while(permutations.hasNext()){
            currentPermutation = permutations.next();
            
            if(containsRandomHeroes && maxCreatures != Formation.MAX_MEMBERS){
                LinkedList<Integer> blankSpaces = passedWithShufflingBlankSpaces(currentPermutation);
                if (blankSpaces != null){
                    if (!Formation.passed(frame.getSolution().getCopy(), enemyFormation.getCopy())){//if there is already a solution from another thread, don't bother.
                        frame.recieveBlankSpaceSolution(currentPermutation,blankSpaces);
                        searching = false;
                    }
                    return;
                }
            }
            else{
                Formation currentFormation = new Formation(currentPermutation);
                if (Formation.passed(currentFormation.getCopy(), enemyFormation.getCopy())){
                    if (!Formation.passed(frame.getSolution().getCopy(), enemyFormation.getCopy())){//if there is already a solution from another thread, don't bother.
                        frame.recieveSolution(currentFormation);
                        searching = false;
                    }

                    return;//stops the solver from overwriting the solution so it is different than in the solution in the replay

                }
            }
            
        }
    }
    
    protected LinkedList<Integer> passedWithShufflingBlankSpaces(LinkedList<Creature> creatureList){
        //System.out.println("PassedWithShufflingBlankSpaces");
        LinkedList<Integer> nums = new LinkedList<>();
        for (int i = 0; i < Formation.MAX_MEMBERS; i++){
            nums.add(i);
        }
        
        Iterator<LinkedList<Integer>> combinations = new CombinationIterator(nums,Formation.MAX_MEMBERS - maxCreatures);
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
            
            //System.out.println("_______________________");
            //for (Creature c : creatureArray){
                //System.out.println(c);
            //}
            
            boolean passed = Formation.passed(new Formation(copyArray), enemyFormation.getCopy());
            //System.out.println(passed);
            if (passed){
                return blankSpaces;
            }
        }
        
        return null;
    }
    
    

    protected void tooManyPrioritizedCreaturesMessage() {
        frame.recieveProgressString("Too many prioritized creatures");
    }

    protected void progressReport(int listNum, LinkedList<Creature> creatureList) {
        frame.recieveProgressString("Search now including creature " + (listNum+1) + ": " + creatureList.get(listNum).getName());
    }

    protected boolean proceedWithPermutations(LinkedList<Creature> combo) {//overritten by sub class
        return true;
    }

    protected void finished() {
        frame.recieveDone();
    }

    protected void sendCreatureList(LinkedList<Creature> creatureList) {
        frame.recieveCreatureList(creatureList);
    }

    /*
    //for debugging, prints parameters
    private void test(){
        System.out.println("Followers: " + followers);
        System.out.println("Max Creatures: " + maxCreatures);
        System.out.println("Heroes: ");
        for (Hero hero: heroes){
            System.out.println("\t" + hero);
        }
        System.out.println("Enemy Formation: " + enemyFormation);
    }
    
    //for debugging. prints sorted creature list
    private void testList(List<Creature> creatureList) {
        for (int i = 0; i < creatureList.size(); i++){
            System.out.println(i + " " + creatureList.get(i));
        }
    }
    
*/
}
