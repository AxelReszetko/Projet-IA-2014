package fr.univ_lorraine.pacman.controller.ghost;

import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;

import fr.univ_lorraine.pacman.controller.GhostController;
import fr.univ_lorraine.pacman.model.Block;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.World;
import java.util.ArrayList;

public class GhostShadow extends GhostController {

    private static final double epsilon = 0.2;
    private Node lastPos;
    private ArrayList<Direction>[] paths;
    private ArrayList<Node> intersections;

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

        public Node(Vector2 pos) {
            this.x = (int) pos.x;
            this.y = (int) pos.y;
            this.dist = 0;
            this.comeFrom = null;
        }

        public int minDistanceTo(Node n2) {
            return Math.abs(this.x - n2.x) + Math.abs(this.y - n2.y);
        }

        public int minDistanceTo(Vector2 n2) {
            return Math.abs(this.x - (int) n2.x) + Math.abs(this.y - (int) n2.y);
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
    }

    public GhostShadow(World world) {
        super(world);
        paths = new ArrayList[4];
        lastPos = null;
        intersections = null;
    }

    @Override
    public void update(float delta) {
        Iterator<Ghost> iterGhost = world.ghostsIterator();
        int i = 0;
        Ghost gh;
        if (lastPos == null || lastPos.minDistanceTo(world.getPacman().getPosition()) > 3) {
            lastPos = new Node(world.getPacman().getPosition());
            intersections = reachableIntersectionsFrom(lastPos);
            System.err.println("intersections (" + lastPos + ") : " + intersections);
        }
        while (iterGhost.hasNext()) {
            gh = iterGhost.next();
            if (canTurn(gh)) {
                if (paths[i] == null || paths[i].isEmpty() || !intersections.contains(getDestination(paths[i]))) {
                    int nearestInt = -1;
                    int distMin = 500;
                    Node pos = new Node(gh.getPosition());
                    for (int j = 0; j < intersections.size(); j++) {
                        Ghost otherGhost = ghostHeadingTo(intersections.get(j));
                        if ((otherGhost == null || otherGhost != gh) && pos.minDistanceTo(intersections.get(j)) < distMin) {
                            distMin = pos.minDistanceTo(intersections.get(j));
                            nearestInt = j;
                        }
                    }
                    if (distMin < pos.minDistanceTo(world.getPacman().getPosition())) {
                        paths[i] = goTo(pos, new Node(world.getPacman().getPosition()));
                    } else {
                        paths[i] = goTo(pos, intersections.get(nearestInt));
                    }
                    //System.err.println("ghost " + i + " : " + getDestination(paths[i]));
                }
                int dir;
                if (paths[i] != null && paths[i].get(0).x == (int) gh.getPosition().x && paths[i].get(0).y == (int) gh.getPosition().y) {
                    dir = paths[i].get(0).dir;
                    paths[i].remove(0);
                    switch (dir) {
                        case Direction.DOWN:
                            gh.turnDown();
                            break;
                        case Direction.LEFT:
                            gh.turnLeft();
                            break;
                        case Direction.RIGHT:
                            gh.turnRight();
                            break;
                        case Direction.TOP:
                            gh.turnUp();
                            break;
                    }
                }
            }
            gh.update(delta);
            i++;
        }
    }

    private ArrayList<Direction> goTo(Node from, Node to) {
        if(from.equals(to))
            return null;
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> toVisit = new ArrayList<>();
        toVisit.add(from);
        int x1, y1;
        GameBasicElement e = null;
        while (!toVisit.isEmpty()) {
            x1 = toVisit.get(0).x;
            y1 = toVisit.get(0).y;
            e = world.getElement((x1 + 26) % 27, y1);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node((x1 + 26) % 27, y1))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node((x1 + 26) % 27, y1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (node.equals(to)) {
                        break;
                    }
                } else if (visited.get(i).dist > toVisit.get(0).dist + 1) {
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement((x1 + 1) % 27, y1);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node((x1 + 1) % 27, y1))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node((x1 + 1) % 27, y1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (node.equals(to)) {
                        break;
                    }
                } else if (visited.get(i).dist > toVisit.get(0).dist + 1) {
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x1, y1 - 1);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node(x1, y1 - 1))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node(x1, y1 - 1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (node.equals(to)) {
                        break;
                    }
                } else if (visited.get(i).dist > toVisit.get(0).dist + 1) {
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x1, y1 + 1);
            if (e == null || !(e instanceof Block)) {
                int i;
                for (i = 0; i < visited.size(); i++) {
                    if (visited.get(i).equals(new Node(x1, y1 + 1))) {
                        break;
                    }
                }
                if (i >= visited.size()) {
                    Node node = new Node(x1, y1 + 1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if (node.equals(to)) {
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
        if(toVisit.isEmpty())
            throw new RuntimeException("error goTo " + from + " ==> " + to);
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
        return path;
    }

    private boolean canTurn(Ghost g) {
        return g.getPosition().x - (int) g.getPosition().x < epsilon && g.getPosition().y - (int) g.getPosition().y < epsilon;
    }

    private ArrayList<Node> reachableIntersectionsFrom(Node from) {
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> toVisit = new ArrayList<>();
        ArrayList<Node> intersections = new ArrayList<>();
        toVisit.add(from);
        int x1, y1;
        GameBasicElement e = null;
        while (!toVisit.isEmpty()) {
            x1 = toVisit.get(0).x;
            y1 = toVisit.get(0).y;
            e = world.getElement((x1 + 26) % 27, y1);
            if (e == null || !(e instanceof Block)) {
                Node node = new Node((x1 + 26) % 27, y1);
                if (!visited.contains(node)) {
                    if (isIntersection(node)) {
                        intersections.add(node);
                    } else {
                        toVisit.add(node);
                    }
                }
            }
            e = world.getElement((x1 + 1) % 27, y1);
            if (e == null || !(e instanceof Block)) {
                Node node = new Node((x1 + 1) % 27, y1);
                if (!visited.contains(node)) {
                    if (isIntersection(node)) {
                        intersections.add(node);
                    } else {
                        toVisit.add(node);
                    }
                }
            }
            e = world.getElement(x1, y1 - 1);
            if (e == null || !(e instanceof Block)) {
                Node node = new Node(x1, y1 - 1);
                if (!visited.contains(node)) {
                    if (isIntersection(node)) {
                        intersections.add(node);
                    } else {
                        toVisit.add(node);
                    }
                }
            }
            e = world.getElement(x1, y1 + 1);
            if (e == null || !(e instanceof Block)) {
                Node node = new Node(x1, y1 + 1);
                if (!visited.contains(node)) {
                    if (isIntersection(node)) {
                        intersections.add(node);
                    } else {
                        toVisit.add(node);
                    }
                }
            }
            visited.add(toVisit.get(0));
            toVisit.remove(0);
        }
        return intersections;
    }

    private boolean isIntersection(Node n) {
        int i = 0;
        GameBasicElement e = world.getElement((n.x + 1) % 27, n.y);
        if (e == null || !(e instanceof Block)) {
            i++;
        }
        e = world.getElement((n.x + 26) % 27, n.y);
        if (e == null || !(e instanceof Block)) {
            i++;
        }
        e = world.getElement(n.x, n.y + 1);
        if (e == null || !(e instanceof Block)) {
            i++;
        }
        e = world.getElement(n.x, n.y - 1);
        if (e == null || !(e instanceof Block)) {
            i++;
        }
        return i > 2;
    }

    private Ghost ghostHeadingTo(Node to) {

        Iterator<Ghost> iterGhost = world.ghostsIterator();
        Ghost ghost;
        while (iterGhost.hasNext()) {
            ghost = iterGhost.next();
            if (to.equals(new Node(ghost.getPosition()))) {
                return ghost;
            }
        }
        int i;
        for (i = 0; i < paths.length; i++) {
            if (paths[i] != null && !paths[i].isEmpty() && getDestination(paths[i]).equals(to)) {
                break;
            }
        }
        if (i < paths.length) {
            int j = 0;
            Ghost gh;
            while (iterGhost.hasNext()) {
                gh = iterGhost.next();
                if (i == j) {
                    return gh;
                }
                j++;
            }
        }
        return null;
    }

    private Node getDestination(ArrayList<Direction> path) {
        Direction last = path.get(path.size() - 1);
        switch (last.dir) {
            case Direction.LEFT:
                return new Node((last.x + 26) % 27, last.y);
            case Direction.RIGHT:
                return new Node((last.x + 1) % 27, last.y);
            case Direction.TOP:
                return new Node(last.x, last.y + 1);
            default:
                return new Node(last.x, last.y - 1);
        }
    }
}
