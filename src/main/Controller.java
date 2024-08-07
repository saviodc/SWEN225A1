package main;

import java.awt.event.KeyEvent;

class Controller extends Keys{
  Controller(Camera c, Sword s){
    setAction(KeyEvent.VK_W, c.set(Direction::up), c.set(Direction::unUp));
    setAction(KeyEvent.VK_S, c.set(Direction::down), c.set(Direction::unDown));
    setAction(KeyEvent.VK_A, c.set(Direction::left), c.set(Direction::unLeft));
    setAction(KeyEvent.VK_D, c.set(Direction::right), c.set(Direction::unRight));
    setAction(KeyEvent.VK_O, s.set(Direction::left), s.set(Direction::unLeft));
    setAction(KeyEvent.VK_P, s.set(Direction::right), s.set(Direction::unRight));
  }
}