import DontGetMadGame.GameEngine;
import DontGetMadGame.GameMove;
import DontGetMadGame.GameState;
import Players.Player;
import java.util.Random;

public class AITester
{
    Player[] players;
    int numPlayers;

    public AITester(Player p0, Player p1)
    {
        this(p0, null, p1, null);
        numPlayers = 2;
    }

    public AITester(Player p0, Player p1, Player p2)
    {
        this(p0, p1, p2, null);
        numPlayers = 3;
    }

    public AITester(Player p0, Player p1, Player p2, Player p3)
    {
        players = new Player[4];
        players[0] = p0;
        players[1] = p1;
        players[2] = p2;
        players[3] = p3;
        numPlayers = 4;
    }

    public int[] run(int games)
    {
        GameState game;
        byte winner;
        Random random = new Random();
        int[] wins = new int[] {0, 0, 0, 0};
        for(int i = 0; i < games; i++)
        {
            game = new GameState((byte)numPlayers);
            winner = -1;
            while (winner == -1)
            {
                Player player = players[game.currP];
                GameMove move = player.play(game.clone(), (byte) (random.nextInt(6) + 1));
                GameEngine.applyMove(game, move);
                winner = GameEngine.getWinner(game);
            }
            wins[winner]++;
        }
        return wins;
    }
}
