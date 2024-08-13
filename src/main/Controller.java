package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

class Controller extends Keys{
	public static List<Character> keys = new ArrayList<>(List.of('w','s','a','d','o','p'));
  Controller(Camera c, Sword s){
	int i = 0;
    setAction(KeyEvent.getExtendedKeyCodeForChar(keys.get(i++)), c.set(Direction::up), c.set(Direction::unUp));
    setAction(KeyEvent.getExtendedKeyCodeForChar(keys.get(i++)), c.set(Direction::down), c.set(Direction::unDown));
    setAction(KeyEvent.getExtendedKeyCodeForChar(keys.get(i++)), c.set(Direction::left), c.set(Direction::unLeft));
    setAction(KeyEvent.getExtendedKeyCodeForChar(keys.get(i++)), c.set(Direction::right), c.set(Direction::unRight));
    setAction(KeyEvent.getExtendedKeyCodeForChar(keys.get(i++)), s.set(Direction::left), s.set(Direction::unLeft));
    setAction(KeyEvent.getExtendedKeyCodeForChar(keys.get(i++)), s.set(Direction::right), s.set(Direction::unRight));
  }
  
 
}
