/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №1 по дисциплине ЛОИС
// Вариант С: Проверить, является ли формула СКНФ
// Выполнена студентом грруппы 821701 БГУИР Клевцевич Александр Владимирович
// Класс предназначен для проведения теста знаний пользователя

package test;

import parser.Constant;
import parser.Parser;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Test {
    private List<String> test;
    private final int COUNT_QUESTIONS = 10;

    public Test() {
        test = new ArrayList<>();
        for (int i = 0; i < COUNT_QUESTIONS; i++) {
            generateFormula();
        }
    }

    private void generateFormula() {
        int countElements = 2 + (int) (Math.random() * 2);
        TruthTable truthTable = new TruthTable(countElements);
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < truthTable.getCountCon() - 1; i++) {
            builder.append("(");
        }
        int count = 0;
        for (int j = 0; j < truthTable.getRows(); j++) {
            if (truthTable.getTable()[j][countElements] == 0) {
                builder.append(createAtom(countElements, truthTable.getTable()[j]));
                if (count != 0) {
                    builder.append(")");
                }
                builder.append("/\\");
                count++;
            }
        }
        builder.setLength(builder.length() - 2);
        int checkTrue = (int) (Math.random() * 2);
//        test.add(builder.toString());
        if ((checkTrue == 0)) {
            test.add(builder.toString());
        } else {
            test.add(makeError(builder.toString(), countElements));
        }
    }

    private String createAtom(int countElements, int[] rowTruthTable) {
        StringBuilder atom = new StringBuilder();
        for (int i = 0; i < countElements - 1; i++) {
            atom.append("(");
        }
        int count = 0;
        for (int i = 0; i < countElements; i++) {
            atom.append((rowTruthTable[i] == 0) ? Constant.SYMBOLS.get(i) : ("(!" + Constant.SYMBOLS.get(i) + ")"));
            if (count != 0) {
                atom.append(")");
            }
            atom.append("\\/");
            count++;
        }
        atom.setLength(atom.length() - 2);
        return atom.toString();
    }

    private String makeError(String expression, int countElements) {
        int typeError = (int) (Math.random() * 3);
        switch (typeError) {
//            change con on dis
            case 0: {
                int countConjunction = count(expression, "/\\");
                int changeConjunction = 1 + (int) (Math.random() * countConjunction);
                return changeSign(expression, "/\\", changeConjunction);
            }
//            change con on dis
            case 1: {
                int countDisjunction = count(expression, "\\/");
                int changeDisjunction = 1 + (int) (Math.random() * countDisjunction);
                return changeSign(expression, "\\/", changeDisjunction);
            }
            default:
                return "";
        }
    }

    public void run() {
        int result = 0;
        for (int i = 0; i < test.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + test.get(i));
            System.out.print("1. Yes;\n2. No.\nYour choice: ");
            Scanner in = new Scanner(System.in);
            try {
                int num = in.nextInt();
                boolean choice = num == 1;
                Parser parser = new Parser(test.get(i));
                if (choice == parser.getResult()) {
                    System.out.println("Correctly!");
                    result++;
                } else {
                    System.out.println("Wrong!");
                    System.out.println(parser.getMessage() + "\n");
                }
            } catch (InputMismatchException e) {
                i--;
                System.out.println("Please enter correct data!");
            }
        }

        System.out.printf("Your result: %s(%d of %d)", result, result, test.size());
    }

    private int count(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    private String changeSign(String expression, String character, int position) {
        String newCharacter = ("'/\\".equals(character)) ? "\\/" : "/\\";
        int positionSign = findSignForCount(expression, character, position);
        String result = copy(expression, 0, positionSign) + newCharacter + copy(expression, positionSign + 2, expression.length());
        return result;
    }

    private String copy(String expression, int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < end; i++) {
            stringBuilder.append(expression.charAt(i));
        }
        return stringBuilder.toString();
    }

    private int findSignForCount(String expression, String sign, int counter) {
        int tempCount = 0;
        for (int i = 0; i < expression.length() - 1; i++) {
            if (sign.equals("" + expression.charAt(i) + expression.charAt(i + 1))) {
                tempCount++;
                if (tempCount == counter) {
                    return i;
                }
            }
        }
        return -1;
    }
}
