import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {


     static final int Width = 1200;
     static final int Height = 1200;
     static final int Rows = 40;
     static final int Columns = 40;
     static final int WindowsSize = Width/Rows;
     static final int Up = 0;
     static final int Down = 1;
     static final int Left = 2;
     static final int Right = 3;


    private final List<Snake> Snake=new ArrayList<>();
    private Snake SnakeHead;
    private boolean GameOver=false;
    private GraphicsContext graphicsContext;
    private int FoodX;
    private int FoodY;
    private int Score=0;
    private int CurrentDirection = Up;
    private boolean Escape=false;



    public static void main(String[] args) {
    launch(args);
}


    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("Snake");
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(Width,Height);
        root.getChildren().add(canvas);
        graphicsContext=canvas.getGraphicsContext2D();
        scene.setFill(Color.MINTCREAM);
        Snake.add(new Snake(Columns / 2,Rows / 2));
        SnakeHead = Snake.get(0);
        GenerateFood();

        primaryStage.setScene(scene);
        primaryStage.show();


        Button Start=new Button("Start");
        Start.setLayoutX(scene.getWidth()/2);
        Start.setLayoutY(scene.getHeight()/2);
        root.getChildren().add(Start);

        Button Load=new Button("Load");
        Load.setLayoutX(scene.getWidth()/2);
        Load.setLayoutY(Start.getLayoutY()+30);
        root.getChildren().add(Load);

        Button Exit=new Button("Exit");
        Exit.setLayoutX(scene.getWidth()/2);
        Exit.setLayoutY(Load.getLayoutY()+30);
        root.getChildren().add(Exit);

        Button Save=new Button("Save");
        Save.setLayoutX(scene.getWidth()/2);
        Save.setLayoutY(Load.getLayoutY()+30);

        Button Restart = new Button("Restart");
        Restart.setLayoutY(Start.getLayoutY()-30);
        Restart.setLayoutX(Start.getLayoutX());





        Start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Timeline timeline=new Timeline(new KeyFrame(Duration.millis(60), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        run(graphicsContext);
                    }
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
                root.getChildren().removeAll(Start, Save, Load, Exit, Restart);

                root.requestFocus();
                root.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {

                    if(keyEvent.getCode() == KeyCode.ESCAPE && !Escape)
                    {
                        Start.setText("Resume");
                        timeline.pause();

                        Save.setLayoutY(Load.getLayoutY()+30);
                        Exit.setLayoutY(Save.getLayoutY()+30);
                        root.getChildren().addAll(Start, Restart, Save, Exit, Load);
                        Escape=true;


                    }
                    else if(keyEvent.getCode() == KeyCode.ESCAPE && Escape)
                    {
                        root.getChildren().removeAll(Start, Restart, Save, Exit, Load);
                        timeline.play();
                        Escape=false;

                    }
                    else if(keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP)
                    {
                        if(CurrentDirection!=Down) {
                            CurrentDirection = Up; }
                    }
                    else if(keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN)
                    {
                        if(CurrentDirection!=Up)
                        {
                            CurrentDirection = Down;
                        }
                    }
                    else if(keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT )
                    {
                        if(CurrentDirection!=Right)
                        {
                            CurrentDirection = Left;
                        }
                    }
                    else if(keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)
                    {
                        if (CurrentDirection!=Left)
                        {
                            CurrentDirection = Right;
                        }
                    }


                }
            });
            }

           

        });


        Load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try(BufferedReader bufferedReader=new BufferedReader(new FileReader("Save.txt")))
                {
                    Reset(canvas);

                    while (bufferedReader.ready()) {
                        String line=bufferedReader.readLine();
                        String[]data=line.split(" ");
                        FoodX=Integer.parseInt(data[0]);
                        FoodY=Integer.parseInt(data[1]);
                        Score=Integer.parseInt(data[2]);
                        CurrentDirection=Integer.parseInt(data[3]);
                        int Snakedata=5;
                        for (int i=0; i<Integer.parseInt(data[4])-1; i++)
                        {
                            Snake.add(new Snake(Integer.parseInt(data[Snakedata]), Integer.parseInt(data[Snakedata])));
                            Snakedata++;
                        }
                        SnakeHead = Snake.get(0);
                        Start.fire();
                    }
                }catch (IOException e)
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.show();
                }

            }
        });
        Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)  {
                if(!GameOver) {
                    File file = new File("Save.txt");
                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                        bufferedWriter.write(FoodX + " " + FoodY + " " + Score + " " + CurrentDirection + " " + Snake.size() + " ");
                        for (Snake snake : Snake) {
                            bufferedWriter.write(snake.getX() + " " + snake.getY() + " ");
                        }

                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText(e.getMessage());
                        alert.show();
                    }
                }
                else {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("You cannot save lost game!");
                    alert.show();
                }
            }
        });

        Exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
            }
        });

        Restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                 Reset(canvas);
                Start.fire();

            }
        });



    }
    private void Reset(Canvas canvas) {
        CurrentDirection = Up;
        Score = 0;
        GameOver = false;
        Snake.removeAll(Snake);
        Snake.add(new Snake(Columns / 2,Rows / 2));
        SnakeHead = Snake.get(0);
        graphicsContext=canvas.getGraphicsContext2D();
        FoodX=(int)(Math.random()*Rows/2);
        FoodY=(int)(Math.random()*Columns/2);
    }
    private void run(GraphicsContext graphicsContext)
    {
        if (GameOver)
        {
            PrintGameOver(graphicsContext);
        }
        else {

            drawBoard();
            drawFood(graphicsContext);
            drawSnake(graphicsContext);
            PrintScore(graphicsContext);

            for (int i = Snake.size() - 1; i >= 1; i--) {
                Snake.get(i).setX(Snake.get(i - 1).getX());
                Snake.get(i).setY(Snake.get(i - 1).getY());
            }

            switch (CurrentDirection) {
                case Up -> SnakeHead.MoveUp();
                case Down -> SnakeHead.MoveDown();
                case Right -> SnakeHead.MoveRight();
                case Left -> SnakeHead.MoveLeft();
            }
            Eat();
            GameOver();
        }

    }

    private void drawBoard() {
        for (int col=0; col<=Columns;col++)
        {
            for (int row=0;row<=Rows;row++)
            {
                graphicsContext.setFill(Color.GREEN);
                graphicsContext.fillRect(col * WindowsSize,row * WindowsSize,WindowsSize,WindowsSize);
            }
        }
    }

    private void drawSnake(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.BLANCHEDALMOND);
        graphicsContext.fillRoundRect(SnakeHead.getX() * WindowsSize, SnakeHead.getY()* WindowsSize, WindowsSize, WindowsSize, 10, 10);
        for (Snake snake : Snake) {
            graphicsContext.fillRoundRect(snake.getX() * WindowsSize, snake.getY() * WindowsSize, WindowsSize, WindowsSize, 10, 10);
        }

    }
    private void drawFood(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(new Color(Math.random(), Math.random(), Math.random(), Math.random()));
        graphicsContext.fillRoundRect(FoodX * WindowsSize, FoodY* WindowsSize, 30, 30,100, 100);
    }
    private void GenerateFood()
    {
        start:
        while (true)
        {
            FoodX=(int)(Math.random()*Rows/2);
            FoodY=(int)(Math.random()*Columns/2);
            for (Snake snake: Snake
                 ) {
                if(snake.getX()==FoodX && snake.getY()==FoodY)
                {
                    continue start;
                }

            }
            break;
        }
    }
    private void Eat()
    {
        if(Snake.get(0).getX()==FoodX && Snake.get(0).getY()==FoodY)
        {
            Snake.add(new Snake(-1,-1));
            GenerateFood();
            Score++;

        }
    }
    private void GameOver()
    {
        if (SnakeHead.getX()<0 || SnakeHead.getY()<0 || SnakeHead.getX() * WindowsSize >= Width || SnakeHead.getY() * WindowsSize >= Height )
        {
            GameOver=true;
        }

            for (int i = 1; i < Snake.size(); i++) {
                if (SnakeHead.getX() == Snake.get(i).getX() && SnakeHead.getY() == Snake.get(i).getY()) {
                    GameOver = true;
                    break;
                }
            }


    }
    private void PrintGameOver(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("YOU LOST",Width/2 , Height/2);


    }
    private void PrintScore(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillText("Score: "+Score, Columns, Rows);
        graphicsContext.setFont(new Font(30));
    }




}

