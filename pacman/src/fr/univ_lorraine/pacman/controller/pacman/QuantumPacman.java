package fr.univ_lorraine.pacman.controller.pacman;

import com.badlogic.gdx.math.Vector2;
import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.Block;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.World;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Fabien
 */
public class QuantumPacman extends PacmanController {

    private Pacman pac;
    private boolean prem;
    private int dir;
    private ArrayList<Direction> path;
    private static final double epsilon = 0.2;

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

        private int dist;
        private Node comeFrom;
        private int x, y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.dist = 0;
            this.comeFrom = null;
        }

        public int getDist() {
            return dist;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }

        public Node getComeFrom() {
            return comeFrom;
        }

        public void setComeFrom(Node comeFrom) {
            this.comeFrom = comeFrom;
            this.dist = comeFrom.dist + 1;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof Node)) {
                return false;
            }
            Node other = (Node) o;
            return other.x == x && other.y == y;
        }

        public double manhattan(Vector2 pos) {
            return Math.abs(x - pos.x) + Math.abs(y - pos.y);
        }
    }

    public QuantumPacman(World world) {
        super(world);
        System.out.println("PacmanWTFCreator");
        pac = world.getPacman();
        path = null;
    }

    @Override
    public void update(float delta) {
        if (canTurn()) {
            if (path == null || path.isEmpty()) {
                path = findClosestPellet((int) pac.getPosition().x, (int) pac.getPosition().y);
            }/**/
            if (path.get(0).x == (int) pac.getPosition().x && path.get(0).y == (int) pac.getPosition().y) {
                dir = path.get(0).dir;
                path.remove(0);
                switch (dir) {
                    case Direction.DOWN:
                        pac.turnDown();
                        break;
                    case Direction.LEFT:
                        pac.turnLeft();
                        break;
                    case Direction.RIGHT:
                        pac.turnRight();
                        break;
                    case Direction.TOP:
                        pac.turnUp();
                        break;
                }
            }

        }
        double quantumTeleportation = 0;
        Iterator<Ghost> it = world.ghostsIterator();
        Node pos = new Node((int) pac.getPosition().x, (int) pac.getPosition().y);
        double dist = 0;
        while (it.hasNext()) {
            if (pos.manhattan(it.next().getPosition()) < dist + 3) {
                dist += 2;
            }
        }
        if (dist > 0) {
            GameBasicElement e;
            int i;
            switch (dir) {
                case Direction.DOWN:
                    for (i = 1;; i++) {
                        e = world.getElement(pos.x, (pos.y + i * 30) % 31);
                        quantumTeleportation = pac.getPosition().y - (double) ((pos.y + i * 30) % 31);
                        if ((e == null || !(e instanceof Block)) && Math.abs(quantumTeleportation) > dist) {
                            if (quantumTeleportation < 0) {
                                pac.turnUp();
                                quantumTeleportation *= -1;
                            }
                            break;
                        }
                    }
                    break;
                case Direction.LEFT:
                    for (i = 1;; i++) {
                        e = world.getElement((pos.x + 26 * i) % 27, pos.y);
                        quantumTeleportation = pac.getPosition().x - (double) ((pos.x + 26 * i) % 27);
                        if ((e == null || !(e instanceof Block)) && Math.abs(quantumTeleportation) > dist) {
                            if (quantumTeleportation < 0) {
                                pac.turnRight();
                                quantumTeleportation *= -1;
                            }
                            break;
                        }
                    }
                    break;
                case Direction.RIGHT:
                    for (i = 1;; i++) {
                        e = world.getElement((pos.x + i) % 27, pos.y);
                        quantumTeleportation = (double) ((pos.x + i) % 27) - pac.getPosition().x;
                        if ((e == null || !(e instanceof Block)) && Math.abs(quantumTeleportation) > dist) {
                            if (quantumTeleportation < 0) {
                                pac.turnLeft();
                                quantumTeleportation *= -1;
                            }
                            break;
                        }
                    }
                    break;
                default:
                    for (i = 1;; i++) {
                        e = world.getElement(pos.x, (pos.y + i) % 31);
                        quantumTeleportation = (double) ((pos.y + i) % 31) - pac.getPosition().y;
                        if ((e == null || !(e instanceof Block)) && Math.abs(quantumTeleportation) > dist) {
                            if (quantumTeleportation < 0) {
                                pac.turnDown();
                                quantumTeleportation *= -1;
                            }
                            break;
                        }
                    }
                    break;
            }
            delta = (float) (quantumTeleportation / 8.0);
            path = null;
        }
        pac.update(delta);

    }

    private ArrayList<Direction> findClosestPellet(int x, int y) {
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> toVisit = new ArrayList<>();
        toVisit.add(new Node(x, y));
        GameBasicElement e = null;
        while (!toVisit.isEmpty()) {
            x = toVisit.get(0).x;
            y = toVisit.get(0).y;
            e = world.getElement((x + 26) % 27, y);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node((x + 26) % 27, y))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node((x + 26) % 27, y);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (e != null) {
                        break;
                    }
                } else if (visited.get(i).dist > toVisit.get(0).dist + 1) {
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement((x + 1) % 27, y);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node((x + 1) % 27, y))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node((x + 1) % 27, y);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (e != null) {
                        break;
                    }
                } else if (visited.get(i).dist > toVisit.get(0).dist + 1) {
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x, y - 1);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node(x, y - 1))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node(x, y - 1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (e != null) {
                        break;
                    }
                } else if (visited.get(i).dist > toVisit.get(0).dist + 1) {
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x, y + 1);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node(x, y + 1))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node(x, y + 1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (e != null) {
                        break;
                    }
                } else if (visited.get(i).dist > toVisit.get(0).dist + 1) {
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }

            }
            visited.add(toVisit.get(0));
            toVisit.remove(0);
        }
        ArrayList<Direction> path = new ArrayList<>();
        Node node;
        if (e != null && !(e instanceof Block)) {
            node = toVisit.get(toVisit.size() - 1);
            while (node.getComeFrom() != null) {
                if (node.getX() - node.getComeFrom().getX() < 0) {
                    path.add(0, new Direction(node.getComeFrom().getX(), node.getComeFrom().getY(), Direction.LEFT));
                } else if (node.getX() - node.getComeFrom().getX() > 0) {
                    path.add(0, new Direction(node.getComeFrom().getX(), node.getComeFrom().getY(), Direction.RIGHT));
                } else if (node.getY() - node.getComeFrom().getY() < 0) {
                    path.add(0, new Direction(node.getComeFrom().getX(), node.getComeFrom().getY(), Direction.DOWN));
                } else {
                    path.add(0, new Direction(node.getComeFrom().getX(), node.getComeFrom().getY(), Direction.TOP));
                }
                node = node.getComeFrom();
            }
        } else {

        }
        return path;
    }

    private boolean canTurn() {
        return pac.getPosition().x - (int) pac.getPosition().x < epsilon && pac.getPosition().y - (int) pac.getPosition().y < epsilon;
    }
}
