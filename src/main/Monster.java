package main;

import java.awt.Dimension;
import java.awt.Graphics;
import imgs.Img;

abstract class Monster implements Entity {
	static class MonsterException extends Error{
		private static final long serialVersionUID = 1L;
		public MonsterException(String message) {
	        super(message);
	    }
	}
	private Point location;

	public Point location() {
		return location;
	}

	public void location(Point p) {
		location = p;
	}

	Monster(Point location) {
		this.location = location;
	}

	public double speed() {
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

	class AliveMonster extends Monster {

		AliveMonster(Point location) {
			super(location);
		}
		@Override
		public void draw(Graphics g, Point center, Dimension size) {
			drawImg(Img.AwakeMonster.image, g, center, size);
		}
	}
	
	class SleepMonster extends Monster {

		SleepMonster(Point location) {
			super(location);
		}
		@Override
		public void draw(Graphics g, Point center, Dimension size) {
			drawImg(Img.SleepMonster.image, g, center, size);
		}
	}

	class DeadMonster extends Monster {
		
		DeadMonster(Point location) {
			super(location);
		}
		
		@Override
		public Point location() {
			throw new MonsterException("Dead Monster");
		}

		public void location(Point p) {
			throw new MonsterException("Dead Monster");
		}

		public double speed() {
			throw new MonsterException("Dead Monster");		
		}

		public void ping(Model m) {
			throw new MonsterException("Dead Monster");
		}

		public double chaseTarget(Monster outer, Point target) {
			throw new MonsterException("Dead Monster");
		}

		public void draw(Graphics g, Point center, Dimension size) {
			drawImg(Img.DeadMonster.image, g, center, size);
		}
	}

}