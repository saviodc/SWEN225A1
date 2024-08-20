package main;

import java.awt.Dimension;
import java.awt.Graphics;

import imgs.Img;

class MonsterException extends Error{
	private static final long serialVersionUID = 1L;
	
	public MonsterException(String message) {
        super(message);
    }
}

enum MonsterStates{
	Awake(){
		public void ping(Model m, Monster mon) {
			double size = chaseTarget(mon, m.camera().location());
			if(size>6) {mon.state = Sleep;return;}
			if (size < 0.6d) {
				mon.state = Dead;
				m.onGameOver();
			}
		}	
	},
	Dead(){
		public void ping(Model m, Monster mon) {
			mon.updateDeath();
		}
		public  double chaseTarget(Monster outer, Point target) {
			 throw new MonsterException("Dead Monster Chasing Call");
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

		public  void draw(Graphics g, Point center, Dimension size, Monster mon) {
			 mon.drawImg(Img.SleepMonster.image, g, center, size);
		 }
		
	}, 
	Roaming(){
		public void ping(Model m, Monster mon) {
			if (mon instanceof RoamingMonster roamingMon) {
				double size = chaseTarget(mon,roamingMon.goal);
				double toPlayer = m.camera().location().distance(mon.location()).size();
				if (toPlayer < 0.6d) {
					m.onGameOver();
					mon.state = Dead;
				}
				roamingMon.checkProgress(size);
			}else {
				throw new MonsterException("Calling on Boss error");
			}
		}
	}, 
	Boss(){
		public void ping(Model m, Monster mon) {
			if (mon instanceof BossMonster bossMon) {
				double size = chaseTarget(mon, m.camera().location());
				bossMon.sword().ping(m);
				if (size < 0.6d) {
					mon.state = Dead;
					m.onGameOver();
				}
			}else {
				throw new MonsterException("Calling on Boss error");
			}
		}

		public void draw(Graphics g, Point center, Dimension size, Monster mon) {
			if (mon instanceof BossMonster bossMon) {
				bossMon.sword().draw(g, center,size);
				mon.drawImg(Img.AwakeMonster.image, g, center, size);
			}else {
				throw new MonsterException("Calling on Boss error");
			}
	}
	};
	 Point location(Monster mon) {
		 return mon.location();
	 }
	 
	 abstract void ping(Model m, Monster mon);
	  void draw(Graphics g, Point center, Dimension size, Monster mon) {
			mon.drawImg(Img.AwakeMonster.image, g, center, size);
	}
	 public double chaseTarget(Monster outer, Point target) {
			var arrow = target.distance(outer.location());
			double size = arrow.size();
			arrow = arrow.times(outer.speed() / size);
			outer.location(outer.location().add(arrow));
			return size;
	}
	
}

class Monster implements Entity {
	int onceDead = 0;
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

	

	public void draw(Graphics g, Point center, Dimension size) {
		state.draw(g, center, size, this);
	}


	public boolean pingedOut() {
		assert state == MonsterStates.Dead;		
		return onceDead >=100;
	}


	void updateDeath() {
		assert state == MonsterStates.Dead;
		onceDead++;
		
	}
}



class RoamingMonster extends Monster{
	private int progress = 0;
	public Point goal = new Point(Math.random()*16, Math.random()*16);
	RoamingMonster(Point location){
		super(location);
		super.state = MonsterStates.Roaming;
	}
	public void checkProgress(double size ) {
		if(progress++ >= 50 || size < 0.6d) {
			goal = new Point(Math.random()*16, Math.random()*16);
			progress = 0;
		}
	}
}

class BossMonster extends Monster{
	Sword sword;
	BossMonster(Point location) {
		super(location);
		super.state = MonsterStates.Boss;
		assignSword();
	}

	private void assignSword() {
		 sword = new Sword(this) {
			public void onHit(Model m, Entity e){
			    if (e instanceof Camera){ m.remove(e); }
			    m.onGameOver();
			  }
			public double speed(){ return 0.4d; }
			public double distance(){ return 1.5d; }
			
		};
		sword.direction(Direction.Left);
	}
	public Sword sword() {
		return sword;
	}
}

