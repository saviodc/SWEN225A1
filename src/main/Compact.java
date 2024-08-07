package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    closePhase.run();
    closePhase = ()->{
     remove(welcome);
     remove(start);
     };
    add(BorderLayout.CENTER, welcome);
    add(BorderLayout.SOUTH, start);
    start.addActionListener(e->phaseOne());
    setPreferredSize(new Dimension(800, 400));
    pack();
  }
  private void phaseOne(){
    setPhase(Phase.level1(()->phaseOne(), ()->phaseZero()));
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