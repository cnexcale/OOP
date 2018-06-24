class WavyWater implements WaterRenderer {

		int xCorrection = 0;
		float waveHeight = 5f;
		float waveHeightCorrection = 0.0f;
		float waveFrequency = 0.05f;
		float waveFrequencyCorrection = 0.0f;
		int absoluteGreenPortion = 30;
		
    WavyWater (ErosionFramework terrain) {
        terrain.setWaterAppearance(this);
        terrain.registerKeyCommand('D', "A useless parameter for demonstration purposes.", new UselessDemoParameter());
        terrain.registerKeyCommand('X', "Generate windy, wavy water", new WindyWavyWater());
        terrain.registerKeyCommand('Q', "Modify wave height logarithmically", new WaveHeightChanger());
        terrain.registerKeyCommand('R', "Modify wave frequency", new WaveFrequencyChanger());
        terrain.registerKeyCommand('G', "Modify green portion of the water color", new WaterColorChanger());
    }

    public float getRippleDelta (int x, int y, TerrainInformation info) {
        return (waveHeight+ waveHeightCorrection) * info.randomSimplexNoise(x+xCorrection, y, waveFrequency+waveFrequencyCorrection);
    }

    public RGBColor getWaterColor (float slope, TerrainInformation info) {
        int lightBrightness = info.brightnessForSlope(slope);
        return new RGBColor(10 + lightBrightness / 5, absoluteGreenPortion + 3 * lightBrightness / 5, 40 + 4 * lightBrightness / 5);
    }

    public static void main (String[] args) {
        ErosionFramework demoLand = new MinimumErosion(8);
        // demoLand.setLandColorDesign(new EarthLandscapeColors());
        // demoLand.setTreePlantingStrategy(new LandscapeWithForest());
        WavyWater ww = new WavyWater(demoLand);
        demoLand.generate(0.5f);
        demoLand.display("Demo Landscape With Wavy Water");
    }
    
    class WindyWavyWater implements ParameterChanger{
    
    	
    	public void changeValue (int changeBy){
    		xCorrection += changeBy;
    	}
    }
    
    class WaveHeightChanger implements ParameterChanger{
    	
    	public void changeValue(int changeBy){
    	if (changeBy == 0)
    		waveHeightCorrection = changeBy;
  		else
				waveHeightCorrection += (waveHeight + waveHeightCorrection)*0.2f*changeBy;
				//Out.println(waveHeightCorrection);
    	}
    }
    
    class WaveFrequencyChanger implements ParameterChanger{
    	public void changeValue(int changeBy){
    		if(changeBy == 0)
    			waveFrequencyCorrection = changeBy;
  			else
    			waveFrequencyCorrection += (waveFrequency+waveFrequencyCorrection)* 0.2f*changeBy;
    	}
    }
    
    class WaterColorChanger implements ParameterChanger{
    	public void changeValue(int changeBy){
    		absoluteGreenPortion += changeBy*5;
    	}
    }

}

class UselessDemoParameter implements ParameterChanger {

    public void changeValue (int changeBy) {
        Out.println("A change of the Demo Parameter has been requested: changeBy = " + changeBy);
    }
}
