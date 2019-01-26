/*

 */
package AI;

import cosmosquestsolver.OtherThings;
import java.util.Iterator;
import java.util.LinkedList;

//iterator for all combinations (order doesn't matter) of a specific nunber (nCr).
//generates combinations as they are needed, not all at once.
//gives combinations involving the items at the front of the list first
// basic rersive idea: for nCr, return all combinations for (n-1)C(r-1), then add the last element in the list
public class CombinationIterator<T> implements Iterator<LinkedList<T>>{
        
        
    private int numElements;
    private int guarenteedIndex;
    private long numLeft;//keeps track of how many combinations are left until a new iterator needs to be generated
    private LinkedList<T> list;
    private CombinationIterator iterator;
    
    public CombinationIterator(LinkedList<T> list, int numElements){//use list instead of linkedList? no, performance, numElements > list.size?
        
        this.numElements = numElements;
        guarenteedIndex = numElements - 1;
        numLeft = OtherThings.nCr(list.size(),numElements);
        this.list = list;
        if (list.size() > numElements && numElements > 0){
            iterator = new CombinationIterator(getFrontCopy(list,numElements-1),numElements - 1);
        }
    }
        
    @Override
    public boolean hasNext() {
        return numLeft > 0;
    }
    
    @Override
     public LinkedList<T> next() {
        if (numElements <= 0){
            //base case for recursion: 0 elements need to be selected
            numLeft --;
            return new LinkedList<>();
        }
        
        if (iterator == null){
            //iterator is null when x elements need to be chosen, but the list 
            // is not big enough
            numLeft --;
            return list;
        }
        
        
        if (iterator.hasNext()){
            // most common non-fringe case. get the next combination from the smaller
            // iterator, then add the new element to it
            LinkedList<T> tempList = iterator.next();
            tempList.add(list.get(guarenteedIndex));
            numLeft --;
            return tempList;
        }
        else{
            // all combinations for the current iterator have been exhausted.
            // make a new iterator that is 1 item bigger (that item being what
            // was being added on at the end before), and select the next item to
            // be added on (guarenteed to be in the combination)
            guarenteedIndex ++;
            iterator = new CombinationIterator(getFrontCopy(list,guarenteedIndex),numElements - 1);
            LinkedList<T> tempList = iterator.next();
            tempList.add(list.get(guarenteedIndex));
            numLeft --;
            return tempList;
        }
    }
    
    //returns a list with only the fonnt x items in it
    private LinkedList<T> getFrontCopy(LinkedList<T> list, int numToCopy){
        int numCopied = 0;
        LinkedList<T> newList = new LinkedList<>();
        for (T i : list){
            if (numCopied >= numToCopy){
                break;
            }
            newList.add(i);
            numCopied ++;
            
        }
        return newList;
    }
    
    
        
}