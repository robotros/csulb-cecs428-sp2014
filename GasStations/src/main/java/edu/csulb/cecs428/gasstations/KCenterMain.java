package edu.csulb.cecs428.gasstations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aron Roberts
 */
public class KCenterMain {
    
    private static String fileName;
    private static String outFileName;
    private static int k;
    private static int[][] graph;
    private static ArrayList<Edge> edgeList;
    private static ArrayList<Integer> vertices;
    private static ArrayList<Integer> solution;
    private static ArrayList<Edge> removeList;
    private static PrintWriter writer;

    //=================
    //= other methods =
    //=================
    /**
     * usage: verify correct matrix is read in.
     *
     * @param matrix : to print out
     */
    public static void printMatrix(int[][] matrix) {
        for (int[] i : matrix) {
            for (int j : i) {
                System.out.print(" " + j + " , ");
            }
            System.out.println("");
        }
    }
    
    public static void printEdges(ArrayList edges) {
        System.out.println(" Edges : ");
        for (Object e : edges) {
            System.out.println(e);
        }
    }

    /**
     * output menu to command line
     *
     * @throws java.io.IOException
     */
    public static void menu() throws IOException {
        String input = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("What file contains the Adjaceny Matrix?");
        input = br.readLine();
        fileName = input;
        
        System.out.println("What is the value of k?");
        System.out.println("Non-Integer Throws exception.");
        input = br.readLine();
        outFileName = "outK" + input+".txt";
        k = Integer.parseInt(input);
    }

    /**
     *
     * @param fileName : name of file to read in.
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void readFile(String fileName) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String matrix = "";
        String line;
        while ((line = reader.readLine()) != null) {
            matrix = matrix + line;
        }

        /* Convert File to Adj Matrix Graph[V][E]*/
        String[] V = matrix.split("x"); // Split String on Vertices
        int size = V.length;
        graph = new int[size][size]; // Set Graph Size
        edgeList = new ArrayList();
        vertices = new ArrayList();
        int i = 0;
        for (String e : V) {
            String[] E = e.split(",");
            int j = 0;
            for (String x : E) {
                graph[i][j] = Integer.parseInt(x.trim());
                j++;
            }
            vertices.add(i);
            i++;
        }
    }

    /**
     * remove edges from
     *
     * @param graph and sort from lowest weight to highest.
     *
     */
    public static void sortEdges(int[][] graph) {
        int n = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = i; j < graph[i].length; j++) {
                if ((i != j) && (graph[i][j] <= k)) {
                    edgeList.add(new Edge(i, j, graph[i][j]));
                }
            }
        }
        System.out.println("remaining edges : "+edgeList.size());
        Collections.sort(edgeList);
    }
    
    /**
     * 
     * @param i 
     */
    public static void removeEdges(int i) {
        removeList = new ArrayList();
        
        for (Edge e : edgeList) {
            
            if (e.getI() == i || e.getJ() == i) {
                removeList.add(e);
            }
        }
        
        for (Edge e : removeList) {
            if (vertices.contains(e.getJ())) {
                vertices.remove(new Integer(e.getJ()));
            }
            edgeList.remove(e);
        }
        
        Collections.sort(edgeList);
        System.out.println(edgeList.size());
        //printEdges(edgeList);
    }
    
    /**
     * 
     */
    public static void placeStations() {
        
        solution = new ArrayList();
        while (vertices.size() > 0 && edgeList.size() > 0) {
            Edge e = (Edge) edgeList.get(0);
            int i = e.getI();
            int j = e.getJ();
            
            if (vertices.contains(i)) {
                vertices.remove(new Integer(i));
                solution.add(i);
                removeEdges(i);
            } else if (vertices.contains(j)) {
                solution.add(j);
                removeEdges(j);
            } else {
                removeEdges(i);
            }
            
            if (vertices.contains(j)) {
                vertices.remove(new Integer(j));
            }
            
            System.out.println("remaining verts : " + vertices.size());
        }
        
        if (vertices.size() > 0) {
            for (int e : vertices) {
                solution.add(e);
            }
        }
    }
    
    public static void fOpen(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        writer = new PrintWriter(fileName, "UTF-8");
    }
    
    public static void fClose() {
        writer.close();
    }

    //========
    //= MAIN =
    //========
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        while (true) {
            try {
                menu();
            } catch (IOException ex) {
                Logger.getLogger(KCenterMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                readFile(fileName);
            } catch (IOException ex) {
                Logger.getLogger(KCenterMain.class.getName()).log(Level.SEVERE, null, ex);
            }

            /*TEST PRINT MATRIX*/
            //printMatrix(graph);

            /*remove and sort edges*/
            sortEdges(graph);

            /*TEST PRINT EDGES */
            //printEdges(edgeList);
            
            placeStations();
            
            System.out.println(solution);
            
            try {
                fOpen(outFileName);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(KCenterMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Collections.sort(solution);
            
            int i = 0;
            for (int e : solution) {
                if (i < solution.size() - 1) {
                    writer.print(e + "x");
                    i++;
                } else {
                    writer.print(e);
                }
            }
            
            fClose();
            
        }
    }
    
}
