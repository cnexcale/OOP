import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill; 
import javafx.scene.layout.CornerRadii;


import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Random;

public class SevenDiceGame extends Application{
	private class Dice {
		private String testInput;
		private int inputPosition;
		private Random source;

      
		Dice () {
			testInput = null;
			inputPosition = 0;
			source = new Random();
		}
      
		int nextOutcome () {
			if (testInput == null) {
				// look for and initialize test input on first use
				testInput = String.join("", getParameters().getUnnamed());
				// allows digits to be split into separate command line parameters
				if (!testInput.matches("[1-6]*"))  // only digits in range 1 to 6 are valid
					throw new IllegalArgumentException("Illegal character in test input for dice");
			}
			if (testInput.isEmpty())  // normal use: return a pseudo random dice result in range 1 to 6
				return source.nextInt(6) + 1;
			else {  // use next digit from command line parameters as the dice result
				if (inputPosition >= testInput.length())
					throw new IndexOutOfBoundsException("Test input for dice has been exhausted");
				return Character.digit(testInput.charAt(inputPosition++), 10);
			}
		}
	}
	
	
	
	private final int DICE_COUNT = 7;
	private int numberOfThrows = 3;
	private Dice theDice;
 	private int diceSize = 30;
	HBox diceRow;
	VBox collector;
	
	public SevenDiceGame(){
		theDice = new Dice();
	}
	public void start (Stage stage){
		
		collector = new VBox(5.0);
		
		//controls
		HBox controlPanel = new HBox(5.0);
			controlPanel.setPadding(new Insets(5.0));
		
		Button reroll = new Button("Reroll");
			reroll.setOnAction(e -> {	reroll();	});
			controlPanel.getChildren().add(reroll);
		
		Button close = new Button("Close");
			close.setOnAction (e -> { Platform.exit(); });
			controlPanel.getChildren().add(close);
		
		collector.getChildren().add(controlPanel);
		
		//dices
		firstRoll(numberOfThrows);

		Scene gameScene = new Scene(collector);
		
		stage.setTitle("Zehntausend");
		stage.setScene(gameScene);
		stage.sizeToScene();
		stage.show();
	}
	
	
	void firstRoll(int numberOfThrows){
		for(int i = 0; i < numberOfThrows; i++){
			diceRow = getDiceRow(DICE_COUNT);
			collector.getChildren().add(diceRow);			
		}		
	}
	
	
	void reroll(){
		for (int i = 1; i <= numberOfThrows; i++){
			collector.getChildren().set(i, getDiceRow(DICE_COUNT));
		}
	}
	
	
	//builds VBox with specified dice number and coloring
	HBox getDiceRow (int numberOfDices){
		HBox newHBox = new HBox();
		int[] diceRoll = getDiceRoll(numberOfDices);
		int[] doublets = getDoublets(diceRoll);
		for(int i = 0; i < diceRoll.length; i++){
			
			Group dice = makeDiceFace(diceRoll[i], getColor(diceRoll[i]), getFill(diceRoll[i], doublets));
			newHBox.getChildren().add(dice);
			newHBox.setMargin(dice, new Insets(5.0));
		}
		return newHBox;
	}
	
	//determines fill of the dice face
	Color getFill(int number, int[] possibleDoublets){
		for(int i = 0; i < possibleDoublets.length; i++){
			if (possibleDoublets[i] == number)
				return Color.RED.brighter().brighter();
		}
		return Color.TRANSPARENT;
	}
	
	//returns possible doublets in an array, used to determine fill
	int[] getDoublets (int[] diceRoll){
		int[] projection = new int[6];
		for(int i = 0; i < diceRoll.length; i++){
			projection[diceRoll[i]-1]++;
		}
		int[] doublets = new int[diceRoll.length/3];
		
		for (int i = 0; i < projection.length; i++){
			int doubCount = 0;
			if (projection[i] > 2){
				doublets[doubCount++] = i+1;
			}
		}
		return doublets;	
	}
	
	//determines dot color 
	Color getColor (int number){
		Color col;
		switch(number){
			case 1: col = Color.PURPLE; break;
			case 5: col = Color.BLUE; break;
			default: col = Color.BLACK; break;
		}
		return col;
	}
	
	//gets the pseudorandom dice roll depending on the desired dice count
	int[] getDiceRoll (int diceCount){
		int[] roll = new int[diceCount];
		for(int i = 0; i < diceCount; i++){
			roll[i] = theDice.nextOutcome();
		}
		return roll;
	}

	//method to create the dice face corresponding to its value
	//passes a matrix representation of the dot pattern to getGridface(int[][])
	Group makeDiceFace(int value, Color dotColor, Color fill){
		GridPane face;

		int[][] one = {
		 	{0,0,0},
		 	{0,1,0},
		 	{0,0,0},
		 	};
		int[][] two = {
		 	{1,0,0},
		 	{0,0,0},
		 	{0,0,1},
		 	};

   	
		int [][] three = addMatrix(one, two);
   	
		int[][] four = {
		 	{1,0,1},
		 	{0,0,0},
		 	{1,0,1},
		 	};
   	
		int[][] five = addMatrix(one, four);
   	
		int [][] six = {
		 	{1,0,1},
		 	{1,0,1},
		 	{1,0,1},
		 	};
   	
   	switch (value){
   		case 1: face = getGridFace(one, dotColor, fill); break;
   		case 2: face = getGridFace(two, dotColor, fill); break;
   		case 3: face = getGridFace(three, dotColor, fill); break;
   		case 4: face = getGridFace(four, dotColor, fill); break;
   		case 5: face = getGridFace(five, dotColor, fill); break;
   		case 6: face = getGridFace(six, dotColor, fill); break;
 			default: return null;
		}
		return new Group(face);	
   }

	//layouts the dice face as gridpane according to given matrix pattern
	GridPane getGridFace (int[][] grid, Color dotColor, Color fill){
		int padding = (int) diceSize/6;
   	GridPane face = new GridPane();
   		face.setHgap(padding);
   		face.setVgap(padding);
   		face.setBackground(new Background(new BackgroundFill(fill, new CornerRadii(0.0), new Insets(0.0) )));
   		face.setPadding(new Insets(padding, padding, padding, padding));
   		face.setStyle("-fx-border-color: black");
   		
   	double dotRadius = diceSize / 3;	
		for(int i = 0; i < grid.length; i++){
			for (int k = 0; k < grid.length; k++){
				if (grid[i][k] > 0)
					face.add(new Circle(dotRadius, dotColor), k, i);
				else face.add(new Circle(dotRadius, Color.TRANSPARENT), k, i);
			}
		}
		return face;	
	}
   
		
	//helper method
	int[][] addMatrix (int[][] mat1, int[][] mat2){
		int size = 3;
		int[][] newMat = new int[size][size];
		for (int i = 0; i < size; i++){
			for (int k = 0; k < size; k++){
				newMat[i][k] = mat1[i][k] + mat2[i][k];
			}
		}
		return newMat;
	}



	public static void main (String[] args){
		launch(args);
	}
}
