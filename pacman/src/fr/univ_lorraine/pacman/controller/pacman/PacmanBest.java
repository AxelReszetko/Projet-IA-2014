package fr.univ_lorraine.pacman.controller.pacman;

import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.Block;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.Pellet;
import fr.univ_lorraine.pacman.model.World;
import fr.univ_lorraine.pacman.model.GameMoveableBasicElement.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class PacmanBest extends PacmanController {

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
    private class Node{
        private int dist;
        private int movePrice;
        private Node comeFrom;
        private int x, y;
        
        public Node(int x, int y){
            this.x = x;
            this.y = y;
            this.dist = 0;
            this.movePrice = 0;
            this.comeFrom = null;
        }

        public int getMovePrice() {
			return movePrice;
		}

		public void setMovePrice(int movePrice) {
			this.movePrice = movePrice;
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
        
        public String toString(){
            return "(" + x + ", " + y + ","+ dist + ")";
        }
        public boolean equals(Object o){
            if(o == null || !(o instanceof Node))
                return false;
            Node other = (Node)o;
            return other.x == x && other.y == y;
        }
        
        public boolean samePlace(Node n)
        {
        	return (this.x == n.x && this.y==n.y);
        }
    }
    
    public PacmanBest(World world) {
        super(world);
        System.out.println("PacmanBestCreator");
        pac = world.getPacman();
        path = null;
        // TODO Auto-generated constructor stub
    }

    private long chrono ; 
    private int dist;
    private boolean hunt=false;
    private Node last;
    private Node pacNode(){
    	return new Node((int) Math.round(pac.getPosition().x), (int) Math.round(pac.getPosition().y));
    }
    
    private int distHunt(){
    	return (28-dist)/2;
    }
    boolean hunting=false;
    Stack<Direction> pHunt;
    @Override
    public void update(float delta) {
		if(hunt &&pac.getState()!=State.HUNTING){
			hunt=false;
			long temps = java.lang.System.currentTimeMillis() - chrono ;
			//System.out.println("Distance = "+dist);
			//System.out.println("Temps  = " + temps + " ms") ; 
		}
		if(pac.getState()==State.HUNTING&&!hunt){
			this.hunt=true;			
			this.dist=0;
			this.chrono = java.lang.System.currentTimeMillis() ;	
			
		}
		if(hunt && (pacNode().getX()!=last.getX() || pacNode().getY()!=last.getY())){
			dist++;
			//System.out.println("Distance actuelle = "+dist);
		}
    	if(canTurn()&&hunt){
    		Stack<Direction> gHunt=findClosestGhost();
			if(gHunt.size()<distHunt()){
				pHunt=gHunt;
				hunting=true;
			}
			if(!pHunt.isEmpty()){
				Node step = new Node(pHunt.peek().x, pHunt.peek().y);
				pHunt.pop();
				if(pHunt.peek().dir == 0)
					pac.turnUp();
				if(pHunt.peek().dir == 1)
					pac.turnDown();
				if(pHunt.peek().dir == 2)
					pac.turnRight();
				if(pHunt.peek().dir == 3)
					pac.turnLeft();
			}
			else{hunting=false;}
    	}
        if (canTurn()&&(!hunt||!hunting)) {        	
        	/*if(hunt){
    		    ArrayList<Direction> tmPath = findClosestGhost((int) pac.getPosition().x, (int) pac.getPosition().y);
    			if(tmPath.size()<distHunt){
    				path=tmPath;

    				System.out.println("Path : "+path.size());
    			}
    			else path = findClosestPellet((int) pac.getPosition().x, (int) pac.getPosition().y);
    		}
        	else  {       		
                path = findClosestPellet((int) pac.getPosition().x, (int) pac.getPosition().y);
            } */
            if (path == null || path.isEmpty()) {
                path = findClosestPellet((int) Math.round(pac.getPosition().x), (int) Math.round(pac.getPosition().y));
            }

        	if (path.get(0).x == (int) Math.round(pac.getPosition().x) && path.get(0).y == (int)Math.round(pac.getPosition().y)) {
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
		this.last=new Node ((int) Math.round(pac.getPosition().x), (int) Math.round(pac.getPosition().y));
        pac.update(delta);
    }

    private ArrayList<Direction> findClosestPellet(int x, int y) {
        ArrayList<Node> visited = new ArrayList<Node>();
        ArrayList<Node> toVisit = new ArrayList<Node>();
        toVisit.add(new Node(x,y));
        GameBasicElement e = null;
        
        while(!toVisit.isEmpty()){        	
            x = toVisit.get(0).x;
            y = toVisit.get(0).y;
            //for(int j=0;j<4;j++){
         
            	int xt=(x + 26)%27; 
            	int yt=y;
            	/* switch (j) {
	            	case 1: xt=(x+1)%27; yt=y; break; 
	            	case 2: xt=x; yt=y-1; break; 
	            	case 3: xt=x; yt=y+1; break;
	            }*/
            	e = world.getElement(xt, yt); 
            	
	            if((e == null || !(e instanceof Block))&&!isGhost(xt,yt)){
	                int i;
                    Node node = new Node(xt, yt);
			        node.setDist(distCloseToGhost(x,y));
	                for(i = 0; i < visited.size(); i++){
	                    if(visited.get(i).equals(node)) {break;}	                    
	                }
	                if(i >= visited.size()){
	                    node.setComeFrom(toVisit.get(0));
	                    toVisit.add(node);
	                    if(e != null) break;
	                }
	                else if(visited.get(i).dist > toVisit.get(0).dist + 1){
	                    visited.get(i).setComeFrom(toVisit.get(0));
	                    toVisit.add(visited.get(i));
	                    visited.remove(i);
	                }
	            }
				xt=(x+1)%27; yt=y; 
				e = world.getElement(xt, yt); 
				
			    if((e == null || !(e instanceof Block))&&!isGhost(xt,yt)){
			        int i;
			        Node node = new Node(xt, yt);
			        node.setDist(distCloseToGhost(x,y));
			        for(i = 0; i < visited.size(); i++){
			            if(visited.get(i).equals(node)) {break;}	                    
			        }
			        if(i >= visited.size()){
			            node.setComeFrom(toVisit.get(0));
			            toVisit.add(node);
			            if(e != null) break;
			        }
			        else if(visited.get(i).dist > toVisit.get(0).dist + 1){
			            visited.get(i).setComeFrom(toVisit.get(0));
			            toVisit.add(visited.get(i));
			            visited.remove(i);
			        }
			    }
				xt=x; yt=y-1;			    
				e = world.getElement(xt, yt); 
				
			    if((e == null || !(e instanceof Block))&&!isGhost(xt,yt)){
			        int i;
			        Node node = new Node(xt, yt);
			        node.setDist(distCloseToGhost(x,y));
			        for(i = 0; i < visited.size(); i++){
			            if(visited.get(i).equals(node)) {break;}	                    
			        }
			        if(i >= visited.size()){
			            node.setComeFrom(toVisit.get(0));
			            toVisit.add(node);
			            if(e != null) break;
			        }
			        else if(visited.get(i).dist > toVisit.get(0).dist + 1){
			            visited.get(i).setComeFrom(toVisit.get(0));
			            toVisit.add(visited.get(i));
			            visited.remove(i);
			        }
			    }
		        xt=x; yt=y+1;
		    	e = world.getElement(xt, yt); 
		    	
		        if((e == null || !(e instanceof Block))&&!isGhost(xt,yt)){
		            int i;
		            Node node = new Node(xt, yt);
			        node.setDist(distCloseToGhost(x,y));
		            for(i = 0; i < visited.size(); i++){
		                if(visited.get(i).equals(node)) {break;}	                    
		            }
		            if(i >= visited.size()){
		                node.setComeFrom(toVisit.get(0));
		                toVisit.add(node);
		                if(e != null) break;
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
        if(e != null && !(e instanceof Block)){
            node = toVisit.get(toVisit.size()-1);
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
        } //System.out.println("Path : "+path.size());
        return path;
    }
    
    public int distCloseToGhost(int x, int y){  
    	int dist=200;
    	Iterator<Ghost> iterGhost = world.ghostsIterator() ;
    	while (iterGhost.hasNext()) {
    		Ghost gh = iterGhost.next();
    		int nDist=manhattan(x,y,getGhostX(gh),getGhostY(gh));
    		if (nDist<dist) dist=nDist;
    	}
    	int ret=0;
    	switch(dist){
    		case 1 : ret=20;
    		case 2 : ret=15;
    		case 3 : ret=10;
    		case 4 : ret=7;
    		case 5 : ret=5;
    		case 6 : ret=4;
    		case 7 : ret=3;
    		case 8 : ret=2;
    		case 9 : ret=1;
    	}
    	return ret;
    }
    

    public int manhattan(int x1, int y1, int x2, int y2){
    	return Math.abs((x1)%26-(x2)%26)+Math.abs(y1-y2);
    }
    
	private int manhattan(Node from, Node to){
		return Math.abs((from.x)%26-(to.x)%26)+Math.abs(from.y-to.y);
	}
	
    public int getEltX(GameBasicElement e){
    	return (int)e.getPosition().x;
    }
    public int getEltY(GameBasicElement e){
    	return (int)e.getPosition().y;
    }
    public int getGhostX(Ghost g){
    	return (int)Math.round(g.getPosition().x);
    }
    public int getGhostY(Ghost g){
    	return (int)Math.round(g.getPosition().y);
    }
    public boolean isGhost(int x, int y){
    	Iterator<Ghost> iterGhost = world.ghostsIterator() ;
    	while (iterGhost.hasNext()) {
    		Ghost gh = iterGhost.next();
    		if (getGhostX(gh)==x&&getGhostY(gh)==y)return true;
    	}
    	return false;
    }
    public boolean isHuntedGhost(int x, int y){
    	Iterator<Ghost> iterGhost = world.ghostsIterator() ;
    	while (iterGhost.hasNext()) {
    		Ghost gh = iterGhost.next();
    		if (getGhostX(gh)==x&&getGhostY(gh)==y&&gh.getState()==State.HUNTED)return true;
    	}
    	return false;
    }
    
    private boolean canTurn() {
        return pac.getPosition().x - (int) pac.getPosition().x < epsilon && pac.getPosition().y - (int) pac.getPosition().y < epsilon;
    }
    
    private Stack<Direction> findClosestGhost(){
    	Iterator<Ghost> iterGhost = world.ghostsIterator() ;
    	Stack<Direction> path;
    	Stack<Direction> nPath;
    	Ghost gh=iterGhost.next();
    	Node test=new Node (getGhostX(gh), getGhostY(gh)); 
    	path=astar(test,pacNode());
    	int longueur=path.size();
    	while (iterGhost.hasNext()) {    		
    		gh = iterGhost.next();    		
    		test=new Node (getGhostX(gh), getGhostY(gh));
        	nPath=astar(test,pacNode());
        	int nLongueur =nPath.size();
    		if(longueur>nLongueur){
    			longueur=nLongueur;
    			path=nPath;
    		}
    	}
    	return path;
    }
    

    private Stack<Direction> astar(Node start, Node to)
	{
		Stack<Direction> path = new Stack<Direction>();
		ArrayList<Node> close = new ArrayList<Node>();//pathes visited
		ArrayList<Node> open = new ArrayList<Node>(); //pathes to visit
		open.add(start);
		Node from = new Node(start.x, start.y);
		int movePrice = 10;
		
		while(!(from.x == to.x && from.y == to.y))
		{
			ArrayList<Node> adjacent = new ArrayList<Node>();
			int localX = from.getX();
			int localY = from.getY();
			
			if(!(world.getElement((localX +25)%26, localY) instanceof Block))//look left
			{
				Node n = new Node((localX + 25)%26, localY);
				if(!close.contains(n))
				{
					n.setComeFrom(from);
					n.setDist(manhattan(n, to));
					n.setMovePrice(movePrice);
					adjacent.add(n);
				}
			}
			if(!(world.getElement((localX + 27)%26, localY) instanceof Block))//look right
			{
				Node n = new Node(localX + 1, localY);
				if(!close.contains(n))
				{
					n.setComeFrom(from);
					n.setDist(manhattan(n, to));
					n.setMovePrice(movePrice);
					adjacent.add(n);
				}
			}
			
			if(localY >1 && !(world.getElement(localX, localY - 1) instanceof Block))//look down
			{
				Node n = new Node(localX , localY - 1);
				if(!close.contains(n))
				{
					n.setComeFrom(from);
					n.setDist(manhattan(n, to));
					n.setMovePrice(movePrice);
					adjacent.add(n);
				}
			}
			if(localY < world.getHeight()-1 && !(world.getElement(localX, localY + 1) instanceof Block))//look up
			{
				Node n = new Node(localX, localY + 1);
				if(!close.contains(n))
				{
					n.setComeFrom(from);
					n.setDist(manhattan(n, to));
					n.setMovePrice(movePrice);
					adjacent.add(n);
				}
			}
					
			
			Node chosen = null;// this will be the chosen one
			for (int i =0; i<open.size();++i)
			{	
				if(open.get(i).movePrice > from.movePrice+movePrice) 
				{
					open.get(i).setComeFrom(from);
					open.get(i).setDist(from.movePrice+movePrice);
				}				
				
				if(chosen == null || (open.get(i).dist + open.get(i).movePrice < chosen.dist + chosen.movePrice) )
					chosen = open.get(i);	
			}
			if(chosen == null) //this is not supposed to happen
			{
				System.out.println("I'm stuck! "+path.toString());
			}
			
			open.addAll(adjacent);
			close.add(chosen);
			open.remove(chosen);
			
			
			from = chosen;
		}
		if(close.size()>0)
		{
			Node currentNode = close.get(close.size()-1);
			while (currentNode.comeFrom != null)
			{
				path.push(new Direction(currentNode.x, currentNode.y, direction(currentNode)));
				currentNode=currentNode.comeFrom;
			}
		}
		return path;
		
	}
    
    private int direction(Node n){
		if(n.x < n.getComeFrom().x)
			return 3;
		if(n.x > n.getComeFrom().x)
			return 2;
		if(n.y < n.getComeFrom().y)
			return 1;
		if(n.y > n.getComeFrom().y)
			return 0;
		return -1;
	}
}
