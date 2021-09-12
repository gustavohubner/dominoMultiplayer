/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.DominoPiece;
import dominoMultiplayer.classes.Domino;
import java.net.URL;
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
        for (int j = 0; j < childrens.size(); j++) {
            ((Text) childrens.get(j)).setText(" ");

        }

        game = new Domino();

    }

    public void gridClick() {
        //dominoGrid.setMargin(childrens.get(10), new Insets(0, 0, 0, -24));
        if (x >= X_LIMIT) {
            x = 0;
            y++;
        }
        if (y >= Y_LIMIT) {
            y = 0;
        }
        if (!game.end()) {
            DominoPiece p = game.getNextPiece();
            ((Text) childrens.get(x + y * X_LIMIT)).setText(horPieces[p.getA()][p.getB()]);
            x++;
        } else {
            System.err.println("empty!!");
        }

        //System.out.println("" + verPieces + "    " + unicodeChar(verPieces, 5));
    }

}
