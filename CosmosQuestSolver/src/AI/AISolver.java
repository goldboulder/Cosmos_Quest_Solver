/*

 */
package AI;

import Formations.Creature;
import Formations.CreatureFactory;
import Formations.Elements;
import Formations.Elements.Element;
import Formations.Formation;
import Formations.Hero;
import Formations.Monster;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

//continuously runs battle simulations on a seperate thread
//to provide a solution for a problem

public abstract class AISolver extends Thread{
    
    protected long followers;
    protected int maxCreatures;
    protected Hero[] heroes;
    protected HashSet<FormationCombinationNode> triedCombinations;
    protected boolean searching;
    
    //returns true if the thread is running
    public boolean isSearching(){
        return searching;
    }
    
    //sets the searching variable to false, thread will stop shortly
    public void stopSearching() {
        searching = false;
    }
    
    //starts the search
    @Override
    public void run() {
        if (searching){
            return;
        }
        searching = true;
        search();
    }
    
    protected abstract void search();
    
    //determines if a combination (order does not matter) has been tried before
    protected boolean isDuplicateCombination(LinkedList<Creature> combo) {
        Formation f = new Formation(combo);
        FormationCombinationNode node = new FormationCombinationNode(f);
        return triedCombinations.contains(node);
    }

    protected void addToTriedCombos(LinkedList<Creature> combo) {
        Formation f = new Formation(combo);
        if (f.containsMonsters()){//heroes are unique, so if a formation contains only heroes, duplicates won't be possible with the combinationIterator
            triedCombinations.add(new FormationCombinationNode(f));
        }
    }
    
    //determines if the monsters in the given list can all be summoned with the followers you have
    protected boolean canAffordMonsters(LinkedList<Creature> combo) {
        long sum = 0;
        for (Creature c : combo){
            sum += c.getFollowers();
        }
        return (sum <= followers);
    }
    
    
    protected int numHeroesStronger(){
        Monster strongestMonster = getStrongestAffordableMonster();
        
        if (strongestMonster == null){//if you can't afford any monsters
            return heroes.length;
        }
        
        int numStrongerHeroes = 0;
        for (Hero h : heroes){
            if(h.getBaseHP() * h.getBaseAtt() * 3 > strongestMonster.getBaseHP() * strongestMonster.getBaseAtt()){//*3? use special abilities instead?
                numStrongerHeroes ++;
            }
        }
        //System.out.println("Strong heroes: " + numStrongerHeroes);
        return numStrongerHeroes;
    }
    
    //determines if followers should start with even strength, or just try the strongest monsters first
    protected boolean mindFollowers(){
        int numStrongerHeroes = numHeroesStronger();//than average monster? 
        return numStrongerHeroes < maxCreatures-1;
    }
    
    protected Monster getStrongestAffordableMonster(){//0 followers?
        Monster strongestMonster = null;
        for (Element element: Elements.MONSTER_ELEMENTS){
            for (int i = Monster.TOTAL_TIERS; i > 0; i --){
                Monster currentMonster = CreatureFactory.getMonster(element,i);
                if(currentMonster.getFollowers() <= followers){
                    if (strongestMonster == null || strongestMonster.getFollowers() < currentMonster.getFollowers()){
                        strongestMonster = currentMonster;
                    }
                    break;
                }
            }
        }
        return strongestMonster;
    }
    
    protected LinkedList<Creature> getCreatureList(){//put in AISolver?
        LinkedList<Creature> creatureList = new LinkedList<>();
        //add all heroes
        for (Hero hero : heroes){
            creatureList.add(hero);
        }
        //add all monsters
        for (Element element: Elements.MONSTER_ELEMENTS){
            
            if (mindFollowers()){
                for (int i = Monster.TOTAL_TIERS; i > 0; i --){//replace with addUsefulMonsters?
                    addMonster(CreatureFactory.getMonster(element, i),creatureList);
                }
            }
            else{
                addStrongestMonsters(creatureList,element);// only consider strongest monsters. Heroes are stronger here...?
            }
        }
        
        return creatureList;
    }
    
    //adds all useful monsters of a specified element to the list. Weeds out the bottom tier monsters
    protected void addAllUsefulMonsters(List<Creature> creatureList, Element element){
        Monster averageMonster = null;// the highest tier monster you can spam the entire row with
        long averageFollowers = followers/maxCreatures;
        for (int i = Monster.TOTAL_TIERS; i > 0; i --){
            Monster m = CreatureFactory.getMonster(element, i);
            
            if (m.getFollowers() > followers){
                continue;
            }
            
            if (averageMonster == null && m.getFollowers() < averageFollowers){
                averageMonster = m;
                //System.out.println("Average monster: " + m);
            }
            // if the monster has clearly inferior stats to the averageMonster, don't bother including it
            if (averageMonster == null || m.getBaseHP() > averageMonster.getBaseHP() || m.getBaseAtt() > averageMonster.getBaseAtt()){
                addMonster(m,creatureList);
            }
            else{//however, sometimes a cheap monster is needed to finish off a monster or act as a tie breaker when it is the last one alive. add one of that monster
                creatureList.add(m);
            }
        }
    }
    
    //only adds the strongest monsters for each element and any lower tier monsters that may have more HP or att than them at the cost of the other
    protected void addStrongestMonsters(List<Creature> creatureList, Element element){
        Monster averageMonster = null;// the highest tier monster you can spam the entire row with
        
        for (int i = Monster.TOTAL_TIERS; i > 0; i --){
            Monster m = CreatureFactory.getMonster(element, i);
            
            if (m.getFollowers() > followers){
                continue;
            }
            
            if (averageMonster == null && m.getFollowers() <= followers){
                averageMonster = m;
                addMonster(m,creatureList);
            }
            // if the monster has clearly inferior stats to the averageMonster, don't bother including it
            if (averageMonster == null || m.getBaseHP() > averageMonster.getBaseHP() || m.getBaseAtt() > averageMonster.getBaseAtt()){
                addMonster(m,creatureList);
            }
            
        }
    }
    
    
    //adds as many monsters of that type to the list as you can afford. caps at Formation.MAX_CREATURES
    protected int addMonster(Monster m, List<Creature> list){
        long tempFollowers = followers;
        int creaturesAdded = 0;
        while(tempFollowers >= m.getFollowers() && creaturesAdded < maxCreatures){//few solutions require all 6 creatures. for quest 100 optimal solution: 2 of the same creature: 10 seconds to get to Chaos. 6: 82***
            list.add(m);//copy?
            tempFollowers -= m.getFollowers();
            creaturesAdded ++;
        }
        //System.out.println("Added " + m);
        return creaturesAdded;
    }
    
    protected int strengthViability(Creature c){
        if (c instanceof Hero){
            return 1000000000 + c.getMainSkill().viability();
            //return 20 * c.getMainSkill().viability();
        }
        return c.getMainSkill().viability();
        
    }
    /*
    protected int followerMinderViability(Creature c, long averageFollowers){
        if (c instanceof Hero){
            return 1000000000 + c.getMainSkill().viability();//heroes still get top priority because they don't cost followers
            
        }
        else{
            int baseViability = c.strength();
            
            //if strength (~= follower count) is greater than average, use the difference to nerf it
            if (baseViability > averageFollowers){
                //return (int)(1.5*(double)averageFollowers * averageFollowers / baseViability);//maybe this is too much of a nerf to high tiers?
                return baseViability;
            }
            else{
                return baseViability;
            }
            
             
        }
    }
*/
    public void recieveRefine() {
        //to be overridden
    }
    
    //used to store formations in the hash map so no duplicate combinations are tried
    protected class FormationCombinationNode{//uses equals function so a proper equals function(order matters) can be used for Formation
        private Formation formation;
        
        public FormationCombinationNode(Formation formation){
            this.formation = formation;
        }
        
        @Override
        public int hashCode(){
            int num = 0;
            for (Creature c :formation.getMembers()){
                num += (c.getBaseAtt() * c.getID()) + (c.getElement().ordinal() * c.getBaseHP());
            }
            return num;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            
            FormationCombinationNode f = (FormationCombinationNode) obj;
            return f.formation.containsSameCreaturesForCombination(formation);
            
        }
        
    }
    
}
