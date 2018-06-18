public class LandscapeTest{

	public static void main (String[] args){
	
		ErosionFramework lt = new MinimumErosion(8);
		lt.generate(0.5f);
		lt.setLandColorDesign(new EarthLandscapeColors());
		lt.setTreePlantingStrategy(new LandscapeWithForest());
		//lt.setTreePlantingStrategy (new LandscapeWithManyTrees());
		lt.display("test");
	}
}
