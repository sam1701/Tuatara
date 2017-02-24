import java.awt.Point;
import java.io.IOException;
import javax.swing.JFrame;

public class CardZone
{
  private static int numberOfZones = 0;
  private int x;
  private int y;
  CardZoneSetter setter;
  
  public CardZone(int x, int y)
  {
    this.x = x;
    this.y = y;
    try
    {
      this.setter = new CardZoneSetter(numberOfZones);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    numberOfZones += 1;
  }
  
  public int getX()
  {
    return -this.x;
  }
  
  public int getY()
  {
    return -this.y;
  }
  
  public void displayZoneSetter()
  {
    this.setter.window.setVisible(true);
  }
  
  public static int getNumberOf()
  {
    return numberOfZones;
  }
  
  public void refreshLocation()
  {
    this.x = (this.setter.location.x - 5);
    this.y = (this.setter.location.y - 5);
  }
}
