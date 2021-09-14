/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.DominoPiece;
import dominoMultiplayer.classes.Domino;
import dominoMultiplayer.classes.DominoTable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author gusta
 */
public class FXMLDocumentController implements Initializable {

    ObservableList<Node> childrens;
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

    Domino game;

    int x = 0, y = 0, X_LIMIT = 10, Y_LIMIT = 5;

    @FXML
    private GridPane dominoGrid;

    public FXMLDocumentController() {

    }

    @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        childrens = dominoGrid.getChildren();
        clearScreen();
        game = new Domino();

    }

    public void gridClick() {
        if (!game.end()) {
            game.getTable().addRight(game.getNextPiece());
        }
        if (!game.end()) {
            game.getTable().addLeft(game.getNextPiece());
        }
        drawGrid(game.getTable());
    }

    public void drawGrid(DominoTable t) {
        List<DominoPiece> left = t.getLeftSide();
        List<DominoPiece> right = t.getRightSide();
        DominoPiece start = t.getStartPiece();

        int l_count, r_count;

        System.out.println("" + left.size() + "   " + right.size());

        setPiece(5, 2, start);

        for (l_count = 0; l_count < left.size(); l_count++) {
            DominoPiece p = left.get(l_count);
            Integer[] pos = path[path.length - l_count - 1];
            setPiece(pos[1], pos[0], p);
        }

        for (r_count = 0; r_count < right.size(); r_count++) {
            DominoPiece p = right.get(r_count);
            Integer[] pos = path[r_count];
            setPiece(pos[1], pos[0], p);
        }
    }

    private void clearScreen() {
        for (int i = 0; i < childrens.size(); i++) {
            ((Text) (childrens.get(i))).setText(" ");
        }
    }

    private void setPiece(int x, int y, DominoPiece p) {
        System.out.println(p.getA() + " " + p.getB() + "");
        if (x == 10 || x == 0 || ((x == 3 || x == 7) && y < 2)) {
            ((Text) childrens.get(x + y * 11)).setText(verPieces[p.getA()][p.getB()]);
        } else {
            ((Text) childrens.get(x + y * 11)).setText(horPieces[p.getA()][p.getB()]);
        }
    }

}
