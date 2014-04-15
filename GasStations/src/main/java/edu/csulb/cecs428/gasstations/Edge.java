/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.csulb.cecs428.gasstations;

/**
 *
 * @author Aron Roberts
 */
public class Edge implements Comparable<Edge> {

    private int i;
    private int j;
    private int weight;

    /**
     *
     * @param i
     * @param j
     * @param weight
     */
    public Edge(int i, int j, int weight) {
        this.i = i;
        this.j = j;
        this.weight = weight;
    }

    public Edge(Edge e) {
        this.i = e.getI();
        this.j = e.getJ();
        this.weight = e.getWeight();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.i;
        hash = 23 * hash + this.j;
        hash = 23 * hash + this.weight;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (this.i != other.i) {
            return false;
        }
        if (this.j != other.j) {
            return false;
        }
        if (this.weight != other.weight) {
            return false;
        }
        return true;
    }



    /**
     *
     * @return : i
     */
    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    /**
     *
     * @return : j
     */
    public int getJ() {
        return j;
    }

    /**
     *
     * @param j
     */
    public void setJ(int j) {
        this.j = j;
    }

    /**
     *
     * @return
     */
    public int getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return weight - other.weight;
    }

    @Override
    public String toString() {
        return "Edge{" + "i=" + i + ", j=" + j + ", weight=" + weight + '}';
    }

}
