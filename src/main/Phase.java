package main;

import java.util.ArrayList;
import java.util.List;


record Phase(Model model, Controller controller){ 
	/**
	 * Rewritten static method for abstraction of making any level.
	 *
	 * @param next - next phase.
	 * @param first - reset phase.
	 * @param Mons - Varying factor between levels.
	 * @return - Phase created for game.
	 */
  static Phase level(Runnable next, Runnable first, List<Entity> Mons) {
    Camera c= new Camera(new Point(5, 5));
    Sword s= new Sword(c);
    Cells cells= new Cells();
    List<Entity> allEntities= new ArrayList<Entity>(List.of(c, s));
    allEntities.addAll(Mons);
    var m= new Model(){
      List<Entity> entities= allEntities;
      public Camera camera(){ return c; }
      public List<Entity> entities(){ return entities; }
      /**
       * Removal of entity, additional functionality for allowing for death ping buffer.
       * After a while of considerations, I decided to solve this with a single collection,
       * and just affect the state of the monster, utilizing the pattern.
       *
       * @param e - entity to remove.
       */
      public void remove(Entity e){ 
    	  if(e instanceof Monster) {
    		  entities.stream().filter(en->en.equals(e)).map(mon->(Monster)mon).forEach(m->m.state=MonsterStates.Dead);
    	  }else {
       entities.stream()
          .filter(ei->!ei.equals(e))
          .toList();
    	 }
      }
      public Cells cells(){ return cells; }
      public void onGameOver(){ first.run(); }
      public void onNextLevel(){ next.run(); }
      /**
       * Additional method, had to be defined here to affect the collection itself, 
       * defining in interface as default could lead to future problems,
       * if user of library wanted to keep dead around in game.
       */
      public void filterDead() {
   	   entities.removeAll(entities.stream().filter(e->e instanceof Monster).map(m->(Monster) m).filter(m->m.state==MonsterStates.Dead).filter(m->m.pingedOut()).toList());
     }
      
    };
    return new Phase(m, new Controller(c, s));    
  }
}