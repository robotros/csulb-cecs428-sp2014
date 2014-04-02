/*
 * Author: Aron Roberts
 * CECS 428 California State University Long Beach
 * April 1st 2014
 * 4D Maze using Disjoint Sets Algorithm
 */
package alienmaze;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aron Roberts
 */
public class AlienMaze {

    private static Cell[][][][] maze;
    private static int NumElements;
    private static final int XUP = 0;
    private static final int XDOWN = 1;
    private static final int YUP = 2;
    private static final int YDOWN = 3;
    private static final int ZUP = 4;
    private static final int ZDOWN = 5;
    private static final int TUP = 6;
    private static final int TDOWN = 7;
    private static int size;

    /**
     * Construct Cells of a Maze using Disjoint Sets.
     *
     * @param numElements the initial number of Cells Available in each
     * Dimension--also the initial number of disjoint sets, since every cell is
     * initially in its own set.
     *
     */
    public AlienMaze(int numElements) {
        int x, y, z, t;
        size = (int) Math.pow(numElements, 4);

        System.out.println("Number of Elements : " + numElements);
        maze = new Cell[numElements][numElements][numElements][numElements];
        for (t = 0; t < numElements; t++) {
            for (z = 0; z < numElements; z++) {
                for (y = 0; y < numElements; y++) {
                    for (x = 0; x < numElements; x++) {
                        maze[t][z][y][x] = new Cell(null, 0, 0b11111111, new int[]{t, z, y, x});
                        maze[t][z][y][x].setRoot(maze[t][z][y][x]);
                    }
                }
            }
        }

    }

    /**
     * union() unites two disjoint sets into a single set. A union-by-rank
     * heuristic is used to choose the new root.
     *
     * @param root1 the root of the first set.
     * @param root2 the root of the other set.
     *
     */
    public void union(Cell root1, Cell root2) {
        /* get roots for each cell */
        root1 = find(root1);
        root2 = find(root2);
        /* check if roots are already identical */
        if (root1 != root2) {
            if (root2.getRank() > root1.getRank()) {
                root1.setRoot(root2);// root2 is taller; make root2 new root
            } else {
                if (root1.getRank() == root2.getRank()) {
                    root1.setRank(root1.getRank() + 1);   // Both trees same height; new one is taller
                }
                root2.setRoot(root1); // root1 equal or taller; make root1 new root
            }
            size--;
        }
    }

    /**
     * find() finds the name of the set containing a given element. Performs
     * path compression along the way.
     *
     * @param x the element sought.
     * @return the set containing x.
     */
    public Cell find(Cell x) {

        if (x == x.getRoot()) {
            return x;     // x is the root of the tree; return it
        } else {
            // Find out who the root is; compress path by making the root x's parent.
            x.setRoot(find(x.getRoot()));
            return x.getRoot();          // Return the root
        }
    }

    /**
     * @return The chosen number of elements in each dimension
     */
    public static int menu() {
        //create buffer to read in from cmd line
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N;
        String input = "0";

        //prompt for N from user
        System.out.println("Please Select N-Size Dimensions");
        System.out.println("Please Select 1-45 a number outside that range will exit");
        System.out.println("Invalid character will throw exception");

        //read in line
        try {
            input = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(AlienMaze.class.getName()).log(Level.SEVERE, null, ex);
        }

        //convert input string to int
        N = Integer.parseInt(input);

        if (N > -1 && N < 46) {
            return N;
        } else {
            return 0;
        }
    }

    /**
     * createMaze() createMaze
     *
     * @param s the Maze to be created.
     *
     */
    public static void createMaze(AlienMaze s) {
        Cell set1, set2;

        /* loop through as many times as their are elements */
        while (size > 1) {
            int success = 0;

            while (success == 0) {
                int t, z, y, x;
                int direction;
                int wall1, wall2;
                wall1 = 0;
                wall2 = 0;

                t = (int) (Math.random() * NumElements);
                z = (int) (Math.random() * NumElements);
                y = (int) (Math.random() * NumElements);
                x = (int) (Math.random() * NumElements);
                set1 = maze[t][z][y][x];

                ArrayList<Integer> d = new ArrayList<>(8);

                int n = 0;
                direction = 9;

                do {

                    do {
                        direction = (int) (Math.random() * 8);
                    } while (d.contains(direction));

                    d.add(direction);
                    n++;

                    switch (direction) {
                        case XUP:
                            if (x + 1 < NumElements) {
                                set2 = maze[t][z][y][x + 1];
                                wall1 = 1;
                                wall2 = 2;
                            } else {
                                set2 = set1;
                            }
                            break;
                        case XDOWN:
                            if (x - 1 >= 0) {
                                set2 = maze[t][z][y][x - 1];
                                wall1 = 2;
                                wall2 = 1;
                            } else {
                                set2 = set1;
                            }
                            break;
                        case YUP:
                            if (y + 1 < NumElements) {
                                set2 = maze[t][z][y + 1][x];
                                wall1 = 4;
                                wall2 = 8;
                            } else {
                                set2 = set1;
                            }
                            break;
                        case YDOWN:
                            if (y - 1 >= 0) {
                                set2 = maze[t][z][y - 1][x];
                                wall1 = 8;
                                wall2 = 4;
                            } else {
                                set2 = set1;
                            }
                            break;
                        case ZUP:
                            if (z + 1 < NumElements) {
                                set2 = maze[t][z + 1][y][x];
                                wall1 = 16;
                                wall2 = 32;
                            } else {
                                set2 = set1;
                            }
                            break;
                        case ZDOWN:
                            if (z - 1 >= 0) {
                                set2 = maze[t][z - 1][y][x];
                                wall1 = 32;
                                wall2 = 16;
                            } else {
                                set2 = set1;
                            }
                            break;
                        case TUP:
                            if (t + 1 < NumElements) {
                                set2 = maze[t + 1][z][y][x];
                                wall1 = 64;
                                wall2 = 128;
                            } else {
                                set2 = set1;
                            }
                            break;
                        case TDOWN:
                            if (t - 1 >= 0) {
                                set2 = maze[t - 1][z][y][x];
                                wall1 = 128;
                                wall2 = 64;
                            } else {
                                set2 = set1;
                            }
                            break;
                        default:
                            set2 = set1;
                    }
                    if (set2 == null) {
                        set2 = set1;
                    }

                } while (s.find(set1) == s.find(set2) && n < 8);

                if (s.find(set1) != s.find(set2)) {
                    s.union(set1, set2);
                    set1.setWalls(set1.getWalls() - wall1);
                    set2.setWalls(set2.getWalls() - wall2);
                    success = 1;
                }
            }

        }

    }

    public static void main(String[] args) {

        do {
            NumElements = menu();
            //create filename using N
            String fileName = "output\\4DMAZE" + NumElements + ".out";

            /* initialize maze with cells with all walls */
            AlienMaze s = new AlienMaze(NumElements);
            System.out.println("remove walls");

            createMaze(s);

            System.out.println("Print to Binary");

            try {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));

                for (Cell[][][] mazeT : s.maze) {
                    for (Cell[][] mazeZ : mazeT) {
                        for (Cell[] mazeY : mazeZ) {
                            for (Cell mazeX : mazeY) {
                                /* uncomment next line to output Cells*/
                                System.out.println(mazeX);
                                out.write(mazeX.getWalls());
                            }
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AlienMaze.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AlienMaze.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("File Printed");
        } while (NumElements != 0);
    }
}
