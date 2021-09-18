/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.DominoPiece;
import dominoMultiplayer.classes.Domino;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author gusta
 */
public class FXMLDocumentController implements Initializable {

    int X_LIMIT = 11, Y_LIMIT = 5;
    ObservableList<Node> pieceGrid;
    Domino game;
    int hash;
    int selecIndex = -1;
    int selecSide = 0;

    Text lastLeft;
    Text lastRight;

    String[][] horPieces
            = {{"🀱", "🀲", "🀳", "🀴", "🀵", "🀶", "🀷"},
            {"🀸", "🀹", "🀺", "🀻", "🀼", "🀽", "🀾"},
            {"🀿", "🁀", "🁁", "🁂", "🁃", "🁄", "🁅"},
            {"🁆", "🁇", "🁈", "🁉", "🁊", "🁋", "🁌"},
            {"🁍", "🁎", "🁏", "🁐", "🁑", "🁒", "🁓"},
            {"🁔", "🁕", "🁖", "🁗", "🁘", "🁙", "🁚"},
            {"🁛", "🁜", "🁝", "🁞", "🁟", "🁠", "🁡"},
            {"🀰"}
            };

    String[][] verPieces
            = {{"🁣", "🁤", "🁥", "🁦", "🁧", "🁨", "🁩"},
            {"🁪", "🁫", "🁬", "🁭", "🁮", "🁯", "🁰"},
            {"🁱", "🁲", "🁳", "🁴", "🁵", "🁶", "🁷"},
            {"🁸", "🁹", "🁺", "🁻", "🁼", "🁽", "🁾"},
            {"🁿", "🂀", "🂁", "🂂", "🂃", "🂄", "🂅"},
            {"🂆", "🂇", "🂈", "🂉", "🂊", "🂋", "🂌"},
            {"🂍", "🂎", "🂏", "🂐", "🂑", "🂒", "🂓"},
            {"🁢"}};

    Integer[][] path = {{2, 6},
    {2, 7},
    {1, 7},
    {0, 7},
    {0, 8},
    {0, 9},
    {0, 10},
    {1, 10},
    {2, 10},
    {3, 10},
    {4, 10},
    {4, 9},
    {4, 8},
    {4, 7},
    {4, 6},
    {4, 5},
    {4, 4},
    {4, 3},
    {4, 2},
    {4, 1},
    {4, 0},
    {3, 0},
    {2, 0},
    {1, 0},
    {0, 0},
    {0, 1},
    {0, 2},
    {0, 3},
    {1, 3},
    {2, 3},
    {2, 4}};

    @FXML
    private GridPane dominoGrid;
    @FXML
    private FlowPane playerHand;
    @FXML
    private Button buyPiece;

    public FXMLDocumentController() {

    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pieceGrid = dominoGrid.getChildren();
        game = new Domino();
        hash = game.addPlayer();
        clearScreen();

        playerHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selecIndex = -1;
                drawGrid(game);
                if (mouseEvent.getTarget().getClass() == Text.class) {
                    Text t = (Text) mouseEvent.getTarget();
                    System.out.println("" + t.getText() + " index:" + playerHand.getChildren().indexOf(t) + " side: " + selecSide);
                    selecIndex = playerHand.getChildren().indexOf(t);

                    t.setFill(javafx.scene.paint.Color.RED);
                }
            }
        });
        dominoGrid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getTarget().getClass() == Text.class) {
                    Text t = (Text) mouseEvent.getTarget();
                    if (t.getText().equals(lastLeft.getText())) {
                        selecSide = 0;
                        System.out.println("left selectect");
                    }
                    if (t.getText().equals(lastRight.getText())) {
                        selecSide = 1;
                        System.out.println("right selectect");
                    }
                }

                if (!game.end()) {
                    if (selecIndex != -1 && selecSide != -1) {
                        boolean b = game.addPiece(selecSide, hash, selecIndex);
                        System.out.println("add resp: " + (b ? "sucesse" : "fail"));
                        selecIndex = -1;
                    }
                }
                drawGrid(game);
            }
        });
        drawGrid(game);

    }

    @FXML
    public void buyPiece() {
        boolean bougth = game.buyPiece(hash);
        drawGrid(game);
        System.out.println("bougth: "+ bougth);
        buyPiece.setDisable(!bougth);
    }

    public void drawGrid(Domino game) {
        clearScreen();
        List<DominoPiece> left = game.getLeftSide();
        List<DominoPiece> right = game.getRightSide();
        DominoPiece start = game.getStartPiece();
        int l_count, r_count;
        if (start != null) {
            Integer[] l = path[0];
            Integer[] r = path[path.length - 1];
            setPiece(5, 2, start);
            lastRight = ((Text) pieceGrid.get(path[0][1] + path[0][0] * X_LIMIT));
            lastLeft = ((Text) pieceGrid.get(path[path.length - 1][1] + path[path.length - 1][0] * X_LIMIT));

            ((Text) pieceGrid.get(path[0][1] + path[0][0] * X_LIMIT)).setText("⇨");
            ((Text) pieceGrid.get(path[path.length - 1][1] + path[path.length - 1][0] * X_LIMIT)).setText("⇦");
        }

        if (left != null) {
            for (l_count = 0; l_count < left.size(); l_count++) {
                DominoPiece p = left.get(l_count);
                Integer[] pos = path[path.length - l_count - 1];
                lastLeft = setPiece(pos[1], pos[0], p);
            }
        }
        if (right != null) {
            for (r_count = 0; r_count < right.size(); r_count++) {
                DominoPiece p = right.get(r_count);
                Integer[] pos = path[r_count];
                lastRight = setPiece(pos[1], pos[0], p);
            }
        }

        LinkedList<DominoPiece> hand = game.getPlayerHand(hash);
        DominoPiece p;
        for (int i = 0; i < hand.size(); i++) {
            p = hand.get(i);
            ((Text) playerHand.getChildren().get(i)).setText(verPieces[p.getA()][p.getB()]);
            ((Text) playerHand.getChildren().get(i)).setFill(javafx.scene.paint.Color.BLACK);
        }
    }

    private void clearScreen() {
        for (int i = 0; i < pieceGrid.size(); i++) {
            ((Text) (pieceGrid.get(i))).setText(" ");
        }
        for (int i = 0; i < playerHand.getChildren().size(); i++) {
            ((Text) playerHand.getChildren().get(i)).setText(" ");
        }
    }

    private Text setPiece(int x, int y, DominoPiece p) {
        ((Text) pieceGrid.get(x + y * X_LIMIT)).setText(horPieces[p.getA()][p.getB()]);
        if (p == null) {
            ((Text) pieceGrid.get(x + y * X_LIMIT)).setText(" ");
        }
        if (x == 7 && y < 2) {
            ((Text) pieceGrid.get(x + y * X_LIMIT)).setText(verPieces[p.getB()][p.getA()]);
        }
        if (x == 3 && y < 2) {
            ((Text) pieceGrid.get(x + y * X_LIMIT)).setText(verPieces[p.getA()][p.getB()]);
        }
        if (y == 4) {
            ((Text) pieceGrid.get(x + y * X_LIMIT)).setText(horPieces[p.getB()][p.getA()]);
        }
        if (x == 10) {
            ((Text) pieceGrid.get(x + y * X_LIMIT)).setText(verPieces[p.getA()][p.getB()]);
        }
        if (x == 0) {
            ((Text) pieceGrid.get(x + y * X_LIMIT)).setText(verPieces[p.getB()][p.getA()]);
        }

        return ((Text) pieceGrid.get(x + y * X_LIMIT));
    }

    @FXML
    public void clickPlayerHand(MouseEvent event) {
        Text target = (Text) event.getTarget();
        System.out.println("Index piece: " + pieceGrid.indexOf(target));
    }
}
