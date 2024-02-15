package Model;

import Model.Board;
import Model.Player;

import java.util.ArrayList;

public class Game {

    public Game(int size) {
        boardSize = size;
        board = new Board(size);
        turn = Player.BLACK;
        blackCaptures = 0;
        whiteCaptures = 0;
        passCount = 0;
        isFinished = false;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Board getBoard() {
        return board;
    }

    public Player getTurn() {
        return turn;
    }

    public int getBlackCaptures() {
        return blackCaptures;
    }

    public int getWhiteCaptures() {
        return whiteCaptures;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void pass() {
        if (turn == Player.BLACK) {
            turn = Player.WHITE;
        }
        else {
            turn = Player.BLACK;
        }
        ++passCount;
        if (passCount >= 2) {
            isFinished = true;
        }
    }

    public void resign() {
        isFinished = true;
    }

    public void placeStone(int[] pos, Player stoneColor) {
        if (isLegal(pos, stoneColor)) {
            passCount = 0;
            board.setStone(pos, stoneColor);

            // check for captures
            ArrayList<int[]> neighbours = getNeighbours(pos);
            ArrayList<int[]> enemyNeighbours = new ArrayList<>();
            for (int[] nb : neighbours) {
                Player color = board.getStone(nb);
                if (color == getOppositeColor(stoneColor) && color != Player.EMPTY) {
                    enemyNeighbours.add(nb);
                }
            }
            for (int[] nb : enemyNeighbours) {
                ArrayList<int[]> enemyGroup = getGroup(nb);
                ArrayList<int[]> enemyGroupLiberties = getGroupLiberties(enemyGroup);
                if (enemyGroupLiberties.isEmpty()) {
                    for (int[] groupMember : enemyGroup) {
                        // getGroup(nb) might return the same group multiple times, thus this check is necessary
                        // to avoid making an error when counting captures.
                        if (board.getStone(groupMember) != Player.EMPTY) {
                            board.setStone(groupMember, Player.EMPTY);
                            if (stoneColor == Player.BLACK) {
                                ++blackCaptures;
                            }
                            else {
                                ++whiteCaptures;
                            }
                        }
                    }
                }
            }

            // TODO: check for KO and update KO status

            // change turn
            turn = getOppositeColor(turn);

        }
    }

    private boolean isLegal(int[] pos, Player stoneColor) {
        /* Game is finished */
        if (isFinished) {
            return false;
        }

        /* Wrong player's turn */
        if (turn != stoneColor) {
            return false;
        }

        /* Space already occupied */
        if (board.getStone(pos) != Player.EMPTY) {
            return false;
        }

        /* Illegal suicide move */
        ArrayList<int[]> neighbours = getNeighbours(pos);
        ArrayList<int[]> friendlyNeighbours = new ArrayList<>();
        ArrayList<int[]> enemyNeighbours = new ArrayList<>();

        // fetch friendly & enemy neighbours... insert code
        for (int[] nb : neighbours) {
            if (board.getStone(nb) == stoneColor) {
                friendlyNeighbours.add(nb);
            }
            else if (board.getStone(nb) == getOppositeColor(stoneColor)) {
                enemyNeighbours.add(nb);
            }
        }
        // if no surrounding liberties
        if (friendlyNeighbours.size() + enemyNeighbours.size() == neighbours.size()) {

            // check if stoneColor has liberties
            boolean illegalSuicide = true;
            for (int[] nb : friendlyNeighbours) {
                ArrayList<int[]> group = getGroup(nb);
                ArrayList<int[]> groupLiberties = getGroupLiberties(group);
                if (groupLiberties.size() > 1) {
                    illegalSuicide = false;
                    break;
                }
            }

            // check for suicide capture
            if (illegalSuicide) {
                for (int[] nb : enemyNeighbours) {
                    ArrayList<int[]> group = getGroup(nb);
                    ArrayList<int[]> groupLiberties = getGroupLiberties(group);
                    if (groupLiberties.size() == 1) {
                        illegalSuicide = false;
                    }
                }
            }
            if (illegalSuicide) return false;
        }

        /* TODO: illegal KO move */

        return true;
    }

    private Player getOppositeColor(Player color) {
        if (color == Player.BLACK) {
            return Player.WHITE;
        }
        else if (color == Player.WHITE) {
            return Player.BLACK;
        }
        else return Player.EMPTY;
    }

    // Fetch all stones in group with a stone at (x,y).
    private ArrayList<int[]> getGroup(int[] pos) {
        return getGroup(pos, board.getStone(pos), new ArrayList<>());
    }

    private ArrayList<int[]> getGroup(int[] pos, Player groupColor, ArrayList<int[]> visited) {
        ArrayList<int[]> group = new ArrayList<>();
        if (!Helper.listContainsArray(visited, pos)) {
            visited.add(pos);
            if (board.getStone(pos) == groupColor) {
                group.add(pos);
                ArrayList<int[]> neighbours = getNeighbours(pos);
                for (int[] space : neighbours) {
                    group.addAll(getGroup(space, groupColor, visited));
                }
            }
        }
        return group;
    }

    // Get all positions of a group's liberties.
    private ArrayList<int[]> getGroupLiberties(ArrayList<int[]> group) {
        ArrayList<int[]> liberties = new ArrayList<>();
        for (int[] space : group) {
            ArrayList<int[]> localLiberties = getNeighbours(space);
            for (int[] lib : localLiberties) {
                if (board.getStone(lib) == Player.EMPTY && !Helper.listContainsArray(liberties, lib)) {
                    liberties.add(lib);
                }
            }
        }
        return liberties;
    }

    // Get adjacent positions to (x,y).
    private ArrayList<int[]> getNeighbours(int[] pos) {
        int x = pos[0];
        int y = pos[1];
        ArrayList<int[]> neighbours = new ArrayList<>();
        int[][] candidates = {{x+1, y}, {x, y-1}, {x-1, y}, {x, y+1}};

        // only add neighbours who aren't out of bounds
        for (int[] space : candidates) {
            if (space[0] >= 0 && space[0] < boardSize && space[1] >= 0 && space[1] < boardSize) {
                neighbours.add(space);
            }
        }
        return neighbours;
    }

    private Board board;
    private int boardSize;
    private Player turn;
    private int blackCaptures;
    private int whiteCaptures;
    private int passCount;
    private boolean isFinished;
}
