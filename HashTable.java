
/**
 * A class representing a hash table
 * @author Petra Abosi Mensah
 * @author Naayi Tei-Dornoo
 */

class HashTable {


    private static class ListNode<T> {
        private String key;
        private Person value;
        private ListNode next;
    }

    private ListNode[] array; // the hash table represented as an array
    private int count;  // counts the number of pairs in the hash table

    public HashTable() {
        array = new ListNode[89];  //  create constructor to initialise the size
    }

    public HashTable(int firstSize) {
        array = new ListNode[firstSize]; // create a hash table with a given size
    }


    /**
     * add method, add items to the array
     *
     * @param
     */
    public void put(String key, Person value) {
        int bucket = hash(key);  // for the location of the key
        ListNode list = array[bucket];  // for traversing the linked list at the appropriate location

        while (list != null) {
            if (list.key.equals(key))  // searching if key already exist
                break;
            list = list.next;

        }
        if (list != null) {
            list.value = value;
        } else {
            if (count >= 0.75 * array.length) {
                resize();
            }
            ListNode newNode = new ListNode();
            newNode.key = key;
            newNode.value = value;
            newNode.next = array[bucket];
            array[bucket] = newNode;
            count++;
        }
    }


    /**
     * find method, returns given item in the array
     *
     * @param
     */
    public boolean containsKey(String key) {
        int bucket = hash(key);
        ListNode list = array[bucket];
        while (list != null) {
            if (list.key.equals(key))
                return true;
            list = list.next;
        }
        return false;
    }

    public Person get(String key){
        int bucket = hash(key);
        ListNode list = array[bucket];
        while(list != null){
            if(list.key.equals(key))
            return list.value;
            list = list.next;
        }
        return null;
    }


    /**
     * delete method, removes item from the array
     *
     * @param
     * @param danny
     */

    public void remove(String key, String danny) {
        int bucket = hash(key);
        if (array[bucket] == null) {
            return;
        }
        if (array[bucket].key.equals(key)) {
            array[bucket] = array[bucket].next;
            count--;
            return;
        }
        ListNode prev = array[bucket];
        ListNode current = prev.next;
        while (current != null && !current.key.equals(key)) {
            current = current.next;
            prev = current;
        }
        if (current != null) {
            prev.next = current.next;
            count--;
        }
    }

    private int hash(String key){
        return (Math.abs(key.hashCode()))% array.length;
        
    }

    public int size() { return count; }
    private void resize() {
        ListNode[] newarray = new ListNode[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            ListNode list = array[i];
            while (list != null) {
                ListNode next = list.next;
                int hash = (Math.abs(list.key.hashCode())) % newarray.length;
                list.next = newarray[hash];
                newarray[hash] = list;
                list = next;
            }
        }
        array = newarray;
    }

}