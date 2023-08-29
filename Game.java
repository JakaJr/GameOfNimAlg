import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;

public class Game {
    
    private static final int numSticks = 10;
    private static final int maxMove = 3;
    private static final int minMove = 1;
    private ArrayList<int[]> decisionList;
    private int[] movesLog = new int[0];
    public final Scanner sc = new Scanner(System.in);
    
    public Game() {}

    public void readInData() throws IOException {
        File f = new File("memory.txt");
        if (!f.exists()) {
            decisionList = new ArrayList<int[]>(10);
            int[] default0 = {0,0,0};
            int[] default1 = {1,-1,-1};
            int[] default2 = {-1,1,-1};
            int[] default3 = {-1,-1,1};
            decisionList.add(0, default1.clone());
            decisionList.add(1, default2.clone());
            decisionList.add(2, default3.clone());
            for (int i = 3; i < numSticks; i++) {
                decisionList.add(i, default0.clone());
            }
        }
        else {
            BufferedReader br = new BufferedReader(new FileReader(f));
            decisionList = new ArrayList<int[]>(10);
            for (int i = 0; i < 10; i++) {
                int[] tempArr = new int[3];
                String tempStr = br.readLine();
                String[] strList = tempStr.split(" ");
                for (int j = 0; j < 3; j++) {
                    tempArr[j] = Integer.parseInt(strList[j]);
                }
                decisionList.add(i, tempArr);
            }
            br.close();
        }
    }

    public int playerMove(int sticks) throws IOException{
        boolean play = true;
        int returnStick = sticks;
        while(play) {
            System.out.println("How many sticks?");
            int pMove = sc.nextInt();
            if(pMove <= sticks && pMove >= Game.minMove && pMove <= Game.maxMove){
                System.out.println("You took " + pMove + " sticks.");
                returnStick -= pMove;
                play = false;
            }
            else {
                System.out.println("Invalid Move, Try again.");
            }
        }
        return returnStick;
    }

    public int compMove(int sticks) {
        int score = -20000000;
        int pick = 0;
        int[] currDecision = decisionList.get(sticks - 1);
        for (int i = 0; i < currDecision.length; i++) {
            if (currDecision[i] >= score) {
                score = currDecision[i];
                pick = i + 1;
            }
        }
        System.out.println("Computer took " + pick + " sticks.");
        
        int[] tempLog = {sticks, pick};
        int newLength = this.movesLog.length + tempLog.length;
        int[] newLog = new int[newLength];
        int i = 0;
        for (int element : this.movesLog) {
            newLog[i] = element;
            i++;    
        }
        for (int element : tempLog) {
            newLog[i] = element;
            i++;    
        }
        this.movesLog = newLog.clone();

        sticks -= pick;
        return sticks;
    }
    
    public int playGame() {
        int currSticks = numSticks;
        boolean play = true;
        System.out.println("Game of NIM:\nThere are " + currSticks + " Sticks.");
        System.out.print("You can pick ");
        for(int i = 1; i < Game.maxMove; i++){
            System.out.print(i + ", ");
        }
        System.out.println("or " + Game.maxMove + " sticks.");
        while(play){
            currSticks = compMove(currSticks);
            System.out.println(currSticks + " Sticks left.\n");
            if(currSticks <= 0) {
                System.out.println("The Computer took the final stick. \n You Lose :(");
                return 1;
            }
            System.out.println("Your Turn");
            try {
                currSticks = playerMove(currSticks);
            }
            catch(Exception IOException) {
                System.out.println("Error: IOException. Input accepts only integers.\nExiting Program.");
                return 0;
            }
            if(currSticks <= 0) {
                System.out.println("You took the final stick. \n~You win :)");
                return -1;
            }
        }
        return 0;
    }

    public void learn(int result) {
        for (int i = 0; i < (this.movesLog.length/2); i++) {
            int[] tempList = decisionList.get(movesLog[2 * i] - 1);
            tempList[movesLog[(2*i)+1] - 1] = tempList[movesLog[(2*i)+1] - 1] + result;
        }
    }

    public void writeOutData() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("memory.txt"));  
        for (int[] listEl : decisionList) {
            for (int i = 0; i < listEl.length; i++) {
                if (listEl[i] >= 0) {
                    bw.write("+" + listEl[i] + " ");
                }
                else {
                    bw.write(listEl[i] + " ");
                }
            }
            bw.newLine();
        }  
        bw.close();  
    }

    // public void printCurrMemory() {
    //     for (int i = 0; i < decisionList.size(); i++) {
    //         System.out.print("[");
    //         int[] temp = decisionList.get(i);
    //         for (int j = 0; j < temp.length - 1; j++) { 
    //             System.out.print(temp[j] + ", ");
    //         }
    //         System.out.println(temp[temp.length - 1] + "]");
    //     }
    // }

    // public void printLog() {
    //     System.out.print("[");
    //     for (int element : movesLog) {
    //         System.out.print(element + " ");
    //     }
    //     System.out.println("] " + this.movesLog.length);
    // }
}