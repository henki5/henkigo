package Controller;

import Model.Game;
import Model.Player;
import View.GameView;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private GameView gameView;
    private Game game;
    public Button passButton;
    public Button resignButton;
    public Text message;
    public Text blackCapturesText;
    public Text whiteCapturesText;

    public GameController() {
        message = new Text();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setup(GameView gameView, int size) {
        this.gameView = gameView;
        this.game = new Game(size);
        message.setText("Black to Play");
    }

    public void updateBoard() {
        TilePane newBoard = gameView.getBoardView();
        final int size = game.getBoardSize();

        // Empty boardView
        for (int i = 0; i < size*size; ++i) {
            ObservableList<Node> sp = ((StackPane) newBoard.getChildren().get(i)).getChildren();
            if (sp.size() == 2) {
                sp.remove(1);
            }
        }

        Image whiteImage = new Image(getClass().getResource("/white_stone.png").toString());
        Image blackImage = new Image(getClass().getResource("/black_stone.png").toString());
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                Player color = game.getBoard().getStone(new int[]{i, j});
                if (color == Player.BLACK) {
                    ((StackPane) newBoard.getChildren().get(size*j + i)).getChildren().add(
                            new ImageView(blackImage)
                    );
                }
                else if (color == Player.WHITE) {
                    ((StackPane) newBoard.getChildren().get(size*j + i)).getChildren().add(
                            new ImageView(whiteImage)
                    );
                }
            }
        }
        gameView.setBoardView(newBoard);
        blackCapturesText.setText("Black captures: " + 2 + game.getBlackCaptures());
        whiteCapturesText.setText("White captures: " + game.getWhiteCaptures());
    }

    public void onBoardPressed(double x, double y) {
        final int tileSize = 41;
        int i = (int) x/41;
        int j = (int) y/41;
        game.placeStone(new int[]{i, j}, game.getTurn());
        updateBoard();
        updateMessage();
    }

    public void onPassed() {
        game.pass();
        updateMessage();
    }

    public void onResigned() {
        game.resign();
        if (game.getTurn() == Player.BLACK) {
            setMessage("Black resigned");
        }
        else {
            setMessage("White resigned");
        }
    }

    private void updateMessage() {
        if (game.isFinished()) {
            setMessage("Game finished");
        }
        else if (game.getTurn() == Player.BLACK) {
            setMessage("Black to play");
        }
        else {
            setMessage("White to play");
        }
    }

    public void setMessage(String msg) {
        message.setText(msg);
    }
}
