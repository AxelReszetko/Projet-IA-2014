package fr.univ_lorraine.pacman.controller.pacman;

import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.Block;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.SuperPellet;
import fr.univ_lorraine.pacman.model.World;
import fr.univ_lorraine.pacman.model.GameMoveableBasicElement.State;

import java.util.ArrayList;
import java.util.Iterator;

public class PacmanWTF extends PacmanController {

    private Pacman pac;
    private int dir;
    private ArrayList<Direction> path;
    private static final double epsilon = 0.2;
    private long chrono ; 
    private long temps;
    private boolean hunt;

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

    private class Node{
        private int dist;
        private int threat;
        private Node comeFrom;
        private int x, y;
        
        public Node(int x, int y){
            this.x = x;
            this.y = y;
            this.dist = 0;
            this.threat = 0;
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
        public double manhattan(Node n) {
            return Math.abs(n.x - x) + Math.abs(n.y - y);
        }
        
        @Override
        public String toString(){
            return "(" + x + ", " + y + ")";
        }
        @Override
        public boolean equals(Object o){
            if(o == null || !(o instanceof Node))
                return false;
            Node other = (Node)o;
            return other.x == x && other.y == y;
        }
        
        public boolean isInArrayList(ArrayList<Node> al){
        	for(int i=0;i<al.size();i++){
        		if (al.get(i).x==this.x&&al.get(i).y==this.y) return true;
        	}
        	return false;
        }
    }
    public PacmanWTF(World world) {
        super(world);
        System.out.println("PacmanWTFCreator");
        pac = world.getPacman();
        path = null;
        chrono=temps=0;
        hunt=false;
        lastHunt=java.lang.System.currentTimeMillis();
        // TODO Auto-generated constructor stub
    }
    double lastHunt;
    public boolean stillSuperPellet(){
    	if(world.getElement(25, 27)!=null)return true;
    	if(world.getElement(1, 27)!=null)return true;
    	if(world.getElement(25, 7)!=null)return true;
    	if(world.getElement(1, 7)!=null)return true;
    	return false;
    }
    
    public boolean isSuperPellet(Node n){
    	int x=n.x,y=n.y;
    	if(x==25&&y==27&&world.getElement(25, 27)!=null)return true;
    	if(x==1&&y==27&&world.getElement(1, 27)!=null)return true;
    	if(x==25&&y==7&&world.getElement(25, 7)!=null)return true;
    	if(x==1&&y==7&&world.getElement(1, 7)!=null)return true;
    	return false;
    }
    
    public int closeSuperPellet(){    	
    	int dist=900;
    	if(stillSuperPellet()){
    		Node pacm = new Node ((int)pac.getPosition().x,(int)pac.getPosition().y);
    		Node tmpN = new Node(25,17);
    		if(isSuperPellet(tmpN)) dist= (int) pacm.manhattan(tmpN);
    		tmpN=new Node(1,27);
    		if(isSuperPellet(tmpN)&&dist>(int) pacm.manhattan(tmpN)) dist =(int) pacm.manhattan(tmpN);
    		tmpN=new Node(1,7);
    		if(isSuperPellet(tmpN)&&dist>(int) pacm.manhattan(tmpN)) dist =(int) pacm.manhattan(tmpN);
    		tmpN=new Node(25,7);
    		if(isSuperPellet(tmpN)&&dist>(int) pacm.manhattan(tmpN)) dist =(int) pacm.manhattan(tmpN);
    	}
    	return dist;
    }
    int dirPac;
    @Override
    public void update(float delta) {
    	int distGhost = 3;
    	int distSP=15;
    	int time =7000;
    	if(hunt&&pac.getState()==State.HUNTING) {
    		this.temps = java.lang.System.currentTimeMillis() - chrono ;
    		lastHunt=0;    	
    	}
    	if(hunt &&pac.getState()!=State.HUNTING){
			this.hunt=false;
			this.temps = 0 ;
			this.lastHunt=java.lang.System.currentTimeMillis();
			
    	}
    	if(pac.getState()==State.HUNTING&&!hunt){
			this.hunt=true;			
			this.chrono = java.lang.System.currentTimeMillis() ;				
		}
    	if(detectGhost(2)){
    		System.out.println("PACMAN ====== Go To Backward! ======");
    		switch (dirPac) {
            case Direction.DOWN:
            	dirPac=0;
                pac.turnUp();
                break;
            case Direction.LEFT:
            	dirPac=2;
                pac.turnRight();
                break;
            case Direction.RIGHT:
            	dirPac=3;
                pac.turnLeft();
                break;
            case Direction.TOP:
            	dirPac=1;
                pac.turnDown();
                break;
                }
    		path=null;
    	}
    	
    	
        if (canTurn()) {
        	if ((path == null || path.isEmpty()) && !hunt && java.lang.System.currentTimeMillis()-lastHunt>time && stillSuperPellet() && closeSuperPellet()<distSP){
        		path=findClosestSuperPellet((int) pac.getPosition().x, (int) pac.getPosition().y, distGhost);
        		if(path!=null&&!path.isEmpty())System.out.println("PACMAN ====== Go To SuperPellet! ======");
        	}
        	if (path == null || path.isEmpty()) {
                path = findClosestPellet((int) pac.getPosition().x, (int) pac.getPosition().y, distGhost);
        		if(path!=null&&!path.isEmpty())System.out.println("PACMAN ====== Go To Pellet with ghost avoid! ======");
            }/**/
            if (path == null || path.isEmpty()) {
            	path = findClosestPellet((int) pac.getPosition().x, (int) pac.getPosition().y, 0);
        		if(path!=null&&!path.isEmpty())System.out.println("PACMAN ====== Go To Pellet without ghost avoid! ======");
        		else {pac.update(delta);return;}
            }
            if (path.get(0).x == (int) pac.getPosition().x && path.get(0).y == (int) pac.getPosition().y) {
                dir = path.get(0).dir;
                path.remove(0);
                switch (dir) {
                    case Direction.DOWN:
                    	dirPac=1;
                        pac.turnDown();
                        break;
                    case Direction.LEFT:
                    	dirPac=3;
                        pac.turnLeft();
                        break;
                    case Direction.RIGHT:
                    	dirPac=2;
                        pac.turnRight();
                        break;
                    case Direction.TOP:
                    	dirPac=0;
                        pac.turnUp();
                        break;
                }
            }

        }
        pac.update(delta);
    }

    public boolean detectGhost(int i){
    	Node pacm = new Node ((int)pac.getPosition().x,(int)pac.getPosition().y);
    	Node fant;
    	int x,y;
    	switch (dirPac){
        	case Direction.DOWN:
        		y=1;
        		for(int j=0;j<i;j++){
        			fant=new Node(pacm.x,pacm.y-y);
        			if(isGhost(fant)) return true;
        		}
        		break;
        	case Direction.LEFT:
        		x=1;
    			for(int j=0;j<i;j++){
    				fant=new Node(pacm.x-x,pacm.y);
    				if(isGhost(fant)) return true;
    			}
        		break;
        	case Direction.RIGHT:
	    		x=1;
				for(int j=0;j<i;j++){
					fant=new Node(pacm.x+x,pacm.y);
					if(isGhost(fant)) return true;
				}
        		break;
        	case Direction.TOP:
        		y=1;
        		for(int j=0;j<i;j++){
        			fant=new Node(pacm.x,pacm.y+y);
        			if(isGhost(fant)) return true;
        		}
        		break;
    	}
    	return false;
    }
    
    
    private ArrayList<Direction> findClosestPellet(int x, int y, int distGhost) {
    	initGhostsNeighbors(distGhost);
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> toVisit = new ArrayList<>();
        toVisit.add(new Node(x,y));
        GameBasicElement e = null;
        while(!toVisit.isEmpty()){
            x = toVisit.get(0).x;
            y = toVisit.get(0).y;
            e = world.getElement((x + 26)%27, y);
            if((e == null || !(e instanceof Block)) && !isGhost((x + 26)%27, y)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node((x + 26)%27, y))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node((x + 26)%27, y);
                    node.setComeFrom(toVisit.get(0));
                    node.setDist(toVisit.get(0).dist + 1);
                    toVisit.add(node);
                    if(e != null)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement((x+1)%27, y);
            if((e == null || !(e instanceof Block)) && !isGhost((x+1)%27, y)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node((x+1)%27, y))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node((x+1)%27, y);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if(e != null)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x, y-1);
            if((e == null || !(e instanceof Block)) && !isGhost(x, y-1)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node(x, y-1))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node(x, y-1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if(e != null)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x, y+1);
            if((e == null || !(e instanceof Block)) && !isGhost(x, y+1)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node(x, y+1))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node(x, y+1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if(e != null)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
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
        //if(e != null && !(e instanceof Block)){
        if(!toVisit.isEmpty()){
            node = toVisit.get(toVisit.size()-1);
            System.out.println("Pacman : goto " + node);
            while(node.getComeFrom() != null){
                if(node.getX() - node.getComeFrom().getX() < 0){
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.LEFT));
                }
                else if(node.getX() - node.getComeFrom().getX() > 0){
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.RIGHT));
                }
                else if(node.getY() - node.getComeFrom().getY() < 0){
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.DOWN));
                }
                else{
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.TOP));
                }
                node = node.getComeFrom();
            }
        }
        return path;
    }
    

    private ArrayList<Direction> findClosestSuperPellet(int x, int y, int distGhost) {
    	initGhostsNeighbors(distGhost);
        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> toVisit = new ArrayList<>();
        toVisit.add(new Node(x,y));
        GameBasicElement e = null;
        while(!toVisit.isEmpty()){
            x = toVisit.get(0).x;
            y = toVisit.get(0).y;
            e = world.getElement((x + 26)%27, y);
            if((e == null || !(e instanceof Block)) && !isGhost((x + 26)%27, y)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node((x + 26)%27, y))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node((x + 26)%27, y);
                    node.setComeFrom(toVisit.get(0));
                    node.setDist(toVisit.get(0).dist + 1);
                    toVisit.add(node);
                    if(e instanceof SuperPellet)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement((x+1)%27, y);
            if((e == null || !(e instanceof Block)) && !isGhost((x+1)%27, y)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node((x+1)%27, y))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node((x+1)%27, y);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if(e instanceof SuperPellet)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x, y-1);
            if((e == null || !(e instanceof Block)) && !isGhost(x, y-1)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node(x, y-1))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node(x, y-1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if(e instanceof SuperPellet)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
                    visited.get(i).setComeFrom(toVisit.get(0));
                    toVisit.add(visited.get(i));
                    visited.remove(i);
                }
            }
            e = world.getElement(x, y+1);
            if((e == null || !(e instanceof Block)) && !isGhost(x, y+1)){
                int i;
                for(i = 0; i < visited.size(); i++){
                    if(visited.get(i).equals(new Node(x, y+1))){
                        break;
                    }
                }
                if(i >= visited.size()){
                    Node node = new Node(x, y+1);
                    node.setComeFrom(toVisit.get(0));
                    toVisit.add(node);
                    if(e instanceof SuperPellet)
                        break;
                }
                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
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
        //if(e != null && !(e instanceof Block)){
        if(!toVisit.isEmpty()){
            node = toVisit.get(toVisit.size()-1);
            System.out.println("Pacman : goto " + node);
            while(node.getComeFrom() != null){
                if(node.getX() - node.getComeFrom().getX() < 0){
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.LEFT));
                }
                else if(node.getX() - node.getComeFrom().getX() > 0){
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.RIGHT));
                }
                else if(node.getY() - node.getComeFrom().getY() < 0){
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.DOWN));
                }
                else{
                    path.add(0, new Direction(node.getComeFrom().getX(),node.getComeFrom().getY(),Direction.TOP));
                }
                node = node.getComeFrom();
            }
        }
        return path;
    }


    private boolean canTurn() {
        return pac.getPosition().x - (int) pac.getPosition().x < epsilon && pac.getPosition().y - (int) pac.getPosition().y < epsilon;
    }
    
    public ArrayList<Node> getNeighbors(Node n){
    	int x=n.getX(); int y=n.getY();
    	ArrayList<Node> neighbors = new ArrayList<>();
    	GameBasicElement e = world.getElement((x + 26)%27, y);
    	if(!(e instanceof Block)) neighbors.add(new Node((x + 26)%27, y));
    	e = world.getElement((x+1)%27, y);
    	if(!(e instanceof Block)) neighbors.add(new Node((x+1)%27, y));
    	e = world.getElement(x, y-1);
    	if(!(e instanceof Block)) neighbors.add(new Node(x, y-1));
    	e = world.getElement(x, y+1);
    	if(!(e instanceof Block)) neighbors.add(new Node(x, y+1));
    	return neighbors;
    }   

    public ArrayList<Node> getMultipleNeighbors(Node n, int nb, ArrayList<Node> verif ){    	
    	ArrayList<Node> multipleNeighbors = getNeighbors(n);
    	for(Node nod : multipleNeighbors){
    		if (!nod.isInArrayList(verif)){
    			verif.add(nod);
    			if(nb>0)verif.addAll(getMultipleNeighbors(nod, nb-1, verif));
    		}
    	}
    	return verif;    	
   }
    
    public ArrayList<Node> getMultipleNeighbors(Node n, int nb){
    	ArrayList<Node> multipleNeighbors = getNeighbors(n);
    	multipleNeighbors.add(n);
    	for(Node nod : getNeighbors(n)){
    		multipleNeighbors.add(nod);
    		multipleNeighbors.addAll(getMultipleNeighbors(nod, nb-1, multipleNeighbors));
    	}
    	return multipleNeighbors;    	
   }
    
    public Node ghostNode(Ghost gh){
    	Node n= new Node((int)gh.getPosition().x,(int)gh.getPosition().y);
    	return n;
    }
    
    public ArrayList<Node> ghostNodes(){
    	 Iterator<Ghost> it = world.ghostsIterator();
    	 ArrayList<Node> ghosts=new ArrayList<>();
    	 while(it.hasNext()){
            ghosts.add(ghostNode(it.next()));
    	 }
    	 return ghosts;
    }
    
    public  ArrayList<Node> ghostsNeighbors;
    
    public void initGhostsNeighbors(int i){
    ghostsNeighbors = new ArrayList<Node>();
     ArrayList<Node> gho=ghostNodes();
     ArrayList<Node> ghostsN =  new ArrayList<Node>();
     gho.addAll(ghostsN);
     for(Node n : gho){
      	   	 if (i>0) ghostsN.addAll(getMultipleNeighbors(n,i));
     }
     ghostsNeighbors=ghostsN;
      
  	}
    
    private boolean isGhost(Node n){
        if(hunt&&temps>200) return false;
        else if(n.isInArrayList(ghostNodes())) return true;      	 
        return false;
     }
    
    
    private boolean isGhost(int x, int y){
    if(hunt&&temps>700) return false;
     Node nod=new Node(x,y);
   	 if(nod.isInArrayList(ghostsNeighbors))		 return true;
   	 
     return false;
    }
}
