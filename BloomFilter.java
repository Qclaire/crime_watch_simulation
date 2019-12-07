

import java.util.BitSet;

/**
 * An implementation of a bloomfilter using Java BitSet and a custom
 * version of the Murmur3 hashing algorithm.
 * 
 * @author Oswald Gyabaah
 * @version 1.0
 * @see phttps://blog.medium.com/what-are-bloom-filters-1ec2a50c68ff
 * 
 */
class BloomFilter {
    // class variables
    int bitSize, hashes;

    BitSet filter;

    /**
     * constructor: Performs the required calcluations and initialises the bitset to be used 
     * 
     * @param errorPercent desired max false positive rate
     * @param size      total items to store in the filter
     */
    public BloomFilter(double errorPercent, int items) {
        double p = errorPercent/100; // change to percentage
        
        // number of bits in the array
        double numerator =  items*Math.log(1/p);
        
        double denum = 1.386; // the value of (ln2)^2

        // total bits in the set
        int m = (int) numerator/(int)denum; 
        

        // bits per item
        double bitRatio = m/items;
        

        // number of hash functions
        int k = (int)(bitRatio*Math.log(2));
        
        hashes = k; // number of hash functions to use
        
        // intialise the filter with total bits to store
        filter = new BitSet(m);
        filter.clear(); // making sure it starts out cleared
        bitSize = m;
    }
    
    /**
     * Adds an item to the filter
     * @param item
     */
    public void addItem(String item){
        int seed = 0;
        item = item.strip();
        for(int i=0; i<hashes; i++){
            int hi = Math.abs(new Murmur3(item, seed).mm3hash());
            int idx = hi % (bitSize-1);
            filter.set(idx);
            seed = hi;
            
        }
    }
    /**
     * Check if a given entry is in the filter
     * @param item
     * @return Boolean: is item in the filter?
     */
    public boolean contains(String item){
        int seed = 0;
        item = item.strip();
        for(int i=0; i<hashes; i++){
            int hi = Math.abs(new Murmur3(item, seed).mm3hash());
            int idx = hi % (bitSize-1);
            
            if(!filter.get(idx)) return false;
            seed = hi;
        } return true;
    }

    /**
     *  method to determine filter is empty
     * @return Boolean representing whether the filte is empty
     */
    public boolean isEmpty(){
        return filter.cardinality() == 0;

    }
    

    // public static void main(String[] args) {
    //     BloomFilter f =  new BloomFilter(1, 100);
    //     f.addItem("Oswald");
    //     System.out.println(f.contains("Oswald "));
    // }
}
