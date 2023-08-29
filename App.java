import java.io.IOException;

public class App {
    public static void main(String[] args) {
        Game testGame = new Game();
        try {
            testGame.readInData();
            testGame.learn(testGame.playGame());
            testGame.sc.close();
            testGame.writeOutData();
        }
        catch (IOException e) {
            System.out.println("Something didn't work when reading in or outputting to file, try the program again.");
            e.printStackTrace();
            testGame.sc.close();
        }
    }
}
