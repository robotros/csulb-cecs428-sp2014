
package edu.csulb.cecs428.gasstations;

/**
 *
 * @author Aron Roberts
 */
public class Node implements Comparable<Node> {

    private int i;
    private int numEdges;

    /**
     *
     * @param i
     * @param weight
     */
    public Node(int i , int weight) {
        this.i = i;
        this.numEdges = weight;
    }

    public Node(Node e) {
        this.i = e.getI();
        this.numEdges = e.getNumEdges();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.i;
        hash = 23 * hash + this.numEdges;
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
        final Node other = (Node) obj;
        if (this.i != other.i) {
            return false;
        }
        return this.numEdges == other.numEdges;
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
     * @return
     */
    public int getNumEdges() {
        return numEdges;
    }

    /**
     *
     * @param numEdges
     */
    public void setNumEdges(int numEdges) {
        this.numEdges = numEdges;
    }

    @Override
    public int compareTo(Node other) {
        return  other.numEdges - numEdges;
    }

    @Override
    public String toString() {
        return "Node{" + "i=" + i  + ", edges=" + numEdges + '}';
    }

}
