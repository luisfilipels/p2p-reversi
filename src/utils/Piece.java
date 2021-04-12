package utils;

import gameLogic.GameControllerSingleton;
import javafx.scene.shape.Circle;

public class Piece extends Circle {

    final int r, c;

    GameControllerSingleton gameController;

    public Piece(int v, int v1, int v2, int r, int c, GameControllerSingleton controller) {
        super(v, v1, v2);
        this.r = r;
        this.c = c;
        this.gameController = controller;

        this.setOnMouseClicked(mouseEvent -> {
            gameController.setBoardPosition(r,c);
        });
    }

}
