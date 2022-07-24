package Game;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class GameCanvas extends JComponent{
	public static int mouseX=0, mouseY=0;
	protected boolean leftPressed=false,rightPressed=false, wheelPressed=false;
	protected Image flagImage=new ImageIcon("img/Flag.png").getImage();
	protected Image blankImage=new ImageIcon("img/Blank.png").getImage();
	protected Image bombImage=new ImageIcon("img/Bomb.png").getImage();
	
	public int mod(int a,int b) {
		int result=a-(int)(Math.floor(a/b)*b);
		return result;
	}
	
	protected int getTile(int x,int y) {
		int tileX=0, tileY=0;
		if(x>game.columns-1) {
			tileX=game.columns-1;
		}else if(x<0) {
			tileX=0;
		}else {
			tileX=x;
		}
		if(y>game.rows-1) {
			tileY=game.rows-1;
		}else if(y<0) {
			tileY=0;
		}else {
			tileY=y;
		}
		int tile=game.tileMap[tileX][tileY];
		return tile;
	}

	
	public int getBombs(int x,int y) {
		int finded=0;
		if(x<game.columns-1) {
			if(getTile(x+1, y)==1) {
				finded++;
			}
		}
		if((y<game.rows-1)) {
			if(getTile(x, y+1)==1) {
				finded++;
			}
		}
		if((x>0)) {
			if(getTile(x-1, y)==1) {
				finded++;
			}
		}
		if((y>0)) {
			if(getTile(x, y-1)==1) {
				finded++;
			}
		}
		if((x<game.columns-1)&&(y<game.rows-1)) {
			if(getTile(x+1, y+1)==1) {
				finded++;
			}
		}
		if((x<game.columns-1)&&(y>0)) {
			if(getTile(x+1, y-1)==1) {
				finded++;
			}
		}
		if((x>0)&&(y<game.rows-1)) {
			if(getTile(x-1, y+1)==1) {
				finded++;
			}
		}
		if((x>0)&&(y>0)) {
			if(getTile(x-1, y-1)==1) {
				finded++;
			}
		}
		return finded;
	}
	
	
	protected void drawGrid(Graphics g) {
		int c=game.columns, r=game.rows;
		for(int column=0; column<c;column++) {
			for(int row=0;row<r;row++) {
				if(game.flagMap[column][row]==0) {
					g.drawImage(blankImage, column*game.tileSize, row*game.tileSize, game.tileSize, game.tileSize, getFocusCycleRootAncestor());
				} else if(game.flagMap[column][row]==1) {
					g.drawImage(flagImage, column*game.tileSize, row*game.tileSize, game.tileSize, game.tileSize, getFocusCycleRootAncestor());
				}else if(game.flagMap[column][row]==2) {
					if(game.tileMap[column][row]==1) {
						g.drawImage(bombImage, column*game.tileSize, row*game.tileSize, game.tileSize, game.tileSize, getFocusCycleRootAncestor());
					}else {
						int bombsAround=getBombs(column,row);
						if(bombsAround>0) {
							String draw=Integer.toString(bombsAround);
							int drawX=(int)((column+0.4)*game.tileSize);
							int drawY=(int)((row+0.5)*game.tileSize);
							g.drawString(draw,drawX,drawY);
						}
					}
				}
			}
		}
	}
	
	
	private static final long serialVersionUID = 1L;
	protected Game game;
	
	
	public	GameCanvas(Game game) {
		this.game=game;
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}
	
  @Override
  protected void paintComponent(Graphics g) {
	  super.paintComponent(g);
	  g.setColor(Color.BLACK);
	  if(game.menu=="Game"||game.menu=="Game Over"||game.menu=="Win") {
		  drawGrid(g);
		  if(game.menu=="Game Over") {
			  g.setColor(Color.RED);
			  g.drawString("Game Over", 155, 170);
			  g.drawString("Press Mouse Wheel to restart", 105, 200);
			  g.drawString("Press Right Mouse to restart", 105, 230);
		  }else if(game.menu=="Win") {
			  g.setColor(Color.RED);
			  g.drawString("You Win!", 155, 170);
			  g.drawString("Press Mouse Wheel to restart", 105, 200);
			  g.drawString("Press Right Mouse to restart", 105, 230);
		  }else {
			  drawPoint(g);
		  }
	  }else {	
		g.drawString(Integer.toString(game.mouseX),25,75);
      	g.drawString(Integer.toString(game.mouseY),25,100);
	  	if(game.menu=="Main") {
      		g.drawString("MINER",200,25);
      		g.drawString("Play",200,100);
      	}else if(game.menu=="Setup") {
    	  	g.drawString("Set your game",150,25);
    	  	g.drawString("Generate Game",375,450);
    	  	g.drawLine(25,37,50,25);
    	  	g.drawLine(25,37,50,50);
    	  	drawBox(g, 100, 100, 400, 125, Color.lightGray);
    	  	drawBox(g, 100+((game.columns-5)*(300/40)), 100, 125+((game.columns-5)*(300/40)), 125, Color.darkGray);
    	  	g.drawString(Integer.toString(game.columns),60,100);
    	  	drawBox(g, 100, 200, 400, 225, Color.lightGray);
    	  	drawBox(g, 100+((game.rows-5)*(300/40)), 200, 125+((game.rows-5)*(300/40)), 225, Color.darkGray);
    	  	g.drawString(Integer.toString(game.rows),60,200);
      	}
	  }
  }
  
  
  protected void drawPoint(Graphics g) {
	 int pointerX=game.mouseX/game.tileSize;
	 int pointerY=game.mouseY/game.tileSize;
	 int tileX=pointerX*game.tileSize;
	 int tileY=pointerY*game.tileSize;
	 g.setColor(Color.RED);
	 g.drawLine(tileX, tileY, tileX+game.tileSize, tileY+game.tileSize);
	 g.drawLine(tileX, tileY+game.tileSize, tileX+game.tileSize, tileY);
  }


@Override
  public void processMouseEvent(MouseEvent e) {
	  super.processMouseEvent(e);
	  if(e.getButton()==MouseEvent.BUTTON1) {
		  leftPressed=true;
	  }
	  if(e.getButton()==MouseEvent.BUTTON2) {
		  wheelPressed=true;
	  }
	  if(e.getButton()==MouseEvent.BUTTON3) {
		  rightPressed=true;
	  }
  }
	protected void drawBox(Graphics g, int x1, int y1, int x2, int y2, Color color) {
		g.setColor(color);
		for(int y=y1;y<y2;y++) {
			g.drawLine(x1, y, x2, y);
		}
}
  
}
