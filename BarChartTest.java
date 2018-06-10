public class BarChartTest{
static int labelLength = 20;
	public static void main (String[]args){
	
		DataFileReader dfr = new DataFileReader("cities.txt");
		dfr.readFile();
		BarChart.printChart(dfr);
		
		dfr = new DataFileReader("continents.txt");
		dfr.readFile();
		BarChart.printChart(dfr);
		
		dfr = new DataFileReader("months.txt");
		dfr.readFile();
		BarChart.printChart(dfr);
	}
}
