import java.util.Stack;
import java.util.Vector;

public class Parser {
	 
	int id = 0,     equal = 1,   semi = 2,  begin = 3,  end = 4,
    	If = 5,     then = 6,    Else = 7,  While = 8,  Do = 9,
    	For = 10,   number = 11, dots = 12, print = 13, not = 14,
    	open = 15,  close = 16,  plus = 17, minus = 18, star = 19, 
    	slash = 20, small = 21,  big = 22,  s_equ = 23, b_equ = 24,
    	d_equ = 25, notequ = 26, xor = 27,  dollar = 28,
    	P = 0,      A = 1,       A1 = 2,    S = 3,      S1 = 4,
    	E = 5,      E1 = 6,      B = 7;
	
	String[][] grammer = { {""},
					/*1*/{"P ->","A"},
					/*2*/{"A ->","S","A1"},
					/*3*/{"A1->","S","A1"},
					/*4*/{"A1->"},
					/*5*/{"S ->","id","=","E",";"},
					/*6*/{"S ->","begin","A","end"},
					/*7*/{"S ->","if","E","then","S","S1"},
					/*8*/{"S1->","else","S"},
					/*9*/{"S1->"},
				   /*10*/{"S ->","while","E","do","S"},
				   /*11*/{"S ->","for","id","=","number",":","number","do","S"},
				   /*12*/{"S ->","print","id",";"},
				   /*13*/{"E ->","!","E","E1"},
				   /*14*/{"E ->","(","E",")","E1"},
				   /*15*/{"E ->","id","E1"},
				   /*16*/{"E ->","number","E1"},
				   /*17*/{"E1->","B","E","E1"},
				   /*18*/{"E1->"},
				   /*19*/{"B ->","+"},
				   /*20*/{"B ->","-"},
				   /*21*/{"B ->","*"},
				   /*22*/{"B ->","/"},
				   /*23*/{"B ->","<"},
				   /*24*/{"B ->",">"},
				   /*25*/{"B ->","<="},
				   /*26*/{"B ->",">="},
				   /*27*/{"B ->","=="},
				   /*28*/{"B ->","!="},
				   /*29*/{"B ->","^"}
			};
	
    int parseTable[][] = new int[8][29];
    Stack<String> stack = new Stack<String>();
    Vector<String> error = new Vector<String>();
    Vector<String> steps = new Vector<String>();
    
	 Parser() {
		 
		 parseTable[P][id] = 1;	    parseTable[P][begin] = 1;   parseTable[P][If] = 1;     parseTable[P][While] = 1;   parseTable[P][For] = 1;     parseTable[P][print] = 1;  parseTable[P][dollar] = 100;
		 parseTable[A][id] = 2;	    parseTable[A][begin] = 2;   parseTable[A][If] = 2;     parseTable[A][While] = 2;   parseTable[A][For] = 2;     parseTable[A][print] = 2;  parseTable[A][end] = 100;   parseTable[A][dollar] = 100;
		 parseTable[A1][id] = 3;    parseTable[A1][begin] = 3;  parseTable[A1][If] = 3;    parseTable[A1][While] = 3;  parseTable[A1][For] = 3;    parseTable[A1][print] = 3; parseTable[A1][end] = 4;    parseTable[A1][dollar] = 4;
		 parseTable[S][id] = 5;     parseTable[S][begin] = 6;   parseTable[S][If] = 7;     parseTable[S][While] = 10;  parseTable[S][For] = 11;    parseTable[S][print] = 12; parseTable[S][Else] = 100;  parseTable[S][dollar] = 100;
		 parseTable[S1][id] = 9;    parseTable[S1][begin] = 9;  parseTable[S1][If] = 9;    parseTable[S1][While] = 9;  parseTable[S1][For] = 9;    parseTable[S1][print] = 9; parseTable[S1][Else] = 8;   parseTable[S1][dollar] = 9;
		 parseTable[E][not] = 13;   parseTable[E][open] = 14;   parseTable[E][id] = 15;    parseTable[E][number] = 16; parseTable[E][semi] = 100;  parseTable[E][then] = 100; parseTable[E][Do] = 100;    parseTable[E][begin] = 100;  parseTable[E][If] = 100;    parseTable[E][While] = 100; parseTable[E][For] = 100; parseTable[E][print] = 100; parseTable[E][dollar] = 100;
		 parseTable[E1][plus] = 17; parseTable[E1][minus] = 17; parseTable[E1][star] = 17; parseTable[E1][slash] = 17; parseTable[E1][small] = 17; parseTable[E1][big] = 17;  parseTable[E1][s_equ] = 17; parseTable[E1][notequ] = 17; parseTable[E1][b_equ] = 17; parseTable[E1][d_equ] = 17; parseTable[E1][xor] = 17; parseTable[E1][semi] = 18;  parseTable[E1][then] = 18; parseTable[E1][Do] = 18; parseTable[E1][id] = 18;     parseTable[E1][begin] = 18; parseTable[E1][If] = 18; parseTable[E1][While] = 18; parseTable[E1][For] = 18; parseTable[E1][print] = 18; parseTable[E1][not] = 18; parseTable[E1][open] = 18; parseTable[E1][id] = 18; parseTable[E1][number] = 18; parseTable[E1][dollar] = 18; parseTable[E1][close] = 18;
		 parseTable[B][plus] = 19;  parseTable[B][minus] = 20;  parseTable[B][star] = 21;  parseTable[B][slash] = 22;  parseTable[B][small] = 23;  parseTable[B][big] = 24;   parseTable[B][s_equ] = 25;  parseTable[B][notequ] = 28;  parseTable[B][b_equ] = 26;  parseTable[B][d_equ] = 27;  parseTable[B][xor] = 29;  parseTable[B][not] = 100;   parseTable[B][open] = 100; parseTable[B][id] = 100; parseTable[B][number] = 100; parseTable[B][dollar] = 100;
	 }
	 
	 void parse(Vector<String> input) {
		 
		 error.clear();
		 steps.clear();
		 
		 input.addElement("$");
		 stack.push("$");
		 stack.push("P");
		 String top = stack.peek();
		 int nextToken = 0;
		 int SP = getIndexOf(top);
		 int T = getIndexOf(input.elementAt(nextToken));
		 steps.addElement(stack.toString());
		 steps.addElement(input.elementAt(nextToken));
		 
		 while(SP != -1) {
			 
			 if(top.equals(input.elementAt(nextToken))) {
				 nextToken++;
				 stack.pop();
				 steps.addElement("");
			 }
			 else {
				 if(isTerminal(top)) {
					 errorHandler(top);
					 stack.pop();
					 steps.addElement("");
				 }
				 else {
					 if(parseTable[SP][T] == 0) {
						 errorHandler(top);
						 nextToken++;
						 steps.addElement("");
					 }
					 else if(parseTable[SP][T] == 100) {
						 top = stack.pop();
						 errorHandler(top);
						 if(stack.search("P") >= 0 || stack.search("A") >= 0 || stack.search("A1") >= 0 || stack.search("S") >= 0|| stack.search("S1") >= 0|| stack.search("E") >= 0|| stack.search("E1") >= 0|| stack.search("B") >= 0)
							 steps.addElement("");
						 else {
							 stack.push(top);
							 nextToken++;
							 steps.addElement("");
						 } 
					 }
					 else {
						 stack.pop();
						 for(int i=grammer[parseTable[SP][T]].length-1 ; i>=1 ; i--)
						 	 	stack.push(grammer[parseTable[SP][T]][i]);
						 
						 String r = "";
						 for(int i=0 ; i<grammer[parseTable[SP][T]].length ; i++)
							 r += grammer[parseTable[SP][T]][i];
						 
						 steps.addElement(r);
					 }
				 }
			 }
			 if(stack.isEmpty()) {
				 
				 SP = -1;
			 }
			 else {

				 top = stack.peek();
				 SP = getIndexOf(stack.peek());
				 T = getIndexOf(input.elementAt(nextToken));
				 steps.addElement(stack.toString());
				 steps.addElement(input.elementAt(nextToken));
			 }
		 }
	 }
	 
	 int getIndexOf(String token) {
		 
		 switch(token) {
		 
		 case "id": return 0;
		 case "=": return 1;
		 case ";": return 2;
		 case "begin": return 3;
		 case "end": return 4;
		 case "if": return 5;
		 case "then": return 6;
		 case "else": return 7;
		 case "P": return 0;
		 case "A": return 1;
		 case "A1": return 2;
		 case "S": return 3;
		 case "S1": return 4;
		 case "E": return 5;
		 case "E1": return 6;
		 case "B": return 7;
		 case "while": return 8;
		 case "do": return 9;
		 case "for": return 10;
		 case "number": return 11;
		 case ":": return 12;
		 case "print": return 13;
		 case "!": return 14;
		 case "(": return 15;
		 case ")": return 16;
		 case "+": return 17;
		 case "-": return 18;
		 case "*": return 19;
		 case "/": return 20;
		 case "<": return 21;
		 case ">": return 22;
		 case "<=": return 23;
		 case ">=": return 24;
		 case "==": return 25;
		 case "!=": return 26;
		 case "^": return 27;
		 case "$": return 28;
		 default : return -1;
		 }
	 }
	 
	 boolean isTerminal(String t) {
		 
		 if(!t.equals("P") && !t.equals("A") && !t.equals("A1") && !t.equals("S") && !t.equals("S1") && !t.equals("E") && !t.equals("E1") && !t.equals("B"))
			 return true;
		 return false;
	 }
	 
	 void errorHandler(String top) {
		 
		 error.addElement("Syntax error, insert ' "+top+" ' to complete Statement");
	 }
}