/*
 * Author: Aron Roberts
 * CECS 428 California State University Long Beach
 * April 1st 2014
 * 4D Maze using Disjoint Sets Algorithm
 */
package alienmaze;

import java.util.Arrays;

/**
 *
 * @author Aron
 */
public class Cell {

    private Cell root;
    private int rank;
    private int walls;
    private int[] location;

    /* constructor */
    public Cell() {

        this.rank = 0;
        this.walls = 0b11111111;

    }

    public Cell(Cell root, int rank, int walls, int[] location) {
        this.root = root;
        this.rank = rank;
        this.walls = walls;
        this.location = location;
    }
    
    /*toString*/
    @Override
    public String toString() {
        return "Cell{" + "root=" + Arrays.toString(root.location) + ", rank=" + rank + ", walls=" + Integer.toBinaryString(walls) + ", location=" + Arrays.toString(location) + '}';
    }

    /* Getters and Setters */
    public Cell getRoot() {
        return root;
    }

    public void setRoot(Cell root) {
        this.root = root;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWalls() {
        return walls;
    }

    public void setWalls(int value) {
        this.walls = value;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

}
