package fr.univ_lorraine.pacman.controller.ghost;

import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;

import fr.univ_lorraine.pacman.controller.GhostController;
import fr.univ_lorraine.pacman.model.Block;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.World;
import java.util.ArrayList;

public class GhostCloaker extends GhostController {

    private static final double epsilon = 0.2;
    private ArrayList<Node> graph;
    private ArrayList<Node> posPacman;

    private class Node {

        private int x, y;
        private ArrayList<Node> neighbors;
        private Node comeFrom; //used for pathfinding
        private double[] distFrom; //distFrom[0] = distance from pacman to this node,
        //distFrom[i] = distance from the ghost i-1 to this node

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.neighbors = new ArrayList<>();
            this.distFrom = new double[5];
        }

        public double getDistFrom(int i) {
            return distFrom[i];
        }

        public void setDistFrom(int i, double distFrom) {
            this.distFrom[i] = distFrom;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Node getComeFrom() {
            return comeFrom;
        }

        public void setComeFrom(Node comeFrom) {
            this.comeFrom = comeFrom;
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

        public double manathan(Vector2 pos) {
            return Math.abs(pos.x - x) + Math.abs(pos.y - y);
        }

        public double manathan(Node n) {
            return Math.abs(n.x - x) + Math.abs(n.y - y);
        }

        @Override
        public String toString() {
            //return "(" + x + ", " + y + ") : " + distFrom[0] + ", " + distFrom[1] + ", " + distFrom[2] + ", " + distFrom[3] + ", " + distFrom[4];
            return "(" + x + ", " + y + ")";
        }

    }

    public GhostCloaker(World world) {
        super(world);
        graph = new ArrayList<>();
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                if (world.getElement(i, j) == null || !(world.getElement(i, j) instanceof Block)) {
                    if ((world.getElement((i + 1) % 27, j) == null || world.getElement((i + 26) % 27, j) == null || !(world.getElement((i + 1) % 27, j) instanceof Block)
                            || !(world.getElement((i + 26) % 27, j) instanceof Block)) && (world.getElement(i, j - 1) == null || world.getElement(i, j + 1) == null
                            || !(world.getElement(i, j - 1) instanceof Block) || !(world.getElement(i, j + 1) instanceof Block))) {
                        graph.add(new Node(i, j));
                    }
                }
            }
        }
        for (Node n : graph) {
            char done = 0;
            char used = 0;
            int i = 0;
            if (world.getElement((n.x + 1) % 27, n.y) == null || !(world.getElement((n.x + 1) % 27, n.y) instanceof Block)) {
                used |= 1;
                if (n.addNeighbor(getNode((n.x + 1 + i) % 27, n.y))) {
                    done |= 1;
                }
            }
            if (world.getElement((n.x + 26) % 27, n.y) == null || !(world.getElement((n.x + 26) % 27, n.y) instanceof Block)) {
                used |= 2;
                if (n.addNeighbor(getNode((n.x + 26 - i) % 27, n.y))) {
                    done |= 2;
                }
            }
            if (world.getElement(n.x, n.y + 1 + i) == null || !(world.getElement(n.x, n.y + 1 + i) instanceof Block)) {
                used |= 4;
                if (n.addNeighbor(getNode(n.x, n.y + 1 + i))) {
                    done |= 4;
                }
            }
            if (world.getElement(n.x, n.y - 1 - i) == null || !(world.getElement(n.x, n.y - 1 - i) instanceof Block)) {
                used |= 8;
                if (n.addNeighbor(getNode(n.x, n.y - 1 - i))) {
                    done |= 8;
                }
            }
            i++;
            while (done != used) {
                if (((done & 1) == 0 && (used & 1) != 0) && (world.getElement((n.x + 1) % 27, n.y) == null || !(world.getElement((n.x + 1) % 27, n.y) instanceof Block))) {
                    if (n.addNeighbor(getNode((n.x + 1 + i) % 27, n.y))) {
                        done |= 1;
                    }
                }
                if (((done & 2) == 0 && (used & 2) != 0) && (world.getElement((n.x + 26) % 27, n.y) == null || !(world.getElement((n.x + 26) % 27, n.y) instanceof Block))) {
                    if (n.addNeighbor(getNode((n.x + 26 - i) % 27, n.y))) {
                        done |= 2;
                    }
                }
                if (((done & 4) == 0 && (used & 4) != 0) && (world.getElement(n.x, n.y + 1 + i) == null || !(world.getElement(n.x, n.y + 1 + i) instanceof Block))) {
                    if (n.addNeighbor(getNode(n.x, n.y + 1 + i))) {
                        done |= 4;
                    }
                }
                if (((done & 8) == 0 && (used & 8) != 0) && (world.getElement(n.x, n.y - 1 - i) == null || !(world.getElement(n.x, n.y - 1 - i) instanceof Block))) {
                    if (n.addNeighbor(getNode(n.x, n.y - 1 - i))) {
                        done |= 8;
                    }
                }
                i++;
            }
        }
    }

    @Override
    public void update(float delta) {
        boolean needUpdate;
        ArrayList<Node>[] targets;
        if (posPacman == null) {
            needUpdate = true;
        } else {
            needUpdate = false;
            ArrayList<Node> currentPos = getNearestNodes(world.getPacman().getPosition());
            if (currentPos.size() != posPacman.size()) {
                needUpdate = true;
            } else {
                for (Node n : posPacman) {
                    if (!currentPos.contains(n)) {
                        needUpdate = true;
                        break;
                    }
                }
            }
        }
        if (needUpdate) {
            updateGraph();
            targets = getTargets();/*
             for (int i = 0; i < 4; i++) {
             System.err.println(targets[i]);
             }
             System.err.println("______________________________________");*/

        }
        Iterator<Ghost> it = world.ghostsIterator();
        Ghost gh;
        while (it.hasNext()) {
            gh = it.next();
            //insert code here to flee super pacman
            if (canTurn(gh)) {
                //here find a good algorithm to affect each ghost to a different node
                //interesting nodes for the i\ieme{} ghost is stored in targets[i]
            }
        }
    }

    private Node getNode(int x, int y) {
        for (Node n : graph) {
            if (n.x == x && n.y == y) {
                return n;
            }
        }
        return null;
    }

    private ArrayList<Node> getNearestNodes(Vector2 pos) {
        ArrayList<Node> nodes = new ArrayList<>();
        if (getNode((int) pos.x, (int) pos.y) != null) {
            nodes.add(getNode((int) pos.x, (int) pos.y));
        } else {
            char used = 0;
            char done = 0;
            int i = 0;
            Node n = new Node((int) pos.x, (int) pos.y);
            if (world.getElement((n.x + 1) % 27, n.y) == null || !(world.getElement((n.x + 1) % 27, n.y) instanceof Block)) {
                used |= 1;
                if (getNode((n.x + 1 + i) % 27, n.y) != null) {
                    nodes.add(getNode((n.x + 1 + i) % 27, n.y));
                    done |= 1;
                }
            }
            if (world.getElement((n.x + 26) % 27, n.y) == null || !(world.getElement((n.x + 26) % 27, n.y) instanceof Block)) {
                used |= 2;
                if (getNode((n.x + 26 - i) % 27, n.y) != null) {
                    nodes.add(getNode((n.x + 26 - i) % 27, n.y));
                    done |= 2;
                }
            }
            if (world.getElement(n.x, n.y + 1 + i) == null || !(world.getElement(n.x, n.y + 1 + i) instanceof Block)) {
                used |= 4;
                if (getNode(n.x, n.y + 1 + i) != null) {
                    nodes.add(getNode(n.x, n.y + 1 + i));
                    done |= 4;
                }
            }
            if (world.getElement(n.x, n.y - 1 - i) == null || !(world.getElement(n.x, n.y - 1 - i) instanceof Block)) {
                used |= 8;
                if (getNode(n.x, n.y - 1 - i) != null) {
                    nodes.add(getNode(n.x, n.y - 1 - i));
                    done |= 8;
                }
            }
            i++;
            while (done != used) {
                if ((done & 1) == 0 && (world.getElement((n.x + 1) % 27, n.y) == null || !(world.getElement((n.x + 1) % 27, n.y) instanceof Block))) {
                    if (getNode((n.x + 1 + i) % 27, n.y) != null) {
                        nodes.add(getNode((n.x + 1 + i) % 27, n.y));
                        done |= 1;
                    }
                }
                if ((done & 2) == 0 && (world.getElement((n.x + 26) % 27, n.y) == null || !(world.getElement((n.x + 26) % 27, n.y) instanceof Block))) {
                    if (getNode((n.x + 26 - i) % 27, n.y) != null) {
                        nodes.add(getNode((n.x + 26 - i) % 27, n.y));
                        done |= 2;
                    }
                }
                if ((done & 4) == 0 && (world.getElement(n.x, n.y + 1 + i) == null || !(world.getElement(n.x, n.y + 1 + i) instanceof Block))) {
                    if (getNode(n.x, n.y + 1 + i) != null) {
                        nodes.add(getNode(n.x, n.y + 1 + i));
                        done |= 4;
                    }
                }
                if ((done & 8) == 0 && (world.getElement(n.x, n.y - 1 - i) == null || !(world.getElement(n.x, n.y - 1 - i) instanceof Block))) {
                    if (getNode(n.x, n.y - 1 - i) != null) {
                        nodes.add(getNode(n.x, n.y - 1 - i));
                        done |= 8;
                    }
                }
                i++;
            }
        }

        return nodes;
    }

    private void updateGraph() {
        ArrayList<Node> visited = new ArrayList();
        ArrayList<Node> toVisit = new ArrayList();
        Vector2[] positions = new Vector2[5];
        positions[0] = world.getPacman().getPosition();
        Iterator<Ghost> it = world.ghostsIterator();
        int i = 1;
        while (it.hasNext()) {
            positions[i] = it.next().getPosition();
            i++;
        }
        for (i = 0; i < 5; i++) {
            for (Node n : getNearestNodes(positions[i])) {
                n.setDistFrom(i, n.manathan(positions[i]));
                visited.add(n);
                for (Node n2 : n.getNeighbors()) {
                    if (!visited.contains(n2)) {
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manathan(n2));
                        toVisit.add(n2);
                    } else if (n2.getDistFrom(i) > n.getDistFrom(i) + n.manathan(n2)) {
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manathan(n2));
                        toVisit.add(n2);
                        visited.remove(n2);
                    }
                }
                toVisit.remove(n);
            }
            Node n;
            while (!toVisit.isEmpty()) {
                n = toVisit.get(0);
                visited.add(n);
                for (Node n2 : n.getNeighbors()) {
                    if (!visited.contains(n2)) {
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manathan(n2));
                        if (!toVisit.contains(n2)) {
                            toVisit.add(n2);
                        }
                    } else if (n2.getDistFrom(i) > n.getDistFrom(i) + n.manathan(n2)) {
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manathan(n2));
                        if (!toVisit.contains(n2)) {
                            toVisit.add(n2);
                        }
                        visited.remove(n2);
                    }
                }
                toVisit.remove(0);
            }
            visited = new ArrayList<>();
        }
    }

    private boolean canTurn(Ghost g) {
        return g.getPosition().x - (int) g.getPosition().x < epsilon && g.getPosition().y - (int) g.getPosition().y < epsilon;
    }

    private ArrayList<Node>[] getTargets() {
        ArrayList<Node>[] targets = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            targets[i] = new ArrayList<>();
        }
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> toVisit;
        Node n;
        for (int i = 1; i < 5; i++) {
            toVisit = getNearestNodes(world.getPacman().getPosition());
            while (!toVisit.isEmpty()) {
                n = toVisit.get(0);
                if (n.getDistFrom(0) > 0.9 * n.getDistFrom(i)) {
                    targets[i - 1].add(n);
                } else {
                    for (Node n2 : n.getNeighbors()) {
                        if (!visited.contains(n2) && !toVisit.contains(n2)) {
                            toVisit.add(n2);
                        }
                    }
                }
                visited.add(n);
                toVisit.remove(0);
            }
            visited = new ArrayList<>();
        }
        return targets;
    }
}
