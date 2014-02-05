package fr.univ_lorraine.pacman.controller.ghost;

import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;

import fr.univ_lorraine.pacman.controller.GhostController;
import fr.univ_lorraine.pacman.model.Block;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.World;
import java.util.ArrayList;

public class GhostCloaker extends GhostController {

    private static final double epsilon = 0.2;
    private ArrayList<Node> graph;

    private class Node {

        private int x, y;
        private ArrayList<Node> neighbors;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.neighbors = new ArrayList<>();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public ArrayList<Node> getNeighbors() {
            return neighbors;
        }

        public boolean addNeighbor(Node neighbor) {
            if (neighbor != null && !neighbors.contains(neighbor)) {
                neighbors.add(neighbor);
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Node)) {
                return false;
            }
            Node other = (Node) o;
            return other.x == this.x && other.y == this.y;
        }

        // dat fail autogeneration : so close from the answer !!
        @Override
        public int hashCode() {
            int hash = 5;
            hash = 41 * hash + this.x;
            hash = 41 * hash + this.y;
            return hash;
        }

    }

    public GhostCloaker(World world) {
        super(world);
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                if (world.getElement(i, j) == null || !(world.getElement(i, j) instanceof Block)) {
                    if ((world.getElement((i + 1) % 27, j) == null || world.getElement((i + 26) % 27, j) == null || !(world.getElement((i + 1) % 27, j) instanceof Block)
                            || !(world.getElement((i + 26) % 27, j) instanceof Block)) && (world.getElement(i, j - 1) == null || world.getElement(i, j + 1) == null
                            || !(world.getElement(i, j - 1) instanceof Block)) || world.getElement(i, j + 1) == null || !(world.getElement(i, j + 1) instanceof Block)) {
                        graph.add(new Node(i, j));
                    }
                }
            }
        }
        for (Node n : graph) {
            int done = 0;
            int i = 0;
            do{
                if ((world.getElement((n.x + 1) % 27, n.y) == null || !(world.getElement((n.x + 1) % 27, n.y) instanceof Block)) && (done & 1) == 0) {
                    if(n.addNeighbor(getNode((n.x + 1 + i) % 27, n.y)))
                        done |= 1;
                }
                if ((world.getElement((n.x + 26) % 27, n.y) == null || !(world.getElement((n.x + 26) % 27, n.y) instanceof Block)) && (done & 2) == 0) {
                    if(n.addNeighbor(getNode((n.x + 26 - i) % 27, n.y)))
                        done |= 2;
                }
                if ((world.getElement(n.x, n.y + 1 + i) == null || !(world.getElement(n.x, n.y + 1 + i) instanceof Block)) && (done & 4) == 0) {
                    if(n.addNeighbor(getNode(n.x, n.y + 1 + i)))
                        done |= 4;
                }
                if ((world.getElement(n.x, n.y - 1 - i) == null || !(world.getElement(n.x, n.y - 1 - i) instanceof Block)) && (done & 8) == 0) {
                    if(n.addNeighbor(getNode(n.x, n.y - 1 - i)))
                        done |= 8;
                }
            }while(done < 15);
        }
    }

    @Override
    public void update(float delta) {

    }
    
    private final Node getNode(int x, int y){
        for(Node n : graph){
            if(n.x == x && n.y == y)
                return n;
        }
        return null;
    }
}
