class LandscapeWithForest extends LandscapeWithManyTrees{
	
	public int decideAboutTreeAt (int x, int y, float height, float slope,
    TerrainInformation info){
    
  	if (isInForestArea(x, y, info)){
				
  		if (isBelowTimberline(height, slope, info)){
  			if (isForestEdge(x, y, info)){
  				if (treePickerAid()) return CONE_BUSH;
  				else return OVAL_BUSH;
  			}else{
  				return CONE_TREE;
  			}
  		} 
  	}		
  return NO_TREE;
  }
  

  
  private boolean isInForestArea (int x, int y, TerrainInformation info){
		return (x < info.getGridSize()/2 && y < info.getGridSize()/2);
  }
  
  private boolean isForestEdge (int x, int y, TerrainInformation info){
  	int borderWidth = (int) (info.getGridSize()/23);
  	return (y < borderWidth || y > info.getGridSize()/2-borderWidth) || 
  	(x < borderWidth || x > info.getGridSize()/2-borderWidth);
  }
}
