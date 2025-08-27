
package BreakOut;

import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class leveltwo extends Application {
    
    Levelone method = new Levelone(); //object to use Levelone methods
    
    @Override
    public void start(Stage primaryStage) {
        //creating pane properties
        method.root = new Pane();
        Scene scene = new Scene(method.root, Levelone.BOARD_WIDTH, Levelone.BOARD_HEIGHT);
        
        //create background for Start
        Image image1 = new Image("Background.jpg");
        BackgroundImage backgroundImage1 = new BackgroundImage(image1, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background background1 = new Background(backgroundImage1);
        method.root.setBackground(background1);
        
        //create background for GamePlay
        Image image = new Image("Background2.png");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background2 = new Background(backgroundImage);
        
        // Create start button
        method.startButton.setFont(Font.font("Times New Roman", 20));
        method.startButton.setPrefWidth(100);
        method.startButton.setLayoutX((Levelone.BOARD_WIDTH / 2 - method.startButton.getPrefWidth() / 2) - 50);
        method.startButton.setLayoutY(Levelone.BOARD_HEIGHT / 2 - method.startButton.getPrefHeight() / 2);
        method.startButton.setOnAction(event -> {
            method.root.setBackground(background2);
            startGame();
        });
        // Create exit button
        method.ExitButton.setFont(Font.font("Times New Roman", 20));
        method.ExitButton.setPrefWidth(100);
        method.ExitButton.setLayoutX((Levelone.BOARD_WIDTH / 2 - method.startButton.getPrefWidth() / 2) + 80);
        method.ExitButton.setLayoutY(Levelone.BOARD_HEIGHT / 2 - method.startButton.getPrefHeight() / 2);
        method.ExitButton.setOnAction(e -> {
            primaryStage.close();
        });
        method.root.getChildren().addAll(method.startButton,method.ExitButton);
        
        //Stage properties
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Breakout Game");
        primaryStage.show();
    }//changed from levelone
    
    private void startGame() {
        
        // remove start & exit button
        method.root.getChildren().clear();

        //creating the game map
        createblocks();
        method.createPaddle();
        method.createBall();
        method.createScoreboard();
        
        //set FPS 'Timeline properties'
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15), event -> {
            method.moveBall();
            checkCollisions();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        method.root.requestFocus();
    }
    
    private void createblocks() {
        int x = 50;
        int y = 50;
        for (int row = 0; row < Levelone.BLOCK_ROWS; row++) {
            for (int col = 0; col < Levelone.BLOCK_COLUMNS - 3; col++) {
            Rectangle block = new Rectangle(x, y, Levelone.BLOCK_WIDTH, Levelone.BLOCK_HEIGHT);
            block.setStroke(Color.BLACK);
            block.setFill(Color.RED);
            block.getProperties().put("hits", 2); // initial hits required to break the block
            method.blocks[row][col] = block;
            method.root.getChildren().add(block);
            x += Levelone.BLOCK_WIDTH + 20; // add horizontal spacing between method.blocks
            }
            x = 50;
            y += Levelone.BLOCK_HEIGHT + 20; // add vertical spacing between rows
        }
    } //changed from levelone

    private void checkCollisions() {
        
        // Check for collisions with method.blocks
        for (int row = 0; row < Levelone.BLOCK_ROWS; row++) {
            for (int col = 0; col < Levelone.BLOCK_COLUMNS; col++) {
                Rectangle block = method.blocks[row][col];
                if (block != null && method.ball.getBoundsInParent().intersects(block.getBoundsInParent())) {
                    
                    // initial hits required to break the block
                    int hits = (int) block.getProperties().get("hits");
                    //check number of hits 
                    if (hits == 1) {
                    // Remove block and update score
                    method.root.getChildren().remove(block);
                    method.blocks[row][col] = null;
                    method.score += 10;
                    method.scoreLabel.setText("Score: " + method.score);
                    //check Win the game by final score
                    if (method.score == 350) {
                    endGame2Win();
                    }
                    // Reverse vertical direction of ball
                    method.ballSpeedY *= -1;
                    }
                    else {
                    // Decrement hits property of block
                    block.getProperties().put("hits", hits - 1);
                    block.setFill(Color.BISQUE);
                    // Reverse vertical direction of ball
                    method.ballSpeedY *= -1;
                    }
                }
            }
        }
        
        // Check for collisions with paddle
        if (method.ball.getBoundsInParent().intersects(method.paddle.getBoundsInParent())) {
            // Reverse vertical direction of ball
            method.ballSpeedY *= -1;
            // Change horizontal direction of ball based on where it hits the paddle
            double ballPositionRelativeToPaddle = method.ball.getCenterX() - method.paddle.getX();
            double paddleWidth = method.paddle.getWidth();
            double horizontalFactor = (ballPositionRelativeToPaddle - paddleWidth / 2) / (paddleWidth / 2);
            method.ballSpeedX = horizontalFactor * 5;
        }
    } //changed from levelone

    private void endGame2Win() {
        
        //remove map
        method.root.getChildren().clear();
        
        //create background for nextlevel
        Image image3 = new Image("Background3.jpg");
        BackgroundImage backgroundImage3 = new BackgroundImage(image3, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background background3 = new Background(backgroundImage3);
        method.root.setBackground(background3);
        
        //exit label
        method.ExitButton.setLayoutX(Levelone.BOARD_WIDTH / 2 - method.startButton.getPrefWidth() / 2);
        method.ExitButton.setLayoutY((Levelone.BOARD_HEIGHT / 2 - method.startButton.getPrefHeight() / 2)+ 50);
        method.root.getChildren().addAll(method.ExitButton);
    }//changed from levelone

    public static void main(String[] args) {
        launch(args);
    }
} 