package apalabrados;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {


    //representación del GRID
    @FXML
    private Button checkButton;
    @FXML
    private Label infoLabel;
    @FXML private TextField t00; @FXML private TextField t01; @FXML private TextField t02; @FXML private TextField t03; @FXML private TextField t04;
    @FXML private TextField t10; @FXML private TextField t11; @FXML private TextField t12; @FXML private TextField t13; @FXML private TextField t14;
    @FXML private TextField t20; @FXML private TextField t21; @FXML private TextField t22; @FXML private TextField t23; @FXML private TextField t24;
    @FXML private TextField t30; @FXML private TextField t31; @FXML private TextField t32; @FXML private TextField t33; @FXML private TextField t34;
    @FXML private TextField t40; @FXML private TextField t41; @FXML private TextField t42; @FXML private TextField t43; @FXML private TextField t44;
    @FXML private TextField t50; @FXML private TextField t51; @FXML private TextField t52; @FXML private TextField t53; @FXML private TextField t54;

    private TextField[][] grid = new TextField[6][5];
    private int currentRow = 0;
    private String target = "CARTA";
    private List<String> words = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarPalabras();
        EscogerPalabra();
        constructorCeldas();
        ActualizarFilaActiva();
        infoLabel.setText("Adivina la palabra (5 letras)");
    }

    private void cargarPalabras() {
        try (InputStream is = getClass().getResourceAsStream("/words.txt")) {
            if (is == null) return;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim().toUpperCase();
                    if (line.length() == 5) words.add(line);
                }
            }
        } catch (IOException e) {
            //si falla, se queda la palabra por defecto
        }
    }

    private void EscogerPalabra() {
        if (!words.isEmpty()) {
            Random r = new Random();
            target = words.get(r.nextInt(words.size()));
        }
    }

    private void constructorCeldas() {
        grid[0][0] = t00; grid[0][1] = t01; grid[0][2] = t02; grid[0][3] = t03; grid[0][4] = t04;
        grid[1][0] = t10; grid[1][1] = t11; grid[1][2] = t12; grid[1][3] = t13; grid[1][4] = t14;
        grid[2][0] = t20; grid[2][1] = t21; grid[2][2] = t22; grid[2][3] = t23; grid[2][4] = t24;
        grid[3][0] = t30; grid[3][1] = t31; grid[3][2] = t32; grid[3][3] = t33; grid[3][4] = t34;
        grid[4][0] = t40; grid[4][1] = t41; grid[4][2] = t42; grid[4][3] = t43; grid[4][4] = t44;
        grid[5][0] = t50; grid[5][1] = t51; grid[5][2] = t52; grid[5][3] = t53; grid[5][4] = t54;

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {
                TextField tf = grid[r][c];
                if (tf == null) continue;
                tf.setTextFormatter(new javafx.scene.control.TextFormatter<String>(change -> {
                    if (change.getText() != null) change.setText(change.getText().toUpperCase());
                    if (change.getControlNewText().length() > 1) return null;
                    return change;
                }));
                final int rowIndex = r;
                final int colIndex = c;
                tf.setOnKeyTyped(ev -> {
                    if (tf.getText() != null && tf.getText().length() == 1 && colIndex < 4) {
                        grid[rowIndex][colIndex + 1].requestFocus();
                    }
                });
            }
        }
    }

    private void ActualizarFilaActiva() {
        for (int r = 0; r < 6; r++) {
            boolean enabled = r == currentRow;
            for (int c = 0; c < 5; c++) {
                grid[r][c].setDisable(!enabled);
            }
        }
    }

    @FXML
    private void onCheck() {
        if (currentRow >= 6) return;
        String guess = FilasTexto(currentRow);
        if (guess.length() != 5) {
            infoLabel.setText("Adivina la palabra");
            return;
        }
        guess = guess.toUpperCase();
        pintarResultado(currentRow, guess, target);
        if (guess.equals(target)) {
            infoLabel.setText("¡Correcto! La palabra era " + target);
            finalizarJuego();
            return;
        }
        currentRow++;
        if (currentRow == 6) {
            infoLabel.setText("Has perdido! La palabra era " + target);
            finalizarJuego();
        } else {
            ActualizarFilaActiva();
            grid[currentRow][0].requestFocus();
        }
    }

    private void finalizarJuego() {
        checkButton.setDisable(true);
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {
                grid[r][c].setDisable(true);
            }
        }
    }

    private String FilasTexto(int row) {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < 5; c++) {
            String t = grid[row][c].getText();
            if (t == null) t = "";
            t = t.trim().toUpperCase();
            if (t.length() == 0) return ""; //si falta alguna fila
            sb.append(t.charAt(0));
        }
        return sb.toString();
    }

    private void pintarResultado(int row, String guess, String word) {
        boolean[] used = new boolean[5];
        for (int i = 0; i < 5; i++) used[i] = false;

        for (int i = 0; i < 5; i++) {
            TextField tf = grid[row][i];
            char g = guess.charAt(i);
            char w = word.charAt(i);
            if (g == w) {
                tf.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: white; -fx-font-weight: bold;");
                used[i] = true;
            } else {
                tf.setStyle("-fx-background-color: #b0bec5;");
            }
        }
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == word.charAt(i)) continue;
            TextField tf = grid[row][i];
            char g = guess.charAt(i);
            int pos = indiceSinUso(word, g, used);
            if (pos != -1) {
                tf.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: black; -fx-font-weight: bold;");
                used[pos] = true;
            }
        }
        //desactivar fila actual
        for (int c = 0; c < 5; c++) grid[row][c].setDisable(true);
    }

    private int indiceSinUso(String word, char ch, boolean[] used) {
        for (int i = 0; i < 5; i++) {
            if (!used[i] && word.charAt(i) == ch) return i;
        }
        return -1;
    }
}
