/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.DominoPiece;
import dominoMultiplayer.classes.Domino;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author gusta
 */
public class FXMLDocumentController implements Initializable {

    private Client client;
    private Server server;

    private int X_LIMIT = 11, Y_LIMIT = 5;
    private ObservableList<Node> pieceGrid;
    private int hash;
    private int selecIndex = -1;
    private int selecSide = 0;

    List<DominoPiece> left;
    List<DominoPiece> right;
    DominoPiece start;

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
    private GridPane menuScreen;
    @FXML
    private GridPane matchMaking;
    @FXML
    private GridPane gameScreen;

    @FXML
    private FlowPane playerHand;
    @FXML
    private Button buyPiece;
    @FXML
    private Button passBtn;
    @FXML
    private Button hostBtn;
    @FXML
    private Button joinBtn;
    @FXML
    private TextField addressInput;
    @FXML
    private Text queueText;
    @FXML
    private Text turnIndicator;

    private LinkedList<DominoPiece> hand;

    public FXMLDocumentController() {

    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gameScreen.setVisible(false);
        menuScreen.setVisible(true);
        matchMaking.setVisible(false);

        pieceGrid = dominoGrid.getChildren();
//        game = new Domino();
//        hash = game.addPlayer();
        clearScreen();

        playerHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selecIndex = -1;
//                drawGrid(game);
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

//                if (!game.end()) {
//                    if (selecIndex != -1 && selecSide != -1) {
//                        boolean b = game.addPiece(selecSide, hash, selecIndex);
//                        System.out.println("add resp: " + (b ? "sucess" : "fail"));
//                        selecIndex = -1;
//                    }
//                }
//                drawGrid(game);
            }
        });
//        drawGrid(game);

    }

    @FXML
    public void buyPiece() {
        client.buyPiece();
//        boolean bougth = game.buyPiece(hash);
//        drawGrid(game);
//        System.out.println("bougth: " + bougth);
//        buyPiece.setDisable(!bougth);
    }

    public void drawGrid() {
        clearScreen();
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

    public void joinBtn() {
        try {
            System.err.println("" + InetAddress.getByName(addressInput.getText()));
            client = new Client(InetAddress.getByName(addressInput.getText()), 42069, this);
            Thread t = new Thread(client);
            t.start();
        } catch (Exception ex) {
            connectionError(addressInput.getText());
        }

    }

    public void passBtn() {
        client.pass();
    }

    public void hostBtn() {
        try {
            server = new Server(InetAddress.getByName(addressInput.getText()));
            joinBtn();
        } catch (Exception ex) {
            connectionError(addressInput.getText());
        }
    }

    public void startGame() {
        matchMaking.setVisible(false);
        gameScreen.setVisible(true);
    }

    public void quitBtn() {
        client.close();
        System.out.println("dominoMultiplayer.FXMLDocumentController.quitBtn()");
        matchMaking.setVisible(false);
        menuScreen.setVisible(true);
    }

    public void connectionError(String address) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Connection Error");
        alert.setHeaderText(null);
        alert.setContentText("Error while trying to connect to " + address);
        alert.showAndWait();
    }

    public void connectionSucess() {
        System.out.println("Sucessful Conection");
        menuScreen.setVisible(false);
        matchMaking.setVisible(true);
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public void setHandString(String handStr) {
        this.hand = StringToDominoList(handStr);
        System.out.println("" + hand);
        drawGrid();
    }

    public void setTableString(String start, String left, String right) {
        if (!start.endsWith("null")) {
            this.start = StringToDominoList(start).pop();
        }
        this.left = StringToDominoList(left);
        this.right = StringToDominoList(right);
    }

    public void setQueue(int queueSize) {
        queueText.setText(queueSize + " / 4 players");
    }

    public void setMyTurn(boolean myTurn) {
        System.err.println("MY TURN? " + myTurn);
        buyPiece.setDisable(!myTurn);
        passBtn.setDisable(!myTurn);
        playerHand.setDisable(!myTurn);
        turnIndicator.setVisible(myTurn);
    }

    private LinkedList<DominoPiece> StringToDominoList(String str) {
        System.out.println("conver string: " + str);
        LinkedList<DominoPiece> list = new LinkedList<>();
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '[' && c[i + 1] != '[') {
                if (c[i + 1] != ']') {
                    list.add(new DominoPiece(Integer.parseInt(c[i + 1] + ""), Integer.parseInt(c[i + 3] + "")));
                }
            }
        }
        return list;
    }
}
