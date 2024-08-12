package main;

import java.awt.Dimension;
import java.awt.Graphics;
import imgs.Img;

class MonsterException extends Error{
	private static final long serialVersionUID = 1L;
	public MonsterException(String s) {
        super("Dead Monster");
    }
}
interface MonsterState{
	 Point location();
	 //default double speed() {}
	 default void location(Point p, Monster m) {}
	 void ping(Model m, Monster mon);
	 double chaseTarget(Monster outer, Point target, Monster mon);
	 void draw(Graphics g, Point center, Dimension size, Monster mon);
}
enum MonsterStates implements MonsterState{
	Awake(){
		public void ping(Model m, Monster mon) {
			var arrow = m.camera().location().distance(mon.location());
			double size = arrow.size();
			arrow = arrow.times(mon.speed() / size);
			mon.location(mon.location().add(arrow));
			if (size < 0.6d) {
				m.onGameOver();
			}
		}
		public double chaseTarget(Monster outer, Point target, Monster mon) {
			var arrow = target.distance(outer.location());
			double size = arrow.size();
			arrow = arrow.times(mon.speed() / size);
			outer.location(outer.location().add(arrow));
			return size;
		}
		public void draw(Graphics g, Point center, Dimension size, Monster mon) {
			mon.drawImg(Img.AwakeMonster.image, g, center, size);
		}
	},
	Dead,
	Sleep;
}
//record AwakeMonster() implements MonsterState{}
//record DeadMonster() implements MonsterState{}
//record SleepMonster() implements MonsterState{}


class Monster implements Entity {
	 
	MonsterStates state = MonsterState.Sleep;
	private Point location;
	Monster(Point location) {
		this.location = location;
	}

	public double speed() {
	

	public Point location() {
		return location;
	}

	public void location(Point p) {
		location = p;
	}

		return 0.05d;
	}

	public void ping(Model m) {
		var arrow = m.camera().location().distance(location);
		double size = arrow.size();
		arrow = arrow.times(speed() / size);
		location = location.add(arrow);
		if (size < 0.6d) {
			m.onGameOver();
		}
	}

	public double chaseTarget(Monster outer, Point target) {
		var arrow = target.distance(outer.location());
		double size = arrow.size();
		arrow = arrow.times(speed() / size);
		outer.location(outer.location().add(arrow));
		return size;
	}

	public void draw(Graphics g, Point center, Dimension size) {
		drawImg(Img.AwakeMonster.image, g, center, size);
	}





}