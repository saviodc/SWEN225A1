package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
      Map<Monster, Integer> dead = new HashMap<>();
      public Camera camera(){ return c; }
      public Map<Monster, Integer> dead(){return dead;}
      public List<Entity> entities(){ return entities; }
      public void remove(Entity e){ 
        entities= entities.stream()
          .filter(ei->!ei.equals(e))
          .toList();
        if(e instanceof Monster mon) {
        	mon.state = MonsterStates.Dead;
        	dead.put(mon, 0);
        }
      }
      public Cells cells(){ return cells; }
      public void onGameOver(){ first.run(); }
      public void onNextLevel(){ next.run(); }
      public void filterDead() {
    	  dead = dead.entrySet().stream().filter(e->e.getValue()<100).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      }
    };
    return new Phase(m, new Controller(c, s));    
  }
}