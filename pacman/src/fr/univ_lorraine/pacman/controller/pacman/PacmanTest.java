package fr.univ_lorraine.pacman.controller.pacman;

import java.util.ArrayList;

import fr.univ_lorraine.pacman.controller.PacmanController;
import fr.univ_lorraine.pacman.model.GameBasicElement;
import fr.univ_lorraine.pacman.model.Pacman;
import fr.univ_lorraine.pacman.model.World;

public class PacmanTest extends PacmanController{
	private Pacman pac;
	ArrayList <int[]> inters; //Format : Ligne, Colonne, IDCheminHaut, IDCheminBas, IDCheminGauche, IDCheminDroite
	ArrayList <int[]> paths; //Format : IDChemin, Ligne1, Colonne1, Ligne2, Colonne2, Distance
	
	
	public PacmanTest(World world) {
		// TODO Auto-generated constructor stub
		super(world);
		System.out.println("PacmanTest Strategy");
		pac = world.getPacman();
		System.out.println("Analyse du terrain");
		buildInters();
		buildPaths();
		System.out.println("Analyse termine");
		//printInters();
		//printPaths();
	
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
	
	public void buildPaths(){
		paths= new ArrayList<int[]>();
		int id=1;
		for(int lig=0;lig<world.getHeight();lig++){
			int path1[]=new int[6];
			int path2[]=new int[6];
			path1[0]=0;
			path2[0]=0;
			boolean tp=false;
			
			for(int col=0;col<world.getWidth();col++){
				//System.out.println("COL"+col);
				if(col==0 && !isBlock(col,lig)){
					tp=true;
					//System.out.println("TP Part1");
					int path[]={id,lig,col,lig,col,0};
					path1=buildPath(path,"ligne");
					col=path1[4]-1;
					id++;
				}
				else if(atInter(lig,col).length>1 && !isBlock(col,lig)&&!isBlock(col+1,lig)){
					//System.out.println("L1 "+lig+" C1 "+col);
					atInter(lig,col)[2]=id;
					int path[]={id,lig,col,lig,col,0};
					path2=buildPath(path,"ligne");
					if(path2!=null){
						col=path2[4]-1;
						//System.out.println("C"+col);
						if(col+2==world.getWidth()&&tp){
							//System.out.println("TP Part2");
							path2[0]=path1[0];
							path2[4]=path1[4];
							path2[5]=path2[5]+path1[5];	
							atInter(path[1],path[2])[4]=path2[0];
							atInter(path[3],path[4])[5]=path2[0];
							id--;
						}
						paths.add(path2);
						id++;
					}
				}
				
			}
			
		}
		for(int col=0;col<world.getWidth();col++){
			int path1[]=new int[6];
			int path2[]=new int[6];
			path1[0]=0;
			path2[0]=0;
			boolean tp=false;
			
			for(int lig=0;lig<world.getHeight();lig++){
				//System.out.println("COL"+col);
				if(lig==0 && !isBlock(col,lig)){
					tp=true;
					int path[]={id,lig,col,lig,col,0};
					path1=buildPath(path,"colonne");
					lig=path1[3]-1;
					id++;
				}
				else if(atInter(lig,col).length>1 && !isBlock(col,lig)&&!isBlock(col,lig+1)){
					atInter(lig,col)[2]=id;
					int path[]={id,lig,col,lig,col,0};
					path2=buildPath(path,"colonne");
					if(path2!=null){
						lig=path2[3]-1;
						//System.out.println("C"+col);
						if(lig+2==world.getHeight()&&tp){
							//System.out.println("TP Part2");
							path2[0]=path1[0];
							path2[3]=path1[3];
							path2[5]=path2[5]+path1[5];	
							atInter(path[1],path[2])[2]=path2[0];
							atInter(path[3],path[4])[3]=path2[0];
							id--;
						}
						paths.add(path2);
						id++;
					}
				}
				
			}
			
		}
	}
	
	public int[] buildPath(int[]path, String direction){
		if(direction=="ligne"){
			while(atInter(path[3],path[4]+1).length<2 && !isBlock(path[4],path[3]) && path[4]+1!=world.getWidth()){
				path[4]++;path[5]++;
			}
			if(atInter(path[3],path[4]+1).length>1 && !isBlock(path[4],path[3])){
				if(!isBlock(path[4]+1,path[3])) atInter(path[3],path[4]+1)[5]=path[0];
				path[4]++;path[5]++;
				//System.out.println("L2 "+path[3]+" C2 "+path[4]+" DI "+path[5]);				
				return path;
			}
			else if( path[4]+1==world.getWidth() && !isBlock(path[4],path[3])){
				return path;
			}
		}
		if(direction=="colonne"){
			while(atInter(path[3]+1,path[4]).length<2 && !isBlock(path[4],path[3]) && path[3]+1!=world.getHeight()){
				
				path[3]++;path[5]++;
			}
			if(atInter(path[3]+1,path[4]).length>1 && !isBlock(path[4],path[3]) ){
				if(!isBlock(path[4],path[3]+1)) atInter(path[3]+1,path[4])[3]=path[0];
				path[3]++;path[5]++;
				return path;
			}
			else if( path[3]+1==world.getHeight() && !isBlock(path[4],path[3]) ){
				return path;
			}
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
	
	/*
	public void printInters(){
		for(int i = 0; i < inters.size(); i++) {  
			String ligne="L:"+(inters.get(i))[0]+" C:"+(inters.get(i))[1]
					+" H:"+(inters.get(i))[2]+" B:"+(inters.get(i))[3]
					+" G:"+(inters.get(i))[4]+" D:"+(inters.get(i))[5];
			System.out.println(ligne);
		} 
	}
	
	public void printPaths(){
		for(int i = 0; i < paths.size(); i++) {  
			String ligne="ID:"+(paths.get(i))[0]+" L1:"+(paths.get(i))[1]
					+" C1:"+(paths.get(i))[2]+" L2:"+(paths.get(i))[3]
					+" C2:"+(paths.get(i))[4]+" DI:"+(paths.get(i))[5];
			System.out.println(ligne);
		} 
	}
	
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
	}*/
}
