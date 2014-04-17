package edu.csulb.cecs428.gasstations;

import java.io.BufferedReader;
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
    private static ArrayList<Integer> nodes;
    private static ArrayList<Integer> solution;
    private static ArrayList<Edge> removeList;
    private static ArrayList<Node> nodeList;
    private static PrintWriter writer;
    private static int size;

    //=================
    //= other methods =
    //=================
    /**
     * output menu to command line
     *
     * @throws java.io.IOException
     */
    public static void menu() throws IOException {
        String input;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("What file contains the Adjaceny Matrix?");
        input = br.readLine();
        fileName = input;

        System.out.println("What is the value of k?");
        System.out.println("Non-Integer Throws exception.");
        input = br.readLine();
        outFileName = "outK" + input + ".txt";
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
        size = V.length;
        graph = new int[size][size];    // Set Graph Size
        edgeList = new ArrayList();
        vertices = new ArrayList();
        nodes = new ArrayList();

        int i = 0;
        for (String e : V) {
            String[] E = e.split(",");  // spilt atring on edges
            int j = 0;
            for (String x : E) {
                graph[i][j] = Integer.parseInt(x.trim());
                j++;
            }
            vertices.add(i);            // add vertice
            nodes.add(i);               // add node
            i++;                        // incriment vertex
        }
    }

    /**
     * find usable edges from graph. add all upper-triangular edges less then k
     * to an edgeList sort list by weight
     *
     * @param graph and sort from lowest weight to highest.
     *
     */
    public static void sortEdges(int[][] graph) {

        for (int i = 0; i < graph.length; i++) {
            for (int j = i; j < graph[i].length; j++) {
                if ((i != j) && (graph[i][j] <= k)) {
                    edgeList.add(new Edge(i, j, graph[i][j]));
                }
            }
        }

        updateNodes();
        System.out.println("remaining edges : " + edgeList.size());
        Collections.sort(edgeList);
    }

    /**
     * iterate through nodes and create a list of nodes with numbered count of
     * useful edges sort list on numEdges
     *
     */
    public static void updateNodes() {

        nodeList = new ArrayList();
        int x = 0;

        for (int v : nodes) {
            for (Edge e : edgeList) {
                if ((v == e.getI() && vertices.contains(e.getJ())) || (v == e.getJ() && vertices.contains(e.getI()))) {
                    x++;
                }
            }
            nodeList.add(new Node(v, x));
            x = 0;
        }
        Collections.sort(nodeList);
    }

    /**
     * remove all edges from edgeList for node i clean up edgeList and sort by
     * weight
     *
     * @param i
     */
    public static void removeEdges(int i) {
        removeList = new ArrayList();

        /* loop through edgeList and remove all edges that reference node i 
         *  or any edges between 2 nodes that are already within k of a center
         **/
        for (Edge e : edgeList) {
            if (e.getI() == i || e.getJ() == i) {
                removeList.add(e);
            } else if (!vertices.contains(e.getI()) && !vertices.contains(e.getJ())) {
                removeList.add(e);
            }
        }

        for (Edge e : removeList) {
            if (vertices.contains(e.getJ())) {
                vertices.remove(new Integer(e.getJ()));
            }
            if (vertices.contains(e.getI())) {
                vertices.remove(new Integer(e.getI()));
            }
            edgeList.remove(e);
        }

        Collections.sort(edgeList);
        updateNodes();
    }

    /**
     * Logic behind placing centers
     */
    public static void placeStations() {

        solution = new ArrayList();

        while (vertices.size() > 0 && edgeList.size() > 0) {
            //&& nodeList.get(0).getNumEdges() > 0
            System.out.println("edges : " + edgeList.size() + " vertices : " + vertices.size());
            Node n = nodeList.get(0);
            int i = n.getI();
            System.out.println(n);

            if (!solution.contains(i)) {
                solution.add(i);
                removeEdges(i);
                if (vertices.contains(i)) {
                    vertices.remove(new Integer(i));
                }
            } else {
                System.out.println("#######WTF WHY AM I HERE#########");
            }

        }// end while loop

        /* add unreachable nodes to cover */
        if (vertices.size() > 0) {
            for (int e : vertices) {
                solution.add(e);
            }
        }
    }

    /**
     *
     * @param fileName
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void fOpen(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        writer = new PrintWriter(fileName, "UTF-8");
    }

    /**
     * close file
     */
    public static void fClose() {
        writer.close();
    }

    //================
    //= MAIN PROGRAM =
    //================
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        do {
            try {
                menu();

            } catch (IOException ex) {
                Logger.getLogger(KCenterMain.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            try {
                readFile(fileName);

            } catch (IOException ex) {
                Logger.getLogger(KCenterMain.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            /*TEST PRINT MATRIX*/
            //printMatrix(graph);
            
            /*remove and sort edges*/
            sortEdges(graph);

            /*TEST PRINT EDGES */
            //printEdges(edgeList);
            
            placeStations();

            try {
                fOpen(outFileName);

            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(KCenterMain.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            Collections.sort(solution);
            System.out.println("Solution Size : " + solution.size());
            System.out.println(solution);

            /* print solution to file*/
            int i = 0;
            for (int e : solution) {
                writer.print((e) + "x");
            }

            fClose();

            try {
                System.out.println("Solution Matches : " + checkAnwer(outFileName));

            } catch (IOException ex) {
                Logger.getLogger(KCenterMain.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } while (k > 0);
    }

    //====================
    //= FOR TESTING ONLY =
    //====================
    /**
     *
     * @param fileName : printed file
     * @return : if file matches solution
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean checkAnwer(String fileName) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String matrix = "";
        String line;
        while ((line = reader.readLine()) != null) {
            matrix = matrix + line;
        }

        String[] answer = matrix.split("x");
        int[] intAnswer = new int[answer.length];

        int i = 0;
        for (String e : answer) {
            intAnswer[i] = Integer.parseInt(e.trim());
            i++;
        }

        System.out.println("Answer Size : " + intAnswer.length);

        i = 0;
        for (int e : solution) {
            if (e != intAnswer[i]) {
                return false;
            }
            i++;
        }

        return true;

    }

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

    /**
     * Usage prints all edges less then k
     *
     * @param edges
     */
    public static void printEdges(ArrayList edges) {
        System.out.println(" Edges : ");
        for (Object e : edges) {
            System.out.println(e);
        }
    }

    /**
     * Usage print edges matching criteria for given node
     *
     * @param v : node index
     */
    public static void printEdges(int v) {
        System.out.println("Edges for vertex : " + v);
        for (Edge e : edgeList) {
            if (e.getI() == v || e.getJ() == v) {
                System.out.println(e);
            }
        }
    }

}
