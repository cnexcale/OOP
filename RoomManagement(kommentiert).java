class Room {
	//"Vorlage" für die einzelnen Raum-Objekte mit name, Sitzplatzzahl und PCs als Feldvariablen und eine 
	//boolean Variable für die Belegung
	String name;
	int seats, compSeats;
	boolean occupied = false;
	//Konstruktur, dem beim Erstellen der Instanz der Room-Class (aka der Objekterzeugung) in der Mainmethode 
	//die entsprechenden Parameter übergeben werden und damit als [Objekt].[Variable] referenziert und bearbeitet
	//werden können
	Room (String newName, int newSeats, int newCompSeats, boolean occupation){
		name = newName;
		seats = newSeats;
		compSeats = newCompSeats;
		occupied = occupation;
	}
}


class Building {

	//Hab die .txt Datei als Gebäude aufgefasst, das die einzelnen Room-Objekte als Array in sich vereint
	//Also Gebäude -> Räume -> Eigenschaften der Räume
	public static Room[] getNewBuilding (String file){
		//Hier les ich die Daten aus der Beispieldatei ein
		//Dafür erstell ich ein neues Array so lang wie die Anzahl der Einträge und erstelle in der for-Schleife
		//für jedes i ein neues Room-Objekt und übergebe dem Room-Constructor die entsprechenden Parameter
		In.open(file);
		Room[] newBuilding = new Room[In.readInt()];
		for (int i = 0; i<newBuilding.length; i++){
			//-> Raumerzeugung, das mit den In.read...() Methoden geht in dem Fall nur, weil die Daten in der Datei
			//in einer Zeile angeordnet sind und man die hintereinander weg einlesen kann
			newBuilding[i] = new Room(In.readString(), In.readInt(), In.readInt(), false);
		}	
		In.close();
	return newBuilding;
	}		
}

public class RoomManagement{

	public static void main (String[]args){
		//Hier wird geguckt, ob beim Aufruf per Console ein Dateiname angegeben wurde, wenn nicht (= args.length==0)
		//wird das Programm beendet
		if (args.length==0){
			Out.println("Bitte Dateinamen beim Aufruf angeben!");
			System.exit(0);
		}
		//Nutzung des beim Aufruf im Terminal übergebenen Namens für die building Class, die einen Dateinamen
		//als String erwartet
		String file = args[0];
		Room[] building = Building.getNewBuilding(file);
		
		//Utility = keine Lehrmöglichkeit, Platzanzahl = 0, korrigiert Anzahl theoretisch freier Räume
		//Man hätte auch die Grenzen bei den jewiligen Vergleichsoperationen anpassen können, jedoch lässt sich
		//über UtilityRooms auch ein anderes Gebäude mit mehr Nicht-Lehrräumen einlesen
		int numberOfUtilityRooms = numberOfUtilityRooms(building);
		
		while (true){
		//Hier startet der Hauptprogrammablauf. While(true) als Bedingung ist zwar gefährlich, da penibel darauf 
		//geachtet werden muss, dass man ne Möglichkeit hat aus der Schleife auszusteigen (Endlosschleifen führen zum
		//crash!)
		//So kann man aber auch genau steuern, wie oft bzw wann der Input nicht mehr abgefragt werden soll
		
			//Funktion zum Userinput hab ich in eigene Methode ausgelagert (kann man so ggf wiederverwenden und is 				//einfacher zu debuggen) 
			
			int neededSeats = getSeatsFromUser();
			//Geforderte Bedingung, dass bei der Eingabe von "0" das Programm beendet wird
			if (neededSeats == 0) {Out.println("Programm wird beendet."); System.exit(0);}
			
			int neededPCs = getCompSeatsFromUser();
			
			//Dis is where the magic happens, die ominöse Raumoptimierungsmethode
			Room suitableRoom = getSuitableRoom(neededSeats, neededPCs, building);
			
			//Alternatives Raumangebot, das den passendsten Raum zurückgibt ohne die vorherige Belegung zu
			//berücksichtigen, den Nutzer jedoch auf bestehende Belegung aufmerksam macht
			//Is in der Aufgabenstellung nicht vorgesehen, kann also eigentlich ignoriert werden
			Room alternativeRoom = getRoomIgnoreOccupation(neededSeats, neededPCs, building);
			
			//Hier wird jetzt der Return Wert von getSuitableRoom() überprüft, da null aus verschiedenen Gründen
			//zurückgegeben werden kann (s.u.)
			if (suitableRoom!=null){
				Out.println("> Der Optimale Raum für Ihre Anforderungen wäre: "+suitableRoom.name);
				Out.println("> Mit "+suitableRoom.seats+" Plätzen insgesamt und "+suitableRoom.compSeats+
				" PC-Arbeitsplätzen.");
				
				//Hier teste ich ob der Alternative Raum der selbe wäre wie der davor bestimmte
				if (!suitableRoom.name.equals(alternativeRoom.name)){
					Out.println("* Der Raum "+alternativeRoom.name+" wäre auch geeignet, ist jedoch leider bereits belegt."
					+" Setzen Sie sich bitte mit der verantwortlichen Person in Kontakt.");
				}
			} else {
				Out.println("Es wurde leider kein passender Raum gefunden.");
			}
			//Abbruchbedingungen der While Schleife
			if (getFreeRooms(building)-numberOfUtilityRooms == 0){
				Out.println("Es sind alle Räume belegt. Das Programm wird nun beendet.");
				break;
			}
		}
	}
	
	//Die Methode, die den entsprechenden Raum finden soll
	//Hat den Datentyp Room als Rückgabewert, den ich in der Klasse ganz oben definiert habe
	private static Room getSuitableRoom(int neededSeats, int neededPCs, Room[] building){
		Room suitable = null;
		//Erster Ausschlusstest, wenn keine freien Räume mehr verfügbar sind
		if (getFreeRooms(building)-numberOfUtilityRooms(building)<1) return null;
		else {
				//vorselektion 
			for (int i = 0; i<building.length; i++){
				//Test auf verschiedene Kriterien, die einen Raum sofort ausschließen (besetzt, initial zu wenig Platz etc.)
				if ((building[i].seats < neededSeats)||(building[i].compSeats < neededPCs)||building[i].occupied == true)
				 continue;
				else {
					//Ersten potentiell passenden als Referenz benutzen, wird also nur im ersten Schleifendurchlauf, wenn
					//suitable noch die "null" Initialisierung vom Anfang besitzt, durchgeführt
					//gegen die Referenz bzw den temporär passenden Raum werden alle anderen Räume getestet, die 
					//den Kriterien entsprechen und wenn einer besser passt wird dieser dann als suitable referenziert
					if (suitable==null) suitable = building[i];
					else {
					//Überschüssige Sitzplätze vergleichen, hab die Methode dafür auch wieder ausgelagert um es 
					//übersichtlicher zu machen/einfacher zu handlen 
					
					int excessSuitable = getExcessSeats(suitable, neededSeats);
					int excessTest = getExcessSeats(building[i], neededSeats);  
						//Ausschlusss hier: der bisher passende hat weniger Überschussplätze, mit "continue" kann ich dann direkt in
						//die nächste Schleifeniteration der for-Schleife "ausbrechen"
						if (excessSuitable < excessTest) 
							continue;
						else{
						//Bei gleichem Überschuss nach überschüssigen Computerarbeitsplätzen auswählen
						if (excessSuitable==excessTest)
							//noch ne ausgelagerte Methode, die die überschüssigen PC Plätze vergleicht und den Room mit der kleinere
							//Differenz zurückgibt, dieser ist dann der temporär passende
							suitable = compareCompSeats(suitable, building[i], neededPCs);
						else
							suitable = building[i];
						}
					}
				}
			}	
		}
		//wenn in der for-Schleife suitable irgendein Raum zugewiesen wird, ist dieser nach den Maßstäbe am Ende der 
		//for-Schleife der Optimale und wird als belegt markiert
		if (suitable!=null) suitable.occupied = true;
		return suitable;
	}
	
	//Das hier ist die Methode für den alternativen Raum. Hier ist es extrem wichtig, ein neues Room[] Array zu erstellen
	//da man sonst die Referenzen zum Originalen, aus der Datei eingelesenen, building überschreiben würde!
	private static Room getRoomIgnoreOccupation (int neededSeats, int neededPCs, Room[] building ){
		//neues Room[] Array mit selber Länge
		Room[] theoreticalBuilding = new Room[building.length];
		for (int i = 0; i < building.length; i++){
			//Diese schleife kopiert einfach die Eigenschaften des originals, setzt aber alle occupied-Variablen
			//auf false (=> Raum ist nicht belegt)
			theoreticalBuilding[i] = new Room(building[i].name, building[i].seats, building[i].compSeats, false);
		}
		//Rückgabewert ist der passende Raum mit dem theoretischen Gebäude, in dem alle Räume wieder frei sind
		//Is wie gesagt nicht gefordert, fand ich aber ein witziges Feature. Außerdem isses ineffizient, 
		// weil ich die für die Alternative die selbe Arbeit zweimal mache. Müsste man eigentlich schöner in die 
		//Ursprungsfunktion integrieren, aber ich war erstmal "zufrieden" mit dieser Lösung
		return getSuitableRoom (neededSeats, neededPCs,theoreticalBuilding);
	}
	
	//Hilfsmethode für die überschüssigen Sitze, nothing too fancy
	private static int getExcessSeats (Room testRoom, int neededSeats){
		return testRoom.seats-neededSeats;
	}
	
	//Hilfsmethode für den Vergleich der Computerarbeitsplätze
	private static Room compareCompSeats(Room suitable, Room testRoom, int neededPCs){
		Room choice;
		if ((suitable.compSeats-neededPCs) <= (testRoom.compSeats-neededPCs)) choice = suitable;
		else choice = testRoom;
		return choice;
	}
	//In unserem Beispiel war eine Abstellkammer dabei, die für die Raumplanung völlig irrelevant war, alleine schon
	//weil "0" benötigte Sitze das Programm beenden sollte, daher hab ich ne Methode eingebaut, die bestimmt wieviele
	//Räume des erstellten Gebäudes keine Sitzplätze bieten und die als Utility Räume gezählt
	//Diese Anzahl kann wichtig sein, wenn man die Anzahl der bereits belegten Räume benötigt, da diese in unserem
	//Beispiel immer bei n-1 bleiben würde, da der Abstellraum nie belegt wird. Für die Skalierbarkeit hab ich also immer
	//die Anzahl der Utilityräume von der Gesamtraumzahl abgezogen und diese Differenz, wo gebraucht, als if-Bedingung
	//genutzt
	private static int numberOfUtilityRooms (Room[] building){
		int count = 0;
		for (int i = 0; i < building.length; i++){
			if(building[i].seats == 0) count++;
		}
		return count;
	}
	
	//Hier werden die insgesamt noch freien Räume bestimmt. Man hätte die Utilityräume auch hier abziehen können, aber
	//dann würde die Methode mehr machen, als sie vorgibt, daher hab ich mich dazu entschieden die Anzahl der Utility Räume
	//immer separat im Programmablauf abzuziehen
	private static int getFreeRooms (Room[] building){
		int freeRooms = 0;
		for (int i = 0; i<building.length; i++){	
			if (building[i].occupied==false) freeRooms++;
		}
		return freeRooms;
	}
	
	//Hier kommen noch die Input Methoden, die die Nutzereingabe abfragen
	private static int getSeatsFromUser(){
		Out.println();
		Out.print("Bitte Sizplatzbedarf insgesamt eingeben (nur ganze Zahlen):");
		int seats = checkIntInput("Sitzplätze");
		return seats;
	}
	private static int getCompSeatsFromUser(){
		Out.print("Bitte PC-Bedarf eingeben (nur ganze Zahlen):");
		int pcSeats = checkIntInput("PC-Arbeitsplätze");
		return pcSeats;
	}
	
	//Den Check hab ich auf Hinweis eines Kommilitonen noch eingebaut, da sich das Programm sonst direkt beendet hat, 
	//wenn man eine nicht-Ziffer eingegeben hat.
	private static int checkIntInput(String what){
		boolean check=false;
		String input;
		do{
		//Daher les ich hier erstmal einen String ein (da man die besser überprüfen kann mit isDigit), die Überprüfung
		//kann man auch sehr gut mit regulären Ausdrücken machen! Muss ich mich nur mal mit beschäftigen...
		input = In.readLine();
		for (int i = 0; i < input.length(); i++){
			//sobald eine nicht-Ziffer entdeckt wird, wird die for-Schleife beendet (break) und die do-while Schleife geht
			//in einen neuen Durchlauf und liest einen neuen String ein
			if (!Character.isDigit(input.charAt(i))){
				Out.println("Ungültige Eingabe, bitte nur ganze Zahlen.");
				Out.print("Neue Eingabe der benötigten " + what+ ": ");
				break;
			}
			//boolean Bedingung der do-while Schleife auf true setzen, wenn man am letzten Index des Input angelangt is
			else if (i==input.length()-1) check = true;
		}
		}while (!check);
		//da die Methode einen integer zurückgeben soll, muss hier noch String -> Int geparsed werde
		return Integer.parseInt(input);
	}
	
	
}

/*
int neededSeats = 101;
int neededPCs = 0;

int neededSeats = 10;
int neededPCs = 26;


int neededSeats = 20;
int neededPCs = 9;

int neededSeats = 20; int neededSeats = 20;
int neededPCs = 10; int neededPCs2 = 10;

*/
