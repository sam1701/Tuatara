import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Driver implements ActionListener
{

	static Webcam webcam;
	Dimension[] res;
	WebcamPanel panel;
	JFrame camMenu = new JFrame();
	JFrame camWindow;
	JFrame menu;
	JPanel menuSetterPanel;
	static CardZone[] zone = new CardZone[10];
	static JButton[] zoneSetterB = new JButton[10];
	static JButton[] zoneGoToB = new JButton[10];
	static String[] zoneSetterDefaultText = new String[10];
	JButton[] zoneB = new JButton[10];
	JButton addZoneB;
	JButton getTestB;
	JButton clearScreen;
	JButton cycleThroughZonesB;
	static int camDimX;
	static int camDimY;
	private String[] args;
	private JPanel menuGoToPanel;
	private JTextField camXT;
	private static Scanner stdin=new Scanner(System.in);
	private JTextField camYT;

	public static void main(String[] args) throws InterruptedException{
		Object[] webcams= Webcam.getWebcams().toArray();
		
		for(Webcam webcam: Webcam.getWebcams()){
			System.out.println(webcam.getName());  
		}
		
		webcam=(Webcam) webcams[stdin.nextInt()];
		Driver d = new Driver(args);
		d.run();
	}
	
	public Driver(String[] args){
		this.camWindow = new JFrame("");
		this.menu = new JFrame("Menu");
		this.args = args;
	}
	
	public void run(){
		initializeMenu();
		displayMenu();
		this.camWindow.setLocation(0, 0);
		WebcamUtils.capture(webcam, "test", "PNG");
	}

	private void initializeMenu(){
		this.menuSetterPanel = new JPanel();
		this.menuGoToPanel = new JPanel();
		this.clearScreen = new JButton("Clear Screen");
		this.clearScreen.addActionListener(this);
		this.clearScreen.setActionCommand("clearScreen");
		this.menuGoToPanel.add(this.clearScreen);
		this.cycleThroughZonesB = new JButton("Cycle Through Zones");
		this.cycleThroughZonesB.addActionListener(this);
		this.cycleThroughZonesB.setActionCommand("cycleThroughZones");
		this.menuGoToPanel.add(this.cycleThroughZonesB);
		this.addZoneB = new JButton("Add Zone");
		this.addZoneB.addActionListener(this);
		this.addZoneB.setActionCommand("addZone");
		this.getTestB = new JButton("Get new test image.");
		this.getTestB.addActionListener(this);
		this.getTestB.setActionCommand("getNewTest");
		this.menuSetterPanel.add(this.getTestB);
		this.menuSetterPanel.add(this.addZoneB);
		this.menuSetterPanel.setLayout(new BoxLayout(this.menuSetterPanel, 1));
		this.menu.add(this.menuSetterPanel);
		this.menuGoToPanel.setLayout(new BoxLayout(this.menuGoToPanel, 1));
		this.menu.add(this.menuGoToPanel);
		this.menu.setDefaultCloseOperation(3);
		this.menu.setSize(1000, 500);
		this.menu.setLocation(1500, 0);
		this.menu.setLayout(new GridLayout());
		this.menu.revalidate();
		this.menu.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				Driver.this.onMenuExit();
			}
		});
		
		this.camXT = new JTextField(1);
		this.camXT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.camMenu.setLocation(1500,700);
		this.camMenu.setLayout(new GridLayout());
		this.camMenu.setSize(200, 300);
		this.camMenu.setVisible(true);
		this.camMenu.add(this.camXT);
		this.camYT = new JTextField(1);
		this.camYT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.camMenu.add(this.camYT);
		this.camMenu.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				Driver.this.onCamMenuExit();
			}
		});
	}
  
	protected void onMenuExit(){
		try{
			System.in.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
  
	public void initializeCam(int x, int y){
		webcam.close();
		res=new Dimension[]{WebcamResolution.HD720.getSize(),new Dimension(1920,1080)};
		
		webcam.setCustomViewSizes(res);
		webcam.setViewSize(new Dimension(x, y));
		
		this.panel = new WebcamPanel(webcam);
		this.panel.setFPSDisplayed(true);
		
		this.camWindow.add(this.panel);
		this.camWindow.setUndecorated(true);
		this.camWindow.setResizable(false);
		this.camWindow.pack();
		this.camWindow.setVisible(true);
		this.camWindow.setLocation(0, 0);
		webcam.open();
	}
  
	public void displayMenu(){
		this.menu.setVisible(true);
	}
  
	private void addCardZone(){
		zone[CardZone.getNumberOf()] = new CardZone(0, 0);
		JButton zoneSetter = new JButton("Zone " + CardZone.getNumberOf() + " Setter");
		zoneSetter.addActionListener(this);
		zoneSetter.setActionCommand("zone" + CardZone.getNumberOf());
		zoneSetterB[(CardZone.getNumberOf() - 1)] = zoneSetter;
		zoneSetterDefaultText[(CardZone.getNumberOf() - 1)] = zoneSetter.getText();
		this.menuSetterPanel.add(zoneSetter);
		this.menuSetterPanel.revalidate();
		
		JButton zoneGoTo = new JButton("Go to Zone " + CardZone.getNumberOf());
		zoneGoTo.addActionListener(this);
		zoneGoTo.setActionCommand("zong" + CardZone.getNumberOf());
		this.menuGoToPanel.add(zoneGoTo);
		this.menuGoToPanel.revalidate();
	}
  
	public void actionPerformed(ActionEvent e){
		String actionCommand = e.getActionCommand();
		System.out.println(actionCommand);
		if (actionCommand.substring(0, 4).equals("zone")){
			int i = Integer.valueOf(actionCommand.substring(4, actionCommand.length())).intValue();
			System.out.println(i);
			zone[(i - 1)].displayZoneSetter();
		}
		if (actionCommand.substring(0, 4).equals("zong")){
			int i = Integer.valueOf(actionCommand.substring(4, actionCommand.length())).intValue();
			System.out.println(i);
			goToZone(i - 1);
		}
		if (actionCommand.equals("addZone")) {
			addCardZone();
		}
		if (actionCommand.equals("clearScreen")) {
			this.camWindow.setLocation(-Integer.valueOf(this.args[0]).intValue(), -Integer.valueOf(this.args[1]).intValue());
		}
		if (actionCommand.equals("getNewTest")){
			WebcamUtils.capture(webcam, "test", "PNG");
			CardZone[] arrayOfCardZone;
			int j = (arrayOfCardZone = zone).length;
			for (int i = 0; i < j; i++){
				CardZone z = arrayOfCardZone[i];
				if (z != null) {
					z.setter.refreshTestImage();
				}
			}
		}
		if (actionCommand.equals("cycleThroughZones")) {
			cycleThroughZones();
		}
		
	}
  
	private void cycleThroughZones(){
		for (int i = 0; i < CardZone.getNumberOf(); i++){
			String s = stdin.nextLine();
			try{
				goToZone(i);
			}
			catch (Exception localException) {}
		}
		System.out.println("Done");
	}
  
	public static void refreshLocation(int zoneIndex){
		zone[zoneIndex].refreshLocation();
		zoneSetterB[zoneIndex].setText(zoneSetterDefaultText[zoneIndex] + " " + zone[zoneIndex].getX() + "," + zone[zoneIndex].getY());
	}
  
	private void goToZone(int zoneIndex){
		this.camWindow.setLocation(zone[zoneIndex].getX(), zone[zoneIndex].getY());
	}
  
	private void onCamMenuExit(){
		camDimX = Integer.valueOf(this.camXT.getText()).intValue();
		camDimY = Integer.valueOf(this.camYT.getText()).intValue();
		initializeCam(camDimX, camDimY);
	}
}
