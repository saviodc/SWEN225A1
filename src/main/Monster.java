package main;

import java.awt.Dimension;
import java.awt.Graphics;
import imgs.Img;

class MonsterException extends Error{
	private static final long serialVersionUID = 1L;
	public MonsterException() {
        super("Dead Monster");
    }
}
interface MonsterState{
	 default Point location(Monster mon) {
		 return mon.location();
	 }
	 
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
				mon.state = Dead;
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
	Dead(){
		public void ping(Model m, Monster mon) {
			throw new MonsterException();
		}
		public  double chaseTarget(Monster outer, Point target, Monster mon) {
			 throw new MonsterException();
		 }
		public  void draw(Graphics g, Point center, Dimension size, Monster mon) {
			 mon.drawImg(Img.DeadMonster.image, g, center, size);
		 }
		
	},
	Sleep(){
		public void ping(Model m, Monster mon) {
			var arrow = m.camera().location().distance(mon.location());
			double size = arrow.size();
			if(size <6d)mon.state = Awake;
		}
		public  double chaseTarget(Monster outer, Point target, Monster mon) {
			 return 0;
		 }
		public  void draw(Graphics g, Point center, Dimension size, Monster mon) {
			 mon.drawImg(Img.SleepMonster.image, g, center, size);
		 }
		
	};
}

class Monster implements Entity {
	 
	MonsterStates state = MonsterStates.Sleep;
	private Point location;
	Monster(Point location) {
		this.location = location;
	}

	public double speed() {
		return 0.05d;
	}
	public Point location(){
		return location;
	}

	public void location(Point p) {
		location=p;
	}


	public void ping(Model m) {
		state.ping(m, this);
	}

	public double chaseTarget(Monster outer, Point target) {
		return state.chaseTarget(outer, target, this);
	}

	public void draw(Graphics g, Point center, Dimension size) {
		state.draw(g, center, size, this);
	}
}