/**
 * This is a slightly modified murmur hashing algorithm v3.
 * It computes the has by mutiplying, rotating and running
 * Bitwise XOR operations on various parts of the computation
 * 
 * @author Oswald Gyabaah
 * @version 1.0
 */
class Murmur3 {
    
    // define class variables
    String data; int seed, hash, bytes, len;
    
    //Define constants to be used
    private final int c1 = 0xcc9e2d51;
    private final int c2 = 0x1b873593;
    private final int c3 = 0x85ebca6b;
    private final int c4 = 0xc2b2ae35;
    private final int m = 0x5;
    private final int n = 0xe6546b64;
    private final int rot1 = 15;
    private final int rot2 = 13;
    
    /**
     * The Constructor: Takes the data and a seed integer to vary the output 
     * for same input
     * @param data a stringified representation of the data we wish to hash.
     * @param seed an integer that alters the output for the same input.
     */
    public Murmur3(String data, int seed){
        this.data = data;
        this.hash = seed;
        this.bytes = this.len/4;
        this.len = data.length();
        
    }
    
    /**
     * A helper funtion that converts each character to its asscii code
     * and shifts the results a number of times representing the character's
     * position in the string
     * @param string a 32-bit string (four characters)
     * @return sum of the shifted asscii values
     */
    private int getShiftedAsscii(String string){
        int sum = 0;
        int len = string.length();
        for(int i=0; i<string.length(); i++)
            sum += shiftRight(((int) string.charAt(i)),(8*(len-i-1)));  
               
        return sum;        
    }

    /**
     * A helper function to shift a given integer by the given steps to the rihgt
     * @param data the Integer to shift right
     * @param steps mumber of bits to shift the Integer
     * @return the shifted Integer
     */    
    private int shiftRight(int data, int steps){
        return data>>steps;  
    }

    /**
     * A helper function that shifts a given integer by the given steps to the left
     * @param data the Integer to shift left
     * @param steps number of bits to shift the inter
     * @return the shifted Integer
     */
    private int shiftLeft(int data, int steps){
        return data<<steps;
        
    }
    
    /**
     * A helper function to rotate a given Integer right by the given steps
     * @param data the data to rotate
     * @param steps number of bits to rotate the data
     * @return the rotated Integer
     */
    private int rotateRight(double data, int steps){
        int bits = 1 + (int)Math.log(data);
        return this.shiftRight((int)data, steps)| shiftLeft((int)data, bits-steps);   
    }
    
    /**
     * A helper function to rotate a given Integer left by the given steps
     * @param data the data to rotate
     * @param steps number of bits to rotate the data
     * @return the rotated Integer
     */
    private int rotateLeft(double data, int steps){
        int bits = 1 + (int)Math.log(data);
        return this.shiftLeft((int)data, steps)| shiftRight((int)data, bits-steps);
        
    }
    
    
    
    /** FIRST BLOCK 32bit = 4byte
     * divide data into four byte chunks and store the remainder if any
     * forEach 4Byte chunk, change each char into asscii
     * shift each asscii right by 8*index
     * sum the resulting asscii in to a variable k
     * multiply k by c1 
     * rotate k left by rot1
     * multiply k c2'
     * k = hash XOR k
     * rotate  hash left by rot2
     * hash =  hash*m+n
    */
    
    private int firstBlock(String string){
        // int bytelength = this.bytes*4;
        int value = 0;
        for(int i=0; i<this.bytes; i++){
            value += getShiftedAsscii(string.substring(4*i, 4*i+4));
        }
        int k = c2 * rotateLeft(value*c1, rot1);
         
        int quaterHash = m*rotateLeft(this.hash ^ k, rot2)+n;

        return quaterHash;   
}
    
    /**
     * SECOND BLOCK
     * convert the remianing bytes also 
     * change each to asscii and shif according
     * to same rules as before and sum
     * then rotate follow everything else in 
     * FIRST BLOCK
     */
    private int secondBlock(String string, int quaterHash){
        int value = getShiftedAsscii(string);
        value = c2 * rotateRight(c1*value, rot1);
        int semiHash = quaterHash ^ value;

        return semiHash;
    }

    private int midOperation(int hash){
        
        return hash^this.len;
    }
    /**
     * Create an avalanche effect so that a small change would create 
     * a huge impact on the outcome. In other words, avoid patterns!
     * 
     * @param hash
     * @return thouroughly mixed up hash
     */
    private int avalancheEffect(int hash){
        hash ^= hash>>16;
        hash *= c3;
        hash ^= hash >> 13;
        hash *= c4;
        hash = hash >> 16;
        return hash;
    }
    
    /**
     * THIRD BLOCK
     * find the avalanche
     * hash ^= hash>>16
     * hash *=c3
     * hash ^= hash >> 13
     * hash *= c4
     * hash = hash >> 16;
     * 
     */
    
    /**
     * Combine all steps into the final hash value
     * @return the final hash value to use
     */
    public int mm3hash(){
        String partialData =  this.data.substring(0, bytes*4);

        int quaterHash = firstBlock(partialData);

        int semiHash = secondBlock(this.data.substring(bytes*4), quaterHash);

        int furtherHash = midOperation(semiHash);

        this.hash = avalancheEffect(furtherHash);

        return this.hash;
    }
    
    
    //  public static void main(String[] args){
    //      Murmur3 n = new Murmur3("Hey oswald youa eraoj dfa lkjfdioea jodfaj ojdfa", 122);
    //     System.out.println("Hash:"+ n.mm3hash());
    //  }   
}
