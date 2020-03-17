import java.io.*;
import java.lang.module.FindException;
import java.util.Dictionary;
import java.util.Comparator; 
import java.util.Collections; 

import java.util.*;
import java.nio.file.*;

class userInput{
    int frequency; 
    String word;
    
    public userInput(int frequency, String word){
        this.frequency = frequency; 
        this.word = word; 
    }
}

class wordCompare implements Comparator<userInput>{
    @Override
    public int compare(userInput word1, userInput word2){
        return word1.word.compareTo(word2.word); 
    }
}

class frequencyCompare implements Comparator<userInput>{
    @Override
    public int compare(userInput frequency1, userInput frequency2){
        return frequency2.frequency - frequency1.frequency; 
    }
}

public class ac_test {
    static StringBuilder prefix = new StringBuilder(); 
    static char input; 
    public static DLB userDLB; 
    public static DLB dictionaryDLB; 
    static ArrayList<String> getfromsuggestion;  
    static ArrayList<String> getall; 
    static ArrayList<String> getfromUserHistory; 
    public static final int NUMBER_OF_WORDS = 5; 
    public static final String DICTIONARY = "dictionary.txt"; 
    public static final String USER = "user_history.txt"; 
    public static ArrayList<String> finalArray= new ArrayList<String>(); 
    public static long time; 
    public static int totalEntries = 0; 

    public static DLB createDLB(String file_name, boolean hasFrequency){
        File input = null; 
        DLB trie = new DLB(); 
        String line;  
        try{
            input = new File(file_name); 
            Scanner scanner = new Scanner(input);
            if(hasFrequency){
                while(scanner.hasNext()){
                    line = scanner.nextLine(); 
                    String[] pair = line.split(" "); 
                    int frequency = Integer.parseInt(pair[0]); 
                    String key = pair[1]; 
                    trie.add(key,frequency); 
                }
            }else{
                while(scanner.hasNext()){
                    line = scanner.nextLine(); 
                    trie.add(line, 0);
                }
            }
            scanner.close();

        }
        catch(FileNotFoundException e){
            System.out.println("Cannot find the file " + file_name + " ,creating one......");
        }
        return trie; 

    }

    public static void SortUserFile(){
        ArrayList<userInput> readtxt = new ArrayList<userInput>(); 
        BufferedReader bf; 
        BufferedWriter wirteFile; 
        PrintWriter out = null; 
        String line; 
        try{
            bf = new BufferedReader(new FileReader(USER)); 
            while((line = bf.readLine())!=null){
                String [] info = line.split(" "); 
                int frequency = Integer.valueOf(info[0]); 
                String word = info[1]; 
                readtxt.add(new userInput(frequency,word)); 
            }
            Collections.sort(readtxt,new frequencyCompare()); 
            wirteFile = new BufferedWriter(new FileWriter(USER)); 
            out = new PrintWriter(wirteFile); 
            for(userInput input:readtxt){
                out.println(input.frequency + " " + input.word);
            }
            bf.close();
            wirteFile.close();
            out.close();
        }catch(IOException e){
            System.out.println("Cannot open the file " + USER); 
        }
    }

    public static void addtoUserHistory(){
        FileWriter userfile = null; 
        BufferedWriter bf = null; 
        PrintWriter out = null; 
        ArrayList<String> wholeArray = userDLB.getAll();
        //System.out.println(wholeArray); 
        try{
            userfile = new FileWriter(USER); 
            bf = new BufferedWriter(userfile); 
            out = new PrintWriter(bf); 
            for(int i=0; i<wholeArray.size();i++){
                out.println(wholeArray.get(i));
            }
            bf.close();
            out.close();
        }catch(IOException e){
            System.out.print("Error occur");
        }
    }

    public static ArrayList<String> getPredictions(char input){
        finalArray = new ArrayList<String>(NUMBER_OF_WORDS);
        getfromUserHistory = userDLB.search(input); 
        int size; 
        int remainNumber; 
        if(getfromUserHistory!=null){
            size = getfromUserHistory.size(); 
        }else{
            size = 0; 
        }
        for(int i=0;i<size;i++){
            finalArray.add(getfromUserHistory.get(i)); 
            //System.out.println(getfromUserHistory);
        }
        remainNumber = NUMBER_OF_WORDS-size;
        if(remainNumber==NUMBER_OF_WORDS){
            getfromsuggestion = dictionaryDLB.search(input); 
            if(getfromsuggestion == null){
                return finalArray; 
            }
            for(int i=0; i<getfromsuggestion.size();i++){
                finalArray.add(getfromsuggestion.get(i)); 
            }
        }
        else if(size<NUMBER_OF_WORDS){
            getfromsuggestion = dictionaryDLB.search(input); 
            if(getfromsuggestion == null){
                return finalArray; 
            }
            for(int i=0;i<NUMBER_OF_WORDS;i++){
                if(!getfromUserHistory.contains(getfromsuggestion.get(i))){
                    finalArray.add(getfromsuggestion.get(i)); 
                }
                if(finalArray.size()==NUMBER_OF_WORDS){
                    break; 
                }
            }
        } 
        return finalArray;  
    } 

    public static void addTime(long totalTime){
        time += totalTime; 
    }

    public static void printAverageTime(){
        time = time/totalEntries; 
        System.out.format("Average time: %fs \n",(double)time/1000000000.0);
        System.out.println("Bye!");
    }

    public static void StartProgram(){
        Scanner scan = new Scanner(System.in);

        userDLB.initialSearch();
        dictionaryDLB.initialSearch();
        System.out.println("Start Programming: "); 
        String word; 
        while ( input != '!'){
            System.out.print("Enter your first character: ");
            input = scan.nextLine().charAt(0);  
            if(input == '!'){
                break; 
            }
            if(input == '$'){
                word = prefix.toString(); 
                userDLB.add(word, 1);
                userDLB.initialSearch();
                dictionaryDLB.initialSearch(); 
                prefix = new StringBuilder("");
            }
            else if('0'<input && input<'6'){
                int position = Character.getNumericValue(input); 
                word = finalArray.get(position-1); 
                System.out.println("WORD COMPLETE: " + word);
                userDLB.add(word, 1);
                userDLB.initialSearch();
                dictionaryDLB.initialSearch();
                prefix = new StringBuilder(""); 
            }
            else{
                prefix = prefix.append(input); 
                long Starttime = System.nanoTime(); 
                finalArray = getPredictions(input); 
                long Endtime = System.nanoTime(); 
                long totalTime = Endtime - Starttime; 
                addTime(totalTime);
                System.out.println("");
                System.out.format("(%fs)\n",(float)totalTime/1000000000.0);
                totalEntries ++; 
            }
            if(finalArray!=null){
                System.out.println(finalArray);
                System.out.println(""); 
            }
        }
    }

    public static void main(String args[]){
        userDLB = createDLB(USER, true); 
        dictionaryDLB = createDLB(DICTIONARY, false);
        //DLB small = createDLB("small.txt", false); 
        StartProgram();
        addtoUserHistory(); 
        SortUserFile(); 
        printAverageTime(); 
    }

}