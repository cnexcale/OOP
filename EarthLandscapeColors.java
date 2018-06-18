class EarthLandscapeColors implements LandColorizer{

	public RGBColor computeColor (float height, float slope, TerrainInformation info){
		float waterZone, grassZone, rockZone;
		float maxHeight = info.getPeakHeight();
		//System.out.println("maxHeight: "+maxHeight);
		//float maxHeight = 300;
		int red, green, blue;
		
		int brightness = info.brightnessForSlope(slope);
		//System.out.println("brightness: "+brightness);
		waterZone = info.getWaterHeight()+maxHeight*0.02f;
		grassZone = maxHeight*(0.6f); //System.out.println("grassZone: "+maxHeight*0.3);
		rockZone = maxHeight*(0.8f); //System.out.println("rockZone: "+rockZone);
		
		if (height < waterZone){	
			red = 250;
			green = 247;
			blue = 82;
		}else{ 
			if (height < grassZone){
				//System.out.println(Math.abs(slope));
				if (Math.abs(slope)<2){
					red = 29;
					green = 130;
					blue = 9;				
				}else{
					//System.out.println("Too steep for grass");
					red = 109;
					green = 117;
					blue = 107;
				}
			}else{
				if (height < rockZone){
					red = 109;
					green = 117;
					blue = 107;
				}else{
					if (Math.abs(slope)>2){
						red = 109;
						green = 117;
						blue = 107;
					}else{
						red = 250;
						green = 240;
						blue = 255;
						
					}
				}
			}
		}
		//System.out.println();
		return new RGBColor(red*brightness/255,  green*brightness/255, blue*brightness/255);
	}
}
