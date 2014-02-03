package fr.univ_lorraine.pacman.controller.pacman;

import java.util.ArrayList;

import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.World;

public class PacmanTest extends PacmanController{
	private Pacman pac;
	ArrayList <int[]> inters;
	ArrayList <int[]> paths;
	
	
	public PacmanTest(World world) {
		// TODO Auto-generated constructor stub
		super(world);
		System.out.println("PacmanTest Strategy");
		pac = world.getPacman();
		buildInters();
		//printInters();
	
	}
	@Override
	public void update(float delta) {		
		// TODO Auto-generated method stub
		
		pac.update(delta);
	}
	
	public boolean isBlock(int i,int j){		
		boolean ib=false;
		try {if(world.getElement(i, j).getClass().getName().contains("Block")) ib=true;
		} catch (Exception ex) {}		
		return ib;
	}
	
	public void drawWorld(){
		for(int j=30;j>-1;j--){//ELEMENTS
			String ligne="";
			for(int i=26;i>-1;i--){
				try {									
					GameBasicElement e = world.getElement(i, j);					
					String el=e.getClass().getName();
					if(el.toLowerCase().contains("block")){ligne+="B";}
					else if(el.toLowerCase().contains("superpellet")){ligne+="*";}
					else if(el.toLowerCase().contains("pellet")){ligne+=".";}
					else {System.out.println(el);}															
							
				} catch (Exception ex) {ligne+=" ";}				
			}
			System.out.println(ligne);
		}
	}
	
	public int bTi (boolean b){return (b) ? 1 : 0;}
	public boolean iTb(int i){return i!=0;}
	
	public void buildInters(){
		inters= new ArrayList<int[]>();
		
		for(int lig=world.getHeight()-1;lig>0;lig--){
			for(int col=world.getWidth()-1;col>0;col--){
				int ht,bs,gc,dt;
				ht=bTi(!isBlock(col,lig+1));
				bs=bTi(!isBlock(col,lig-1));
				gc=bTi(!isBlock(col+1,lig));
				dt=bTi(!isBlock(col-1,lig));
				
				int inter[] = { lig,col,ht,bs,gc,dt};
				if(!isBlock(col,lig)&&(ht+gc>1||ht+dt>1||bs+gc>1||bs+dt>1)){
					inters.add(inter);
				}
			}
			
		}
	}
	/*
	public void buildPaths(){
		int id=1;
		for(int lig=world.getHeight();lig>-1;lig--){
			int path1[]=new int[6];
			int path2[]=new int[6];
			path1[0]=0;
			path2[0]=0;
			
			for(int col=world.getWidth();col>-1;col--){
				if(col==0 && !isBlock(col,lig)){
					path1[0]=id;
					path1[5]=0;
					id++;
				}
				
				
				int inter[] = { j,i,ht,bs,gc,dt};
				if(!isBlock(i,j)&&(ht+gc>1||ht+dt>1||bs+gc>1||bs+dt>1)){
					inters.add(inter);
				}
			}
			
		}
		
	}
	
	public int[] buildPath(int[]path, String direction){
		if(direction=="ligne"){
			if(atInter(path[1],path[2]))
		}
		return null;
		
	}
	
	public int[] atInter(int lig, int col){
		for(int i=0;i<inters.size();i++){
			if(inters.get(i)[0]==lig && inters.get(i)[1]==col){
				return inters.get(i);
			}
		}
		int[] no={0};
		return no;		
	}
	
	
	public void printInters(){
		for(int i = 0; i < inters.size(); i++) {  
			String ligne="L:"+(inters.get(i))[0]+" C:"+(inters.get(i))[1]
					+" H:"+(inters.get(i))[2]+" B:"+(inters.get(i))[3]
					+" G:"+(inters.get(i))[4]+" D:"+(inters.get(i))[5];
			System.out.println(ligne);
		} 
	}
	*/
	public int getInterLig(int inter){
		return inters.get(inter)[0];
	}
	public int getInterCol(int inter){
		return inters.get(inter)[1];
	}
	public int getInterHt(int inter){
		return inters.get(inter)[2];
	}
	public int getInterBs(int inter){
		return inters.get(inter)[3];
	}
	public int getInterGc(int inter){
		return inters.get(inter)[4];
	}
	public int getInterDt(int inter){
		return inters.get(inter)[5];
	}
	public int getPathID(int path){
		return paths.get(path)[0];
	}
	public int getPathL1(int path){
		return paths.get(path)[1];
	}
	public int getPathC1(int path){
		return paths.get(path)[2];
	}	
	public int getPathL2(int path){
		return paths.get(path)[3];
	}
	public int getPathC2(int path){
		return paths.get(path)[4];
	}
	public int getPathDist(int path){
		return paths.get(path)[5];
	}
	public void setInterLig(int inter, int lig){
		inters.get(inter)[0]=lig;
	}
	public void setInterCol(int inter, int col){
		inters.get(inter)[1]=col;
	}
	public void setInterHt(int inter, int ht){
		inters.get(inter)[2]=ht;
	}
	public void setInterBs(int inter, int bs){
		inters.get(inter)[3]=bs;
	}
	public void setInterGc(int inter, int gc){
		inters.get(inter)[4]=gc;
	}
	public void setInterDt(int inter, int dt){
		inters.get(inter)[5]=dt;
	}
	public void setPathID(int path, int id){
		paths.get(path)[0]=id;
	}
	public void setPathL1(int path, int l1){
		paths.get(path)[1]=l1;
	}
	public void setPathC1(int path, int c1){
		paths.get(path)[2]=c1;
	}	
	public void setPathL2(int path, int l2){
		paths.get(path)[3]=l2;
	}
	public void setPathC2(int path, int c2){
		paths.get(path)[4]=c2;
	}
	public void setPathDist(int path, int dis){
		paths.get(path)[5]=dis;
	}
}
