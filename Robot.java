class Robot{
	
	int xPos, yPos;
	int saveX, saveY;
	boolean robotInSight;
	
	Robot (int initXPos, int initYPos ){
		xPos = initXPos;
		saveX = initXPos;
		yPos = initYPos;
		saveY = initYPos;
		Grid.placeRobot(initXPos, initYPos);
	}
	
	void goUp(){ //x - 1
		this.moveRobot(-1, 0);
	}
	
	void goDown(){ //x + 1
		this.moveRobot(1,0);
	}
	
	void goLeft(){ //y -1 
		this.moveRobot(0, -1);
	}
	
	void goRight(){ //y + 1
		this.moveRobot(0,1);
	}
	
	boolean canSeeRobot(){
		for (int i = 0, j = 0; Grid.insideBounds(i,j); i++, j++){
			if (Grid.robotAt(xPos, i) && i != yPos) return true;
			if (Grid.robotAt(j, yPos) && j != xPos ) return true;
		}
		return false;
	}
	
	void reset (){
		Grid.removeRobot(xPos, yPos);
		Grid.placeRobot(saveX, saveY);
	}
	
	private void moveRobot(int x, int y){ 
		if(Grid.insideBounds((xPos+x), (yPos+y) )){
			if(!(Grid.robotAt((xPos+x),(yPos+y)))){
				Grid.removeRobot(xPos, yPos);
				Out.print("Entferne bei "+xPos+", "+yPos);
				xPos += x; 
				yPos += y;
				Out.println("Setze bei "+xPos+", "+yPos);
				Grid.placeRobot(xPos, yPos);					
			}
		}
	}
	
}
