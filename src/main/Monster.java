package main;

import java.awt.Dimension;
import java.awt.Graphics;

import imgs.Img;

class MonsterException extends Error{
	private static final long serialVersionUID = 1L;
	public MonsterException() {
        super("Dead Monster");
    }
	public MonsterException(String message) {
        super(message);
    }
}
interface MonsterState{
	 default Point location(Monster mon) {
		 return mon.location();
	 }
	 
	 void ping(Model m, Monster mon);
	 double chaseTarget(Monster outer, Point target, Monster mon);
	 default void draw(Graphics g, Point center, Dimension size, Monster mon) {
			mon.drawImg(Img.AwakeMonster.image, g, center, size);
	}
}
enum MonsterStates implements MonsterState{
	Awake(){
		public void ping(Model m, Monster mon) {
			var arrow = m.camera().location().distance(mon.location());
			double size = arrow.size();
			if(size>6) {mon.state = Sleep;return;}
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
		
		
	},
	Dead(){
		public void ping(Model m, Monster mon) {
			
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
		
	}, 
	Roaming(){
		Point goal = new Point(Math.random()*16, Math.random()*16);
		int progress = 0;
		public void ping(Model m, Monster mon) {
			var arrow = goal.distance(mon.location());;
			double toGoal = arrow.size();
			arrow = arrow.times(mon.speed() / toGoal);
			mon.location(mon.location().add(arrow));
			double toPlayer = m.camera().location().distance(mon.location()).size();
			if (toPlayer < 0.6d) {
				m.onGameOver();
				mon.state = Dead;
			}
			if(progress++ == 50 || toGoal < 0.6d) {
				goal = new Point(Math.random()*16, Math.random()*16);
				progress = 0;
			}
		}
		public double chaseTarget(Monster outer, Point target, Monster mon) {
			var arrow = target.distance(outer.location());
			double size = arrow.size();
			arrow = arrow.times(mon.speed() / size);
			outer.location(outer.location().add(arrow));
			return size;
		}
		
	}, 
	Boss(){
		Sword sword;
		public void ping(Model m, Monster mon) {
			var arrow = m.camera().location().distance(mon.location());
			double size = arrow.size();
			//if(size>6) {mon.state = Sleep;return;}
			arrow = arrow.times(mon.speed() / size);
			mon.location(mon.location().add(arrow));
			sword.ping(m);
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
		@Override
		public void assignSword(Monster m) {
			assert m.state==this;
			sword = new Sword(m) {
				public void onHit(Model m, Entity e){
					System.out.println("out");
				    if (e instanceof Camera){ m.remove(e); }
				    m.onGameOver();
				  }
				public double speed(){ return 0.4d; }
				public double distance(){ return 1.5d; }
				
			};
			sword.direction(Direction.Left);
		}
		public void draw(Graphics g, Point center, Dimension size, Monster mon) {
			sword.draw(g, center,size);
			//sword.drawImg(Img.Sword.image, g, center, size);
			mon.drawImg(Img.AwakeMonster.image, g, center, size);
	}
	};

	void assignSword(Monster monster) {throw new MonsterException("This Monster cannot weild sword");}

}

class Monster implements Entity {
	int onceDead = 0;
	MonsterStates state = MonsterStates.Sleep;
	private Point location;
	Monster(Point location) {
		this.location = location;
	}
	
	Monster(Point location, String s) {
		this.location = location;
		if(s.equals("roam")) {
			state = MonsterStates.Roaming;
		}else if (s.equals("boss")){
			state = MonsterStates.Boss;
			MonsterStates.Boss.assignSword(this);
		}else {
			throw new MonsterException();
		}
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