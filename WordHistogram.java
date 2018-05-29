import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/*
ANMERKUNG
Um die unterschiedlichen Worte zu zählen, wurde die addWord Methode dahingehend angepasst,
dass die Groß- und Kleinschreibung beim Hinzufügen ignoriert wurde. Dies kann natürlich
dazu führen, dass Worte die semantisch von der Groß- und Kleinschreibung abhängig sind 
falsch gezählt werden.
Ohne diese Anpassung würde sich die Anzahl unterschiedlicher Worte jedoch durch die 
Großschreibung an Satzanfängen verfälscht werden
*/

class WordEntry {
	String word;
	int frequency;
}

class WordHistogram {
	WordEntry[] entries;
	int entryCount;
	
	WordHistogram (int size) {
		entries = new WordEntry[size];
		entryCount = 0;
	}
	
	WordHistogram () {
		this(1000);
	}

	void addWord (String word, int n) {
		word = word.toLowerCase(); //Anpassung, abhängig von gewünschter Zählweise
		
		if (n == 0) return;  // never add zero frequency entries to histogram
		if (n < 0) {
			removeWord(word, -n);  // turn addWord("foo", -3) into removeWord("foo", 3)
			return;
		}
		int i = 0;
		while (i < entryCount && !word.equals(entries[i].word)) i++;
		if (i >= entryCount) {
			if (entryCount >= entries.length) Out.println("Zu viele Einträge.");
			else {
				WordEntry we = new WordEntry();
				we.word = word;
				we.frequency = n;
				entries[entryCount] = we;
				entryCount++;
			}
		}
		else
			entries[i].frequency += n;
	}

	void addWord (String word) {
		addWord(word, 1);
	}

	void removeWord (String word, int n) {
		word = word.toLowerCase(); //Anpassung, abhängig von gewünschter Zählweise
		if (n == 0) return;  // has no effect on contents of histogram
		if (n < 0) {
			addWord(word, -n);  // turn removeWord("foo", -3) into addWord("foo", 3)
			return;
		}
		int i = 0;
		while (i < entryCount && !word.equals(entries[i].word)) i++;
		if (i < entryCount) {
			entries[i].frequency -= n;
			if (entries[i].frequency <= 0) {
				entries[i] = entries[entryCount - 1];
				entryCount -= 1;
				entries[entryCount] = null;
			}
		}
	}

	void removeWord (String word) {
		removeWord(word, 1);
	}

	void printEntries () {
		Out.println("Anzahl Einträge: " + entryCount);
		for (int i = 0; i < entryCount; i++) {
			WordEntry we = entries[i];
			Out.println("#" + (i+1) + " " + we.word + ": " + we.frequency);
		}
	}

	void readWordList (String filename) {
		In.open(filename);
		if (!In.done()) {
			Out.println("Datei nicht gefunden.");
			return;
		}
		String line = In.readLine();
		while (In.done()) {
			addWord(line);
			line = In.readLine();
		}
		In.close();
	}

	// combination of readWordList with slide OPJ-4.7
	void readText (String filename) {
		In.open(filename);
		if (!In.done()) {
			Out.println("Datei nicht gefunden.");
			return;
		}
		String line = In.readLine();
		while (In.done()) {
			int i = 0, last = line.length() - 1;
			while (i <= last) {
				while (i <= last && !Character.isLetter(line.charAt(i))) i++;
				int beg = i;
				while (i <= last && Character.isLetter(line.charAt(i))) i++;
				if (i > beg)
					addWord(line.substring(beg, i));
			}
			line = In.readLine();
		}
		In.close();
	}
	
	boolean contains (String word){
		if (getWordFrequency(word)>0) return true;
		else return false;
	}
	
	int getWordFrequency (String word){
		for (int i = 0; i < entryCount; i++){
			if (entries[i].word.equalsIgnoreCase(word)) return entries[i].frequency; 
		}
		return 0;
	}
	
	long getTotalWordCount(){
		long wordCount = 0;
		for (int i = 0; i < entryCount; i++){
			wordCount += entries[i].frequency;
		}
		return wordCount;
	}
	
	int getDifferentWordCount(){
		return entryCount;
	}
	
	void addAll (WordHistogram toBeAdded){
		for (int i = 0; i < toBeAdded.entryCount; i++){
			addWord(toBeAdded.entries[i].word, toBeAdded.entries[i].frequency);
		}
	}
	
	void removeAll(WordHistogram toBeRemoved){
		for (int i = 0; i < toBeRemoved.entryCount; i++){
			removeWord(toBeRemoved.entries[i].word, toBeRemoved.entries[i].frequency);
		}
	}
	
	void intersectHistograms(WordHistogram otherWH){
		int i = 0;
		while(i < entryCount){
			int j = 0;
			while (j < otherWH.entryCount && !entries[i].word.equals(otherWH.entries[j].word)) j++;
				if (j >= otherWH.entryCount){
					removeWord(entries[i].word, entries[i].frequency);
					continue;
				}else{
					if (entries[i].frequency > otherWH.entries[j].frequency){
						entries[i].word = otherWH.entries[j].word;
						entries[i].frequency = otherWH.entries[j].frequency;
						//Man könnte hier noch den Eintrag aus otherWH löschen, um im Folgenden Suchschritte zu
						//sparen, allerdings würde das in dem Fall auch die Daten des Parameter-Objektes permanent
						//verändern
					}
					i++;
				}
		}	
	}
	
	void writeToFile (String fileName){
		try{
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (int i = 0; i < entryCount; i++){
				bw.write(entries[i].word+"-"+entries[i].frequency);
				bw.newLine();
			}
			bw.close();
			fw.close();
		}catch (IOException e){
			Out.println(e.getMessage());
		}
		
	}
}
