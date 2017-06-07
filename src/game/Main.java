package game;
	
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.geometry.Pos;


public class Main extends Application {
		
	private TicTacToe ttt;
	
	//save the buttons as a list to represent the game board
	private List<Button> buttonList;
	
	//whether the game ends
	private boolean isEnded;
	
	//label is used to represent the result
	private Label label;
	
	//mark of player
	private String player = TicTacToe.playerMark+"";
	
	//mark of ai
	private String ai = TicTacToe.aiMark+"";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("Start!!! ");
			//initialize this game
			ttt = new TicTacToe();
			isEnded = false;
			buttonList = new ArrayList<>();
			
			BorderPane root = new BorderPane();
			
			//set up the label
			label = new Label("");
			label.setFont(new Font("Arial", 30));
			root.setCenter(label);
			BorderPane.setAlignment(label, Pos.BOTTOM_CENTER);
			
			//set up the grid pane
		    GridPane gridPane = new GridPane();
		    gridPane.setPrefHeight(400);
		    gridPane.setPrefWidth(400);
		    root.setBottom(gridPane);
		    
		    //set up the buttons that represents the board
		    //set their id, height, width, font, action
		    int counter = 0;
		    for(int row = 0; row < 4; row++) {
		        for(int col = 0; col < 4; col++) {
		            Button button = new Button();
		            button.setId(""+counter++);
		            gridPane.add(button, col, row);
		            button.setPrefHeight(100);
		            button.setPrefWidth(100);
		            button.setFont(new Font("Arial", 25));
		            button.setOnAction(e -> handleButtonAction(e));
		            buttonList.add(button);
		        }
		    }
		    
		    //add a drop down menu for the difficulty and order to play
		    ChoiceBox<Object> cb = new ChoiceBox<>();
		    cb.getItems().addAll("Difficult", "Intermediate", "Easy", new Separator(),"Go First", "Go Second");
		    cb.setValue("Difficult");
		    cb.setTooltip(new Tooltip("Select the language or Restart"));
		    root.setTop(cb);
		    //add a listener for the choice box
		    cb.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)->handleCBoxAction(oldValue, newValue, cb));
		    
		    //show the scene
			Scene scene = new Scene(root, 400, 475);
			primaryStage.setScene(scene);
			primaryStage.show();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleCBoxAction(Object oldValue, Object newValue, ChoiceBox<Object> cb) {
		//if restart, keep the former difficulty, the old value
		int diff = newValue.equals("Easy")? 1 :
			newValue.equals("Intermediate")? 2:
				newValue.equals("Difficult")? 3: ttt.difficulty;
		if(newValue.equals("Go First") || newValue.equals("Go Second")) {
			cb.setValue(oldValue);
		}
		//initialize the GUI and the inner logic
		ttt = new TicTacToe(diff);
		isEnded = false;
		for(Button btn : buttonList) {
			btn.setText("");
		}
		label.setText("");
		if(newValue.equals("Go Second")) {
			//AI has the first move
			ttt.currmark = TicTacToe.aiMark;
			ttt.aiMove();
			int[] move = ttt.nextMove;
			int currId = move[0] * 4 + move[1];
			buttonList.get(currId).setText(ai);			
		}
	}
	
	private void handleButtonAction(ActionEvent event) {
	    // Button was clicked, set the text of that button
		// if game ends, or button has been occupied, no reaction
    		if(isEnded) return;
        Button btn = (Button) event.getSource();
        int id = Integer.parseInt(btn.getId());
        int x = id / 4;
        int y = id % 4;
        if(btn.getText().equals("")) {
        		btn.setText(player);
        		ttt.put(x, y);
        		int res = ttt.gameResult;
        		//res: -1: continue, 0: player draws, 1: ai draws, 
        		//2: player wins, 3: ai wins
        		if(res == 0) {
        			label.setText("DRAW!");
        			isEnded = true;
        		}else if(res == 2) {
        			label.setText(player+" WINNER!");
        			isEnded = true;
        		}else {
        			//player's move doesn't leads to an end
        			//ai will play its move
        			int[] move = ttt.nextMove;
        			int currId = move[0] * 4 + move[1];
        			buttonList.get(currId).setText(ai);
        			if(res == 1) {
              		label.setText("DRAW!");
            			isEnded = true;
        			}else if(res == 3) {
        				label.setText(ai+" WINNER!");
        				isEnded = true;
        			}
        		}
        }
	 }
	
	public static void main(String[] args) {
		launch(args);
	}
}
