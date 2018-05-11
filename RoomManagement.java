class Room {
	
	String name;
	int seats, compSeats;
	boolean occupied = false;;
	
	Room (String newName, int newSeats, int newCompSeats, boolean occupation){
		name = newName;
		seats = newSeats;
		compSeats = newCompSeats;
		occupied = occupation;
	}
}


class Building {

	public static Room[] getNewBuilding (String file){
		In.open(file);
		Room[] newBuilding = new Room[In.readInt()];
		for (int i = 0; i<newBuilding.length; i++){
			newBuilding[i] = new Room(In.readString(), In.readInt(), In.readInt(), false);
		}	
		In.close();
	return newBuilding;
	}		
}

public class RoomManagement{

	public static void main (String[]args){
		
		if (args.length==0){
			Out.println("Bitte Dateinamen beim Aufruf angeben!");
			System.exit(0);
		}
		String file = args[0];
		Room[] building = Building.getNewBuilding(file);
		
		//Utility = keine Lehrmöglichkeit, Platzanzahl = 0, korrigiert Anzahl theoretisch freier Räume
		//Man hätte auch die Grenzen bei den jewiligen Vergleichsoperationen anpassen können, jedoch lässt sich
		//über UtilityRooms auch ein anderes Gebäude mit mehr Nicht-Lehrräumen einlesen
		int numberOfUtilityRooms = numberOfUtilityRooms(building);
		
		while (true){
			int neededSeats = getSeatsFromUser();
			
			if (neededSeats == 0) {Out.println("Programm wird beendet."); System.exit(0);}
			
			int neededPCs = getCompSeatsFromUser();
			
			Room suitableRoom = getSuitableRoom(neededSeats, neededPCs, building);
			
			//Alternatives Raumangebot, das den passendsten Raum zurückgibt ohne die vorherige Belegung zu
			//berücksichtigen, den Nutzer jedoch auf bestehende Belegung aufmerksam macht
			Room alternativeRoom = getRoomIgnoreOccupation(neededSeats, neededPCs, building);
			
			if (suitableRoom!=null){
				Out.println("> Der Optimale Raum für Ihre Anforderungen wäre: "+suitableRoom.name);
				Out.println("> Mit "+suitableRoom.seats+" Plätzen insgesamt und "+suitableRoom.compSeats+
				" PC-Arbeitsplätzen.");
				if (!suitableRoom.name.equals(alternativeRoom.name)){
					Out.println("* Der Raum "+alternativeRoom.name+" wäre auch geeignet, ist jedoch leider bereits belegt.\n 						* Setzen Sie sich bitte mit der verantwortlichen Person in Kontakt.");
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
	
	
	private static Room getSuitableRoom(int neededSeats, int neededPCs, Room[] building){
		Room suitable = null;
		if (getFreeRooms(building)-numberOfUtilityRooms(building)<1) return null;
		else {
				//vorselektion 
			for (int i = 0; i<building.length; i++){
				if ((building[i].seats < neededSeats)||(building[i].compSeats < neededPCs)||building[i].occupied == true)
				 continue;
				else {
					//Ersten potentiell passenden als Referenz benutzen
					if (suitable==null) suitable = building[i];
					else {
					//Überschüssige Sitzplätze vergleichen
					int excessSuitable = getExcessSeats(suitable, neededSeats);
					int excessTest = getExcessSeats(building[i], neededSeats);  
						if (excessSuitable < excessTest)
							continue;
						else{
						//Bei gleichem Überschuss nach überschüssigen Computerarbeitsplätzen auswählen
						if (excessSuitable==excessTest)
							suitable = compareCompSeats(suitable, building[i], neededPCs);
						else
							suitable = building[i];
						}
					}
				}
			}	
		}
		if (suitable!=null) suitable.occupied = true;
		return suitable;
	}
	
	private static Room getRoomIgnoreOccupation (int neededSeats, int neededPCs, Room[] building ){
		Room[] theoreticalBuilding = new Room[building.length];
		for (int i = 0; i < building.length; i++){
			theoreticalBuilding[i] = new Room(building[i].name, building[i].seats, building[i].compSeats, false);
		}
		return getSuitableRoom (neededSeats, neededPCs,theoreticalBuilding);
	}
	
	
	private static int getExcessSeats (Room testRoom, int neededSeats){
		return testRoom.seats-neededSeats;
	}
	
	
	private static Room compareCompSeats(Room suitable, Room testRoom, int neededPCs){
		Room choice;
		if ((suitable.compSeats-neededPCs) <= (testRoom.compSeats-neededPCs)) choice = suitable;
		else choice = testRoom;
		return choice;
	}

	private static int numberOfUtilityRooms (Room[] building){
		int count = 0;
		for (int i = 0; i < building.length; i++){
			if(building[i].seats == 0) count++;
		}
		return count;
	}
	
	private static int getFreeRooms (Room[] building){
		int freeRooms = 0;
		for (int i = 0; i<building.length; i++){	
			if (building[i].occupied==false) freeRooms++;
		}
		return freeRooms;
	}
	
	private static int getSeatsFromUser(){
		Out.println();
		Out.print("Bitte Sizplatzbedarf insgesamt eingeben (nur ganze Zahlen):");
		return In.readInt();
	}
	private static int getCompSeatsFromUser(){
		Out.print("Bitte PC-Bedarf eingeben (nur ganze Zahlen):");
		return In.readInt();
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
