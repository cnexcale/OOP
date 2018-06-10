class BarChart {
	
	final static int LABEL_LENGTH = 20;
	final static int MAX_BAR_LENGTH = 59;
	private static double maxValue;
	private static int scaledMedian;
	
	
	static void printChart(LabeledDataSeries newData){
		maxValue = Statistics.max(newData);
		scaledMedian = scaleValue(Statistics.median(newData));
		//Out.println("Scaled Median: "+scaledMedian);
		Out.println("Neues Balkendiagramm zur Datenreihe: "+newData.getTitle());
		Out.println("| bzw. : markieren den Median");
		Out.println();
		for (int i = 0; i < newData.getItemCount(); i++){
			Out.println(normalizeLabel(newData.getLabel(i))+" "+normalizeValue(newData.getItem(i)));
		}
		Out.println(); Out.println();
	}	
	
	private static String normalizeLabel(String label){
		String normalizedLabel = label;
		if (label.length()<LABEL_LENGTH){
			while(normalizedLabel.length()<LABEL_LENGTH)	normalizedLabel += " "; 
		}else{
			normalizedLabel = label.substring(0,19)+".";
		}
		return normalizedLabel;
	}
	
	private static int scaleValue(double value){
		return (int) ((value / maxValue)*MAX_BAR_LENGTH);
	}
	
	/*
	Baut den Balken als String auf, inkl. Skalierung, entsprechend den Vorgaben
	Angelehnt an das Beispiel in der Aufgabe wurden die Medianzeichen als vollwertiges Symbol des
	Balkens mitgezählt. Dadurch ergeben sich jedoch augenscheinlich gleichlange Balken, die direkt am
	Median mit | oder : enden, je nach dem ob deren Wert gleich dem skalierten Median ist oder um 1 kleiner
	*/
	private static String normalizeValue(double value){
		String bar = "";
		int barLength = scaleValue(value);
		//Out.println("Barlength: "+barLength);
		if (barLength >= scaledMedian){
			for (int i = 0; i < barLength; i++){	
				if (i+1 == scaledMedian) {
					bar += "|";
					i++;
				}
				else {
					bar += "#";
				}
			}
		}else{
			for (int i = 0; i < scaledMedian-1; i++){
				if(i < barLength){ bar += "#";} 
				else {bar += " ";}
			} 
			bar += ":";	
		}
		return bar;
	}
}
