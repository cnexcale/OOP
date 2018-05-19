import java.util.ArrayList;
import java.util.List;
public class postfixCalc {

	public static void main (String[] args){
		boolean done = false;
		
		while (!done){
			Out.println("Bitte neuen Ausdruck in postfix Notation eingeben:");
			String newInput = In.readLine();
			
			if (newInput.equalsIgnoreCase("exit")) {
				done = true;
				Out.println("Das Programm wird beendet.");
			}
			else {
				String result = getResult(newInput);
				if (result.equals("error")) Out.println("Ungültigerausdruck!");
			}
		}
	}
	
	private static String getResult (String newInput){
		String[] split = splitInput(newInput);
		if (!checkExpression(split)) return "error";
		
		Stack stack = new Stack (split.length);
		
			for (int i = 0; i < split.length; i++){
				if (split[i].equals("=")){
					int temp = stack.pop();
					Out.println(temp);
				} 
				else {
					if (split[i].matches("[0-9]+")){
						stack.push(Integer.parseInt(split[i]));
					}
					else{
						if (split[i].charAt(0) == ':') {
							int temp = stack.pop();
							stack.push(temp);
							stack.push(temp);
						}
						else{
							int temp = calculate(stack.pop(), stack.pop(), split[i].charAt(0));
							stack.push(temp);
						}
					}
				}
			}	
	return "fine";
	}
	
	private static int calculate (int number1, int number2, char operator){
		int result = 0;
		switch (operator){
			case '+': result = number1 + number2; break;
			case '-': result = number1 - number2; break;
			case '*': result = number1 * number2; break;
			case '/': result = (int) (number1 / number2); break;
		}
		return result;
	}
	
	/*
	private static String[] splitInput(String newInput){
	List<String> split = new ArrayList<String>();
	String workingInput = newInput.trim();
	int lastIndex = 0;
	for (int i = 0; i < workingInput.length(); i++){
		if (i < workingInput.length()-1){
			if (workingInput.charAt(i)==' '){ 
					String temp = workingInput.substring(lastIndex, i ).replace(" ", "");
				if (temp.length()>0){
					if(checkInputEntry(temp)){
						split.add(temp);
						lastIndex = i;
					} else {
						split.add(null); 
						lastIndex = i;}
				}
				else continue;
			}
		} else {
			split.add(workingInput.substring(lastIndex, i+1).trim());
		}
	}
	return split.toArray(new String[split.size()]);
	}

	private static boolean checkInputEntry (String input){
		if (input.matches("[0-9]+")) return true;
		for (int i = 0; i < input.length(); i++){
			if (input.charAt(i)=='+'||input.charAt(i)=='-'||
					input.charAt(i)=='*'||input.charAt(i)=='/'||
						input.charAt(i)==':'||input.charAt(i)=='=') return true;
		}
		return false;
	}

	*/
	
	private static String[] splitInput (String newInput){
		List<String> split = new ArrayList<String>();
		String workingInput = newInput.trim();
		int i = 0;
		while(i < workingInput.length()){
			if (Character.isDigit(workingInput.charAt(i))){
				int k = i;
				while (Character.isDigit(workingInput.charAt(k))) k++;			
				split.add(workingInput.substring(i,k));
				i = k;
			} else if (workingInput.charAt(i)=='+'||workingInput.charAt(i)=='-'||
									workingInput.charAt(i)=='*'||workingInput.charAt(i)=='/'||
										workingInput.charAt(i)==':'||workingInput.charAt(i)=='='){
							split.add(workingInput.substring(i,i+1));
							i++;
			} else if (workingInput.charAt(i)==' '){
				i++;
			}else {split.add(null); i++;}
		}
		return split.toArray(new String[split.size()]);
	}
	
	

	
	private static boolean checkExpression (String[] inputExpression){
		for (int i = 0; i < inputExpression.length; i++){
			if (inputExpression[i]==null) return false;
		}
		return true;
	}
}
