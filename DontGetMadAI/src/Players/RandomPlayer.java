package Players;

import DontGetMadGame.GameEngine;
import DontGetMadGame.GameMove;
import DontGetMadGame.GameState;

import java.util.Random;

public class RandomPlayer extends Player
{
    @Override
    public GameMove play(GameState state, byte roll)
    {
        GameMove[] moves = GameEngine.getMoves(state, roll);
        if(moves.length == 0)
        {
            return null;
        }
        Random random = new Random();
        return moves[random.nextInt(moves.length)];
    }
}
