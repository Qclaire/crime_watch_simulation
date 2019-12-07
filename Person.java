/**
 * A class reprensting a person
 * @author Oswald Gyabaah
 * @version 1.0
 */
class Person{

    // Class Variable declarations
    private String firstname, middlename, lastname, DoB, country;
    private int age, weight, height;

    /**
     * Constructor to create a full person object
     * @param firstname
     * @param middlename
     * @param lastname
     * @param age
     * @param DoB
     * @param country
     * @param weight
     * @param height
     */
    public Person(String firstname, String middlename, String lastname, int age, String DoB, String country, int weight, int height ){
        this. firstname = firstname.trim();
        this.lastname = lastname.trim();
        this.middlename = middlename.trim();
        this.DoB = DoB.trim();
        this.country = country.trim();
        this.age = age;
        this.height = height;
        this.weight = weight;

    }public String getFirstName(){
        return this.firstname;
    
    }public String getLastName(){
        return this.lastname;
    
    }public String getMiddleName(){
        return this.middlename;
    
    }public int getAge(){
        return this.age;
    
    }public String getDoB(){
        return this.DoB;
    
    }public int getWeight(){
        return this.weight;

    }public int getHeight(){
        return this.height;
    
    }public String getCountry(){
        return this.country;
    
    }public String getFullName(){
        return this.getFirstName()+" "+this.getMiddleName() + " " + this.getLastName();
    
    }
    /**
     * this method strings together all attributes of the person
     * and passes them to the hashing algos to provide a unique 
     * hash for each person
     * @return string representing all attributes of the person
     */
    public String getSignature(){
        String sig = this.firstname + this.middlename + this.lastname
        + this.age  + this.weight + this.height + this.country + 
        this.DoB;
        return sig;
    }

    
}
    