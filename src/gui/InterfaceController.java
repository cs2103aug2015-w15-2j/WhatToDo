package gui;

import backend.Logic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.LinearGradient;
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
    private static HBox filepathBox;
    private static VBox filepathBoxWithLines;
    private static Label filepathLabel;
    private static Region leftSpace, rightSpace;
    private static Line titleLine, filepathLine;

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
    private static VBox textBox;
    private static StackPane textStack;
    private static Rectangle textRect;
    private static TextField textField;

    // Used for initFeedbackBar
    private static Pane feedbackPane;
    private static VBox feedbackBox, feedbackBoxWithLine;
    private static StackPane feedbackStack;
    private static Label feedbackLabel;
    private static Rectangle feedbackRect;
    private static Line feedbackLine;

    // Used for initDefTaskView
    private static Pane defTaskPane;
    private static VBox defTaskBox, defTaskContentBox;
    private static ScrollPane defTaskScroll;
    private static ImageView defTaskImage;

    // Used for initDefEventView
    private static Pane defEventPane;
    private static VBox defEventBox, defEventContentBox;
    private static ScrollPane defEventScroll;
    private static ImageView defEventImage;

    // Used for initDefView
    private static Pane defPane;
    private static HBox defBox;

    // Used for initMainInterface
    private static Scene mainScene;
    private static VBox mainBox;
    private static Line contentBoxLine;

    private static Logic logic;

    private static final double DEFAULT_WIDTH = 100;
    private static final double DEFAULT_SIZE_BUTTON = 50;

    /*
    private static void initGradientBG() {

        // Define the start and end colors
        Stop stops[] = new Stop[] {
                new Stop(0, Color.rgb(250, 250, 250)),  // End color (top half)
                new Stop(1, Color.rgb(240, 240, 240))   // Start color (bottom half)
        };

        // Create the gradient
        background = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    }

    private static void initGradientBGSelect() {

        // Define the start and end colors
        Stop stops[] = new Stop[] {
                new Stop(0, Color.rgb(220, 220, 220)),  // End color (top half)
                new Stop(1, Color.rgb(240, 240, 240))   // Start color (bottom half)
        };

        // Create the gradient
        backgroundSelected = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    }
    */

    private static void initFilePathBar() {

        filepathLabel = new Label(logic.getFilepath());
        titleLine = new Line(0, 0, DEFAULT_WIDTH, 0);
        filepathLine = new Line(0, 0, DEFAULT_WIDTH, 0);

        // Create space nodes to center the text
        leftSpace = new Region();
        rightSpace = new Region();
        HBox.setHgrow(leftSpace, Priority.ALWAYS);
        HBox.setHgrow(rightSpace, Priority.ALWAYS);

        filepathBox = new HBox(leftSpace, filepathLabel, rightSpace);
        filepathBoxWithLines = new VBox(titleLine, filepathBox, filepathLine);

        // Set margins for the filepath label
        HBox.setMargin(filepathLabel, new Insets(8, 10, 0, 10));

        // Fix height for the filepath bar without lines
        filepathBox.setMaxHeight(35);
        filepathBox.setMinHeight(35);

        // Fix height for the filepath bar
        // +2 for line widths
        filepathBoxWithLines.setMaxHeight(37);
        filepathBoxWithLines.setMinHeight(37);

        // CSS
        titleLine.getStyleClass().add("line");
        filepathLine.getStyleClass().add("line");
        filepathBox.getStyleClass().add("gradient-regular");
    }

    private static void initSideBarHomeButton(String imagePath) {

        sbHomeImage = new ImageView(imagePath);
        sbHomeLine = new Line(0, 0, DEFAULT_SIZE_BUTTON, 0);

        sbHomeBox = new VBox(sbHomeImage, sbHomeLine);

        // Set margins for the home button
        VBox.setMargin(sbHomeImage, new Insets(10));

        // Fix width and height for the button
        // +1 for line width
        sbHomeBox.setMaxWidth(50);
        sbHomeBox.setMinWidth(50);

        sbHomeBox.setMaxHeight(51);
        sbHomeBox.setMinHeight(51);

        // CSS
        sbHomeLine.getStyleClass().add("line");
        sbHomeBox.getStyleClass().add("gradient-regular");
    }

    private static void initSideBar() {

        initSideBarHomeButton("gui/resources/homeButton.png");

        sbBox = new VBox(sbHomeBox);
        sbLine = new Line(0, 0, 0, DEFAULT_SIZE_BUTTON);

        sbBoxWithLine = new HBox(sbBox, sbLine);

        // Fix the width for the sidebar
        // +1 for line width
        sbBoxWithLine.setMaxWidth(51);
        sbBoxWithLine.setMinWidth(51);

        // CSS
        sbLine.getStyleClass().add("line");
        sbBoxWithLine.getStyleClass().add("gradient-regular");
    }

    private static void initTextField() {

        textField = new TextField();
        textField.requestFocus();

        textBox = new VBox(textField);

        // Set the margins for the text field
        VBox.setMargin(textField, new Insets(10));

        // Fix the height of the text field
        textBox.setMaxHeight(45);
        textBox.setMinHeight(45);

        // CSS
        textBox.getStyleClass().add("gradient-regular");
    }

    private static void initFeedbackBar() {

        feedbackLabel = new Label("No commands entered yet.");
        feedbackLine = new Line(0, 0, DEFAULT_WIDTH, 0);

        feedbackBox = new VBox(feedbackLabel);
        feedbackBoxWithLine = new VBox(feedbackBox, feedbackLine);

        // Set margins for the feedback label
        VBox.setMargin(feedbackLabel, new Insets(8, 15, 0, 15));

        // Fix the height of the feedback label
        feedbackBox.setMaxHeight(35);
        feedbackBox.setMinHeight(35);

        // Fix the height of the feedback bar
        // +1 for line width
        feedbackBoxWithLine.setMaxHeight(36);
        feedbackBoxWithLine.setMinHeight(36);

        // CSS
        feedbackLine.getStyleClass().add("line");
        feedbackBox.getStyleClass().add("gradient-regular");
    }

    private static void initDefTaskView() {

        defTaskImage = new ImageView("gui/resources/taskHeader.png");

        String returnMessage = "something\nsomething else\nsomething\ntest";
        Label defTaskLabel = new Label(returnMessage);

        String returnMessage2 = "something\nsomething else\nsomething\ntest";
        Label defTaskLabel2 = new Label(returnMessage2);

        defTaskContentBox = new VBox(defTaskLabel, defTaskLabel2);
        defTaskScroll = new ScrollPane(defTaskContentBox);
        defTaskBox = new VBox(defTaskImage, defTaskScroll);
        defTaskPane = new Pane(defTaskBox);

        defTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    private static void initDefEventView() {

        defEventImage = new ImageView("gui/resources/eventHeader.png");

        String returnMessage = "something\nsomething else\nsomething\ntest";
        Label defTaskLabel = new Label(returnMessage);

        String returnMessage2 = "something\nsomething else\nsomething\ntest";
        Label defTaskLabel2 = new Label(returnMessage2);

        String returnMessage3 = "something\nsomething else\nsomething\ntest";
        Label defTaskLabel3 = new Label(returnMessage3);

        defEventContentBox = new VBox(defTaskLabel, defTaskLabel2, defTaskLabel3);
        defEventScroll = new ScrollPane(defEventContentBox);
        defEventBox = new VBox(defEventImage, defEventScroll);
        defEventPane = new Pane(defEventBox);

        defEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    private static void initDefView() {

        initDefTaskView();
        initDefEventView();

        defBox = new HBox(defTaskPane, defEventPane);
        defPane = new Pane(defBox);
    }

    public static void initMainInterface() {

        // Attempt to create a logic object to obtain the filepath
        try {
            logic = new Logic();
        } catch (FileSystemException e) {
            // nothing
        }

        initFilePathBar();
        initSideBar();
        initFeedbackBar();
        initTextField();

        // Create the region below the filepath bar excluding the sidebar
        VBox contentBoxNoSideBar = new VBox(feedbackBoxWithLine, textBox);

        // Create the region below the filepath bar including the sidebar
        HBox contentBoxWithSideBar = new HBox(sbBoxWithLine, contentBoxNoSideBar);

        // Set the width of contentBoxNoSideBar to grow with window
        HBox.setHgrow(contentBoxNoSideBar, Priority.ALWAYS);

        // Create the main VBox containing everything
        mainBox = new VBox(filepathBoxWithLines, contentBoxWithSideBar);

        // Set the height of contentBoxWithSideBar to grow with window
        VBox.setVgrow(contentBoxWithSideBar, Priority.ALWAYS);

        mainScene = new Scene(mainBox);

        // Set resize listeners for the main scene
        mainScene.heightProperty().addListener(new HeightListener());
        mainScene.widthProperty().addListener(new WidthListener());

        // Set CSS styling
        mainScene.getStylesheets().add(InterfaceController.class.getResource(
                "/gui/stylesheets/Interface.css").toExternalForm());

        // Set the scene in MainApp
        MainApp.defaultView = mainScene;
    }

    private static class HeightListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue,
                            Number newValue) {
            sbLine.setEndY((Double)newValue - 37);
        }
    }

    private static class WidthListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue,
                            Number newValue) {
            feedbackLine.setEndX((Double)newValue - 51);
            titleLine.setEndX((Double)newValue);
            filepathLine.setEndX((Double)newValue);
        }
    }
}
