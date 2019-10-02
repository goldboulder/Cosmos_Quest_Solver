/*

 */
package AI;

import Formations.Creature;
import Formations.Hero;
import GUI.HeroCustomizationPanel.Priority;
import GUI.QuestSolverFrame;
import Skills.Nothing;
import java.util.LinkedList;

//used by WeirdHeroQuestSolver
public class SpecialQuestSolver extends QuestSolver{
        
    private int originalMaxCreatures;
    private Hero weirdHero;
    private WeirdHeroQuestSolver parentSolver;
        
    public SpecialQuestSolver(QuestSolverFrame frame, WeirdHeroQuestSolver parentSolver, int maxCreatures, Hero weirdHero) {
        super(frame);
        this.parentSolver = parentSolver;
        this.maxCreatures = maxCreatures;
        originalMaxCreatures = frame.getMaxCreatures();
        this.weirdHero = weirdHero;
    }
        
    @Override
    protected void obtainProblem(){
        followers = frame.getFollowers();
        // maxCreatures already known
        originalMaxCreatures = frame.getMaxCreatures();
        heroes = frame.getHeroes();//is this field needed?
        prioritizedHeroes = frame.getHeroes(Priority.ALWAYS);
        enemyFormation = frame.getEnemyFormation();
        yourRunes = frame.getYourRunes();
        enemyRunes = frame.getEnemyRunes();
        enemyFormation.addRuneSkills(enemyRunes);
        containsRandomHeroes = enemyFormation.containsRandomHeroes();
        
        for (int i = 0; i < maxCreatures; i++){
            //System.out.println(yourRunes[i]);
            if (!(yourRunes[i] instanceof Nothing)){
                hasRunes = true;
                //System.out.println("hasRunes");
                break;
            }
        }
    }
        
    
        
    @Override
    protected void tooManyPrioritizedCreaturesMessage() {
        //nothing
    }
        
    @Override
    protected void progressReport(int listNum, LinkedList<Creature> creatureList) {
        if (originalMaxCreatures == maxCreatures){
            frame.recieveProgressString("Biggest search now including creature " + (listNum+1) + ": " + creatureList.get(listNum).getName());
        }
    }
        
    //if combination size is smaller thatn
    // max creatures on original problem, only do permutations if the weird
    //hero is in the combination. If it is not, a solution with maxCreatures
    //will also work.
    @Override
    protected boolean proceedWithPermutations(LinkedList<Creature> combo) {
        if (combo.size() == originalMaxCreatures || weirdHero == null){
            return true;
        }
        else{
            for (Creature c : combo){
                if (c.getName().equals(weirdHero.getName())){
                    return true;
                }
            }
        }
        return false;
    }
        
    //if one solver found a solution, the others can stop
    @Override
    protected void tryPermutations(LinkedList<Creature> list) {
        super.tryPermutations(list);
        if (!searching){
            parentSolver.stopSearching();
        }
    }
        
    //if one solver found a solution, the others can stop
    @Override
    protected void finished() {
        if (originalMaxCreatures == maxCreatures){
            super.finished();
        }
    }
        
    //only send the list if it is the largest search (most max creatures)
    @Override
    protected void sendCreatureList(LinkedList<Creature> creatureList) {
        if (originalMaxCreatures == maxCreatures){
            frame.recieveCreatureList(creatureList);
        }
    }
        
}
