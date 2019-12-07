
import java.io.File;
import java.lang.Math;
import java.util.Scanner;
import java.io.FileNotFoundException;
import static java.util.Objects.hash;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The combined security application unifying the  bloom filter and a Hashtable to 
 * fetch actual data for each suspected criminal. This is useful for clarifying false
 * positives and tracing records for actual criminals.
 * 
 * The Hashtable stores person objects holding the details of person in the database. 
 */
public class TSA{

    BloomFilter tsaFilter;
    HashTable tsaHashtable;


    public void bootstrap(String dbFilename){
        tsaHashtable = new HashTable(200);
        tsaFilter = new BloomFilter(1, 200);

        System.out.println("\nBooting, please wait...\n");

        
        System.out.println("\nConnecting to database, just a moment...\n");
        try{
            File db =  new File(dbFilename); // create a file reference 

            if (db.exists() && db.canRead()){ // check file accessibility
                Scanner reader =  new Scanner(db);
            
                if(reader.hasNextLine()){
                    String headerRow = reader.nextLine(); // read off the first line it contains headers
                    int count =  1;
                    System.out.println("\nPopulating Database...\n");
                    while(reader.hasNextLine()){
                        String[] line  = reader.nextLine().split(" ");
                        
                        if(line.length == 8){
                            Person person = new Person(
                                    line[0], line[1], line[2], Integer.parseInt(line[3]),
                                    line[4], line[5], Integer.parseInt(line[6]), Integer.parseInt(line[7])
                                );
                            //System.out.println(count+". Adding " + person.getFullName());
                            tsaFilter.addItem(person.getFullName());
                            tsaHashtable.put(person.getFullName(), person);
                                count += 1;
                        }
                    }if(tsaFilter.isEmpty()) System.out.println("Empty database");
                    System.out.println("\nDatabase ready \n");
            
                } else System.out.println("\nEmpty Database\n");

                System.out.println("\nWelcome to the TSA crimine watch system\n");
                System.out.println("Press Enter to Start \n");
            } else System.out.println("File missing or inaccessible");
        } catch(FileNotFoundException error){
            System.out.println(error.getMessage());

        }
    }

    public void interactiveMode(){
        String[] instructions =  {"Add new record", "Quick Check Record", "Retrieve full Record", "Exit"};
        // int last = instructions.length-1;
        int input = 0;
        while(input!=-1){
            System.out.println("\n\t\tChoose an Option\n\t=============================\n");
            for(int num =0 ; num<instructions.length; num++)
                System.out.println("Enter "+(num+1)+" to "+ instructions[num]);
            input = new Scanner(System.in).nextInt();
            switch(input){
                case 1:
                    addRecord("db.txt");
                    break;
                case 2:
                    quickChek();
                    break;
                case 3: 
                    getFullRecord();
                    break;
                case 4:
                    input = -1;
                default: 
                    System.out.println("\t\t=============\n\tinvalid Input. please try again\n\t\t=============\n\n");
            }
            
        }System.out.println("Interactive Session Endded...\n");
    }

    public void quickChek(){
        boolean exit = false;
        String linebreak = "\n\t======================================================\n\t";
        while(!exit){
            System.out.println("please enter full name: ");
            String input = new Scanner(System.in).nextLine().trim();
            if(tsaFilter.contains(input)){
                
                System.out.println(
                    linebreak +
                      "  There is a hit! Please pull full records to confirm"
                      + linebreak.substring(0, linebreak.length()-2)
                    );
            }else System.out.println(linebreak+"\t\tClean records"+linebreak.substring(0, linebreak.length()-2));
            System.out.print("\nTo Return Enter 0\nTo check again, Press Enter\n");
            
            input = new Scanner(System.in).nextLine().trim();
            try{
                if(Integer.parseInt(input)==0) exit = true;
                else continue;
            }catch(NumberFormatException error){
                continue;
            }
            
        }
    }

    public void addRecord(String dbFilename){
        File dbfile =  new File(dbFilename);

        boolean stop = false;
        while(!stop){
            Person p;
            System.out.println("\nYou are about to add a new record to the database\n\t\tPlease input details below");
            System.out.println("===================================================\n\n");
            String[] attr = {
                "First name: ", "Middle name: ", "Last name: ", "Age: ",
                "DoB(dd/mm/yy): ", "Country: ", "Weight: ", "Height: "
            };
            int count = 1;
            String[] temp = new String[8];
            for(String att: attr){
                System.out.print("\n"+count+". "+att);
                String input = new Scanner(System.in).nextLine().trim();
                temp[count-1] = input;
                count = count + 1;
            }
            try{
            
                p = new Person(temp[0], temp[1], temp[2], Integer.parseInt(temp[3]),
                temp[4], temp[5], Integer.parseInt(temp[6]), Integer.parseInt(temp[7])
                );
            }catch(Exception e){
                System.out.println("\nInvalid data please try again\n");
                continue;
            }
            try{
                if(dbfile.exists() && dbfile.canWrite() && dbfile.setWritable(true, false)){
                    FileWriter db = new FileWriter(dbfile);
                    db.append(p.getSignature());
                    tsaFilter.addItem(p.getFullName());
                    tsaHashtable.put(p.getFullName(), p);
                }
            }catch(Exception e){
                System.out.println("\nSorry! Something went wrong\nWe couldn't add the entry");
            }


            System.out.println("========================");
            System.out.println("\nRecord added\nPress 0 to return\n\nPress enter to add another");
            
            String input = new Scanner(System.in).nextLine().trim();
            try{
                if(Integer.parseInt(input)==0) stop = true;
            }catch(NumberFormatException error){
                continue;
            }
        }
            
    }
    
    public void getFullRecord(){

        System.out.println("Please input full name ");
        String key = new Scanner(System.in).nextLine().trim();
        System.out.println("Requesting record for "+key);
        Person data = getHashData(key);
        if(data!=null){

            System.out.println(
                "\n==============================="+
                "\nName: "+ data.getFullName() +
                "\nAge: "+ data.getAge() +
                "\nDate of Birth: " + data.getDoB()+
                "\nCountry: "+ data.getCountry()+
                "\nHeight: "+ data.getHeight() + 
                "\nWeight: "+data.getWeight()+
                "\n===============================\n"


                );
        }else{
            System.out.println(
                "\n=========================================\n"+
                "\tSnap! that was a false Positive"+
                "\n==========================================\n"
            );
        }


    }
    public Person getHashData(String key){
        Person data =  tsaHashtable.get(key);
        return data;
    }
    public static void main(String[] args){
        TSA tsa = new TSA();
        tsa.bootstrap("db.txt");

        new Scanner(System.in).nextLine();
        
        tsa.interactiveMode();
    }
}


