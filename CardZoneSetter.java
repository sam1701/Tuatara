import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardZoneSetter
  extends JPanel
  implements MouseListener
{
  JLabel label;
  private static final long serialVersionUID = 1L;
  JFrame window = new JFrame();
  Point location;
  int zone;
  
  public CardZoneSetter(int zone)
    throws IOException
  {
    this.zone = zone;
    Image image = ImageIO.read(new File("test.png"));
    ImageIcon imageIcon = new ImageIcon(getScaledImage(image, Driver.camDimX, Driver.camDimY));
    this.label = new JLabel(imageIcon);
    add(this.label);
    addMouseListener(this);
    this.window.add(this);
    this.window.setResizable(true);
    this.window.pack();
    this.window.setVisible(false);
    this.window.setLocation(0, 0);
    setLocation(0, 0);
  }
  
  void refreshTestImage()
  {
    ImageIcon imageIcon = null;
    try
    {
      imageIcon = new ImageIcon(ImageIO.read(new File("test.png")));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    remove(this.label);
    this.label = new JLabel(imageIcon);
    
    add(this.label);
    revalidate();
  }
  
  public void mouseClicked(MouseEvent arg0)
  {
    this.location = getMousePosition();
    this.window.setVisible(false);
    Driver.refreshLocation(this.zone);
  }
  
  public void mouseEntered(MouseEvent arg0) {}
  
  public void mouseExited(MouseEvent arg0) {}
  
  public void mousePressed(MouseEvent arg0) {}
  
  public void mouseReleased(MouseEvent arg0) {}
  
  private Image getScaledImage(Image srcImg, int w, int h)
  {
    BufferedImage resizedImg = new BufferedImage(w, h, 2);
    Graphics2D g2 = resizedImg.createGraphics();
    
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(srcImg, 0, 0, w, h, null);
    g2.dispose();
    
    return resizedImg;
  }
}
