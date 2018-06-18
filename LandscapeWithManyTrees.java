class LandscapeWithManyTrees implements TreeDecider{
	
   int NO_TREE = 0;
   int CONE_TREE = 1;
   int OVAL_TREE = 2;
   int CONE_BUSH = 3;
   int OVAL_BUSH = 4;
	
  public int decideAboutTreeAt (int x, int y, float height, float slope,
                          TerrainInformation info){
		float waterZone, grassZone;
		float maxHeight = info.getPeakHeight(); 
 	 	
		waterZone = info.getWaterHeight()+maxHeight*0.02f;
		grassZone = maxHeight*(0.6f); //System.out.println("grassZone: "+maxHeight*0.3);
		
		if (height > waterZone && height < grassZone && Math.abs(slope) < 2){
			if (Math.random()*10 > 4.5) return CONE_TREE;
			else return CONE_BUSH;
		}else return NO_TREE;         
  }
}
