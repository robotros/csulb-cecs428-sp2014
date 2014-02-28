/*
 * Author: Aron Roberts
 * CECS 428 California State University Long Beach
 * January 22nd 2014
 * Decription: Prints to file a list of Gray Codes consisting of Binary Strings
 * N bits long starting with 0..0. Where N is inputed by user. 
 * For the constraits of this program N is limited from 0-23 for larger N some
 * modification is required but minor.
 * results are written to a text file with exactly one codeword per line.
 *
 * recursive algorithm used: any gray code set of length N can be retrieved
 * by taking the set of N-1 and appending 0, and then 1
 * and then appending 1, and then 0 to each element in sequence until complete.
 */

package greycode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aron
 */

public class GreyCode {
    
    public static PrintWriter writer;

    // append reverse of order n gray code to prefix string, and print
    public static void yarg(String prefix, int n) {
        if (n == 0) writer.println(prefix);
        else {
            gray(prefix + "1", n - 1);
            yarg(prefix + "0", n - 1);
        }
    }  

    // append order n gray code to end of prefix string, and print
    public static void gray(String prefix, int n) {
        if (n == 0) writer.println(prefix);
        else {
            gray(prefix + "0", n - 1);
            yarg(prefix + "1", n - 1);
        }
    }  
    
    public static void fOpen (String fileName) throws FileNotFoundException, UnsupportedEncodingException{
        writer = new PrintWriter(fileName, "UTF-8");
    }
    
    public static void fClose() {
        writer.close();
    }


    public static void main(String[] args) {
        
        //create buffer to read in from cmd line
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N=0;
        String input = "0";
        
        do {
        //prompt for N from user
            System.out.println("Please Select N-length Gray Code");
            System.out.println("Please Select 0-23 a number outside that range will exit");
            System.out.println("Invalid character will throw exception");
            
            //read in line
            try {
                input = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(GreyCode.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            //convert input string to int
             N = Integer.parseInt(input);
             
            if (N > -1 && N <24){
        
                //create filename using N
                String fileName = "output\\grayCodeOut" + input + ".txt";
        
                //open or create file
                try {
                    fOpen(fileName);
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    Logger.getLogger(GreyCode.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                //start recursive algorithm
                gray("", N);        
        
                fClose(); //close file
            }
        } while (N <24 && N > -1);
        
    }
}
