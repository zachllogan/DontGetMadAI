package DontGetMadGame;

import java.util.ArrayList;

public class GameEngine
{
    public static GameMove[] getMoves(GameState state, byte roll)
    {
        ArrayList<GameMove> moves = new ArrayList<>();
        byte[] currPMarbles = state.marbles[state.currP];
        boolean noCircuitMarble = true;
        boolean spawnMoveAdded = false;
        for(byte i = 0; i < 4; i++)
        {
            if(currPMarbles[i] < 0)
            {
                if(currPMarbles[i] - roll >= -4 && noMarbleBetween(currPMarbles, (byte)(currPMarbles[i] - roll), (byte)(currPMarbles[i] - 1)))
                {
                    moves.add(new GameMove(state.currP, i, (byte) (currPMarbles[i] - roll), (byte) -1, (byte) -1, roll));
                }
                continue;
            }
            else if(currPMarbles[i] == 0)
            {
                if(!spawnMoveAdded && (roll == 1 || roll == 6) && noMarbleBetween(currPMarbles, GameState.SPAWNS[state.currP], GameState.SPAWNS[state.currP]))
                {
                    moves.add(new GameMove(state.currP, i, GameState.SPAWNS[state.currP], (byte) -1, (byte) -1, roll));
                    spawnMoveAdded = true;
                }
                continue;
            }
            noCircuitMarble = false;
            if(roll == 4)
            {
                for(byte p = 0; p < 4; p++)
                {
                    if(state.players[p] && p != state.currP)
                    {
                        for(byte j = 0; j < 4; j++)
                        {
                            if(state.marbles[p][j]>0)
                            {
                                moves.add(new GameMove(state.currP, i, (byte)-1, p, j, roll));
                            }
                        }
                    }
                }
            }
            byte excess = excessPastHome(state, currPMarbles[i], roll);
            if(excess <= 0)
            {
                if(noMarbleBetween(currPMarbles, wrap((byte)(currPMarbles[i]+roll)), wrap((byte)(currPMarbles[i]+roll))))
                {
                    moves.add(new GameMove(state.currP, i, wrap((byte) (currPMarbles[i] + roll)), (byte) -1, (byte) -1, roll));
                }
            }
            else if(excess <=4 && noMarbleBetween(currPMarbles, (byte)(-excess), (byte)(-1)))
            {
                moves.add(new GameMove(state.currP, i, (byte)(-excess), (byte) -1, (byte) -1, roll));
            }
        }
        if(moves.size() == 0 && noCircuitMarble)
        {
            if(state.blockedTurns[state.currP] >= 3)
            {
                for(byte i = 0; i < 4; i++)
                {
                    if(currPMarbles[i] == 0)
                    {
                        moves.add(new GameMove(state.currP, i, GameState.SPAWNS[state.currP], (byte) -1, (byte) -1, roll));
                        break;
                    }
                }
            }
        }
        return moves.toArray(new GameMove[]{});
    }

    static boolean noMarbleBetween(byte[] marbles, byte lower, byte upper)
    {
        for(byte i = 0; i < 4; i++)
        {
            if(marbles[i] >= lower && marbles[i] <= upper)
            {
                return false;
            }
        }
        return true;
    }

    static byte excessPastHome(GameState state, byte marble, byte roll)
    {
        if(marble <= state.HOMES[state.currP])
        {
            return (byte)((marble + roll) - state.HOMES[state.currP]);
        }
        return (byte)0;
    }

    public static byte excessPastHome(GameState state, byte player, byte marble, byte roll)
    {
        if(marble <= state.HOMES[player])
        {
            return (byte)((marble + roll) - state.HOMES[player]);
        }
        return (byte)0;
    }

    public static byte wrap(byte loc)
    {
        return loc > 40 ? (byte)(loc - 40) : loc;
    }

    public static void applyMoves(GameState state, GameMove[] moves)
    {
        if(moves == null)
        {
            return;
        }
        for (GameMove move : moves)
        {
            applyMove(state, move);
        }
    }

    public static void applyMove(GameState state, GameMove move)
    {
        if(move == null || move.player == -1)
        {
            if(move == null)
            {
                boolean noCircuitMarble = true;
                for (int i = 0; i < 4; i++)
                {
                    if(state.marbles[state.currP][i] > 0) { noCircuitMarble = false; break; }
                }
                if(noCircuitMarble) { state.blockedTurns[state.currP]++; }
            }
            passTurn(state);
            return;
        }
        state.blockedTurns[move.player] = 0;
        byte marbleLoc = state.marbles[move.player][move.marble];
        if(move.swapToPlayer != -1)
        {
            marbleLoc = state.marbles[move.player][move.marble];
            state.marbles[move.player][move.marble] = state.marbles[move.swapToPlayer][move.swapToMarble];
            state.marbles[move.swapToPlayer][move.swapToMarble] = marbleLoc;
            passTurn(state);
            return;
        }
        state.marbles[move.player][move.marble] = move.whereTo;
        if(!(move.roll == 6 || move.roll == 1))
        {
            passTurn(state);
        }
    }

    static void passTurn(GameState state)
    {
        byte player;
        for(byte i = 1; i < 4; i++)
        {
            player = (byte)((i+state.currP)%4);
            if(state.players[player])
            {
                state.currP = player;
                return;
            }
        }
    }

    public static byte getWinner(GameState state)
    {
        for(byte i = 0; i<4; i++)
        {
            if(state.players[i])
            {
                boolean won = true;
                for (byte marble : state.marbles[i])
                {
                    if(marble >= 0)
                    {
                        won = false;
                        break;
                    }
                }
                if(won)
                {
                    return i;
                }
            }
        }
        return -1;
    }
}
