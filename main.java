import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.concurrent.ThreadLocalRandom;

public class  main{
    /* thoughts
       if i use letter frequency analysis on the poisition of letters after the filyer to bring to the top of my
       selection list the word with the highest chances for having a letter in one of those positions i.e. most likely
       to lock a letter to a position thus decreasing the words to chose from. must be ran after each filter due to changes in the word pool
    */
    public static void  main(String[] args)
    {

        int totalRuns = 1000 ;
        int hits=0;

        int gotIt = 0;
        for(int i=0 ;i<totalRuns;i++ ) {
            File file = new File("C:\\Users\\Zachariah\\Desktop\\wordle Project\\english_5_leters.txt");
            choiceGenerator run= new choiceGenerator();
            run.generatePool(file);

            int attempts=0;
            int randomNum = ThreadLocalRandom.current().nextInt(0, run.words.size() + 1);
            run.selectAnswer(randomNum);
            while(gotIt==0 && attempts<6 ){
                gotIt= run.analyze();
                run.columnReset();
                attempts++;
            }
            if(gotIt==1)
            {
                    hits++;
            }
            gotIt=0;
            System.out.println(i+"    hits:"+hits);
        }

    }




}