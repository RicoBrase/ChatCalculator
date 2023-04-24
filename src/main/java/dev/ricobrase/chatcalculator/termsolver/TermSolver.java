package dev.ricobrase.chatcalculator.termsolver;

import java.util.*;

public class TermSolver {

    static Map<String, Operators> operators = new HashMap<>(){
        {
            put("+", Operators.PLUS);
            put("-", Operators.MINUS);
            put("*", Operators.MULTIPLY);
            put("/", Operators.DIVIDE);
            put("^", Operators.POW);
        }
    };

    public static Optional<String> transformInfixToPostfix(String input) {

        char[] infix = input.replaceAll(" ", "").toCharArray();
        Stack<String> opStack = new Stack<>();
        StringJoiner postfix = new StringJoiner(" ");

        StringBuilder operand = new StringBuilder();

        for(int i = 0; i < infix.length; i++) {
            if(!operators.containsKey(String.valueOf(infix[i])) && infix[i] != '(' && infix[i] != ')') {
                operand.append(infix[i]);
                if(i < infix.length - 1) {
                    continue;
                }
            }

            if(operand.length() > 0) {
                postfix.add(operand.toString());
                operand = new StringBuilder();
            }

            if(infix[i] == '(') {
                opStack.push("(");
            }else if(infix[i] == ')') {
                if(opStack.size() > 0) {
                    String op = opStack.pop();
                    while(!op.equals("(")) {
                        postfix.add(op);
                        op = opStack.pop();
                        if(opStack.size() == 0) break;
                    }
                }
            }else if(operators.containsKey(String.valueOf(infix[i]))) {
                if(opStack.size() == 0 || opStack.get(opStack.size() - 1).equals("(")) {
                    opStack.push(String.valueOf(infix[i]));
                }else{

                    if(
                        (operators.get(String.valueOf(infix[i])).getPriority() > operators.get(opStack.get(opStack.size() - 1)).getPriority()) ||
                        (operators.get(String.valueOf(infix[i])).getPriority() == operators.get(opStack.get(opStack.size() - 1)).getPriority() && operators.get(String.valueOf(infix[i])).isRightAssociative())
                    ) {
                        opStack.push(String.valueOf(infix[i]));
                    }else{
                        while(
                                operators.get(String.valueOf(infix[i])).getPriority() < operators.get(opStack.get(opStack.size() - 1)).getPriority() ||
                                (operators.get(String.valueOf(infix[i])).getPriority() == operators.get(opStack.get(opStack.size() - 1)).getPriority() && !operators.get(String.valueOf(infix[i])).isRightAssociative())
                        ) {
                            postfix.add(opStack.pop());
                            if(opStack.size() == 0) break;
                        }
                        opStack.push(String.valueOf(infix[i]));
                    }
                }
            }
        }

        if(operand.length() > 0) {
            postfix.add(operand);
        }

        while (opStack.size() > 0) {
            postfix.add(opStack.pop());
        }

        return Optional.of(postfix.toString());
    }

    public static double solvePostfix(String input) throws NumberFormatException{
        String[] in = input.split(" ");
        Stack<Double> stack = new Stack<>();
        for(String o : in) {
            try {
                double operand = Double.parseDouble(o);
                stack.push(operand);
            }catch (NumberFormatException ex) {
                if(!operators.containsKey(o)) {
                    throw ex;
                }

                if(stack.size() < 2) {
                    throw new NumberFormatException("Invalid operator, expected number");
                }

                double right = stack.pop();
                double left = stack.pop();
                double result = operators.get(o).compute(left, right);
                stack.push(result);
            }
        }
        return stack.pop();
    }

}
