package Players;

import DontGetMadGame.GameMove;
import DontGetMadGame.GameState;

public abstract class Player
{
    public abstract GameMove play(GameState state, byte roll);
}
