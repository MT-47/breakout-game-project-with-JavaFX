
package BreakOut;

import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.geometry.*;
import javafx.scene.image.Image;

public class Levelone extends Application {

    //creating window properties 
    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 400;

    //creating bricks properties
    public static final int BLOCK_ROWS = 5;
    public static final int BLOCK_COLUMNS = 10;
    public static final int BLOCK_WIDTH = BOARD_WIDTH / BLOCK_COLUMNS;
    public static final int BLOCK_HEIGHT = 20;
    public static final Color[] BLOCK_COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE};
    public final Rectangle[][] blocks = new Rectangle[BLOCK_ROWS][BLOCK_COLUMNS]; 
    
    //creating paddle properties
    public Rectangle paddle;
    
    //creating ball properties
    public Circle ball;
    public double ballSpeedX = 5;
    public double ballSpeedY = -5;

    //creating dashboard 
    public int lives = 3;
    public int score = 0;
    public Label scoreLabel;
    public Label livesLabel;
    
    // start and exit declarition
    public Button startButton = new Button("Start");
    public Button ExitButton = new Button("Exit");
    
    //next level button
    public Button Level2 = new Button("Level 2");
    
    public Pane root; //public pane to use in leveltwo
    
    @Override
    public void start(Stage primaryStage) {
        
        //creating pane properties
        root = new Pane();
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        
        //create background for Start
        Image image1 = new Image("Background.jpg");
        BackgroundImage backgroundImage1 = new BackgroundImage(image1, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background background1 = new Background(backgroundImage1);
        root.setBackground(background1);
        
        //create background for GamePlay
        Image image = new Image("Background2.png");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background2 = new Background(backgroundImage);
        
        // Create start button
        startButton.setFont(Font.font("Times New Roman", 20));
        startButton.setPrefWidth(100);
        startButton.setLayoutX((BOARD_WIDTH / 2 - startButton.getPrefWidth() / 2) - 50);
        startButton.setLayoutY(BOARD_HEIGHT / 2 - startButton.getPrefHeight() / 2);
        startButton.setOnAction(event -> {
            root.setBackground(background2);
            startGame();
        });
        // Create exit button
        ExitButton.setFont(Font.font("Times New Roman", 20));
        ExitButton.setPrefWidth(100);
        ExitButton.setLayoutX((BOARD_WIDTH / 2 - startButton.getPrefWidth() / 2) + 80);
        ExitButton.setLayoutY(BOARD_HEIGHT / 2 - startButton.getPrefHeight() / 2);
        ExitButton.setOnAction(e -> {
            primaryStage.close();
        });
        root.getChildren().addAll(startButton,ExitButton);
        
        //creating object from class level 2 
        leveltwo nextlevel = new leveltwo();
        //creat next level button properties
        Level2.setFont(Font.font("Times New Roman", 20));
        Level2.setLayoutX((BOARD_WIDTH / 2 - startButton.getPrefWidth() / 2) + 110);
        Level2.setLayoutY((BOARD_HEIGHT / 2 - startButton.getPrefHeight() / 2) + 20);
        Level2.setPrefWidth(100);
        Level2.setOnAction(e -> {   
            nextlevel.start(primaryStage);
        });
        
        //Stage properties
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Breakout Game");
        primaryStage.show();
    }

    private void startGame() {
        
        // remove start & exit button
        root.getChildren().clear();

        //creating the game map
        createBlocks();
        createPaddle();
        createBall();
        createScoreboard();
        
        //set FPS 'Timeline properties'
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15), event -> {
            moveBall();
            checkCollisions();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        root.requestFocus();
    }//private to levelone only

    private void createBlocks() {
        for (int row = 0; row < BLOCK_ROWS; row++) {
            for (int col = 0; col < BLOCK_COLUMNS; col++) {
                Rectangle block = new Rectangle(col * BLOCK_WIDTH, row * BLOCK_HEIGHT + 30, BLOCK_WIDTH, BLOCK_HEIGHT);
                block.setFill(BLOCK_COLORS[row]);
                block.setStroke(Color.BLACK);
                blocks[row][col] = block;
                root.getChildren().add(block);
            }
        }
    }//private to levelone only

    public void createPaddle() {
        
        //creating paddle properties
        paddle = new Rectangle(BOARD_WIDTH / 2 - 50, BOARD_HEIGHT - 50, 100, 20);
        paddle.setFill(Color.WHITE);
        paddle.setArcHeight(30);
        paddle.setArcWidth(30);
        root.getChildren().add(paddle);

        // Add mouse event listeners to move the paddle
        root.setOnMouseMoved(event -> {
            //mouse X cordinates
            double x = event.getX();
            //left move condition
            if (x < paddle.getWidth() / 2) {
                x = paddle.getWidth() / 2;
            }
            //right move condition
            else if (x > BOARD_WIDTH - paddle.getWidth() / 2) {
                x = BOARD_WIDTH - paddle.getWidth() / 2;
            }
            //move paddle line
            paddle.setX(x - paddle.getWidth() / 2);
        });
        
        //Add Keyboard event listeners to move the paddle
        root.setOnKeyPressed(event -> {
            //left move condition
            if(event.getCode() == KeyCode.LEFT){
                //left limit
                if(paddle.getX() > 0){
                    paddle.setX(paddle.getX() - 15);
                }
            }
            //right move condition
            else if(event.getCode() == KeyCode.RIGHT){
                //right limit
                if(paddle.getX() < BOARD_WIDTH - paddle.getWidth()){
                    paddle.setX(paddle.getX() + 15);
                }
            }
        });
    }

    public void createBall() {
        ball = new Circle(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 10, 10);
        ball.setFill(Color.WHITE);
        root.getChildren().add(ball);
    }

    public void createScoreboard() {
        
        //create scorcelabel properties
        scoreLabel = new Label("Score: " + score);
        scoreLabel.setFont(Font.font("Times New Roman", 20));
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setAlignment(Pos.CENTER_LEFT);
        scoreLabel.setPrefWidth(100);
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(BOARD_HEIGHT - 30);
        
        //create liveslabel properties
        livesLabel = new Label("Lives: " + lives);
        livesLabel.setFont(Font.font("Times New Roman", 20));
        livesLabel.setTextFill(Color.WHITE);
        livesLabel.setAlignment(Pos.CENTER_RIGHT);
        livesLabel.setPrefWidth(100);
        livesLabel.setLayoutX(BOARD_WIDTH - livesLabel.getPrefWidth() - 10);
        livesLabel.setLayoutY(BOARD_HEIGHT - 30);

        root.getChildren().addAll(scoreLabel, livesLabel);
    }

    public void moveBall() {
        
        //start move
        ball.setCenterX(ball.getCenterX() + ballSpeedX);
        ball.setCenterY(ball.getCenterY() + ballSpeedY);
        //right and left impact
        if (ball.getCenterX() < 0 || ball.getCenterX() > BOARD_WIDTH) {
            ballSpeedX *= -1;
        }
        //up impact
        if (ball.getCenterY() < 0) {
            ballSpeedY *= -1;
        }
        //fall ball
        if (ball.getCenterY() > BOARD_HEIGHT) {
            loseLife();
        }
    }
    
    private void checkCollisions() {
        
        // Check for collisions with blocks
        for (int row = 0; row < BLOCK_ROWS; row++) {
            for (int col = 0; col < BLOCK_COLUMNS; col++) {
                Rectangle block = blocks[row][col];
                if (block != null && ball.getBoundsInParent().intersects(block.getBoundsInParent())) {
                    // Remove block and update score
                    root.getChildren().remove(block);
                    blocks[row][col] = null;
                    score += 10;
                    scoreLabel.setText("Score: " + score);
                    //check Win the game by final score
                    if (score == 500) {
                    endGame1Win();
                    }
                    // Reverse vertical direction of ball
                    ballSpeedY *= -1;
                }
            }
        }
        
        // Check for collisions with paddle
        if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
            // Reverse vertical direction of ball
            ballSpeedY *= -1;
            // Change horizontal direction of ball based on where it hits the paddle
            double ballPositionRelativeToPaddle = ball.getCenterX() - paddle.getX();
            double horizontalFactor = (ballPositionRelativeToPaddle - paddle.getWidth() / 2) / (paddle.getWidth() / 2);
            ballSpeedX = horizontalFactor * 5;
        }
    }//private to levelone only
    
    public void loseLife() {
        lives--;
        livesLabel.setText("Lives: " + lives);
        if (lives == 0) {
            endGame1Lose();
        } else {
            resetBall();
        }
    }
    
    public void resetBall() {
        ball.setCenterX(paddle.getX() + paddle.getWidth() / 2);
        ball.setCenterY(paddle.getY() - 10);
    }
    
    private void endGame1Win() {
        
        //remove map
        root.getChildren().clear();
        
        //create background for nextlevel
        Image image3 = new Image("Background3.jpg");
        BackgroundImage backgroundImage3 = new BackgroundImage(image3, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background background3 = new Background(backgroundImage3);
        root.setBackground(background3);
        
        //exit label
        ExitButton.setLayoutX((BOARD_WIDTH / 2 - startButton.getPrefWidth() / 2) - 50);
        ExitButton.setLayoutY((BOARD_HEIGHT / 2 - startButton.getPrefHeight() / 2)+ 50);
        //next level label
        Level2.setLayoutX((BOARD_WIDTH / 2 - startButton.getPrefWidth() / 2) + 60);
        Level2.setLayoutY((BOARD_HEIGHT / 2 - startButton.getPrefHeight() / 2) + 50);
        //add them
        root.getChildren().addAll(ExitButton,Level2);
    }//private to levelone only
    
    public void endGame1Lose() {
        
        //remove map
        root.getChildren().clear();
        
        //create background for GameOver
        Image image4 = new Image("Background4.jpg");
        BackgroundImage backgroundImage4 = new BackgroundImage(image4, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        Background background4 = new Background(backgroundImage4);
        root.setBackground(background4);
        
        //exit button
        ExitButton.setLayoutX((BOARD_WIDTH / 2 - startButton.getPrefWidth() / 2));
        ExitButton.setLayoutY((BOARD_HEIGHT / 2 - startButton.getPrefHeight() / 2)+ 50);
        root.getChildren().add(ExitButton);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
