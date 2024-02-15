package View;

import Controller.GameController;
import Model.Helper;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.fxml.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameView extends Application {

    private GameController controller;
    private TilePane boardView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("henkigo");
        primaryStage.setResizable(false);

        // temporary settings
        final int BOARD_SIZE = 19;

        // A GameController is already created when loading the fxml for the right layout,
        // this controller should also be used for GameView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
        Pane rightLayout = loader.load();
        controller = loader.<GameController>getController();
        controller.setup(this, BOARD_SIZE);


        boardView = createBoardView(BOARD_SIZE);
        GridPane layout = new GridPane();
        layout.add(boardView, 0, 0);
        layout.add(rightLayout, 1, 0);

        final int height = BOARD_SIZE * 41;
        final int width = height + 300;
        Scene scene = new Scene(layout, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setBoardView(TilePane board) {
        boardView = board;

    }

    public TilePane getBoardView() {
        return boardView;
    }

    public TilePane createBoardView(int size) {
        //load images for board
        URL middleURL = getClass().getResource("/middle_tile.png");
        URL edgeURL = getClass().getResource("/edge_tile.png");
        URL cornerURL = getClass().getResource("/corner_tile.png");
        URL starURL = getClass().getResource("/star_tile.png");

        Image middleImage = new Image(middleURL.toString());
        Image edgeImage = new Image(edgeURL.toString());
        Image cornerImage = new Image(cornerURL.toString());
        Image starImage = new Image(starURL.toString());

        /* Find star points */
        ArrayList<int[]> starPoints = new ArrayList<>();
        final int mid = (size-1)/2;

        // tengen
        if (size % 2 == 1) {
            starPoints.add(new int[]{mid, mid});
        }

        // corner stars
        if (size >= 11) {
            starPoints.add(new int[]{3, 3});
            starPoints.add(new int[]{3, size-4});
            starPoints.add(new int[]{size-4, 3});
            starPoints.add(new int[]{size-4, size-4});
        }
        else if (size >= 8) {
            starPoints.add(new int[]{2, 2});
            starPoints.add(new int[]{2, size-3});
            starPoints.add(new int[]{size-3, 2});
            starPoints.add(new int[]{size-3, size-3});
        }

        // edge stars
        if (size >= 17) {
            starPoints.add(new int[]{mid, 3});
            starPoints.add(new int[]{3, mid});
            starPoints.add(new int[]{size-4, mid});
            starPoints.add(new int[]{mid, size-4});
        }

        /* start creating the board as a tilepane */
        TilePane boardLayout = new TilePane();
        boardLayout.setPrefColumns(size);
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                ImageView tileImageView;
                int[] pos = {j, i};

                // check for corners
                if (Arrays.equals(pos, new int[]{0, 0})) {
                    tileImageView = new ImageView(cornerImage);
                }
                else if (Arrays.equals(pos, new int[]{size-1, 0})) {
                    tileImageView = new ImageView(cornerImage);
                    tileImageView.setRotate(90);
                }
                else if (Arrays.equals(pos, new int[]{0, size-1})) {
                    tileImageView = new ImageView(cornerImage);
                    tileImageView.setRotate(270);
                }
                else if (Arrays.equals(pos, new int[]{size-1, size-1})) {
                    tileImageView = new ImageView(cornerImage);
                    tileImageView.setRotate(180);
                }

                // check for edges
                else if (pos[1] == 0) {
                    tileImageView = new ImageView(edgeImage);
                }

                else if (pos[0] == size-1) {
                    tileImageView = new ImageView(edgeImage);
                    tileImageView.setRotate(90);
                }
                else if (pos[1] == size-1) {
                    tileImageView = new ImageView(edgeImage);
                    tileImageView.setRotate(180);
                }
                else if (pos[0] == 0) {
                    tileImageView = new ImageView(edgeImage);
                    tileImageView.setRotate(270);
                }

                // check for star point
                else if (Helper.listContainsArray(starPoints, pos)) {
                    tileImageView = new ImageView(starImage);
                }

                // normal intersection
                else {
                    tileImageView = new ImageView(middleImage);
                }
                StackPane tile = new StackPane(tileImageView);
                boardLayout.getChildren().add(tile);

            }
        }
        boardLayout.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            controller.onBoardPressed(x, y);
        });
        return boardLayout;
    }


}
