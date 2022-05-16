import java.io.*;
import java.util.*;
import java.lang.*;

public class choiceGenerator {
    private String Answer;
    private int first[];
    private int second[] ;
    private int third[] ;
    private int fourth[] ;
    private int fifth[] ;
    private int overall[];
    private String resultCheck= "22222";
    private ArrayList<poolOfChoice> choicesOrigin;
    private ArrayList<poolOfChoice> choicesOne;
    private ArrayList<poolOfChoice> choicesTwo;
    public ArrayList<String> words;
    public choiceGenerator()// generates and populates all of the nessecary fields
    {
        this.first = new int[26];
        this.second= new int[26];
        this.third = new int[26];
        this.fourth= new int[26];
        this.fifth = new int[26];
        this.overall= new int[26];
        for(int i = 0 ; i < 26 ; i++)
        {
            this.first[i]=0;
            this.second[i]=0;
            this.third[i]=0;
            this.fourth[i]=0;
            this.fifth[i]=0;
            this.overall[i]=0;
        }
        this.words=new ArrayList<String>();
        this.choicesOrigin = new ArrayList<>();
        this.choicesOne = new ArrayList<>();
        this.choicesTwo = new ArrayList<>();
        System.out.println("all letter tables made");
    }
    public void generatePool(File file)// takes all of the information stored in the file and adds it to words
    {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                this.words.add(line);
            }
        }
        catch (Exception e) {
            System.out.println("come inside loop to check logs.label update faild");
        }
    }
    public void selectAnswer( int i)
    {
        this.Answer = this.words.get(i);
    }//self expalanatory
    public int analyze()
    {
        for( String x : this.words) // gets data for optimal choice from words
        {
            tracker(x.charAt(0), this.first, overall);
            tracker(x.charAt(1), this.second, overall);
            tracker(x.charAt(2), this.third, overall);
            tracker(x.charAt(3), this.fourth, overall);
            tracker(x.charAt(4), this.fifth, overall);
        }
        for( String x : words)//scores the words based on likely hood to contain a locking letter and stores it in choicesOrigin
        {
            int score =  first[x.charAt(0)-97]+second[x.charAt(1)-97]+third[x.charAt(2)-97]+fourth[x.charAt(3)-97]+fifth[x.charAt(4)-97];
            choicesOrigin.add(new poolOfChoice(x , score));
        }
        // should move the item with the largest score to the top
        // note: without the clear functions no actual sorting would happen as it would just push the new sorting to the bottom
        // and the next geuss will just be the next unremoved word from the first round of analysis
        sort(choicesOrigin,choicesTwo);
        String result =guessCheck(choicesTwo.get(0).word);// checks if the guessed letters are hits locks or misses. meaning that the answer contains a letter in the same location, conatains the letter, or doesnt contain that letter.
        System.out.println( result+"   "+choicesTwo.get(0).word +"    "+this.Answer);// displays data
        if(!result.equals(resultCheck)) // check if it is the correct guess
        {
            filter(result, choicesTwo.get(0).getWord());
            choicesOrigin.clear();
            choicesTwo.clear();
            return 0;
        }
        return 1;
    }
    public static void sort( ArrayList<poolOfChoice> old , ArrayList<poolOfChoice>New)
    {
        int currPosition=0;
        int removePosition=0;

        while (!old.isEmpty()) {
            poolOfChoice toADD = new poolOfChoice(old.get(0).getWord(), old.get(0).getScore());
            for (poolOfChoice x : old)
            {
                if(x.score>toADD.score)
                {
                    toADD.setScore(x.getScore());
                    toADD.setWord(x.getWord());
                    removePosition= currPosition;
                }
                currPosition++;
            }
            old.remove(removePosition);
            New.add(toADD);
            removePosition=0;
            currPosition=0;
        }
    }
    public void tracker(char a, int column[], int overall[])//adds to frequency position of the letter
    {
        int i = a - 97;
        column[i]++;
        overall[i]++;
    }
    public String guessCheck(String guess)// genertates miss hit or lock string
    {
        String response="";

        for( int i = 0; i< this.Answer.length(); i++)
        {
            int j=0;
            if(this.Answer.charAt(i) == guess.charAt(i))
            {
                response=response+"2";
                j=2;
            }
            else
            {
                for(int t=0; t< this.Answer.length();t++  )
                {
                    if(guess.charAt(i)==this.Answer.charAt(t))
                    {
                        response=response+"1";
                        j=1;
                        break;
                    }
                }
            }
            if (j==0)
            {
                response=response+"0";
            }
        }
        return response;
    }
    public void filter(String result, String guess)
    {
        char pos[]= new char[5];
        for(int i =0; i<guess.length();i++)
        {pos[i]=guess.charAt(i);}
        this.choicesTwo.remove(0);
        for(int i =0 ; i<result.length();i++)
        {
            if (result.charAt(i)=='2')
            {
                removeLock(pos[i], i );
            }
            if(result.charAt(i)=='1')
            {
                removeHit(pos[i], i);
            }
            if (result.charAt(i) == '0')
            {
                removeMiss(pos[i]);
            }
        }
        this.words.clear();
        for(poolOfChoice x : choicesTwo)
        {
            this.words.add(x.word);
        }
    }
    public void removeLock(char let, int pos)//appears to work flawlessly
    {
        Iterator<poolOfChoice> x = choicesTwo.iterator();// creates a list to iterate through
        while (x.hasNext())//check if there is a next element
        {
            poolOfChoice y=x.next();// both moves the nex element and get info from it
            if (let!=y.word.charAt(pos))
            {
                x.remove();
            }
        }
    }
    public void removeHit(char let, int pos)//if the word does not contain the letter remove. if letter in the hit location remove
    {

        Iterator<poolOfChoice> x = choicesTwo.iterator();
        while(x.hasNext())
        {
                poolOfChoice y= x.next();
                int hasLetter=0;
                int hasLetterInPosition=0;
                for(int t = 0; t<y.word.length();t++)
                {
                    if(let== y.word.charAt(t))//letter is in word. if it does keep
                    {
                        hasLetter=1;
                    }
                    if((let==y.word.charAt(t)) && (pos ==t)) //hasLetterInPosition so remove
                    {
                        hasLetterInPosition=1;
                    }
                }
                if(hasLetter==0 || hasLetterInPosition==1)
                {
                    x.remove();
                }
        }
    }
    public void removeMiss(char let)// the let should not be in the word so if at any point it appears in the word remove it
    {
        Iterator<poolOfChoice> x = choicesTwo.iterator();
        while(x.hasNext())
        {
            poolOfChoice y = x.next();
            for(int t = 0; t<y.word.length();t++)
            {
                if(let== y.word.charAt(t))
                {
                    x.remove();
                    break;
                }
            }
        }
    }
    public void columnReset()
    {
        for(int i = 0 ; i < 26 ; i++)
        {
            this.first[i]=0;
            this.second[i]=0;
            this.third[i]=0;
            this.fourth[i]=0;
            this.fifth[i]=0;
            this.overall[i]=0;
        }
    }
}
