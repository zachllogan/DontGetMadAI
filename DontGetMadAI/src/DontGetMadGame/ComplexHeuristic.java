package DontGetMadGame;

import Players.Heuristic;

public class ComplexHeuristic extends Heuristic
{
    int w1, w2, w3, w4;

    public ComplexHeuristic()
    {
        this(1, 1, 1, 1);
    }

    public ComplexHeuristic(int w1, int w2, int w3, int w4)
    {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.w4 = w4;
    }

    @Override
    public int score(GameState state)
    {
        int score = 0;
        score += distancesFromHome(state)*w1;
        score -= opponentsDistsFromHome(state)*w2;
        score -= opponentThreats(state)*w3;
        score += homeScore(state)*w4;
        return score;
    }

    private int distancesFromHome(GameState state)
    {
        int dist = 0;
        for (int i = 0; i < 4; i++)
        {
            if(state.marbles[state.currP][i] >= 0)
            {
                dist += distanceFromHome(state, state.currP, i);
            }
        }
        return dist;
    }

    private int opponentsDistsFromHome(GameState state)
    {
        int dist = 0;
        for (int i = 0; i < 4; i++)
        {
            if(i == state.currP || !state.players[i]) { continue; }
            for (int j = 0; j < 4; j++)
            {
                if(state.marbles[i][j] >= 0)
                {
                    dist += distanceFromHome(state, i, j);
                }
            }
        }
        return dist;
    }

    private int distanceFromHome(GameState state, int i, int j)
    {
        if(state.marbles[i][j] == 0)
        {
            return 40;
        }
        byte loc = state.marbles[i][j];
        return 40 - ((loc - 10*i)%40);
    }

    private int opponentThreats(GameState state)
    {
        int threats = 0;
        for (byte i = 0; i < 4; i++)
        {
            if(i == state.currP || !state.players[i]) { continue; }
            for (byte j = 0; j < 4; j++)
            {
                if(state.marbles[i][j] > 0)
                {
                    byte excess = GameEngine.excessPastHome(state, i, j, (byte)6);
                    int maxRoll = 6;
                    if(excess > 0) {maxRoll -= excess;}
                    for (int k = 0; k < 4; k++)
                    {
                        if(state.marbles[i][j] < state.marbles[state.currP][k] && state.marbles[i][j] + maxRoll >= state.marbles[state.currP][k])
                        {
                            threats++;
                        }
                        else if(GameEngine.wrap((byte)(state.marbles[i][j] + maxRoll)) <= maxRoll && GameEngine.wrap((byte)(state.marbles[i][j] + maxRoll)) >= state.marbles[state.currP][k])
                        {
                            threats++;
                        }
                    }
                }
            }
        }
        return threats;
    }

    private int homeScore(GameState state)
    {
        int score = 0;
        for (int i = 0; i < 4; i++)
        {
            if(state.marbles[state.currP][i] < 0)
            {
                score += Math.pow(state.marbles[state.currP][i], 2);
            }
        }
        return score;
    }
}
