package main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


record Phase(Model model, Controller controller){ 
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
      public void filterDead() {
   	   entities.removeAll(entities.stream().filter(e->e instanceof Monster).map(m->(Monster) m).filter(m->m.state==MonsterStates.Dead).filter(m->m.pingedOut()).toList());
     }
      
    };
    return new Phase(m, new Controller(c, s));    
  }
}