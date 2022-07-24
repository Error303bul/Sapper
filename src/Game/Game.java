package Game;
import javax.swing.*;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

public class Game{
	public JFrame window;
	public int tileSize=40;
	public int columns=10, rows=10, totalBombs=20;
	public String menu="Main";
	protected int fps, framesCompleted=0,mouseX=0,mouseY=0;
	public GameCanvas panel;
	protected int[][] flagMap, tileMap;
	protected Point location = MouseInfo.getPointerInfo().getLocation();
	protected boolean rightClicked = false,leftClicked=false, rightPressed=false,leftPressed=false,wheelClicked=false, wheelPressed=false;
	
	/*
	 * 0-Closed
	 * 1-Flag and closed
	 * 2-Opened
	 * */
	
	public boolean checkWin() {
		int bombsNotFinded=totalBombs;
		for(int checkCol=0;checkCol<columns;checkCol++) {
			for(int checkRow=0;checkRow<rows;checkRow++) {
				if(tileMap[checkCol][checkRow]==1) {
					if(flagMap[checkCol][checkRow]==1) {
						bombsNotFinded--;
					}
				}
			}
		}
		return bombsNotFinded==0?true:false;
	}
	
	
	public void openAllTiles() {
		for(int column=0;column<columns;column++) {
			for(int row=0;row<rows;row++) {
				if(tileMap[column][row]==0) {
					flagMap[column][row]=2;
				}else {
					if(flagMap[column][row]==0) {
						flagMap[column][row]=2;
					}
				}
			}
		}
	}
	
	
	
	public void openTile(int x, int y) {
		flagMap[x][y]=2;
		if(panel.getBombs(x,y)<1) {
			if(x<columns-1) {
				if(flagMap[x+1][y]==0) {
					openTile(x+1, y);
				}
			}
			if((y<rows-1)) {
				if(flagMap[x][y+1]==0) {
					openTile(x, y+1);
				}
			}
			if((x>0)) {
				if(flagMap[x-1][y]==0) {
					openTile(x-1,y);
				}
			}
			if((y>0)) {
				if(flagMap[x][y-1]==0) {
					openTile(x, y-1);
				}
			}
			if((x<columns-1)&&(y<rows-1)) {
				if(flagMap[x+1][y+1]==0) {
					openTile(x+1,y+1);
				}
			}
			if((x<columns-1)&&(y>0)) {
				if(flagMap[x+1][y-1]==0) {
					openTile(x+1,y-1);
				}
			}
			if((x>0)&&(y<rows-1)) {
				if(flagMap[x-1][y+1]==0) {
					openTile(x-1,y+1);
				}
			}
			if((x>0)&&(y>0)) {
				if(flagMap[x-1][y-1]==0) {
					openTile(x-1,y-1);
				}
			}
		}
	}
	
	
	public boolean tileMapContains(int tile) {
		for(int checkC=0;checkC<columns;checkC++) {
			for(int checkR = 0;checkR<rows;checkR++) {
				if(tileMap[checkC][checkR]==tile) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean flagMapContains(int tile) {
		for(int checkC=0;checkC<columns;checkC++) {
			for(int checkR = 0;checkR<rows;checkR++) {
				if(flagMap[checkC][checkR]==tile) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void openRandom() {
		int X=rnd(0,columns-1);
		int Y=rnd(0,rows-1);
		int bombs=panel.getBombs(X, Y);
		if((tileMap[X][Y]==0)&&(bombs<1)) {
			openTile(X, Y);
		}else {
			openRandom();
		}
	}
	
	
	protected int getTileX(int x) {
		int tileX=x/tileSize;
		if(tileX<0) {
			tileX=0;
		}else if(tileX>columns-1) {
			tileX=columns-1;
		}
		return tileX;
	}
	
	
	protected int getTileY(int y) {
		int tileY=y/tileSize;
		if(tileY<0) {
			tileY=0;
		}else if(tileY>rows-1) {
			tileY=rows-1;
		}
		return tileY;
	}
	
	
	public int rnd(int min,int max) {
		double gen = (Math.random() *((max - min) + 1)) + min;
		int result=(int)gen;
		return result;
	}
	
	
	private void setupGame() {
		tileSize=750/((columns+rows)/2);
		if(totalBombs<columns*rows/5) {
			totalBombs=columns*rows/5;
		}else if(totalBombs>columns*rows/2) {
			totalBombs=columns*rows/2;
		}
		flagMap=new int[columns][rows];
		tileMap=new int[columns][rows];
		for(int col=0;col<columns;col++) {
			for(int row=0;row<rows;row++) {
				flagMap[col][row]=0;
				tileMap[col][row]=0;
			}
		}
		for(int bomb=0;bomb<totalBombs;) {
			int generations=0;
			while(generations<500) {
				int bombX=rnd(0,columns-1);
				int bombY=rnd(0,rows-1);
				if(tileMap[bombX][bombY]==0) {
					tileMap[bombX][bombY]=1;
					bomb++;
					break;
				}
				generations++;
			}
		}
		window.setSize((tileSize+1)*columns+(tileSize/2),(tileSize+1)*rows+(tileSize/2));
		window.setResizable(false);
	}
	
	
	public void createWindow(String title,int Width,int Height) {
		window=new JFrame(title);
	    panel=new GameCanvas(this);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(panel);
		window.setSize(Width,Height);
		window.setResizable(true);
		window.setVisible(true);
		window.setBackground(Color.white);
		window.setAlwaysOnTop(true);
	}
	
	public void start(int speed) {
		framesCompleted=0;
		fps=speed;
		int rightTime=0, leftTime=0,wheelTime=0;
		while(true) {
			location = MouseInfo.getPointerInfo().getLocation();
			mouseX=(int) ((location.getX())-(window.getLocationOnScreen().getX()));
			mouseY=(int) ((location.getY())-(window.getLocationOnScreen().getY()));
			leftPressed=panel.leftPressed;
			if(leftPressed) {
				if(leftTime<fps/5) {
					leftClicked=true;
				}else {
					leftClicked=false;
					panel.leftPressed=false;
				}
				leftTime++;
			}else {
				leftClicked=false;
				leftTime=0;
			}
			rightPressed=panel.rightPressed;
			if(rightPressed) {
				if(rightTime<fps/5) {
					rightClicked=true;
				}else {
					rightClicked=false;
					panel.rightPressed=false;
				}
				rightTime++;
			}else {
				rightClicked=false;
				rightTime=0;
			}
			wheelPressed=panel.wheelPressed;
			if(wheelPressed) {
				if(wheelTime<fps/5) {
					wheelClicked=true;
				}else {
					wheelClicked=false;
					panel.wheelPressed=false;
				}
				wheelTime++;
			}else {
				wheelClicked=false;
				wheelTime=0;
			}
			try {
				panel.repaint();
				TimeUnit.MILLISECONDS.sleep(1000/fps);
				framesCompleted++;
				if(menu=="Main") {
					if(leftClicked) {
						if(mouseX>199&&mouseY>120&&mouseX<221&&mouseY<135) {
							menu="Setup";
						}
					}
				}else if(menu=="Setup") {
					if(leftClicked) {
						if(mouseX>29&&mouseY>49&&mouseX<61&&mouseY<84) {
							menu="Main";
						}else if(mouseX>380&&mouseY>365&&mouseX<475&&mouseY<480) {
							menu="Game";
							setupGame();
							openRandom();
							leftTime=fps/5;
						}else if(mouseX>109&&mouseY>99&&mouseX<401&&mouseY<156) {
							columns=(int)((mouseX-100)/5);
							if(columns<5) {
								columns=5;
							}else if(columns>45) {
								columns=45;
							}
						}else if(mouseX>109&&mouseY>239&&mouseX<401&&mouseY<256) {
							rows=(int)((mouseX-100)/5);
							if(rows<5) {
								rows=5;
							}else if(rows>45) {
								rows=45;
							}
						}
					}
				}else if(menu=="Game") {
					int x=getTileX(mouseX);
					int y=getTileY(mouseY);
					if(leftClicked) {
						if(flagMap[x][y]<2) {
							if(flagMap[x][y]==0) {
								 flagMap[x][y]=2;
								 if(tileMap[x][y]==1) {
									 menu="Game Over";
									 openAllTiles();
								 }else {
									 if(checkWin()) {
										 openAllTiles();
										 menu="Win";
									 }
								 }
							}
						 }
					} else if(rightClicked) {
						if(flagMap[x][y]<2) {
							 if(flagMap[x][y]==0) {
								 flagMap[x][y]=1;
							 }
						 }
					}else if(wheelClicked) {
						if(flagMap[x][y]<2) {
							 if(flagMap[x][y]==1) {
								 flagMap[x][y]=0;
							 }
						 }
					}
				}else if(menu=="Game Over"||menu=="Win") {
					if(wheelClicked) {
						menu="Game";
						setupGame();
						openRandom();
						leftTime=fps/5;
					}else if(rightClicked) {
						menu="Main";
						window.setSize(500,500);
						window.setResizable(true);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	public static void main(String[] args) {
		Game game=new Game();
		game.createWindow("MINER",500,500);
		game.start(50);
	}
    
}
