package gui;

import backend.Logic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class InterfaceController {

    /*
     * ================================================================================
     * JavaFX controls used in the general interface:
     *
     *  Scene interfaceScene
     *      VBox interfaceBox
     *          StackPane headerStack
     *              HBox headerBox
     *                  ImageView headerIcon
     *                  Label headerLabel
     *              Pane headerBG
     *                  LinearGradient headerGradient
     *                  Rectangle headerRectangle
     *          Line headerLine
     *          StackPane filepathStack
     *              HBox filepathBox
     *                  Label filepathLabel
     *              Pane filepathBG
     *                  LinearGradient filepathGradient
     *                  Rectangle filepathRectangle
     *          Line filepathLine
     *          HBox bodyBox
     *              StackPane sidebarStack
     *                  VBox sidebarBox
     *                      StackPane bufferStack
     *                      StackPane homeButtonStack
     *                          ImageView homeButton
     *                          Pane homeButtonBG
     *                              LinearGradient homeButtonGradient
     *                              Rectangle homeButtonRectangle
     *                      StackPane defaultButtonStack
     *                          ImageView defaultButton
     *                          Pane defaultButtonBG
     *                              LinearGradient defaultButtonGradient
     *                              Rectangle defaultButtonRectangle
     *                  Pane sidebarBG
     *                      LinearGradient sidebarGradient
     *                      Rectangle sidebarRectangle
     *              VBox bodyViewBox
     *
     * ================================================================================
     */
    private static Scene interfaceScene;
    private static VBox interfaceBox, sidebarBox, bodyViewBox;
    private static HBox headerBox, filepathBox, bodyBox;
    private static StackPane headerStack, filepathStack, bufferStack,
            homeButtonStack, defaultButtonStack, sidebarStack;
    private static ImageView headerIcon, homeButton, defaultButton;
    private static Label filepathLabel;
    private static Line headerLine, filepathLine;
    private static LinearGradient headerGradient, filepathGradient, homeButtonGradient,
            defaultButtonGradient, sidebarGradient;
    private static Rectangle headerRectangle, filepathRectangle, homeButtonRectangle,
            defaultButtonRectangle, sidebarRectangle;
    private static Pane headerBG, filepathBG, homeButtonBG, defaultButtonBG,
            sidebarBG;

    private static Logic logic;

    public static Scene initInterface() {

        /* ================================================================================
         * Construct the scene from bottom up starting with the deepest nested nodes
         * ================================================================================
         */

        // headerIcon, homeButton, defaultButton
        headerIcon = new ImageView("gui/resources/headerIcon.png");

        homeButton = new ImageView("gui/resources/home.png");
        // TODO: make default button

        filepathLabel = new Label("C:/Users/workspace/todo.txt");

        // headerGradient, filepathGradient, homeButtonGradient, defaultButtonGradient
        // sidebarGradient
        Stop stops[] = new Stop[] {
                new Stop(0, Color.rgb(246, 246, 246)),
                new Stop(1, Color.rgb(240, 240, 240))
        };

        headerGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        filepathGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        homeButtonGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        defaultButtonGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        sidebarGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        // headerRectangle, filepathRectangle, homeButtonRectangle, defaultButtonRectangle
        // sidebarRectangle
        headerRectangle = new Rectangle(MainApp.MIN_WINDOW_WIDTH, 60, headerGradient);
        filepathRectangle = new Rectangle(MainApp.MIN_WINDOW_WIDTH, 25, filepathGradient);
        homeButtonRectangle = new Rectangle(40, 40, homeButtonGradient);
        defaultButtonRectangle = new Rectangle(MainApp.MIN_WINDOW_WIDTH, 75, defaultButtonGradient);
        sidebarRectangle = new Rectangle(50, 50, sidebarGradient);

        // headerBG, filepathBG, homeButtonBG, defaultButtonBG
        headerBG = new Pane(headerRectangle);
        filepathBG = new Pane(filepathRectangle);
        homeButtonBG = new Pane(homeButtonRectangle);
        defaultButtonBG = new Pane(defaultButtonRectangle);
        sidebarBG = new Pane(sidebarRectangle);

        // headerBox, filepathBox
        headerBox = new HBox(headerIcon);
        filepathBox = new HBox(filepathLabel);

        // bufferStack, headerStack, filepathStack, homeButtonStack, defaultButtonStack
        bufferStack = new StackPane();
        headerStack = new StackPane(headerBG, headerBox);

        filepathStack = new StackPane(filepathBG, filepathLabel);
        filepathStack.setMaxHeight(25);
        filepathStack.setMinHeight(25);

        homeButtonStack = new StackPane(homeButtonBG, homeButton);
        // TODO: defaultButtonStack = new StackPane(defaultButtonBG, defaultButton);

        // sidebarBox, bodyViewBox
        sidebarBox = new VBox(bufferStack, homeButtonStack);
        // TODO: make body view box

        sidebarStack = new StackPane(sidebarBG, sidebarBox);
        sidebarStack.setMaxWidth(50);

        bodyBox = new HBox(sidebarStack);

        // headerLine, filepathLine
        headerLine = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);
        headerLine.endXProperty().bind(MainApp.stage.widthProperty());

        filepathLine = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);
        filepathLine.endXProperty().bind(MainApp.stage.widthProperty());

        interfaceBox = new VBox(headerStack, headerLine, filepathStack,
                filepathLine, bodyBox);

        /* ================================================================================
         * Adjust sizes of the panes
         * ================================================================================
         */
        headerRectangle.widthProperty().bind(interfaceBox.widthProperty());
        headerRectangle.heightProperty().bind(headerStack.heightProperty());

        filepathRectangle.widthProperty().bind(interfaceBox.widthProperty());

        StackPane.setAlignment(filepathLabel, Pos.CENTER_LEFT);
        StackPane.setMargin(filepathLabel, new Insets(0, 50, 0, 50));

        StackPane.setMargin(headerBox, new Insets(10, 0, 10, 50));

        VBox.setVgrow(bodyBox, Priority.ALWAYS);
        sidebarRectangle.heightProperty().bind(bodyBox.heightProperty());

        interfaceScene = new Scene(interfaceBox);
        interfaceScene.getStylesheets().add(
                InterfaceController.class.getResource("/gui/stylesheets/Interface.css").toExternalForm());

        return interfaceScene;
    }
}
