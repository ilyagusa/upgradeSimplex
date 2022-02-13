/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 *
 */
public class View {

    private BorderPane border;
    GridPane root1;
    GridPane root2;
    GridPane root;

    private HBox boxButtonMode;
    private HBox boxButtonDecision;
    private ToggleGroup groupFractionOrNubmer;
    private ToggleGroup groupDecision;
    private HBox choiceNumber;
    private ToggleGroup groupz;

    private GridPane gridSimplex;
    public String mode = "Работа с дробями";
    public String modeDecision = "Симплекс-метод";
    public String modeAutoOrNormal = "Пошаговый режим";
    private int colX = 3;//кол-во переменных
    private int colR = 2;//кол-во огр.
    private VBox vbox;
    TextField[][] arr;
    TextField[] x0;
    ComboBox comboBoxX;
    ComboBox comboBoxRestrictions;
    TableView<String[]> table;
    private ToggleGroup group;
    private int count;

    public View(Stage primaryStage) throws ClassNotFoundException, NullPointerException, WrongNumException {
        setElements(primaryStage);
    }

    private void setElements(Stage primaryStage) throws WrongNumException {
        border = new BorderPane();
        border.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        root = new GridPane();
        root2 = new GridPane();
        root2.setPadding(new Insets(10, 10, 10, 10));
        root.getColumnConstraints().add(new ColumnConstraints(350));
        root.getColumnConstraints().add(new ColumnConstraints(1100));
        createChoice();
        root.setGridLinesVisible(true); // делаем видимой сетку строк и столбцов
        root.setColumnIndex(root1, 0);
        root.setColumnIndex(root2, 1);
        root.getChildren().addAll(root1, root2);
        createMenu();
        border.setCenter(root);

        Scene scene = new Scene(border, 1450, 800);
        primaryStage.setTitle("Симплекс-метод");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createMenu() {
        MenuBar menuBar = new MenuBar();
        //Меню файл
        Menu file = new Menu("Файл");
        MenuItem mainPage = new MenuItem("Начальная страница");
        mainPage.setOnAction((ActionEvent t) -> {
            InputInfo.clear();
            setMainGrid();
        });

        MenuItem readFileFraction = new MenuItem("Чтение файла(дроби)");
        readFileFraction.setOnAction((ActionEvent t) -> {
            createChoiceFileNameFraction();
        });

        MenuItem readFileNumber = new MenuItem("Чтение файла(числа)");
        readFileNumber.setOnAction((ActionEvent t) -> {
            createChoiceFileNameNumber();
        });

        MenuItem exit = new MenuItem("Выход");
        exit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        exit.setOnAction((ActionEvent t) -> {
            Platform.exit();
        });
        file.getItems().addAll(mainPage, readFileFraction, readFileNumber, new SeparatorMenuItem(), exit);

        //Меню справка
        Menu help = new Menu("Справка");
        MenuItem helpContent = new MenuItem("Содержание справки");
        helpContent.setOnAction((ActionEvent t) -> {
            createHelpContent();
        });
        help.getItems().add(helpContent);
        menuBar.getMenus().addAll(file, help);
        border.setTop(menuBar);
    }

    private void createHelpContent() {
        Stage stageInfo = new Stage();
        GridPane paneInfo = new GridPane();

        Label labelHead = new Label("\t\t\t\tCПРАВКА");
        labelHead.setStyle("-fx-font: 26 arial;");
        paneInfo.add(labelHead, 0, 0);

        Label labelInfo = new Label("Это приложение позволяет решать задачу линейного программирования.\n");
        labelInfo.setMaxWidth(685);
        labelInfo.setMaxHeight(200);
        labelInfo.setWrapText(true);
        labelInfo.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfo, 0, 1);

        Image choice = new Image("file:data/mainChoice.jpg");
        ImageView choicePic = new ImageView();
        choicePic.setFitHeight(300);
        choicePic.setFitWidth(690);
        choicePic.setPreserveRatio(true);
        choicePic.setImage(choice);
        paneInfo.add(choicePic, 0, 2);

        Label labelInfoChoice = new Label("В данном меню выбора , изначально выбран режим работы с дробями, симплекс-метод , а также пошаговый вариант работы программы."
                + "Чтобы сменить режим работы, нужно нажать на соответствующую кнопку, а затем нажать на кнопку поменять тип решения"
                + "В полях с числами можно выбрать нужный размер задачи(для решения подобных задач кол-во перем. должно быть больше кол-ва огр., после выбора нужно нажать на кнопку (Построить таблицу заданного размера))"
                + "\n\nПосле нажатия на данную кнопку, будет построено поле ввода(Решается задача на минимум), в данном случае 3х2.");
        labelInfoChoice.setMaxWidth(655);
        labelInfoChoice.setWrapText(true);
        labelInfoChoice.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfoChoice, 0, 3);

        Image inputTable = new Image("file:data/inputTablePic.jpg");
        ImageView inputTablePic = new ImageView();
        inputTablePic.setFitHeight(300);
        inputTablePic.setFitWidth(690);
        inputTablePic.setPreserveRatio(true);
        inputTablePic.setImage(inputTable);
        paneInfo.add(inputTablePic, 0, 4);

        Label labelInfoSave = new Label("При нажатии на кнопку (Сохранить задачу в файл), введенная вами задача будет сохранена в файл, с именем, которое вы укажете.");
        labelInfoSave.setMaxWidth(655);
        labelInfoSave.setWrapText(true);
        labelInfoSave.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfoSave, 0, 5);

        Image save = new Image("file:data/saveInFile.jpg");
        ImageView savePic = new ImageView();
        savePic.setFitHeight(300);
        savePic.setFitWidth(690);
        savePic.setPreserveRatio(true);
        savePic.setImage(save);
        paneInfo.add(savePic, 0, 6);

        Label labelInfoTable = new Label("Выбрав режим работы с дробями, пошаговый режим, метод искусственного базиса, и введя задачу, будет доступно следующее окно, в котором можно выбрать опорный элемент, после чего нажать кнопку (след. шаг), если данный шаг был последним , программа оповестит вас об этом.");
        labelInfoTable.setMaxWidth(655);
        labelInfoTable.setWrapText(true);
        labelInfoTable.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfoTable, 0, 7);

        Image tableDec = new Image("file:data/tablePic.jpg");
        ImageView tableDecPic = new ImageView();
        tableDecPic.setFitHeight(300);
        tableDecPic.setFitWidth(690);
        tableDecPic.setPreserveRatio(true);
        tableDecPic.setImage(tableDec);
        paneInfo.add(tableDecPic, 0, 8);

        Label labelInfoR = new Label("В конце работы алгоритма, вы можете с помощью меню вернуться на главную страницу(ctrl+x).");
        labelInfoR.setMaxWidth(655);
        labelInfoR.setWrapText(true);
        labelInfoR.setStyle("-fx-font: 16 arial;");
        paneInfo.add(labelInfoR, 0, 9);

        ScrollPane scrollInfo = new ScrollPane();
        scrollInfo.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollInfo.setContent(paneInfo);
        scrollInfo.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Scene sceneHelp = new Scene(scrollInfo, 690, 400);
        stageInfo.setScene(sceneHelp);
        stageInfo.show();
    }

    private void createInputTableForFileFraction(String fileName) throws WrongNumException {
        FractionsList a = InputInfo.readFileFractionParam(fileName);
        Fractions target = a.getFractions(0);
        a.remove(0);
        colX = target.size();
        colR = a.size();
        createInputTable();
        for (int j = 0; j < target.size(); j++) {
            arr[0][j].setText(target.getEl(j).toString());
        }
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < (target.size() + 1); j++) {
                arr[i + 1][j].setText(a.getFractions(i).getEl(j).toString());
            }
        }
        root2.getChildren().removeAll(root2.getChildren());
        root2.add(gridSimplex, 0, 0);
    }

    private void createInputTableForFileNumber(String fileName) throws WrongNumException {
        ArrayList<ArrayList<Double>> a = InputInfo.readFileNumberParam(fileName);
        ArrayList<Double> target = a.get(0);
        a.remove(0);
        colX = target.size();
        colR = a.size();
        createInputTable();
        for (int j = 0; j < target.size(); j++) {
            arr[0][j].setText(target.get(j).toString());
        }
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < (target.size() + 1); j++) {
                arr[i + 1][j].setText(a.get(i).get(j).toString());
            }
        }
        root2.getChildren().removeAll(root2.getChildren());
        root2.add(gridSimplex, 0, 0);
    }

    private void createChoiceFileNameFraction() {
        Stage stage = new Stage();
        stage.setTitle("Чтение из файла");
        Scene scene = new Scene(new HBox(20), 380, 40);
        HBox box2 = (HBox) scene.getRoot();
        box2.setPadding(new Insets(5, 5, 5, 5));
        TextField fileName = new TextField();
        Button enterName = new Button("Ввод");
        enterName.setOnAction((ActionEvent event) -> {
            try {
                if (new File("data/" + fileName.getText() + ".txt").exists()) {
                    createInputTableForFileFraction(fileName.getText());
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка с файлом");
                    alert.setContentText("Файла с таким именем не существует");
                    alert.showAndWait();
                }
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        box2.getChildren().addAll(new Text("Введите имя файла"), fileName, enterName);

        stage.setScene(scene);
        stage.show();
    }

    private void createChoiceFileNameNumber() {
        Stage stage = new Stage();
        stage.setTitle("Чтение из файла");
        Scene scene = new Scene(new HBox(20), 380, 40);
        HBox box3 = (HBox) scene.getRoot();
        box3.setPadding(new Insets(5, 5, 5, 5));
        TextField fileName = new TextField();
        Button enterName = new Button("Ввод");
        enterName.setOnAction((ActionEvent event) -> {
            try {
                if (new File("data/" + fileName.getText() + ".txt").exists()) {
                    createInputTableForFileNumber(fileName.getText());
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка с файлом");
                    alert.setContentText("Файла с таким именем не существует");
                    alert.showAndWait();
                }
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        box3.getChildren().addAll(new Text("Введите имя файла"), fileName, enterName);

        stage.setScene(scene);
        stage.show();
    }

    private void createChoice() {
        root1 = new GridPane();
        root1.setPadding(new Insets(10, 10, 10, 10));

        comboBoxX = new ComboBox();
        comboBoxX.getItems().addAll(
                "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"
        );
        comboBoxX.setValue("3");
        comboBoxRestrictions = new ComboBox();
        comboBoxRestrictions.getItems().addAll(
                "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"
        );
        comboBoxRestrictions.setValue("2");
        choiceNumber = new HBox();
        choiceNumber.setPadding(new Insets(5, 5, 5, 5));
        choiceNumber.getChildren().addAll(new Label("Кол-во перем."), comboBoxX, new Label("\tКол-во огр."), comboBoxRestrictions);
        root1.add(choiceNumber, 0, 0);
        createRadioButtonForMode();
        createRadioButtonForModeDecision();
        Button btnSize = new Button("Построить таблицу заданного размера");
        btnSize.setOnAction((ActionEvent event) -> {
            colX = Integer.parseInt((String) comboBoxX.getValue());
            colR = Integer.parseInt((String) comboBoxRestrictions.getValue());

            if (colX > colR) {
                root2.getChildren().removeAll(root2.getChildren());
                createInputTable();
                root2.add(gridSimplex, 0, 0);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка с размерами");
                alert.setContentText("\"Количество переменных должно быть > количества ограничений\"");
                alert.showAndWait();
            }
        });
        HBox btnSizeBox = new HBox();
        btnSizeBox.getChildren().addAll(new Label("  "), btnSize);

        root1.add(btnSizeBox, 0, 1);
        HBox btnOk = new HBox();
        Button okButton = new Button("Поменять тип решения");
        okButton.setPadding(new Insets(5, 5, 5, 5));
        btnOk.getChildren().addAll(new Label("  "), okButton);
        root1.add(btnOk, 0, 5);
        Label firstLabel = new Label("Выбрана " + mode + " и " + modeDecision + "\n" + modeAutoOrNormal);
        firstLabel.setPadding(new Insets(15, 5, 5, 5));
        root1.add(firstLabel, 0, 6);
        okButton.setOnAction((ActionEvent event) -> {
            RadioButton selectionFrOrNum = (RadioButton) groupFractionOrNubmer.getSelectedToggle();
            RadioButton selectionDecision = (RadioButton) groupDecision.getSelectedToggle();
            RadioButton selectionAutoOrNormal = (RadioButton) groupz.getSelectedToggle();
            modeDecision = selectionDecision.getText();
            mode = selectionFrOrNum.getText();
            modeAutoOrNormal = selectionAutoOrNormal.getText();
            Label labelMode = new Label("Выбрана " + mode + " и " + modeDecision + "\n" + modeAutoOrNormal);
            if (root1.getChildren().size() == 7) {
                root1.getChildren().remove(6);
            }
            labelMode.setPadding(new Insets(15, 5, 5, 5));
            root1.add(labelMode, 0, 6);

        });

    }

    private void createRadioButtonForMode() {
        RadioButton fractionBtn = new RadioButton("Работа с дробями");
        RadioButton numberBtn = new RadioButton("Работа с числами");
        groupFractionOrNubmer = new ToggleGroup();
        fractionBtn.setToggleGroup(groupFractionOrNubmer);
        numberBtn.setToggleGroup(groupFractionOrNubmer);
        fractionBtn.setSelected(true);
        boxButtonMode = new HBox();
        boxButtonMode.setPadding(new Insets(7, 7, 7, 7));
        boxButtonMode.getChildren().addAll(fractionBtn, new Label("\t    "), numberBtn);
        root1.add(boxButtonMode, 0, 2);
    }

    private void createRadioButtonForModeDecision() {
        RadioButton simplexBtn = new RadioButton("Симплекс-метод");
        RadioButton artificialBtn = new RadioButton("Искусств.базис");
        groupDecision = new ToggleGroup();
        simplexBtn.setToggleGroup(groupDecision);
        artificialBtn.setToggleGroup(groupDecision);
        simplexBtn.setSelected(true);
        boxButtonDecision = new HBox();
        boxButtonDecision.setPadding(new Insets(7, 7, 7, 7));
        boxButtonDecision.getChildren().addAll(simplexBtn, new Label("\t      "), artificialBtn);
        root1.add(boxButtonDecision, 0, 3);
        autoOrNormalMode();
    }

    private void choiceFileName() {
        Stage stage = new Stage();
        stage.setTitle("Сохравнение в файл");
        Scene scene = new Scene(new HBox(20), 380, 40);
        HBox box1 = (HBox) scene.getRoot();
        box1.setPadding(new Insets(5, 5, 5, 5));
        TextField fileName = new TextField();
        Button enterName = new Button("Сохранить");
        enterName.setOnAction((ActionEvent event) -> {
            if ("Работа с дробями".equals(mode)) {
                try {
                    InputInfo.saveInFileFraction(arr, colR, colX, fileName.getText());
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (WrongNumException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ("Работа с числами".equals(mode)) {
                try {
                    InputInfo.saveInFileNumber(arr, colR, colX, fileName.getText());
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        box1.getChildren().addAll(new Text("Введите имя файла"), fileName, enterName);

        stage.setScene(scene);
        stage.show();
    }

    public void createInputTable() {
        gridSimplex = new GridPane();
        gridSimplex.getChildren().removeAll(gridSimplex.getChildren());
        x0 = new TextField[colX];
        arr = new TextField[colR + 1][colX + 1];
        int idi = 0;
        for (int i = 0; i < (colR + 1); i++) {
            int idj = 0;
            Text txName = new Text("Функция:");
            if (idi != 0) {
                txName.setText((i) + " Ограничение");
            }
            gridSimplex.add(txName, 0, idi);
            idi++;
            for (int j = 0; j < colX + 1; j++) {
                arr[i][j] = new TextField();
                arr[i][j].setMinWidth(30);
                if ((i != 0 || j != colX)) {
                    gridSimplex.add(arr[i][j], idj, idi);
                } else {
                    gridSimplex.add(new Label("---------> MIN"), idj, idi);
                }
                idj++;
                Text tx = new Text("*x" + (j + 1) + "+");
                if (j == colX - 1) {
                    tx.setText("*x" + (j + 1) + "=");
                }
                if (j == colX) {
                    tx.setText("");
                }
                gridSimplex.add(tx, idj, idi);
                idj++;
            }
            idi++;
        }
        arr[0][colX].setText("0");
        gridSimplex.add(new Text(""), 0, (2 * colR) + 3);
        Button btnInput = new Button("Ввод");
        btnInput.setOnAction((ActionEvent event) -> {
            if ("Работа с дробями".equals(mode)) {
                try {
                    if (checkInputValueFraction()) {
                        InputInfo.readInputFraction(arr, colR, colX);
                        gridSimplex.setVisible(false);
                        if (!MethodsForFraction.checkDependence(InputInfo.sTableFraction.getMatrix())) {
                            if ("Симплекс-метод".equals(modeDecision)) {
                                ChoiceBasisDialogFraction a = new ChoiceBasisDialogFraction(InputInfo.sTableFraction);
                            } else if ("Искусств.базис".equals(modeDecision)) {
                                try {
                                    MethodsForFraction.handleArtificial(InputInfo.sTableFraction);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            InputInfo.sTableFraction = null;
                            Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                            alertDependence.setTitle("Ошибка");
                            alertDependence.setHeaderText("Ошибка в матрице");
                            alertDependence.setContentText("В введенной вами матрице , ограничения являются линейно-зависимыми. Введите подходящую матрицу(линейно-независмую)");
                            alertDependence.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка");
                        alert.setHeaderText("Ошибка с форматом ввода");
                        alert.setContentText("На ввод должно поступать целое число, либо обыкновенная дробь \nФормат числа[1,213,-123...]\nФормат дроби[1/2,-1/3,-1/-3,0/2...]");
                        alert.showAndWait();
                    }

                } catch (IOException | WrongNumException | NullPointerException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ("Работа с числами".equals(mode)) {
                //Здесь те же самые действия и провери что и для дробей!
                if (checkInputValueNumber()) {
                    try {
                        InputInfo.readInputNumber(arr, colR, colX);
                        if (!MethodsForNumber.checkDependence(InputInfo.sTableNumber.getMatrix())) {
                            if ("Симплекс-метод".equals(modeDecision)) {
                                ChoiceBasisDialogNumber a = new ChoiceBasisDialogNumber(InputInfo.sTableNumber);
                            } else if ("Искусств.базис".equals(modeDecision)) {
                                try {
                                    MethodsForNumber.handleArtificial(InputInfo.sTableNumber);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            InputInfo.sTableNumber = null;
                            Alert alertDependence = new Alert(Alert.AlertType.ERROR);
                            alertDependence.setTitle("Ошибка");
                            alertDependence.setHeaderText("Ошибка в матрице");
                            alertDependence.setContentText("В введенной вами матрице , ограничения являются линейно-зависимыми. Введите подходящую матрицу(линейно-независмую)");
                            alertDependence.showAndWait();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (WrongNumException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка с форматом ввода");
                    alert.setContentText("На ввод должно поступать число вида [1.43 , -2.32, 1 , 2 ....]");
                    alert.showAndWait();
                }
            }
        });
        gridSimplex.add(btnInput, 0, (2 * colR) + 4);

        Button btnSave = new Button("Сохранить задачу в файл");
        btnSave.setOnAction((ActionEvent event) -> {
            if ("Работа с дробями".equals(mode)) {
                if (checkInputValueFraction()) {
                    choiceFileName();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка с форматом ввода");
                    alert.setContentText("На ввод должно поступать целое число, либо обыкновенная дробь \nФормат числа[1,213,-123...]\nФормат дроби[1/2,-1/3,-1/-3,0/2...]");
                    alert.showAndWait();
                }
            }
            if ("Работа с числами".equals(mode)) {
                if (checkInputValueNumber()) {
                    choiceFileName();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Ошибка с форматом ввода");
                    alert.setContentText("На ввод должно поступать число вида [1.43 , -2.32, 1 , 2 ....]");
                    alert.showAndWait();
                }
            }
        });
        HBox saveBox = new HBox();
        saveBox.getChildren().add(btnSave);
        saveBox.setPadding(new Insets(15, 0, 0, 0));
        gridSimplex.add(saveBox, 0, (2 * colR) + 5);
    }

    private boolean checkInputValueFraction() {
        boolean result = true;
        for (TextField[] elm : arr) {
            for (TextField input : elm) {
                //проверка числа/дроби
                if (!input.getText().matches("-?[0-9]+(['/']{1}-?[1-9]{1}[0-9]*)?")) {
                    result = false;
                }
            }
        }
        return result;
    }

    private boolean checkInputValueNumber() {
        boolean result = true;
        for (TextField[] elm : arr) {
            for (TextField input : elm) {
                //проверка числа/дроби
                if (!input.getText().matches("^[-+]?[0-9]*[.]?[0-9]+(?:[eE][-+]?[0-9]+)?$")) {
                    result = false;
                }
            }
        }
        return result;
    }

    public void createSimplexStepTableArtificial() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {

            if (!MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
                if (!InputInfo.stepBaseFraction.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForArtificialFraction();
        } else if ("Работа с числами".equals(mode)) {
            if (!MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
                if (!InputInfo.stepBaseNumber.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForArtificialNumber();
        }
        border.setCenter(vbox);
    }

    public void createSimplexTableAutoArtificial() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        TableView<String[]> table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {

            createButtonForAutoArtificialFraction();
        } else if ("Работа с числами".equals(mode)) {
            createButtonForAutoArtificialNumber();
        }
        border.setCenter(vbox);
    }

    public void createSimplexStepTableStandard() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {

            if (!MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
                if (!InputInfo.stepBaseFraction.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForStandardFraction();
        } else if ("Работа с числами".equals(mode)) {
            if (!MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
                if (!InputInfo.stepBaseNumber.getLastStep().end()) {
                    createHelper();
                    createSupportElm();
                }
            }
            createButtonForStandardNumber();
        }
        border.setCenter(vbox);
    }

    public void createSimplexStepTableAutoStandard() throws WrongNumException, InterruptedException {
        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 10));
        String[][] staffArray = null;
        if ("Работа с дробями".equals(mode)) {
            staffArray = MethodsForFraction.getSimplexTable(InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getMatrix(), InputInfo.stepBaseFraction.getStep(InputInfo.stepBaseFraction.size() - 1).getPosition());
        } else if ("Работа с числами".equals(mode)) {
            //
            staffArray = MethodsForNumber.getSimplexTable(InputInfo.stepBaseNumber.getLastStep().getMatrix(), InputInfo.stepBaseNumber.getLastStep().getPosition());
        }
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        TableView<String[]> table = new TableView<>();
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            tc.setPrefWidth(90);
            table.getColumns().add(tc);
        }
        table.setMaxWidth(1080);
        table.setItems(data);
        vbox.getChildren().addAll(table);
        if ("Работа с дробями".equals(mode)) {
            createButtonForAutoStandardFraction();
        } else if ("Работа с числами".equals(mode)) {
            createButtonForAutoStandardNumber();
        }
        border.setCenter(vbox);
    }

    private void createHelper() throws InterruptedException {
        HBox helper = new HBox();
        Label label = new Label("Чтобы продолжить надо выбрать один опорный элемент\nОни выбираются минимальными из B[i]/A[i][j].");
        Button btnToolTip = new Button("  ?  ");
        btnToolTip.getStyleClass().add("helper");
        Tooltip helperTooltip = new Tooltip("Такие значения могут быть только для:\nотрицательных f[j] и положительных A[i][j].\n");
        helperTooltip.setStyle("-fx-font: 16 arial;");
        helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
        hackTooltipStartTiming(helperTooltip);
        btnToolTip.setTooltip(helperTooltip);
        helper.getChildren().addAll(label, btnToolTip);
        label.setStyle("-fx-font: 16 arial;");
        vbox.getChildren().add(helper);
    }

    private void createSupportElm() throws WrongNumException {
        ArrayList support = null;
        if ("Работа с дробями".equals(mode)) {
            support = MethodsForFraction.supportElmTmpStep(InputInfo.stepBaseFraction.getLastStep());
        } else if ("Работа с числами".equals(mode)) {
            support = MethodsForNumber.supportElmTmpStep(InputInfo.stepBaseNumber.getLastStep());
        }
        group = new ToggleGroup();
        RadioButton[] btn = new RadioButton[support.size()];
        HBox hboxSup = new HBox();
        for (int i = 0; i < support.size(); i++) {
            btn[i] = new RadioButton(support.get(i).toString() + "\t");
            btn[i].setStyle("-fx-font: 16 arial;");
            btn[i].setMinWidth(70);
            btn[i].setToggleGroup(group);
            hboxSup.getChildren().add(btn[i]);
        }

        if ("Работа с числами".equals(mode)) {
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                    // Has selection.
                    if (old_toggle != null) {
                        RadioButton oldButton = (RadioButton) old_toggle;
                        String oldSelected = oldButton.getText();
                        TableColumn temp = table.getColumns().get(MethodsForNumber.needSupport(oldSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForNumber.needSupport(oldSelected)[0]) {
                                                this.setStyle("");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForNumber.needSupport(oldSelected)[1] + 1, temp);
                    }

                    RadioButton newButton = (RadioButton) new_toggle;
                    String newSelected = newButton.getText();
                    if (new_toggle != null) {
                        TableColumn temp = table.getColumns().get(MethodsForNumber.needSupport(newSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForNumber.needSupport(newSelected)[0]) {
                                                this.setStyle("-fx-background-color: #10C872;");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForNumber.needSupport(newSelected)[1] + 1, temp);
                    }
                }
            });
        } else if ("Работа с дробями".equals(mode)) {
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                    // Has selection.
                    if (old_toggle != null) {
                        RadioButton oldButton = (RadioButton) old_toggle;
                        String oldSelected = oldButton.getText();
                        TableColumn temp = table.getColumns().get(MethodsForFraction.needSupport(oldSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForFraction.needSupport(oldSelected)[0]) {
                                                this.setStyle("");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForFraction.needSupport(oldSelected)[1] + 1, temp);
                    }

                    RadioButton newButton = (RadioButton) new_toggle;
                    String newSelected = newButton.getText();
                    if (new_toggle != null) {
                        TableColumn temp = table.getColumns().get(MethodsForFraction.needSupport(newSelected)[1] + 1);
                        temp.setCellFactory(new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn param) {
                                return new TableCell<String[], String>() {

                                    @Override
                                    public void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (!isEmpty()) {
                                            if (this.getIndex() == MethodsForFraction.needSupport(newSelected)[0]) {
                                                this.setStyle("-fx-background-color: #10C872;");
                                            }
                                            setText(item.toString());
                                        }
                                    }
                                };
                            }
                        });

                        table.getColumns().set(MethodsForFraction.needSupport(newSelected)[1] + 1, temp);
                    }
                }
            });
        }
        btn[0].setSelected(true);
        ScrollPane scroll = new ScrollPane(hboxSup);
        scroll.setFitToHeight(false);
        scroll.setPrefViewportHeight(27.5);
        scroll.setMaxWidth(1080);
        vbox.getChildren().add(scroll);
    }

    private void createButtonForArtificialFraction() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexFraction nextStep;
                nextStep = MethodsForFraction.nextStepArtificialFraction(InputInfo.stepBaseFraction.getLastStep(), MethodsForFraction.needSupport(selectedString)[0], MethodsForFraction.needSupport(selectedString)[1]);
                InputInfo.stepBaseFraction.add(nextStep);
                createSimplexStepTableArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseFraction.removeLast();
                createSimplexStepTableArtificial();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseFraction.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseFraction.getLastStep().end() || MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            nextButton.setVisible(false);
        }
        if (InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            nextButton.setVisible(false);
        }
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        //если это конец искувственного базиса
        if (InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            space.setText("\t\tМетод искусственного базиса закончен!\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis());
            space.setStyle("-fx-font: 16 arial;");
            hboxBtn.getChildren().remove(nextButton);
            Button next = new Button("Продолжить");
            hboxBtn.getChildren().add(next);
            next.setOnAction((ActionEvent event) -> {
                vbox.getChildren().removeAll(vbox.getChildren());
                try {
                    count++;
                    InputInfo.stepBaseFraction.add(MethodsForFraction.createStepAfterArtificialMethodFraction(InputInfo.stepBaseFraction.getLastStep(), InputInfo.sTableFraction, InputInfo.sTableFraction.getTarget()));
                    createSimplexStepTableArtificial();
                } catch (WrongNumException | InterruptedException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        if (InputInfo.stepBaseFraction.getLastStep().end() && !InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            //если это конец обычного алгоритма count - счётчик нажатия кнопки продолжить, которая появляется только если найден какой-то базис с помощью искусственного базиса
            if (count != 0) {
                space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
                space.setStyle("-fx-font: 16 arial;");
                createButtonExit();
            } else if (count == 0) {
                //если система огр. противоречива, то есть алгоритм искуственного базиса закончен, но кнопки продолжить не появилось,  то есть базис не был найден()
                space.setText("\tСистема ограничений противоречива. Решения нет.\n\t\t");
                space.setStyle("-fx-font: 16 arial;");
                hboxBtn.getChildren().add(createHelperBadSolution());
                createButtonExit();
            }
        }

        //если решение не может быть достигнуто
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("\t\tФункция неограничена снизу, решения нет.");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как один из столбцов(k) содержит отрциательный элемент в последней строке и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    private Button createHelperBadSolution() {
        Button btnToolTip = new Button("  ?  ");
        btnToolTip.getStyleClass().add("helper");
        Tooltip helperTooltip = new Tooltip("\tПри окончании алгоритмаf0 > 0.\n\tCистема противоречива!");
        helperTooltip.setStyle("-fx-font: 16 arial;");
        helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
        hackTooltipStartTiming(helperTooltip);
        btnToolTip.setTooltip(helperTooltip);
        return btnToolTip;
    }

    private void createButtonForAutoArtificialFraction() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);
        if (InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            space.setText("\t\tМетод искусственного базиса закончен!\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis());
        }
        space.setStyle("-fx-font: 16 arial;");
        Button next = new Button("Продолжить");
        hboxBtn.getChildren().addAll(space, next);
        if (InputInfo.stepBaseFraction.getLastStep().end() && !InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            if (count != 0) {
                space.setMinWidth(987);
                space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
                space.setStyle("-fx-font: 18 arial;");
                hboxBtn.getChildren().removeAll(next);
                createButtonExit();
            } else if (count == 0) {
                space.setText("\tСистема ограничений противоречива. Решения нет(метод искусс. базиса не закончился).\n\t\t");
                space.setStyle("-fx-font: 16 arial;");
                hboxBtn.getChildren().removeAll(next);
                createButtonExit();
            }
        }
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("\t\tФункция неограничена снизу, решения нет.");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            hboxBtn.getChildren().add(createHelperBadSolution());
            createButtonExit();
        }
        next.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                count++;
                //
                InputInfo.stepBaseFraction.add(MethodsForFraction.createStepAfterArtificialMethodFraction(InputInfo.stepBaseFraction.getLastStep(), InputInfo.sTableFraction, InputInfo.sTableFraction.getTarget()));
                MethodsForFraction.autoSimplexMethodFraction();
                //
                createSimplexTableAutoArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForAutoArtificialNumber() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);
        if (InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            space.setText("\t\tМетод искусственного базиса закончен!\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis());
        }
        space.setStyle("-fx-font: 16 arial;");
        Button next = new Button("Продолжить");
        hboxBtn.getChildren().addAll(space, next);
        if (InputInfo.stepBaseNumber.getLastStep().end() && !InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            if (count != 0) {
                space.setMinWidth(987);
                space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
                space.setStyle("-fx-font: 18 arial;");
                hboxBtn.getChildren().removeAll(next);
                count = 0;
                createButtonExit();
            } else if (count == 0) {
                space.setText("\tСистема ограничений противоречива. Решения нет(метод искусс. базиса не закончился).\n\t\t");
                space.setStyle("-fx-font: 16 arial;");
                hboxBtn.getChildren().removeAll(next);
                hboxBtn.getChildren().add(createHelperBadSolution());
                createButtonExit();
            }
        }
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            space.setText("\t\tФункция неограничена снизу, решения нет.");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как один из столбцов(k) содержит отрциательный элемент в последней строке и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            hboxBtn.getChildren().addAll(space, btnToolTip);
            createButtonExit();
        }
        next.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                count++;
                InputInfo.stepBaseNumber.add(MethodsForNumber.createStepAfterArtificialMethodNumber(InputInfo.stepBaseNumber.getLastStep(), InputInfo.sTableNumber, InputInfo.sTableNumber.getTarget()));
                MethodsForNumber.autoSimplexMethodNumber();
                createSimplexTableAutoArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForStandardFraction() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexFraction nextStep;
                nextStep = MethodsForFraction.nextStep(InputInfo.stepBaseFraction.getLastStep(), MethodsForFraction.needSupport(selectedString)[0], MethodsForFraction.needSupport(selectedString)[1]);
                InputInfo.stepBaseFraction.add(nextStep);
                createSimplexStepTableStandard();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseFraction.removeLast();
                createSimplexStepTableStandard();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseFraction.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseFraction.getLastStep().end() || MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            nextButton.setVisible(false);
        }

        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        if (InputInfo.stepBaseFraction.getLastStep().end()) {
            //если это конец обычного алгоритма а
            space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
            space.setStyle("-fx-font: 16 arial;");
            createButtonExit();
        }

        //если решение не может быть достигнуто
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("\t\tФункция неограничена снизу, решения нет.");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как один из столбцов(k) содержит отрциательный элемент в последней строке и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForAutoStandardFraction() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);

        space.setStyle("-fx-font: 16 arial;");
        hboxBtn.getChildren().addAll(space);
        if (InputInfo.stepBaseFraction.getLastStep().end() && !InputInfo.stepBaseFraction.getLastStep().endArtificial()) {
            space.setMinWidth(987);
            space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseFraction.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseFraction.getLastStep().getF0());
            space.setStyle("-fx-font: 18 arial;");

        }
        if (MethodsForFraction.checkInfinity(InputInfo.stepBaseFraction.getLastStep())) {
            space.setText("\t\tФункция неограничена снизу, решения нет.");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как один из столбцов(k) содержит отрциательный элемент в последней строке и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            hboxBtn.getChildren().addAll(space, btnToolTip);
        }
        vbox.getChildren().addAll(hboxBtn);
        createButtonExit();
    }

    private void createButtonForStandardNumber() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexNumber nextStep;
                nextStep = MethodsForNumber.nextStep(InputInfo.stepBaseNumber.getLastStep(), MethodsForNumber.needSupport(selectedString)[0], MethodsForNumber.needSupport(selectedString)[1]);
                InputInfo.stepBaseNumber.add(nextStep);
                createSimplexStepTableStandard();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseNumber.removeLast();
                createSimplexStepTableStandard();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseNumber.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseNumber.getLastStep().end() || MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            nextButton.setVisible(false);
        }

        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        if (InputInfo.stepBaseNumber.getLastStep().end()) {
            //если это конец обычного алгоритма а
            space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis() + "\n\t\tf0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
            space.setStyle("-fx-font: 16 arial;");
            createButtonExit();
        }

        //если решение не может быть достигнуто
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep()) == true) {
            space.setText("\t\tФункция неограничена снизу, решения нет.");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как один из столбцов(k) содержит отрциательный элемент в последней строке и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    private void createButtonForAutoStandardNumber() throws WrongNumException {
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(994);

        space.setStyle("-fx-font: 16 arial;");
        hboxBtn.getChildren().addAll(space);
        if (InputInfo.stepBaseNumber.getLastStep().end() && !InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            space.setMinWidth(987);
            space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
            space.setStyle("-fx-font: 18 arial;");

        }
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            space.setText("Решение не может быть достигнуто, т.к целевая функция неограничена снизу!");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(695);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как один из столбцов(k) содержит отрциательный элемент в последней строке и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().removeAll(hboxBtn.getChildren());
            hboxBtn.getChildren().addAll(space, btnToolTip);
        }
        vbox.getChildren().addAll(hboxBtn);
        createButtonExit();
    }

    private void createButtonForArtificialNumber() throws WrongNumException {
        Button nextButton = new Button("Следующий шаг");
        Button backButton = new Button("Предыдущий шаг");
        nextButton.setOnAction((ActionEvent event) -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            String selectedString = selection.getText();
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                StepSimplexNumber nextStep;
                nextStep = MethodsForNumber.nextStepArtificialNumber(InputInfo.stepBaseNumber.getLastStep(), MethodsForNumber.needSupport(selectedString)[0], MethodsForNumber.needSupport(selectedString)[1]);
                InputInfo.stepBaseNumber.add(nextStep);
                createSimplexStepTableArtificial();
            } catch (WrongNumException | InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        backButton.setOnAction((ActionEvent event) -> {
            vbox.getChildren().removeAll(vbox.getChildren());
            try {
                InputInfo.stepBaseNumber.removeLast();
                createSimplexStepTableArtificial();
            } catch (WrongNumException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Если в массиве шагов только один шаг убрать кнопку назад!
        if (InputInfo.stepBaseNumber.size() == 1) {
            backButton.setVisible(false);
        }
        if (InputInfo.stepBaseNumber.getLastStep().end() || MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            nextButton.setVisible(false);
        }
        if (InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            nextButton.setVisible(false);
        }
        HBox hboxBtn = new HBox();
        Label space = new Label();
        space.setMinWidth(861);
        hboxBtn.getChildren().addAll(backButton, space, nextButton);

        //если это конец искувственного базиса
        if (InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            space.setText("\t\tМетод искусственного базиса закончен!\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis());
            space.setStyle("-fx-font: 16 arial;");
            hboxBtn.getChildren().remove(nextButton);
            Button next = new Button("Продолжить");
            hboxBtn.getChildren().add(next);
            next.setOnAction((ActionEvent event) -> {
                vbox.getChildren().removeAll(vbox.getChildren());
                try {
                    count++;
                    InputInfo.stepBaseNumber.add(MethodsForNumber.createStepAfterArtificialMethodNumber(InputInfo.stepBaseNumber.getLastStep(), InputInfo.sTableNumber, InputInfo.sTableNumber.getTarget()));
                    createSimplexStepTableArtificial();
                } catch (WrongNumException | InterruptedException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        if (InputInfo.stepBaseNumber.getLastStep().end() && !InputInfo.stepBaseNumber.getLastStep().endArtificial()) {
            //если это конец обычного алгоритма count - счётчик нажатия кнопки продолжить, которая появляется только если найден какой-то базис с помощью искусственного базиса
            if (count != 0) {
                space.setText("\t\tРешение:\n\t\t" + InputInfo.stepBaseNumber.getLastStep().basis() + "\t f0 = " + InputInfo.stepBaseNumber.getLastStep().getF0());
                space.setStyle("-fx-font: 16 arial;");
                createButtonExit();
            } else if (count == 0) {
                //если система огр. противоречива, то есть алгоритм искуственного базиса закончен, но кнопки продолжить не появилось,  то есть базис не был найден()
                space.setText("\tСистема ограничений противоречива. Решения нет.\n\t\t");
                space.setStyle("-fx-font: 16 arial;");
                space.setMinWidth(350);
                hboxBtn.getChildren().add(createHelperBadSolution());
                createButtonExit();
            }
        }

        //если решение не может быть достигнуто
        if (MethodsForNumber.checkInfinity(InputInfo.stepBaseNumber.getLastStep())) {
            space.setText("\t\tФункция неограничена снизу, решения нет.");
            space.setStyle("-fx-font: 18 arial;");
            space.setMinWidth(820);
            Button btnToolTip = new Button("  ?  ");
            btnToolTip.getStyleClass().add("helper");
            Tooltip helperTooltip = new Tooltip("\tФункция неограничена снизу так как один из столбцов(k) содержит отрциательный элемент в последней строке и неположительные элементы a[i][k]");
            helperTooltip.setStyle("-fx-font: 16 arial;");
            helperTooltip.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
            hackTooltipStartTiming(helperTooltip);
            hboxBtn.getChildren().remove(nextButton);
            btnToolTip.setTooltip(helperTooltip);
            hboxBtn.getChildren().add(btnToolTip);
            createButtonExit();
        }
        vbox.getChildren().addAll(hboxBtn);
    }

    public void setMainGrid() {
        root.getChildren().removeAll(root.getChildren());
        root = new GridPane();
        root.getColumnConstraints().add(new ColumnConstraints(350));
        root.getColumnConstraints().add(new ColumnConstraints(1100));
        createChoice();
        root.setGridLinesVisible(true); // делаем видимой сетку строк и столбцов
        root.setColumnIndex(root1, 0);
        root.setColumnIndex(root2, 1);
        root.getChildren().addAll(root1, root2);
        border.setCenter(root);
    }

    private void createButtonExit() {
        Button exit = new Button("Начальная страница");
        exit.setOnAction((ActionEvent event) -> {
            InputInfo.clear();
            setMainGrid();
        });
        vbox.getChildren().add(exit);
    }

    private void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(100)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void autoOrNormalMode() {
        RadioButton stepBtn = new RadioButton("Пошаговый режим");
        RadioButton nostepBtn = new RadioButton("Автоматический режим");
        groupz = new ToggleGroup();
        stepBtn.setToggleGroup(groupz);
        nostepBtn.setToggleGroup(groupz);
        stepBtn.setSelected(true);
        HBox hboxz = new HBox();
        hboxz.setPadding(new Insets(7, 7, 7, 7));
        hboxz.getChildren().addAll(stepBtn, new Label("\t  "), nostepBtn);
        root1.add(hboxz, 0, 4);
    }

}
