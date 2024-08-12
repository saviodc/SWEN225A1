package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

class Controller extends Keys{
	public static List<Character> keys = new ArrayList<>(List.of('w','s','a','d','o','p'));
  Controller(Camera c, Sword s){
	int i = 0;
    setAction(charToKeyEvent(keys.get(i++)), c.set(Direction::up), c.set(Direction::unUp));
    setAction(charToKeyEvent(keys.get(i++)), c.set(Direction::down), c.set(Direction::unDown));
    setAction(charToKeyEvent(keys.get(i++)), c.set(Direction::left), c.set(Direction::unLeft));
    setAction(charToKeyEvent(keys.get(i++)), c.set(Direction::right), c.set(Direction::unRight));
    setAction(charToKeyEvent(keys.get(i++)), s.set(Direction::left), s.set(Direction::unLeft));
    setAction(charToKeyEvent(keys.get(i++)), s.set(Direction::right), s.set(Direction::unRight));
  }
  
  public static int charToKeyEvent(char c) {
	  return switch (c) {
      case 'a' -> KeyEvent.VK_A;
      case 'b' -> KeyEvent.VK_B;
      case 'c' -> KeyEvent.VK_C;
      case 'd' -> KeyEvent.VK_D;
      case 'e' -> KeyEvent.VK_E;
      case 'f' -> KeyEvent.VK_F;
      case 'g' -> KeyEvent.VK_G;
      case 'h' -> KeyEvent.VK_H;
      case 'i' -> KeyEvent.VK_I;
      case 'j' -> KeyEvent.VK_J;
      case 'k' -> KeyEvent.VK_K;
      case 'l' -> KeyEvent.VK_L;
      case 'm' -> KeyEvent.VK_M;
      case 'n' -> KeyEvent.VK_N;
      case 'o' -> KeyEvent.VK_O;
      case 'p' -> KeyEvent.VK_P;
      case 'q' -> KeyEvent.VK_Q;
      case 'r' -> KeyEvent.VK_R;
      case 's' -> KeyEvent.VK_S;
      case 't' -> KeyEvent.VK_T;
      case 'u' -> KeyEvent.VK_U;
      case 'v' -> KeyEvent.VK_V;
      case 'w' -> KeyEvent.VK_W;
      case 'x' -> KeyEvent.VK_X;
      case 'y' -> KeyEvent.VK_Y;
      case 'z' -> KeyEvent.VK_Z;
      case '0' -> KeyEvent.VK_0;
      case '1' -> KeyEvent.VK_1;
      case '2' -> KeyEvent.VK_2;
      case '3' -> KeyEvent.VK_3;
      case '4' -> KeyEvent.VK_4;
      case '5' -> KeyEvent.VK_5;
      case '6' -> KeyEvent.VK_6;
      case '7' -> KeyEvent.VK_7;
      case '8' -> KeyEvent.VK_8;
      case '9' -> KeyEvent.VK_9;
      default -> throw new IllegalArgumentException("Unsupported character: " + c);
  };
}
}
