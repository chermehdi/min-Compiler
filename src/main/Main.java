package main;

import componenets.LexAnays;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class Main {

    static String path = "";
    static FileInputStream fs;
    static BufferedReader bf;
    static PrintWriter pw;


    public static void main(String[] args) {
        LexAnays lx = new LexAnays();
        lx.setBuff("&&");
        lx.lex();
    }

}
