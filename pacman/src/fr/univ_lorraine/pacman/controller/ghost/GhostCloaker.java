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
    private Node[] lastPos;
    private ArrayList<Direction>[] paths;
    private ArrayList<Direction>[] flee;

    private class Direction {

        private int x, y, dir;

        public static final int TOP = 0;
        public static final int DOWN = 1;
        public static final int RIGHT = 2;
        public static final int LEFT = 3;

        public Direction(int x, int y, int dir) {
            this.dir = dir;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getDir() {
            return dir;
        }

        public String toString() {
            String ret = "(" + x + ", " + y + ") : ";
            switch (dir) {
                case LEFT:
                    ret += "LEFT";
                    break;
                case RIGHT:
                    ret += "RIGHT";
                    break;
                case TOP:
                    ret += "TOP";
                    break;
                default:
                    ret += "DOWN";
            }
            return ret;
        }
    }

    private class Node {

        private int x, y;
        private ArrayList<Node> neighbors;
        private Node comeFrom; //used for pathfinding
        private double weight;
        private double[] distFrom; //distFrom[0] = distance from pacman to this node,
        //distFrom[i] = distance from the ghost i-1 to this node

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.neighbors = new ArrayList<>();
            this.distFrom = new double[5];
        }

        public Node(Vector2 pos) {
            this.x = (int) pos.x;
            this.y = (int) pos.y;
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

        public double manhattan(Vector2 pos) {
            return Math.abs(pos.x - x) + Math.abs(pos.y - y);
        }

        public double manhattan(Node n) {
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
        lastPos = new Node[5];
        lastPos[0] = new Node(world.getPacman().getPosition());
        Iterator<Ghost> it = world.ghostsIterator();
        paths = new ArrayList[4];
        flee = new ArrayList[4];
        int i = 1;
        while (it.hasNext()) {
            lastPos[i++] = new Node(it.next().getPosition());
        }

        for (i = 0; i < world.getWidth(); i++) {
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
            i = 0;
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
        updateGraph();
    }

    @Override
    public void update(float delta) {
        boolean needUpdate = false;
        Iterator<Ghost> it = world.ghostsIterator();
        Ghost gh;
        Node pac = new Node(world.getPacman().getPosition());
        if (!lastPos[0].equals(pac)) {
            lastPos[0] = pac;
            needUpdate = true;
        }
        int i = 1;
        while (it.hasNext()) {
            gh = it.next();
            if (!lastPos[i].equals(new Node(gh.getPosition()))) {
                lastPos[i] = new Node(gh.getPosition());
                needUpdate = true;
            }
            i++;
        }

        Node[] targets = null;
        if (needUpdate) {
            updateGraph();
            for (int k = 0; i < 4; i++) {
                paths[k] = null;
            }
        }
        i = 1;
        Direction nextDir;
        boolean turn;
        it = world.ghostsIterator();
        while (it.hasNext()) {
            gh = it.next();
            turn = canTurn(gh);
            nextDir = null;
            if (gh.getState() == Ghost.State.DEAD) {
                if (flee[i - 1] == null || flee[i - 1].isEmpty()) {
                    flee[i - 1] = goTo(new Node(gh.getPosition()), new Node(13, 18));
                }
                if(flee[i-1] != null && !flee[i-1].isEmpty()){
                nextDir = flee[i - 1].get(0);
                flee[i - 1].remove(0);
                turn = canTurn(gh, nextDir.dir);
                }
                System.out.println("Ghost " + (i - 1) + " : goto base");
            } else if (gh.getState() == Ghost.State.HUNTED) {
                flee[i - 1] = null;
                double dist = 0, tmp;
                Node dest = null;
                ArrayList<Node> neighbors = getNearestNodes(gh.getPosition());
                if (neighbors.size() == 1) {
                    neighbors = neighbors.get(0).getNeighbors();
                }
                for (Node n : neighbors) {
                    tmp = n.getDistFrom(0) + n.manhattan(gh.getPosition());
                    if ((tmp > dist || tmp > 3) && !isPathBlocked(gh, n)) {
                        dist = tmp;
                        dest = n;
                        if (tmp > 3) {
                            break;
                        }
                    }
                }
                nextDir = goTo(new Node(gh.getPosition()), dest).get(0);
                System.out.println("Ghost " + (i - 1) + " (fleeing): goto " + dest);
                /**/

            } else {
                Node target;
                flee[i - 1] = null;
                if (paths[i - 1] == null || paths[i - 1].isEmpty()) {
                    if (targets == null || targets[i - 1] == null) {
                        targets = affectTargets();
                    }
                    target = targets[i - 1] == null ? pac : targets[i - 1];
                    paths[i - 1] = goTo(new Node(gh.getPosition()), target);
                    System.out.println("Ghost " + (i - 1) + " : goto " + target);
                }
                if (paths[i - 1] != null && !paths[i - 1].isEmpty()) {
                    nextDir = paths[i - 1].get(0);
                    paths[i - 1].remove(0);

                }
            }
            if (turn && nextDir != null) {
                if (lastPos[i].x == nextDir.x && lastPos[i].y == nextDir.y) {
                    switch (nextDir.dir) {
                        case Direction.DOWN:
                            gh.turnDown();
                            break;
                        case Direction.TOP:
                            gh.turnUp();
                            break;
                        case Direction.RIGHT:
                            gh.turnRight();
                            break;
                        default:
                            gh.turnLeft();
                    }
                }
            }
            gh.update(delta);
            i++;
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
                if ((used & 1) > 0 && (done & 1) == 0 && (world.getElement((n.x + 1) % 27, n.y) == null || !(world.getElement((n.x + 1) % 27, n.y) instanceof Block))) {
                    if (getNode((n.x + 1 + i) % 27, n.y) != null) {
                        nodes.add(getNode((n.x + 1 + i) % 27, n.y));
                        done |= 1;
                    }
                }
                if ((used & 2) > 0 && (done & 2) == 0 && (world.getElement((n.x + 26) % 27, n.y) == null || !(world.getElement((n.x + 26) % 27, n.y) instanceof Block))) {
                    if (getNode((n.x + 26 - i) % 27, n.y) != null) {
                        nodes.add(getNode((n.x + 26 - i) % 27, n.y));
                        done |= 2;
                    }
                }
                if ((used & 4) > 0 && (done & 4) == 0 && (world.getElement(n.x, n.y + 1 + i) == null || !(world.getElement(n.x, n.y + 1 + i) instanceof Block))) {
                    if (getNode(n.x, n.y + 1 + i) != null) {
                        nodes.add(getNode(n.x, n.y + 1 + i));
                        done |= 4;
                    }
                }
                if ((used & 8) > 0 && (done & 8) == 0 && (world.getElement(n.x, n.y - 1 - i) == null || !(world.getElement(n.x, n.y - 1 - i) instanceof Block))) {
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

    private ArrayList<Node> getNearestNodes(int x, int y) {
        return getNearestNodes(new Vector2(x, y));
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
                n.setDistFrom(i, n.manhattan(positions[i]));
                visited.add(n);
                for (Node n2 : n.getNeighbors()) {
                    if (!visited.contains(n2)) {
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manhattan(n2));
                        toVisit.add(n2);
                    } else if (n2.getDistFrom(i) > n.getDistFrom(i) + n.manhattan(n2)) {
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manhattan(n2));
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
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manhattan(n2));
                        if (!toVisit.contains(n2)) {
                            toVisit.add(n2);
                        }
                    } else if (n2.getDistFrom(i) > n.getDistFrom(i) + n.manhattan(n2)) {
                        n2.setDistFrom(i, n.getDistFrom(i) + n.manhattan(n2));
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

    private boolean canTurn(Ghost g, int dir) {
        if (dir == Direction.DOWN || dir == Direction.TOP) {
            return g.getPosition().x - (int) g.getPosition().x < epsilon;
        } else {
            return g.getPosition().y - (int) g.getPosition().y < epsilon;
        }
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

    private ArrayList<Direction> goTo(Node from, Node to) {
        ArrayList<Direction> path = new ArrayList<>();
        Node currentNode;
        ArrayList<Node> toVisit = getNearestNodes(from.x, from.y);
        if (toVisit.containsAll(getNearestNodes(to.x, to.y))) {
            if (to.equals(from)) {
                return null;
            }
            to.comeFrom = from;
        } else {
            from.comeFrom = null;
            for (Node n : graph) {
                n.weight = -1;
            }
            if (toVisit.size() == 1) {
                toVisit.get(0).comeFrom = null;
                toVisit.get(0).weight = 0;
            } else {
                for (Node n : toVisit) {
                    n.comeFrom = from;
                    n.weight = n.manhattan(from);
                }
            }
            to.weight = -1;
            to.comeFrom = null;
            while (!toVisit.isEmpty()) {
                currentNode = toVisit.get(0);
                for (Node tmp : currentNode.getNeighbors()) {
                    if (tmp.weight > currentNode.weight + tmp.manhattan(currentNode) || tmp.weight < 0) {
                        tmp.weight = currentNode.weight + tmp.manhattan(currentNode);
                        tmp.comeFrom = currentNode;
                        if (!toVisit.contains(tmp)) {
                            toVisit.add(tmp);
                        }
                    }
                }
                toVisit.remove(0);
            }
            for (Node tmp : getNearestNodes(to.x, to.y)) {
                if (to.weight > tmp.weight + tmp.manhattan(to) || to.weight < 0) {
                    to.weight = tmp.weight + tmp.manhattan(to);
                    to.comeFrom = tmp;
                }
            }
        }
        currentNode = to;
        while (currentNode.getComeFrom() != null) {
            if (currentNode.getX() - currentNode.getComeFrom().getX() < 0) {
                path.add(0, new Direction(currentNode.getComeFrom().getX(), currentNode.getComeFrom().getY(), Direction.LEFT));
            } else if (currentNode.getX() - currentNode.getComeFrom().getX() > 0) {
                path.add(0, new Direction(currentNode.getComeFrom().getX(), currentNode.getComeFrom().getY(), Direction.RIGHT));
            } else if (currentNode.getY() - currentNode.getComeFrom().getY() < 0) {
                path.add(0, new Direction(currentNode.getComeFrom().getX(), currentNode.getComeFrom().getY(), Direction.DOWN));
            } else {
                path.add(0, new Direction(currentNode.getComeFrom().getX(), currentNode.getComeFrom().getY(), Direction.TOP));
            }
            currentNode = currentNode.getComeFrom();
        }
        return path;
    }

    private boolean isPathBlocked(Ghost gh, Node to) {
        Node pacman = new Node((int) world.getPacman().getPosition().x, (int) world.getPacman().getPosition().y);
        ArrayList<Node> neighbors = pacman.getNeighbors();
        if (neighbors.size() == 1) {
            neighbors = neighbors.get(0).getNeighbors();
            neighbors.add(pacman);
        }
        return neighbors.contains(to) && neighbors.containsAll(getNearestNodes(gh.getPosition()));
    }

    private Node[] affectTargets() {
        Node[] ret = new Node[4];
        ArrayList<Node>[] targets = getTargets();
        double min = 500;
        double tmp;
        boolean[] hasResult = new boolean[3];
        for (Node t1 : targets[0]) {
            hasResult[0] = false;
            for (Node t2 : targets[1]) {
                if (!t1.equals(t2)) {
                    hasResult[0] = true;
                    hasResult[1] = false;
                    for (Node t3 : targets[2]) {
                        if (!t3.equals(t2) && !t3.equals(t1)) {
                            hasResult[1] = true;
                            hasResult[2] = false;
                            for (Node t4 : targets[3]) {
                                if (!t4.equals(t3) && !t4.equals(t2) && !t3.equals(t1)) {
                                    hasResult[2] = true;
                                    tmp = t1.getDistFrom(0) + t2.getDistFrom(0) + t3.getDistFrom(0) + t4.getDistFrom(0);
                                    tmp /= 4;
                                    if (tmp < min) {
                                        min = tmp;
                                        ret[0] = t1;
                                        ret[1] = t2;
                                        ret[2] = t3;
                                        ret[3] = t4;
                                    }
                                }
                            }
                            if (!hasResult[2]) {
                                tmp = t1.getDistFrom(0) + t2.getDistFrom(0) + t3.getDistFrom(0);
                                tmp /= 3;
                                if (tmp < min) {
                                    min = tmp;
                                    ret[0] = t1;
                                    ret[1] = t2;
                                    ret[2] = t3;
                                    ret[3] = null;
                                }
                            }
                        }
                    }
                    if (!hasResult[1]) {
                        tmp = t1.getDistFrom(0) + t2.getDistFrom(0);
                        tmp /= 2;
                        if (tmp < min) {
                            min = tmp;
                            ret[0] = t1;
                            ret[1] = t2;
                            ret[2] = null;
                            ret[3] = null;
                        }
                    }
                }
            }
            if (!hasResult[0] && t1.getDistFrom(0) < min) {
                min = t1.getDistFrom(0);
                ret[0] = t1;
                ret[1] = null;
                ret[2] = null;
                ret[3] = null;
            }
        }
        Iterator<Ghost> it = world.ghostsIterator();
        Ghost gh;
        Vector2 pacman = world.getPacman().getPosition();
        for (int i = 0; i < 4; i++) {
            gh = it.next();
            if (ret[i] != null && ret[i].manhattan(gh.getPosition()) >= Math.abs(gh.getPosition().x - pacman.x) + Math.abs(gh.getPosition().y - pacman.y)) {
                ret[i] = null;
            }
        }
        return ret;
    }
}
