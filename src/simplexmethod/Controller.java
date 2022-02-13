/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplexmethod;

import javafx.stage.Stage;

/**
 *
 */
public class Controller {

    static View view2;

    public static void SetScene(Stage primaryStage) throws ClassNotFoundException, NullPointerException, WrongNumException {
        view2 = new View(primaryStage);
    }
}
