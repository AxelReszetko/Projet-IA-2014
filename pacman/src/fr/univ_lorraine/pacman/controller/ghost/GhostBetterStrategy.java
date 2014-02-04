package fr.univ_lorraine.pacman.controller.ghost;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;

import fr.univ_lorraine.pacman.controller.GhostController;
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
        private Node comeFrom;
        private int x, y;
        
        public Node(int x, int y){
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
        
        public String toString(){
            return "(" + x + ", " + y + ")";
        }
        public boolean equals(Object o){
            if(o == null || !(o instanceof Node))
                return false;
            Node other = (Node)o;
            return other.x == x && other.y == y;
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
		while(iterGhost.hasNext())
		{
			Ghost gh = iterGhost.next();
			Vector2 pacPosition = pac.getPosition();
			Vector2 ghPosition = gh.getPosition();
			float deltaX = pacPosition.x - ghPosition.x;
			float deltaY = pacPosition.y - ghPosition.y;
			float ghX = gh.getPosition().x;
			float ghY = gh.getPosition().y;
			System.out.println("X: "+pacPosition.x+" Y: "+pacPosition.y);
			
			if(ghY<19 && ghY>10 && ghX<9 && ghX>13)
			{
				
			}
			
			if (Math.abs(deltaY)>Math.abs(deltaX))
			{
				if(deltaY>0)
					gh.turnUp();
				else
					gh.turnDown();
			}
			else
			{
				if(deltaX>0)
					gh.turnRight();
				else
					gh.turnLeft();
			}	
			gh.update(delta);
		}
		
	}
	
	private Pacman pac = world.getPacman();

}
