package Players;

import DontGetMadGame.GameMove;
import DontGetMadGame.GameState;

public abstract class Heuristic
{
    public abstract int score(GameState state);
}
