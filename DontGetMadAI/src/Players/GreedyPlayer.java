package Players;

import DontGetMadGame.GameEngine;
import DontGetMadGame.GameMove;
import DontGetMadGame.GameState;

import java.util.ArrayList;
import java.util.Random;

public class GreedyPlayer extends Player
{
    Heuristic h;

    public GreedyPlayer(Heuristic h)
    {
        this.h = h;
    }

    @Override
    public GameMove play(GameState state, byte roll)
    {
        GameMove[] moves = GameEngine.getMoves(state, roll);
        if(moves.length == 0)
        {
            return null;
        }
        int score;
        int bestScore = Integer.MIN_VALUE;
        ArrayList<Integer> bestScoresI = new ArrayList<>();
        for (int i = 0; i < moves.length; i++)
        {
            GameState nextState = state.clone();
            GameEngine.applyMove(nextState, moves[i]);
            score = h.score(nextState);
            if(score > bestScore)
            {
                bestScoresI.clear();
                bestScore = score;
                bestScoresI.add(i);
            }
            else if(score == bestScore)
            {
                bestScoresI.add(i);
            }
        }
        return moves[bestScoresI.get(new Random().nextInt(bestScoresI.size()))];
    }
}
