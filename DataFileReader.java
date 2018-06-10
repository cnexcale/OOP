class SimpleData {
	String label;
	double value;
	
	SimpleData (String label, double value){
		this.label = label;
		this.value = value;
	}
	
	String getLabel(){
		return label;
	}
	
	double getValue(){
		return value;
	}
}

class DataFileReader implements LabeledDataSeries{
	
	String fileName, title;
	int numberOfEntries = 0;
	SimpleData[] data = new SimpleData[100];
	
	DataFileReader (String fileName){
		this.fileName = fileName;
	}
	
	void readFile (){
		if (fileName == null){
			Out.println("Fehler! Kein Dateiname vorhanden.");
		}else{
			In.open(fileName);
			title = In.readString();
			int i = 0;
			data[i] = new SimpleData(In.readString(), In.readDouble());
			i++;
			while (In.done()){
				data[i] = new SimpleData(In.readString(), In.readDouble());
				numberOfEntries++;
				i++;
			}
			In.close();			
		}
		
	}
	
	public int getItemCount(){
		return numberOfEntries;
	}
	
	public String getTitle(){
		return title;
	}
	
	public double getItem (int index){
		return data[index].getValue();
	}
	
	public String getLabel(int index){
		return data[index].getLabel();
	}
}
