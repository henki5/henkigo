package Model;

public class Board {

    public Board(int size) {
        this(size, size);
    }

    public Board(int width, int height) {
        // create empty board
        grid = new Player[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                int[] ij = {i, j};
                setStone(ij, Player.EMPTY);
            }
        }
    }

    public void setStone(int[] pos, Player stone) {
        grid[pos[0]][pos[1]] = stone;
    }

    public Player getStone(int[] pos) {
        return getStone(pos[0], pos[1]);
    }

    public Player getStone(int x, int y) {
        return grid[x][y];
    }

    private Player[][] grid;
}
