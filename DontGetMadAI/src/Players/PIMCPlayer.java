package Players;

import DontGetMadGame.GameEngine;
import DontGetMadGame.GameMove;
import DontGetMadGame.GameState;
import Tree.GenericTree;

import java.util.Random;

public class PIMCPlayer extends Player
{
    double c;
    int d;
    int s;
    int p;
    double e;
    boolean bias;
    Heuristic h;

    PIMCPlayer(int p, double c, int d, int s, double e, boolean bias, Heuristic h)
    {
        this.p = p;
        this.c = c;
        this.d = d;
        this.s = s;
        this.e = e;
        this.bias = bias;
        this.h = h;
    }

    public PIMCPlayer(int p, int d, int s, double e, boolean bias, Heuristic h)
    {
        this(p, 0.2, d, s, e, bias, h);
    }

    public PIMCPlayer(int p, double c, int d, int s)
    {
        this(p, c, d, s, 1, false, null);
    }

    public PIMCPlayer(int p, int d, int s)
    {
        this(p, 0.2, d, s, 1, false, null);
    }

    public PIMCPlayer()
    {
        this(0, 0.2, 1, 1, 1, false, null);
    }

    @Override
    public GameMove play(GameState state, byte roll)
    {
        GameMove[] moves = GameEngine.getMoves(state, roll);
        if(moves.length == 0)
        {
            return null;
        }
        Random random = new Random();
        double[] moveWinRates = new double[moves.length];
        for(int i = 0; i < d; i++)
        {
            arrayAdd(moveWinRates, determinizedPlay(state.clone(), roll, random.nextLong()));
        }
        int maxI = 0;
        for (int i = 0; i < moveWinRates.length; i++)
        {
            if(moveWinRates[i] > moveWinRates[maxI])
            {
                maxI = i;
            }
        }
        return moves[maxI];
    }

    private int rollNum = 0;
    private double[] determinizedPlay(GameState state, byte roll, long seed)
    {
        byte[] rolls = generateRolls(seed, 10000);
        rolls[0] = roll;
        MCTSTreeValue root = new MCTSTreeValue(state.clone());
        GenericTree<MCTSTreeValue> tree = new GenericTree<>(root);
        for (int i = 0; i < s; i++)
        {
            rollNum = 0;
            //System.out.println("selecting...");
            GenericTree<MCTSTreeValue> selected = Selection(tree, rolls);
            byte winner = GameEngine.getWinner(selected.getValue().state);
            if(winner != -1)
            {
                backpropagate(selected, winner);
                continue;
            }
            GenericTree<MCTSTreeValue> expanded = Expansion(selected, rolls);
            byte score = playout(expanded, rolls);
            backpropagate(expanded, score);
        }
        //System.out.println("FINISHED!");
        MCTSTreeValue[] children = tree.getChildrenVals(MCTSTreeValue.class);
        double[] winRates = new double[children.length];
        for (int i = 0; i < children.length; i++)
        {
            winRates[i] = children[i].wins/children[i].visits;
        }
        return winRates;
    }

    private GenericTree<MCTSTreeValue> Selection(GenericTree<MCTSTreeValue> tree, byte[] rolls)
    {
        MCTSTreeValue[] children = tree.getChildrenVals(MCTSTreeValue.class);
        GameState state = tree.getValue().state.clone();
        int oldRollNum = rollNum;
        passTurns(state, rolls);
        while(children.length == GameEngine.getMoves(state, rolls[rollNum%rolls.length]).length && children.length != 0)
        {
            double maxUCB = 0;
            int maxUCBI = 0;
            double UCB;
            for (int i = 0; i < children.length; i++)
            {
                UCB = children[i].wins/children[i].visits + c*Math.sqrt(Math.log(tree.getValue().visits)/children[i].visits) + children[i].score/(children[i].visits+1);
                if(UCB > maxUCB)
                {
                    maxUCB = UCB;
                    maxUCBI = i;
                }
            }
            //System.out.println("used roll #" + rollNum + ": " + rolls[rollNum]);
            rollNum++;
            tree = tree.getChildren()[maxUCBI];
            children = tree.getChildrenVals(MCTSTreeValue.class);
            state = tree.getValue().state.clone();
            oldRollNum = rollNum;
            passTurns(state, rolls);
        }
        //System.out.println("selected roll #" + rollNum + ": " + rolls[rollNum]);
        //System.out.println(children.length + ", " + GameEngine.getMoves(state, rolls[rollNum%rolls.length]).length);
        rollNum = oldRollNum;
        return tree;
    }

    private void passTurns(GameState state, byte[] rolls)
    {
        if(GameEngine.getWinner(state) != -1) { return; }
        GameMove[] moves = GameEngine.getMoves(state, rolls[rollNum%rolls.length]);
        while(moves.length == 0)
        {
            //System.out.println("passed roll #" + rollNum + ": " + rolls[rollNum%rolls.length]);
            GameEngine.applyMove(state, null);
            rollNum++;
            moves = GameEngine.getMoves(state, rolls[rollNum%rolls.length]);
        }
    }

    private GenericTree<MCTSTreeValue> Expansion(GenericTree<MCTSTreeValue> selected, byte[] rolls)
    {
        GameState newState = selected.getValue().state.clone();
        passTurns(newState, rolls);
        GameMove[] moves = GameEngine.getMoves(newState, rolls[rollNum%rolls.length]);
        GameEngine.applyMove(newState, moves[selected.getChildren().length]);
        MCTSTreeValue child = new MCTSTreeValue(newState);
        if(h != null)
        {
            child.score = h.score(child.state);
        }
        selected.addChild(child);
        //System.out.println("expanded roll #" + rollNum + ": " + rolls[rollNum]);
        rollNum++;
        return selected.getChildren()[selected.getChildren().length-1];
    }

    private byte playout(GenericTree<MCTSTreeValue> expanded, byte[] rolls)
    {
        RandomPlayer randomPlayer = new RandomPlayer();
        GreedyPlayer greedyPlayer = new GreedyPlayer(h);
        GameState game = expanded.getValue().state.clone();
        byte nodePlayer = game.currP;
        byte winner = -1;
        int count = 0;
        while(winner == -1)
        {
            Player player;
            if(e == 1 || Math.random() <= e) { player = randomPlayer; }
            else { player = greedyPlayer; }
            GameEngine.applyMove(game, player.play(game, rolls[rollNum%rolls.length]));
            rollNum++;
            count++;
            winner = count >= 10000 ? -2 : GameEngine.getWinner(game);
        }
        return winner;
    }

    private void backpropagate(GenericTree<MCTSTreeValue> expanded, byte winner)
    {
        GenericTree<MCTSTreeValue> node = expanded;

        while(node.getParent() != null)
        {
            node.getValue().visits++;
            if(node.getParent().getValue().state.currP == winner)
            {
                node.getValue().wins += 1;
            }
            node = node.getParent();
        }
    }

    private byte[] generateRolls(long seed, int num)
    {
        byte[] rolls = new byte[num];
        Random random = new Random(seed);
        for (int i = 0; i < num; i++)
        {
            rolls[i] = (byte)(random.nextInt(6)+1);
        }
        return rolls;
    }

    private void arrayAdd(double[] base, double[] addition)
    {
        for (int i = 0; i < base.length; i++)
        {
            base[i] += addition[i];
        }
    }
}
