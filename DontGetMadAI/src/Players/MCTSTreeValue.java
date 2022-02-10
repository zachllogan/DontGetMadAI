package Players;

import DontGetMadGame.GameState;

public class MCTSTreeValue
{
    public GameState state;
    public double visits;
    public double wins;
    public double score;

    public MCTSTreeValue(GameState state, double visits, double wins, double score)
    {
        this.state = state;
        this.visits = visits;
        this.wins = wins;
        this.score = score;
    }

    public MCTSTreeValue(GameState state, Heuristic h)
    {
        this(state, 0, 0, h.score(state));
    }

    public MCTSTreeValue(GameState state)
    {
        this(state, 0, 0, 0);
    }
}
