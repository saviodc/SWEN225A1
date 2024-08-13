package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class Compact extends JFrame{
  private static final long serialVersionUID= 1L;
  Runnable closePhase= ()->{};
  Phase currentPhase;
  
  Compact(){
    assert SwingUtilities.isEventDispatchThread();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    phaseZero();
    setVisible(true);
    addWindowListener(new WindowAdapter(){
      public void windowClosed(WindowEvent e){ closePhase.run(); }
    });
  }
  private void phaseZero() {
    var welcome= new JLabel("Welcome to Compact. A compact Java game!");
    var start= new JButton("Start!");
    var keys = new KeyBindDisplay();
    closePhase.run();
    closePhase = ()->{
     remove(welcome);
     remove(start);
     remove(keys);
     };

    add(BorderLayout.CENTER, welcome);
    add(BorderLayout.SOUTH, start);
    add(BorderLayout.NORTH, keys);
    start.addActionListener(e->phaseOne());
    setPreferredSize(new Dimension(800, 400));
    pack();
  }
  
  private void phaseEnd() {
	  var message = new JLabel("Congratulations");
	  var close = new JButton("Close");
	  close.addActionListener(e->this.dispose());
	  closePhase.run();
	  close.setMaximumSize(new Dimension(10,10));
	  message.setHorizontalAlignment(SwingConstants.CENTER);
	  add(BorderLayout.CENTER, message);
	  add(BorderLayout.SOUTH, close);
	  pack();
	  
  }
  private void phaseOne(){
    setPhase(Phase.level(()->phaseTwo(), ()->phaseZero(), List.of(new Monster(new Point(0, 0)))));
  }
  private void phaseTwo() {
	  setPhase(Phase.level(()->phaseThree(), ()->phaseZero(), List.of(new Monster(new Point(0, 0), "roam"), new Monster(new Point(13,13)), new Monster(new Point(0,13)), new Monster(new Point(13,0)))));
  }
  private void phaseThree() {
	  setPhase(Phase.level(()->phaseEnd(), ()->phaseZero(), List.of(new Monster(new Point(0, 0), "boss"))));
  }
  void setPhase(Phase p){
    //set up the viewport and the timer
    Viewport v= new Viewport(p.model());
    v.addKeyListener(p.controller());
    v.setFocusable(true);
    Timer timer= new Timer(34, unused->{
      assert SwingUtilities.isEventDispatchThread();
      p.model().ping();
      v.repaint();
    });
    closePhase.run();//close phase before adding any element of the new phase
    closePhase = ()->{ timer.stop(); remove(v); };
    add(BorderLayout.CENTER, v);//add the new phase viewport
    setPreferredSize(getSize());//to keep the current size
    pack();                     //after pack
    v.requestFocus();//need to be after pack
    timer.start();
  }
}

/**
 * Class for key binding setter by button
 * On-click prompts a JOption pane
 */
class KeyButton extends JButton{
	
	private static final long serialVersionUID = 1L;
	//public static final List<Character> keys = new ArrayList<>(List.of('w','a','s','d','o','p'));
	public String key, act;
	KeyButton(String key, String act){
		super(act + ": " +key, null);
		this.key = key;
		this.act = act;
	}
	public static Compact frame;
	
	void updateKey(String key) {
		this.key = key;
	}
	
	boolean matches(String s) {
		return s.equals(key);
	}
	String key() {return key;}
	
}

@SuppressWarnings("serial")
class KeyBindDisplay extends JPanel{
	public static final List<KeyButton> setKeys; 
	static {
		setKeys = new ArrayList<>(List.of(new KeyButton("w", "Up"), new KeyButton("a", "Left"),new KeyButton("s", "Down"),new KeyButton("d", "Right"),new KeyButton("o", "Sword Left"),new KeyButton("p", "Sword Right")));
		
	}
	KeyBindDisplay(){
		super(new GridLayout(3, 2, 5, 5));
		setKeys.forEach(key->{
			key.addActionListener(e->{
				char old = key.key.charAt(0);
				String newKey = key.key;
				List<String> strCheck = setKeys.stream().map(KeyButton::key).toList();
				while(newKey.length()>1||newKey.isEmpty()||strCheck.contains(newKey)) {
					newKey = JOptionPane.showInputDialog(KeyButton.frame,"New Key for "+ key.act +": ", "Input single char", JOptionPane.QUESTION_MESSAGE);
					if(newKey == null)return;
				}
				key.updateKey(newKey);
				key.setText(key.act + ": " +newKey);
				Controller.keys.set(Controller.keys.indexOf(old), newKey.charAt(0)); 
			});
			this.add(key);
		});
		
	}
	
}
