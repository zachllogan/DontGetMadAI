package DontGetMadGame;

import java.util.Arrays;

public class GameState
{
    public static final byte[] HOMES = {40, 10, 20, 30};
    public static final byte[] SPAWNS = {1, 11, 21, 31};

    public boolean[] players;

    public byte[] blockedTurns;
    public byte[][] marbles;
    public byte currP;

    public GameState()
    {
        currP = 0;
        blockedTurns = new byte[] {0, 0, 0, 0};
        marbles = new byte[][] {{0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}};
        players = new boolean[] {false, false, false, false};
    }

    public GameState(byte numPlayers)
    {
        this();
        switch (numPlayers)
        {
            case 1: players = new boolean[] {true, false, false, false}; break;
            case 2: players = new boolean[] {true, false, true, false}; break;
            case 3: players = new boolean[] {true, true, true, false}; break;
            case 4: players = new boolean[] {true, true, true, true}; break;
        }
    }

    public GameState(byte numPlayers, GameMove[] moves)
    {
        this(numPlayers);
        GameEngine.applyMoves(this, moves);
    }

    public GameState(boolean[] players, byte[] blockedTurns, byte[][] marbles, byte currP)
    {
        this.players = players;
        this.blockedTurns = blockedTurns;
        this.marbles = marbles;
        this.currP = currP;
    }

    public GameState clone()
    {
        boolean[] players = this.players.clone();
        byte[] blockedTurns = this.blockedTurns.clone();
        byte[][] marbles = Arrays.stream(this.marbles)
                .map(a ->  Arrays.copyOf(a, a.length))
                .toArray(byte[][]::new);
        GameState newGame = new GameState(players, blockedTurns, marbles, currP);
        return newGame;
    }
}
