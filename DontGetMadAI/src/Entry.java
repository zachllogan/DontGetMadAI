import DontGetMadGame.ComplexHeuristic;
import DontGetMadGame.GameEngine;
import DontGetMadGame.GameMove;
import DontGetMadGame.GameState;
import Players.*;

import java.util.Random;

public class Entry
{
    public static void main(String args[])
    {
        Player randomPlayer = new RandomPlayer();
        Heuristic balancedHeuristic = new ComplexHeuristic(1, 10, 0, 100);
        Player greedyPlayer = new GreedyPlayer(balancedHeuristic);
        Player basic2pPIMCPlayer = new PIMCPlayer(2, 25, 100);
        Player greedy2pPIMCPlayer = new PIMCPlayer(2, 3, 100, 0.05, false, new ComplexHeuristic(1, 10, 0, 100));
        Player greedy2pPIMCPlayer2 = new PIMCPlayer(2, 25, 100, 0.50, false, new ComplexHeuristic(1, 10, 0, 100));
        Player biased2pPIMCPlayer = new PIMCPlayer(2, 25, 100, 0.05, false, new ComplexHeuristic(1, 10, 0, 100));
        Player biasedGreedy2pPIMCPlayer = new PIMCPlayer(2, 3, 100, 0.05, false, new ComplexHeuristic(1, 10, 0, 100));

        int[] wins = new AITester(randomPlayer, basic2pPIMCPlayer).run(100);
        System.out.println(wins[0] + ", " + wins[1] + ", " + wins[2] + ", " + wins[3]);
    }
}
