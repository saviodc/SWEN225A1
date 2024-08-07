package main;

import java.awt.Dimension;
import java.awt.Graphics;
import imgs.Img;

class Monster implements Entity{
  private Point location;
  public Point location(){ return location; }
  public void location(Point p){ location = p; }
  Monster(Point location){ this.location= location; }
  public double speed(){ return 0.05d; }

  public void ping(Model m){
    var arrow= m.camera().location().distance(location);
    double size= arrow.size();
    arrow = arrow.times(speed() / size);
    location = location.add(arrow); 
    if (size < 0.6d){ m.onGameOver(); }
  }
 
  public double chaseTarget(Monster outer, Point target){
    var arrow= target.distance(outer.location());
    double size= arrow.size();
    arrow = arrow.times(speed() / size);
    outer.location(outer.location().add(arrow));
    return size;
  }
  public void draw(Graphics g, Point center, Dimension size) {
    drawImg(Img.AwakeMonster.image, g, center, size);
  }
}