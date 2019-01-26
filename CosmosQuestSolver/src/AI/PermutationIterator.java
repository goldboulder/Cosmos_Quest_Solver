/*

 */
package AI;

import cosmosquestsolver.OtherThings;
import java.util.Iterator;
import java.util.LinkedList;


//iterator for all permutations of a list (order matters)
//generates permutaions as needed, not all at once
//Basic recursion principle: select 1 object to always be first and return all permutaions for
// the rest of the items with the selected object tacked on in the front
public class PermutationIterator<T> implements Iterator<LinkedList<T>>{
    
    private long numLeft;//counter to determine how many permutations are left in the current iterator
    private LinkedList<T> objectsLeft;
    private T currentObject;
    private PermutationIterator iterator;
    
    public PermutationIterator(LinkedList<T> list){
          
        numLeft = (long) OtherThings.fact(list.size());
        objectsLeft = getListCopy(list);// remove 1 item from list. It will be added back to the front after the order of the others is determined
        currentObject = objectsLeft.pop();
        
        //if there are still items in the list after removing one, have a recursive iterator
        // determine the order for the rest
        if (!objectsLeft.isEmpty()){
            iterator = new PermutationIterator(getListCopy(objectsLeft));
        }
    }

    // checks the counter to determine how many permutations are left in the current
    // iterator. if it is not zero, there is a next.
    @Override
    public boolean hasNext() {
        return numLeft != 0;
    }
    
    @Override
    public LinkedList<T> next() {
        if (iterator == null){
            //there is only 1 object to choose from (1P1) return the current item in list form
            LinkedList<T> singleList = new LinkedList<>();
            singleList.add(currentObject);
            numLeft --;
            return singleList;
        }
        
        if (iterator.hasNext()){
            //monst common non-fringe case. 
            // get the interior iterator's list, and add the current object to the front
            LinkedList<T> list = iterator.next();
            list.add(0,currentObject);
            numLeft --;
            return list;
        }
        else{
            //the internal iterator has no more permutations. 
            //select a new object to be first
            T temp = objectsLeft.pop();
            objectsLeft.add(currentObject);
            currentObject = temp;
            
            iterator = new PermutationIterator(objectsLeft);
            
            LinkedList<T> list = iterator.next();
            list.add(0,currentObject);
            numLeft --;
            return list;
        }
    }
    
    //returns shallow copy of list
    private LinkedList<T> getListCopy(LinkedList<T> list){
        LinkedList<T> newList = new LinkedList<>();
        for (T i : list){
            newList.add(i);
        }
        return newList;
    }
        
    
        
}
