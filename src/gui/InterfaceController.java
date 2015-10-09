package gui;

import backend.Logic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private static VBox filepathBoxWithLine;
    private static Label filepathLabel;
    private static Region leftSpace, rightSpace;
    private static Line filepathLine;

    // Used for initSideBarHomeButton
    private static VBox sbHomeBox;
    private static ImageView sbHomeImage;
    private static Line sbHomeLine;

    // Used for initSideBar
    private static VBox sbBox;
    private static HBox sbBoxWithLine;
    private static Line sbLine;

    // Used for initTextField
    // textField set to protected to allow LogicController to access
    private static VBox textBox;
    protected static TextField textField;

    // Used for initFeedbackBar
    private static VBox feedbackBox, feedbackBoxWithLine;
    private static Label feedbackLabel;
    private static Line feedbackLine;

    // Used for initDefTaskView
    private static VBox defTaskBox, defTaskContentBox;
    private static ScrollPane defTaskScroll;
    private static ImageView defTaskImage;

    // Used for initDefEventView
    private static VBox defEventBox, defEventContentBox;
    private static ScrollPane defEventScroll;
    private static ImageView defEventImage;

    // Used for initDefView
    private static HBox defBox, tempBox;
    private static Line defScrollLine;

    // Used for initMainInterface
    private static Scene mainScene;
    private static VBox mainBox;
    private static Line defLine;

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

        filepathLabel = new Label("something");
        filepathLine = new Line(0, 0, DEFAULT_WIDTH, 0);

        filepathBox = new HBox(filepathLabel);
        filepathBoxWithLine = new VBox(filepathBox, filepathLine);

        // Set margins for the filepath label
        HBox.setMargin(filepathLabel, new Insets(8, 71, 0, 71));

        // Fix height for the filepath bar without lines
        filepathBox.setMaxHeight(35);
        filepathBox.setMinHeight(35);

        // Fix height for the filepath bar
        // +1 for line widths
        filepathBoxWithLine.setMaxHeight(36);
        filepathBoxWithLine.setMinHeight(36);

        // CSS
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
        VBox.setMargin(feedbackLabel, new Insets(8, 20, 0, 20));

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
    
    private static HBox initDisplayElement(String displayData) {
    	
    	// Define a containing HBox that will contain a label with the 
    	// formatted data and a background
    	HBox elementBox;
    	
    	Label elementLabel = new Label(displayData);
    	elementBox = new HBox(elementLabel);
    	
    	// Set the margins of the element node label within the HBox
    	HBox.setMargin(elementLabel, new Insets(10));
    	
    	// CSS
    	elementBox.getStyleClass().add("element-box");
    	
    	return elementBox;
    }
    
    private static void initDefTaskContents() {
    	
    	// TODO
    }

    private static void initDefTaskView() {

        defTaskImage = new ImageView("gui/resources/taskHeader.png");

        String returnMessage = "something\nsomething else\nsomething\ntest";
        String returnMessage2 = "something\nsomething else\nsomething even longer to test\ntest";

        defTaskContentBox = new VBox();
        
        // Use a temporary component for formatting
        tempBox = initDisplayElement(returnMessage);
        VBox.setMargin(tempBox, new Insets(0, 0, 15, 0));
        defTaskContentBox.getChildren().add(tempBox);
        
        tempBox = initDisplayElement(returnMessage2);
        VBox.setMargin(tempBox, new Insets(0, 0, 15, 0));       
        defTaskContentBox.getChildren().add(tempBox);
        
        defTaskScroll = new ScrollPane(defTaskContentBox);
        defTaskScroll.setFitToWidth(true);
        
        defTaskBox = new VBox(defTaskImage, defTaskScroll);
        
        // Set margins for the scroll pane
        VBox.setMargin(defTaskScroll, new Insets(10));
        
        // Set the alignment of the header image to be in the center
        defTaskBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(defTaskScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        defTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private static void initDefEventView() {

        defEventImage = new ImageView("gui/resources/eventHeader.png");

        String returnMessage = "something\nsomething else\nsomething\ntest";
        String returnMessage2 = "something\nsomething else\nsomething\ntest";
        String returnMessage3 = "something\nsomething else\nsomething\ntest";
        String returnMessage4 = "something\nsomething else\nsomething\ntest";
        String returnMessage5 = "something\nsomething else\nsomething\ntest";

        defEventContentBox = new VBox();
        
        // Use a temporary component for formatting
        tempBox = initDisplayElement(returnMessage);
        VBox.setMargin(tempBox, new Insets(0, 0, 15, 0));
        defEventContentBox.getChildren().add(tempBox);
        
        tempBox = initDisplayElement(returnMessage2);
        VBox.setMargin(tempBox, new Insets(0, 0, 15, 0));
        defEventContentBox.getChildren().add(tempBox);
        
        tempBox = initDisplayElement(returnMessage3);
        VBox.setMargin(tempBox, new Insets(0, 0, 15, 0));
        defEventContentBox.getChildren().add(tempBox);
        
        tempBox = initDisplayElement(returnMessage4);
        VBox.setMargin(tempBox, new Insets(0, 0, 15, 0));
        defEventContentBox.getChildren().add(tempBox);
        
        tempBox = initDisplayElement(returnMessage5);
        VBox.setMargin(tempBox, new Insets(0, 0, 15, 0));
        defEventContentBox.getChildren().add(tempBox);
        
        defEventScroll = new ScrollPane(defEventContentBox);
        defEventScroll.setFitToWidth(true);
        
        defEventBox = new VBox(defEventImage, defEventScroll);
        
        // Set margins for the scroll pane
        VBox.setMargin(defEventScroll, new Insets(10));
        
        // Set the alignment of the header image to be in the center
        defEventBox.setAlignment(Pos.CENTER);

        // Set the height of the scroll pane to grow with the window height
        VBox.setVgrow(defEventScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        defEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private static void initDefView() {

        initDefTaskView();
        initDefEventView();
        
        defScrollLine = new Line(0, 0, 0, DEFAULT_SIZE_BUTTON);
        
        defBox = new HBox(defTaskBox, defScrollLine, defEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        defTaskScroll.prefViewportWidthProperty().bind(defBox.widthProperty().divide(2));
        defEventScroll.prefViewportWidthProperty().bind(defBox.widthProperty().divide(2));
        
        // CSS
        defScrollLine.getStyleClass().add("line");
    }

    public static void initMainInterface() {

        // Attempt to create a logic object to obtain the filepath
        /*try {
            logic = new Logic();
        } catch (FileSystemException e) {
            // nothing
        }*/

        initFilePathBar();
        initSideBar();
        initDefView();
        initFeedbackBar();
        initTextField();
        
        // Create the line separator for defBox
        defLine = new Line(0, 0, DEFAULT_WIDTH, 0);

        // Create the region below the filepath bar excluding the sidebar
        VBox contentBoxNoSideBar = new VBox(defBox, defLine, feedbackBoxWithLine, textBox);
        
        // Set the height of defBox to grow with the window
        VBox.setVgrow(defBox, Priority.ALWAYS);

        // Create the region below the filepath bar including the sidebar
        HBox contentBoxWithSideBar = new HBox(sbBoxWithLine, contentBoxNoSideBar);

        // Set the width of contentBoxNoSideBar to grow with window
        HBox.setHgrow(contentBoxNoSideBar, Priority.ALWAYS);

        // Create the main VBox containing everything
        mainBox = new VBox(filepathBoxWithLine, contentBoxWithSideBar);

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

            // Set the height of the sidebar separator to
            // window height - height of filepath bar(35) - height of line(1)
            sbLine.setEndY((Double)newValue - 36);
            
            // Set the height of the scroll pane separator to
            // window height - height of the filepath bar(35) -
            // height of feedback bar(35) - height of text bar(45) - 
            // 2 * height of line(1)
            defScrollLine.setEndY((Double)newValue - 117);
        }
    }

    private static class WidthListener implements ChangeListener<Number> {
    	
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue,
                            Number newValue) {

            // Set the width of the feedback and view box separators to
            // window width - size of sidebar(50) - width of line(1)
            feedbackLine.setEndX((Double)newValue - 51);
            defLine.setEndX((Double)newValue - 51);

            // Set the width of the filepath separator to window width
            filepathLine.setEndX((Double)newValue);
        }
    }
}
