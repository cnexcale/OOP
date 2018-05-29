public class RobotTwins{
	public static void main (String[]args){
		//Gerade Feldgrößen verhindern, dass sich die Robots sehen können
		boolean allowedSize = false;
		int fieldSize;
		do{
			Out.print("Bitte gewünschte Quadratgröße eingeben (eine ganze Zahl): ");
			fieldSize = In.readInt();
			if (fieldSize > 3 && fieldSize < 13) allowedSize = true;
			else{
				Out.println("Zulässige Feldgrößen: mind. 4x4, max. 16x16.");
			}
		}while(!allowedSize);
		
		boolean newGame = false;
		do{
			Grid.create(fieldSize, fieldSize);
			Robot robotOne = new Robot(0,0);
			Robot robotTwo = new Robot(fieldSize-1, fieldSize-1);
			
			while(!robotOne.canSeeRobot()){
				char moveInput = Grid.readKey();
				Out.println(moveInput);
				switch (moveInput){
					case 'w': robotOne.goUp();
										robotTwo.goDown();
										break;
					case 's': robotOne.goDown();
										robotTwo.goUp();
										break;
					case 'a': robotOne.goLeft();
										robotTwo.goRight();
										break;
					case 'd': robotOne.goRight();
										robotTwo.goLeft();
										break;		
				}
			}
			newGame = Grid.askQuestion("Neues Spiel beginnen?");
		}while(newGame);
	
		
	}
}
