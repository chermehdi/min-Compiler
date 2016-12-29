package componenets;

import java.util.Arrays;

/**
 * class Responsible for doing lexical analisis for the given files
 * Created by mehdi on 27/12/16.
 */
public class LexAnays {

    public static final String Identifier = "identifier";
    public static final String Comment = "comment";
    public static final String Separator = "separator";
    public static final String Int = "Integer";
    public static final String Flo = "Float";
    public static final String Doub = "Double";
    public static final String Oper = "Operator";
    public static final String ClassName = "ClassName";
    public static final String OParent = "Opening Parenthesis";
    public static final String CParent = "Closing Parenthesis";

    public static int LineNumber = 1;
    public static String Assignment = "Assignment";
    public int[][] transition;
    public char[] lexeme, ul;
    public char[] buff;
    public int buffPointer;

    /**
     * Constructing the lexical analys
     */
    public LexAnays() {
        transition = new int[10][256];
        buff = new char[1024];
        buffPointer = 0;
    }

    /**
     * filling the buffer
     *
     * @param s
     */
    public void setBuff(String s) {
        buff = new char[1024];
        for (int i = 0; i < s.length(); i++)
            buff[i] = s.charAt(i);
        buffPointer = 0;
    }

    /**
     * initializing the trasition matrix
     */
    private void init() {
        for (int i = 0; i < 10; i++) {
            Arrays.fill(transition[i], -1);
        }
    }

    /**
     * testing if the given token is an integer
     *
     * @return
     */
    public boolean isInteger() {
        init();
        int[] finals = {2, 3};
        transition[0]['-'] = transition[0]['+'] = 1;
        transition[0]['0'] = 3;
        for (int i = '1'; i <= '9'; i++)
            transition[0][i] = transition[1][i] = transition[2][i] = 2;
        transition[2]['0'] = 2;
        return checkAutomate("Integer", finals);
    }

    /**
     * testing if the given token is a reel number
     *
     * @return
     */
    public boolean isFloat() {
        init();
        int[] finals = {3};
        transition[0]['+'] = transition[0]['-'] = 1;
        transition[1]['.'] = 2;
        for (int i = '0'; i <= '9'; i++) {
            transition[0][i] = transition[1][i] = 1;
            transition[2][i] = transition[3][i] = 3;
        }
        return checkAutomate("Float", finals);
    }

    /**
     * testing if the given token is a comment
     *
     * @return
     */
    public boolean isComment() {
        init();
        int[] finals = {4};
        transition[0]['/'] = 1;
        transition[1]['/'] = 5;
        transition[3]['/'] = 4;
        transition[1]['*'] = 2;
        transition[2]['*'] = 3;
        transition[3]['*'] = 3;
        for (int i = 0; i < 255; i++) {
            if (i != '*') {
                transition[2][i] = 2;
            }
            if (i != '/' && i != '*') {
                transition[3][i] = 2;
            }
            if (i != '\n') {
                transition[5][i] = 4;
            }
        }
        return checkAutomate("Comment", finals);
    }

    /**
     * testing if the given token is an Separator
     *
     * @return
     */
    public boolean isSeparator() {
        init();
        int[] finals = {1};
        transition[0][';'] = transition[0][','] = transition[1][';'] = 1;
        return checkAutomate("Separator", finals);
    }

    /**
     * testing if the given token is an operator
     *
     * @return
     */

    public boolean isOperator() {
        init();
        int[] finals = {1, 2};
        transition[0]['+'] = transition[0]['/'] = transition[0]['-'] = transition[0]['*'] = 1;
        transition[0]['&'] = transition[0]['|'] = transition[0]['^'] = 3;
        transition[3]['&'] = transition[3]['|'] = 2;
        transition[1]['='] = 1;
        transition[3]['='] = 2;
        transition[1]['&'] = 2;
        return checkAutomate("Operator", finals);
    }

    public boolean isOpeningParenth() {
        init();
        int[] finals = {1};
        transition[0]['('] = 1;
        return checkAutomate(OParent, finals);
    }

    public boolean isClosingParenth() {
        init();
        int[] finals = {1};
        transition[0][')'] = 1;
        return checkAutomate(CParent, finals);
    }

    public boolean isOpening() {
        init();
        int[] finals = {1};
        transition[0]['{'] = 1;
        return checkAutomate("Op", finals);
    }

    public boolean isClosing() {
        init();
        int[] finals = {1};
        transition[0]['}'] = 1;
        return checkAutomate("Cp", finals);
    }

    public boolean isOpeningBracket() {
        init();
        int[] finals = {1};
        transition[0]['['] = 1;
        return checkAutomate("Obracket", finals);
    }

    public boolean isClosingBracket() {
        init();
        int[] finals = {1};
        transition[0][']'] = 1;
        return checkAutomate("Cbracket", finals);
    }

    public boolean isAssigment() {
        init();
        int[] finals = {1};
        transition[0]['='] = 1;
        return checkAutomate(Assignment, finals);
    }

    /**
     * testing if the given token is a white space
     *
     * @return
     */
    public boolean isSpace() {
        init();
        int[] finals = {1};
        transition[0][' '] = transition[0]['\n'] = transition[0]['\t'] = 1;
        transition[1][' '] = transition[1]['\n'] = transition[1]['\t'] = 1;
        return checkAutomate("Space", finals);
    }

    /**
     * testing if the given token is an identifier
     *
     * @return
     */

    public boolean isIdentifier() {
        init();
        int[] finals = {1};
        for (int i = '0'; i <= '9'; i++)
            transition[1][i] = 1;
        for (int i = 'A'; i <= 'Z'; i++)
            transition[0][i] = transition[1][i] = 1;
        for (int i = 'a'; i <= 'z'; i++)
            transition[0][i] = transition[1][i] = 1;
        transition[0]['_'] = transition[1]['_'] = 1;
        return checkAutomate(Identifier, finals);
    }

    /**
     * checking the correctness of the automate and extracting the lexical units
     *
     * @param s
     * @param finals
     * @return
     */
    public boolean checkAutomate(String s, int[] finals) {
        //System.err.println("checking " + s );
        int state = 0;
        int it = 0;
        StringBuilder sb = new StringBuilder();
        while (transition[state][buff[buffPointer]] != -1) {
            state = transition[state][buff[buffPointer]];
            sb.append(buff[buffPointer++]);
            //  System.err.println(sb + " " + state);
        }
        lexeme = sb.toString().toCharArray();
        for (int i = 0; i < finals.length; i++) {
            if (finals[i] == state && state != 0) {
                ul = s.toCharArray();
                return true;
            }
        }
        return false;
    }


    /**
     * Lex Function
     *
     * @return
     */
    public boolean lex() {
        if (isClassName() || isAssigment() || isOpeningParenth()
                || isClosingParenth() || isIdentifier() || isComment()
                || isOperator() || isFloat() || isInteger() || isSeparator()) {
            System.out.println(new String(lexeme) + " " + new String(ul));
            return true;
        }
        if (isSpace()) {
            return lex();
        }
        System.out.println("lexical error at line number => " + LineNumber);
        return false;
    }

    public boolean isClassName() {
        init();
        int[] finals = {1, 2};
        for (int i = 'A'; i <= 'Z'; i++) {
            transition[0][i] = 1;
            transition[1][i] = 2;
        }
        for (int i = 'a'; i <= 'z'; i++) {
            transition[1][i] = 2;
            transition[2][i] = 2;
        }
        return checkAutomate(ClassName, finals);
    }
}
