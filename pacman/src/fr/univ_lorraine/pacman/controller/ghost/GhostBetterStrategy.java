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
	

    private Vector2 pacLastPos;    
    private Pacman pac = world.getPacman();
	private ArrayList<Stack<Direction>> ghPath = new ArrayList<>();
	private ArrayList<Node> ghGoal = new ArrayList<>();
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
    
    public GhostBetterStrategy(World world)
    {
    	super(world);
    	pacLastPos=new Vector2();
    }
    
    public void setPath(Node position, Node goal)
    {
    	
    }


	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		Iterator<Ghost> iterGhost = world.ghostsIterator();
		Node goal;
		int rank = 0;
		goal = new Node ((int)pac.getPosition().x,(int)pac.getPosition().y);
		if(ghPath.size()==0)
		{
			for(int i = 0; i< 4;++i)
			{
				if(i==2)
				{
					goal=getPinkyGoal();
				}
				ghPath.add(null);
				ghGoal.add(goal);
			}
		}
		while(iterGhost.hasNext())
		{
			Ghost gh = iterGhost.next();
			Node ghPosition = new Node((int)Math.round(gh.getPosition().x),(int)Math.round(gh.getPosition().y));
			Stack<Direction> path = ghPath.get(rank);
			if (canTurn(gh))
			{
				path = astar(ghPosition, goal);
				ghPath.set(rank, path);
			}
			if(!path.isEmpty())
			{
				Node step = new Node(path.peek().x, path.peek().y);
				if(step.samePlace(ghPosition) && canTurn(gh))
				{
					path.pop();
					ghPath.set(rank, path);
					break;
				}
				if(path.peek().dir == 0)
					gh.turnUp();
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
		return Math.abs((from.x)%26-(to.x)%26)+Math.abs(from.y-to.y);
	}
	
	private boolean canTurn(Ghost g) {
	        return g.getPosition().x - (int) g.getPosition().x < epsilon && g.getPosition().y - (int) g.getPosition().y < epsilon;
	    }
	
	private int getPacmanDir()
	{
		if(pac.getPosition().x < pacLastPos.x)
			return 3;
		if(pac.getPosition().x > pacLastPos.x)
			return 2;
		if(pac.getPosition().y < pacLastPos.y)
			return 1;
		if(pac.getPosition().y > pacLastPos.y)
			return 0;
		pacLastPos = pac.getPosition();
		return 0;
	}
	
	private Node getPinkyGoal()
	{
		if(getPacmanDir()==3)
		{
			if(!(world.getElement(ghGoal.get(1).x - 2, ghGoal.get(1).y) instanceof Block))
				return new Node (ghGoal.get(1).x-2,ghGoal.get(1).y);
			if(!(world.getElement(ghGoal.get(1).x - 1, ghGoal.get(1).y) instanceof Block))
				return new Node (ghGoal.get(1).x-1,ghGoal.get(1).y);
		}
		if(getPacmanDir()==2)
		{
			if(!(world.getElement(ghGoal.get(1).x + 2, ghGoal.get(1).y) instanceof Block))
				return new Node (ghGoal.get(1).x+2,ghGoal.get(1).y);
			if(!(world.getElement(ghGoal.get(1).x + 1, ghGoal.get(1).y) instanceof Block))
				return new Node (ghGoal.get(1).x+1,ghGoal.get(1).y);
		}
		if(getPacmanDir()==1)
		{
			if(!(world.getElement(ghGoal.get(1).x, ghGoal.get(1).y-2) instanceof Block))
				return new Node (ghGoal.get(1).x,ghGoal.get(1).y-2);
			if(!(world.getElement(ghGoal.get(1).x, ghGoal.get(1).y-1) instanceof Block))
				return new Node (ghGoal.get(1).x,ghGoal.get(1).y-1);
		}
		if(getPacmanDir()==0)
		{
			if(!(world.getElement(ghGoal.get(1).x , ghGoal.get(2).y+2) instanceof Block))
				return new Node (ghGoal.get(1).x,ghGoal.get(1).y+2);
			if(!(world.getElement(ghGoal.get(1).x , ghGoal.get(1).y+1) instanceof Block))
				return new Node (ghGoal.get(1).x,ghGoal.get(1).y+1);
		}
		return ghGoal.get(1);
	}
	

}
