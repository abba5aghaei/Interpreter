import java.util.Vector;

public class Scanner {

	private String[] keyword = { "if", "else", "while", "for", "begin", "end", "then", "do", "print" };
	static Vector[] symbolTable = new Vector[4];
	Vector<String> error = new Vector<String>();
	Vector<String> tokens = new Vector<String>();
	String message = "";
	
	Scanner() {
		
		symbolTable[0] = new Vector<String>();
		symbolTable[1] = new Vector<String>();
		symbolTable[2] = new Vector<String>();
		symbolTable[3] = new Vector<String>();
	}
	
	public String scan(String input) {
		
		char ch;
		input += '$';
		int index = 0;
		boolean flag = true;
		error.clear();
		tokens.clear();
		symbolTable[0].clear();
		symbolTable[1].clear();
		symbolTable[2].clear();
		symbolTable[3].clear();
		symbolTable[0].addElement("Type");
		symbolTable[1].addElement("Value");
		symbolTable[2].addElement("Index");
		symbolTable[3].addElement("Line");
		int i = 0, state = 0, line = 0;
		String token = "", number = "", e = "", output = "";
		
		while(i<input.length()) {
			
			ch = input.charAt(i);
			
			switch(state) {
			
			case 0: {
				if(ch == ' ' | ch == '\t')
					state = 0;
				
				else if(ch == '\n' | ch == '\r') {
					
					i++;
					output += '\n';
					state = 0;
					line++;
				}
				else if(ch >= 'a' & ch <= 'z' || ch >= 'A' & ch <= 'Z') {
					
					state = 1;
					token += ch;
				}
				else if(isInt(ch)) {
					
					state = 2;
					number += ch;
				}
				else if(ch == '=') state = 3;
				
				else if(ch == '>') state = 4;
				
				else if(ch == '<') state = 5;
				
				else if(ch == '!') state = 6;
				
				else if(ch == '[') state = 7;
				
				else if(ch == ']') state = 8;
				
				else if(ch == '+') state = 9;
				
				else if(ch == '-') state = 10;
				
				else if(ch == '*') state = 11;
				
				else if(ch == '/') state = 12;
				
				else if(ch == '{') state = 13;
				
				else if(ch == '}') state = 14;
				
				else if(ch == '.') state = 15;
				
				else if(ch == '?') state = 16;
				
				else if(ch == '%') state = 17;
				
				else if(ch == '(') state = 18;
				
				else if(ch == ')') state = 19;
				
				else if(ch == ',') state = 20;
				
				else if(ch == ';') state = 21;
				
				else if(ch == ':') state = 22;
				
				else if(ch == '^') state = 23;
				
				else if(ch == '$') {
					
					message = "Scan is compelet...";
					 
					state = 0;
					}
				else {
					e = "";
					int j = i+1;
					while(ch != ' ' & ch != '\t' & ch != '\r' && j<input.length()) {
						e += ch;
						ch = input.charAt(j);
						j++;
						}
					error.addElement("can not define<"+e+">in line:"+line);
					state = 0;
					}
				i++;
				break;
				}
			case 1: {
				if(ch >= 'a' & ch <= 'z' || ch >= 'A' & ch <= 'Z' || ch =='_') {
					
					state = 1;
					token += ch;
					i++;
				}
				else if(isInt(ch)) {
					
					state = 1;
					token += ch;
					i++;
				}
				else {
					state = 0;
					if(isKeyword(token)) {
						
						output += token+" ";
						tokens.addElement(token);
						symbolTable[0].addElement("Keyword");
						symbolTable[1].addElement(token);
						symbolTable[2].addElement("");
						symbolTable[3].addElement(line);
					}
					else {
						
						if(isIdExist(token)) {
								
								output += "id"+symbolTable[2].elementAt(symbolTable[1].indexOf(token))+" ";
								tokens.addElement("id");
							}
							else {
								tokens.addElement("id");
								symbolTable[0].addElement("id");
								symbolTable[1].addElement(token);
								symbolTable[2].addElement(index);
								symbolTable[3].addElement(line);
								output += "id"+index+" ";
								index++;
							}
					}
					token = "";
				}
				break;
			}
			case 2: {
				if(isInt(ch)) {
					
					state = 2;
					number += ch;
					i++;
					}
				else if(ch == '.' && flag) {
					
					flag = false;
					state = 2;
					number += ch;
					i++;
					}
				else if(ch >= 'a' & ch <= 'z' || ch >= 'A' & ch <= 'Z') {
				
					e = "";
					int j = i+1;
					while(ch != ' ' & ch != '\t' & ch != '\r' && j<input.length()) {
						
						e += ch;
						ch = input.charAt(j);
						j++;
						}
					error.addElement("id can not start with number <"+e+"> in line:"+line);
					state = 0;
					}
				else {
					state = 0;
					output += "number ";
					tokens.addElement("number");
					symbolTable[0].addElement("number");
					symbolTable[1].addElement(number);
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					number = "";
					flag = true;
				}
			break;
			}
			case 3: {
				if(ch == '=') {
					
					state = 0;
					output += "== ";
					tokens.addElement("==");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("==");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else {
					state = 0;
					output += "= ";
					tokens.addElement("=");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 4: {
				if(ch == '=') {
					
					state = 0;
					output += ">= ";
					tokens.addElement(">=");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement(">=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else {
					state = 0;
					output += "> ";
					tokens.addElement(">");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement(">");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 5: {
				if(ch == '=') {
					
					state = 0;
					output += "<= ";
					tokens.addElement("<=");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("<=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else {
					state = 0;
					output += "< ";
					tokens.addElement("<");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("<");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 6: {
				if(ch == '=') {
					
					state = 0;
					output += "!= ";
					tokens.addElement("!=");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("!=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else {
					state = 0;
					output += "! ";
					tokens.addElement("!");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("!");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 7: {
				state = 0;
				output += "[ ";
				tokens.addElement("[");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement("[");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 8: {
				state = 0;
				output += "] ";
				tokens.addElement("]");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement("]");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 9: {
				if(ch == '+') {
					
					state = 0;
					output += "++ ";
					tokens.addElement("++");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("++");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else if(ch == '=') {
					
					state = 0;
					output += "+= ";
					tokens.addElement("+");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("+=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else {
					state = 0;
					output += "+ ";
					tokens.addElement("+");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("+");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 10: {
				if(ch == '-') {
					
					state = 0;
					output += "-- ";
					tokens.addElement("--");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("--");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else if(ch == '=') {
					
					state = 0;
					output += "-= ";
					tokens.addElement("-=");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("-=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else {
					state = 0;
					output += "- ";
					tokens.addElement("-");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("-");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 11: {
				if(ch == '=') {
					
					state = 0;
					output += "*= ";
					tokens.addElement("*=");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("*=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else {
					state = 0;
					output += "* ";
					tokens.addElement("*");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("*");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 12: {
				if(ch == '=') {
					
					state = 0;
					output += "/= ";
					tokens.addElement("/=");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("/=");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
					i++;
				}
				else if(ch == '/') {
					
					while(ch != '\n' && i<input.length()) {
						
						i++;
						ch = input.charAt(i);
					}
					state = 0;
				}
				else {
					state = 0;
					output += "/ ";
					tokens.addElement("/");
					symbolTable[0].addElement("operator");
					symbolTable[1].addElement("/");
					symbolTable[2].addElement("");
					symbolTable[3].addElement(line);
				}
				break;
			}
			case 13: {
				state = 0;
				output += "{ ";
				tokens.addElement("{");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement("{");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 14: {
				state = 0;
				output += "} ";
				tokens.addElement("}");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement("}");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 15: {
				state = 0;
				output += ". ";
				tokens.addElement(".");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement(".");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 16: {
				state = 0;
				output += "? ";
				tokens.addElement("?");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement("?");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 17: {
				state = 0;
				output += "% ";
				tokens.addElement("%");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement("%");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 18: {
				state = 0;
				output += "( ";
				tokens.addElement("(");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement(")");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 19: {
				state = 0;
				output += ") ";
				tokens.addElement(")");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement(")");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 20: {
				state = 0;
				output += ", ";
				tokens.addElement(",");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement(",");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 21: {
				state = 0;
				output += "; ";
				tokens.addElement(";");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement(";");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 22: {
				state = 0;
				output += ": ";
				tokens.addElement(":");
				symbolTable[0].addElement("seperator");
				symbolTable[1].addElement(":");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			case 23: {
				state = 0;
				output += "^ ";
				tokens.addElement("^");
				symbolTable[0].addElement("operator");
				symbolTable[1].addElement("^");
				symbolTable[2].addElement("");
				symbolTable[3].addElement(line);
				break;
			}
			default : message = "state not defined"; break;
			}
		}
		return output;
	}
	
	boolean isKeyword(String token) {
		
		for(int k=0 ; k<keyword.length ; k++)
			if(keyword[k].equals(token))
				return true;
		return false;
	}
	boolean isInt(char c) {
		
		String s = "";
		s += c;
		try {
			Integer.parseInt(s);
		}
		catch(Exception e) { return false; }
		
		return true;
	}
	
	boolean isIdExist(String token) {
		
		for(int j=0 ; j<symbolTable[1].size() ; j++)
			if(symbolTable[1].elementAt(j).equals(token)) 
				return true;
		return false;
	}
	
	void getToken() { }
}