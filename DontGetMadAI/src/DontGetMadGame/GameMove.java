package DontGetMadGame;

public class GameMove
{
    public byte player;
    public byte marble;
    public byte whereTo;
    public byte swapToPlayer;
    public byte swapToMarble;
    public byte roll;

    public GameMove(byte player, byte marble, byte whereTo, byte swapToPlayer, byte swapToMarble, byte roll)
    {
        this.player = player;
        this.marble = marble;
        this.whereTo = whereTo;
        this.swapToPlayer = swapToPlayer;
        this.swapToMarble = swapToMarble;
        this.roll = roll;
    }

    public GameMove()
    {
        this((byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1);
    }
}
