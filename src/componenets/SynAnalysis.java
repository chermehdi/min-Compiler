package componenets;


public class SynAnalysis {

    private LexAnays lex;

    public static void main(String[] args) {
        SynAnalysis ss = new SynAnalysis();
        ss.lex = new LexAnays();
        ss.lex.setBuff("Object o = new Object();");
        if (ss.objectDec()) {
            System.err.println("Syntax verified With Success ");
        } else {
            System.err.println("Syntax Error ");
        }
    }


    /**
     * Simple type declaration
     *
     * @return
     */
    boolean declaration() {
        if (type()) {
            lex.lex();
            if (new String(lex.ul).equals(LexAnays.Identifier)) {
                if (lid()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Types Definition
     *
     * @return
     */
    boolean type() {
        lex.lex();
        String lexeme = new String(lex.lexeme);
        if (lexeme.equals("int") || lexeme.equals("float")
                || lexeme.equals("double") || lexeme.equals("boolean")
                || lexeme.equals("byte") || lexeme.equals("char")
                || lexeme.equals("long")) {
            System.err.println("int found ");
            return true;
        }
        return false;
    }

    /**
     * Java Keywords Definition
     *
     * @return
     */
    boolean keyword() {
        lex.lex();
        String lexeme = new String(lex.lexeme);
        if (lexeme.equals("final") || lexeme.equals("transient")
                || lexeme.equals("public") || lexeme.equals("void")
                || lexeme.equals("protected") || lexeme.equals("private")
                || lexeme.equals("static") || lexeme.equals("synchronized") || lexeme.equals("class")) {
            return true;
        }
        return false;
    }

    /**
     * Java Object Declaration and initialization
     *
     * @return
     */

    boolean objectDec() {
        lex.lex();
        if (new String(lex.ul).equals(LexAnays.ClassName)) {
            lex.lex();
            if (new String(lex.ul).equals(LexAnays.Identifier)) {
                lex.lex();
                if (new String(lex.lexeme).equals("=")) {
                    lex.lex();
                    if (new String(lex.lexeme).equals("new")) {
                        lex.lex();
                        if (new String(lex.ul).equals(LexAnays.ClassName)) {
                            lex.lex();
                            if (new String(lex.ul).equals(LexAnays.OParent)) {
                                lex.lex();
                                //adding parameters;
                                if (new String(lex.ul).equals(LexAnays.CParent)) {
                                    lex.lex();
                                    if (new String(lex.lexeme).equals(";")) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean lid() {
        lex.lex();
        if (new String(lex.lexeme).equals(",")) {
            lex.lex();
            if (new String(lex.ul).equals(LexAnays.Identifier)) {
                if (lid()) {
                    return true;
                }
            }
        } else {
            if (new String(lex.lexeme).equals(";")) {
                return true;
            }
        }
        return false;
    }

}
