package gui;

import backend.Logic;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.nio.file.FileSystemException;

public class InterfaceController {

    /*
     * ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */

    // Used for design
    private static LinearGradient background, backgroundSelected;

    // Used for initFilePathBar
    private static Pane filepathPane;
    private static VBox filepathBox;
    private static StackPane filepathStack;
    private static Label filepathLabel;
    private static Rectangle filepathRect;
    private static Line filepathLine;

    // Used for initSideBarHomeButton
    private static Pane sbHomePane;
    private static VBox sbHomeBox;
    private static StackPane sbHomeStack;
    private static ImageView sbHomeImage;
    private static Rectangle sbHomeRect;
    private static Line sbHomeLine;

    // Used for initSideBar
    private static Pane sbPane;
    private static StackPane sbStack;
    private static VBox sbBox;
    private static HBox sbBoxWithLine;
    private static Rectangle sbRect;
    private static Line sbLine;

    // Used for initTextField
    private static Pane textPane;
    private static StackPane textStack;
    private static Rectangle textRect;
    private static TextField textField;

    // Used for initFeedbackBar
    private static Pane feedbackPane;
    private static VBox feedbackBox;
    private static StackPane feedbackStack;
    private static Label feedbackLabel;
    private static Rectangle feedbackRect;
    private static Line feedbackLine;

    // Used for initMainInterface
    private static HBox contentBoxWithSideBar;
    private static VBox contentBox, mainBox;

    // Used for initDefTaskView
    private static Pane defTaskPane;
    private static VBox defTaskBox, defTaskContentBox;
    private static ScrollPane defTaskScroll;
    private static ImageView defTaskImage;

    // Used for initDefView
    private static Pane defPane;
    private static HBox defBox;

    // Used for initMainInterface
    private static Scene mainScene;
    private static Line contentBoxLine;

    private static Logic logic;

    private static void initGradientBG() {

        // Define the start and end colors
        Stop stops[] = new Stop[] {
                new Stop(0, Color.rgb(246, 246, 246)),  // End color (top half)
                new Stop(1, Color.rgb(240, 240, 240))   // Start color (bottom half)
        };

        // Create the gradient
        background = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    }

    private static void initGradientBGSelect() {

        // Define the start and end colors
        Stop stops[] = new Stop[] {
                new Stop(0, Color.rgb(220, 220, 220)),  // End color (top half)
                new Stop(1, Color.rgb(230, 230, 230))   // Start color (bottom half)
        };

        // Create the gradient
        backgroundSelected = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    }

    private static void initFilePathBar() {

        filepathLabel = new Label(logic.getFilepath());
        filepathRect = new Rectangle(MainApp.MIN_WINDOW_WIDTH, 35, background);
        filepathLine = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);

        filepathStack = new StackPane(filepathRect, filepathLabel);
        filepathBox = new VBox(filepathStack, filepathLine);
        filepathPane = new Pane(filepathBox);

        // Set the alignment for the label to be in the center
        StackPane.setAlignment(filepathLabel, Pos.CENTER);

        // Set the alignment for the rectangle to begin from the left
        StackPane.setAlignment(filepathRect, Pos.CENTER_LEFT);

        // Fix height for the filepath bar
        // +1 for the line width
        filepathPane.setMaxHeight(36);
        filepathPane.setMinHeight(36);
    }

    private static void initSideBarHomeButton(String imagePath) {

        sbHomeImage = new ImageView(imagePath);
        sbHomeRect = new Rectangle(50, 50, backgroundSelected);
        sbHomeLine = new Line(0, 0, 50, 0);

        sbHomeStack = new StackPane(sbHomeRect, sbHomeImage);
        sbHomeBox = new VBox(sbHomeStack, sbHomeLine);
        sbHomePane = new Pane(sbHomeBox);

        // Set center alignment for the home button
        StackPane.setAlignment(sbHomeImage, Pos.CENTER);

        // Fix width and height for the button
        // +1 for line width
        sbHomePane.setMaxWidth(50);
        sbHomePane.setMinWidth(50);

        sbHomePane.setMaxHeight(51);
        sbHomePane.setMinHeight(51);

        // Set the background to fit to width and height
        sbHomeRect.widthProperty().bind(sbHomePane.widthProperty());

        // Set the background to align to the left
        StackPane.setAlignment(sbHomeRect, Pos.CENTER_LEFT);
    }

    private static void initSideBar() {

        initSideBarHomeButton("gui/resources/homeButton.png");

        sbBox = new VBox(sbHomePane);
        sbLine = new Line(0, 0, 0, 250);
        sbRect = new Rectangle(50, 250, background);

        sbStack = new StackPane(sbRect, sbBox);
        sbBoxWithLine = new HBox(sbStack, sbLine);
        sbPane = new Pane(sbBoxWithLine);

        // Fix the width for the sidebar
        // +1 for line width
        sbPane.setMaxWidth(51);
        sbPane.setMinWidth(51);
    }

    private static void initTextField() {

        textField = new TextField();
        textField.requestFocus();

        textRect = new Rectangle(250, 40, background);

        textStack = new StackPane(textRect, textField);
        textPane = new Pane(textStack);

        StackPane.setMargin(textField, new Insets(0, 10, 0, 10));

        // Set the alignment of the background to start from left
        StackPane.setAlignment(textRect, Pos.CENTER_LEFT);

        // Fix the height of the text field
        textPane.setMaxHeight(40);
        textPane.setMinHeight(40);
    }

    private static void initFeedbackBar() {

        feedbackLabel = new Label("No commands entered yet.");
        feedbackRect = new Rectangle(250, 35, background);
        feedbackLine = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);

        feedbackStack = new StackPane(feedbackRect, feedbackLabel);
        feedbackBox = new VBox(feedbackStack, feedbackLine);
        feedbackPane = new Pane(feedbackBox);

        // Align the text using CENTER_LEFT
        StackPane.setAlignment(feedbackLabel, Pos.CENTER_LEFT);
        StackPane.setMargin(feedbackLabel, new Insets(0, 10, 0, 10));

        // Set the alignment for the background to start from the left
        StackPane.setAlignment(feedbackRect, Pos.CENTER_LEFT);

        // Fix the height of the feedback bar
        // +1 for line width
        feedbackPane.setMaxHeight(36);
        feedbackPane.setMinHeight(36);
    }

    private static void initDefTaskView() {

        defTaskImage = new ImageView("gui/resources/taskHeader.png");

        String returnMessage = logic.taskDefaultView();
        Label defTaskLabel = new Label(returnMessage);

        defTaskContentBox = new VBox(defTaskLabel);
        defTaskScroll = new ScrollPane(defTaskContentBox);
        defTaskBox = new VBox(defTaskImage, defTaskScroll);
        defTaskPane = new Pane(defTaskBox);

        // Set the text to wrap
        defTaskLabel.setWrapText(true);

        // Set the size of the task pane to grow
        // Will even out with defEventBox
        HBox.setHgrow(defTaskBox, Priority.ALWAYS);
    }

    private static void initDefView() {

        initDefTaskView();

        defBox = new HBox(defTaskPane);
        defPane = new Pane(defBox);
    }

    public static Scene initMainInterface() {

        // Attempt to create a logic object to obtain the filepath
        try {
            logic = new Logic();
        } catch (FileSystemException e) {
            // nothing
        }

        // Initialize the gradients used for styling
        initGradientBG();
        initGradientBGSelect();

        // Initialize the components of the interface
        initFilePathBar();
        initSideBar();
        initTextField();
        initFeedbackBar();
        initDefView();

        // Create the content box without the sidebar first
        contentBoxLine = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);

        contentBox = new VBox(defPane, contentBoxLine, feedbackPane, textPane);
        HBox.setHgrow(contentBox, Priority.ALWAYS);
        VBox.setVgrow(defPane, Priority.ALWAYS);

        // Add the sidebar to another content box
        contentBoxWithSideBar = new HBox(sbPane, contentBox);
        VBox.setVgrow(contentBoxWithSideBar, Priority.ALWAYS);

        // Add the file path bar on top
        mainBox = new VBox(filepathPane, contentBoxWithSideBar);

        mainScene = new Scene(mainBox);

        /* ================================================================================
         * Perform width and height bindings that require all components to be created
         * ================================================================================
         */

        // Set the line to scale with the width of the window when resized
        filepathLine.endXProperty().bind(mainScene.widthProperty());

        // Make the background scale with the width of the window when resized
        filepathRect.widthProperty().bind(mainScene.widthProperty());

        // Set height of the sidebar background to scale with height of the sidebar
        // Sidebar height set by VBox.setVgrow()
        sbRect.heightProperty().bind(sbPane.heightProperty());

        // Set height of sidebar line separator to scale with sidebar height
        sbLine.endYProperty().bind(sbPane.heightProperty());

        // Set text field background to scale with width of the content box
        // Content box width set by HBox.setHgrow()
        textRect.widthProperty().bind(contentBox.widthProperty());

        // Set the feedback bar width to scale with width of the content box
        feedbackRect.widthProperty().bind(contentBox.widthProperty());

        // Set the feedback box line separator to scale with width of content box
        feedbackLine.endXProperty().bind(contentBox.widthProperty());

        // Set the default line to scale with window width
        contentBoxLine.endXProperty().bind(contentBox.widthProperty());

        // Set CSS styling
        mainScene.getStylesheets().add(InterfaceController.class.getResource(
                "/gui/stylesheets/Interface.css").toExternalForm());

        return mainScene;
    }

    //public static Scene initInterface() {

        /* ================================================================================
         * Construct the scene from bottom up starting with the deepest nested nodes
         * ================================================================================
         */

        // headerIcon, homeButton, defaultButton
        /*headerIcon = new ImageView("gui/resources/headerIcon.png");

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

        contentBoxWithSideBar = new HBox(sidebarStack);

        // headerLine, filepathLine
        headerLine = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);
        headerLine.endXProperty().bind(MainApp.stage.widthProperty());

        filepathLine = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);
        filepathLine.endXProperty().bind(MainApp.stage.widthProperty());

        interfaceBox = new VBox(headerStack, headerLine, filepathStack,
                filepathLine, contentBoxWithSideBar);

        /* ================================================================================
         * Adjust sizes of the panes
         * ================================================================================
         *//*
        headerRectangle.widthProperty().bind(interfaceBox.widthProperty());
        headerRectangle.heightProperty().bind(headerStack.heightProperty());

        filepathRectangle.widthProperty().bind(interfaceBox.widthProperty());

        StackPane.setAlignment(filepathLabel, Pos.CENTER_LEFT);
        StackPane.setMargin(filepathLabel, new Insets(0, 50, 0, 50));

        StackPane.setMargin(headerBox, new Insets(10, 0, 10, 50));

        VBox.setVgrow(contentBoxWithSideBar, Priority.ALWAYS);
        sidebarRectangle.heightProperty().bind(contentBoxWithSideBar.heightProperty());

        interfaceScene = new Scene(interfaceBox);
        interfaceScene.getStylesheets().add(
                InterfaceController.class.getResource("/gui/stylesheets/Interface.css").toExternalForm());

        return interfaceScene;
    }*/
}
