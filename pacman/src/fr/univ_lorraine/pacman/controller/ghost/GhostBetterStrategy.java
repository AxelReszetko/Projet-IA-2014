package fr.univ_lorraine.pacman.controller.ghost;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.controller.GhostController;
import fr.univ_lorraine.pacman.model.Block;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Ghost;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.World;

public class GhostBetterStrategy extends GhostController{
	
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
            return "(" + x + ", " + y + ")";
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
    
    public GhostBetterStrategy(World world)
    {
    	super(world);
    }
    
    public void setPath(Node position, Node goal)
    {
    	
    }


	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		Iterator<Ghost> iterGhost = world.ghostsIterator();
		goal = new Node ((int)pac.getPosition().x,(int)pac.getPosition().y);
		//goal = new Node(13,18);
		int rank = 0;
		if(ghPath.size()==0)
		{
			for(int i = 0; i< 4;++i)
			{
				ghPath.add(null);
			}
		}
		while(iterGhost.hasNext())
		{
			Ghost gh = iterGhost.next();
			iterGhost.next();
			iterGhost.next();
			iterGhost.next();
			Node ghPosition = new Node((int)Math.floor(gh.getPosition().x),(int)Math.floor(gh.getPosition().y));
			//ArrayList<Direction> path = new ArrayList<Direction>();
			Stack<Direction> path = ghPath.get(rank);
			if (path==null)//it it has no path, then compute it 
			{
				path = astar(ghPosition, goal);
				ghPath.set(rank, path);
				System.out.println(path);
			}
			if(!path.isEmpty())
			{
				Node step = new Node(path.peek().x, path.peek().y);
				if(step.samePlace(ghPosition))
				{
					path.pop();
					ghPath.set(rank, path);
					break;
				}
				if(path.peek().dir == 0)
				{
					gh.turnUp();
				}
				if(path.peek().dir == 1)
					gh.turnDown();
				if(path.peek().dir == 2)
					gh.turnRight();
				if(path.peek().dir == 3)
					gh.turnLeft();
			}
			gh.update(delta);
			++rank;
		}
		
	}
	
	private Stack<Direction> astar(Node start, Node to)
	{
		Stack<Direction> path = new Stack<Direction>();
		ArrayList<Node> close = new ArrayList<Node>();//pathes visited
		ArrayList<Node> open = new ArrayList<Node>(); //pathes to visit
		close.add(new Node(11, 17));
		close.add(new Node(12, 17));
		close.add(new Node(13, 17));
		close.add(new Node(14, 17));
		close.add(new Node(15, 17));
		close.add(new Node(11, 16));
		close.add(new Node(12, 16));
		close.add(new Node(13, 16));
		close.add(new Node(14, 16));
		close.add(new Node(15, 16));
		close.add(new Node(11, 15));
		close.add(new Node(12, 15));
		close.add(new Node(13, 15));
		close.add(new Node(14, 15));
		close.add(new Node(15, 15));
		open.add(start);
		Node from = new Node(start.x, start.y);
		int movePrice = 1;
		
		while(!(from.x == to.x && from.y == to.y))
		{
			System.out.println("from: "+from.toString());
			ArrayList<Node> adjacent = new ArrayList<Node>();
			int localX = from.getX();
			int localY = from.getY();
			if(!(world.getElement(localX, localY - 1) instanceof Block))//look down
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
			if(!(world.getElement(localX, localY + 1) instanceof Block))//look up
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
			if(!(world.getElement(localX - 1, localY) instanceof Block))//look left
			{
				Node n = new Node(localX - 1, localY);
				if(!close.contains(n))
				{
					n.setComeFrom(from);
					n.setDist(manhattan(n, to));
					n.setMovePrice(movePrice);
					adjacent.add(n);
				}
			}
			if(!(world.getElement(localX + 1, localY) instanceof Block))//look right
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
						
			
			Node chosen = null;// this will be the chosen one
			for (int i =0; i<adjacent.size();++i)
			{	
				if(open.contains(adjacent.get(i)) && adjacent.get(i).movePrice > from.movePrice+movePrice) 
				{
					adjacent.get(i).setComeFrom(from);
					adjacent.get(i).setDist(from.movePrice+movePrice);
				}				
				
				if(chosen == null || (adjacent.get(i).dist + adjacent.get(i).movePrice < chosen.dist + chosen.movePrice) )
					chosen = adjacent.get(i);	
			}
			System.out.println("chosen: "+chosen.toString());
			
			if(chosen == null) //this is not supposed to happen
			{
				System.out.println("I'm stuck! "+path.toString());
			}
			
			open.addAll(adjacent);
			close.add(chosen);
			open.remove(chosen);
			
			
			from = chosen;
		}
		Node currentNode = close.get(close.size()-1);
		while (currentNode.comeFrom != null)
		{
			path.push(new Direction(currentNode.x, currentNode.y, direction(currentNode)));
			currentNode=currentNode.comeFrom;
		}
		return path;
		
	}
	
	private int direction(Node n)
	{
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
	
	private int manhattan(Node from, Node to)
	{
		return Math.abs(from.x-to.x)+Math.abs(from.y-to.y);
	}
	
	private Pacman pac = world.getPacman();
	private ArrayList<Stack<Direction>> ghPath = new ArrayList<>();
	private Node goal;

}
