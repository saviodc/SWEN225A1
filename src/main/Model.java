package main;

import java.util.List;

public interface Model{
  Camera camera();
  List<Entity> entities();
  void remove(Entity e);
  Cells cells();
  void onGameOver();
  void onNextLevel();
  void filterDead();//read explanation in Phase for method justification.
  default void ping(){
    entities().forEach(e->e.ping(this));
    filterDead();
    var end = entities().stream().noneMatch(e->e instanceof Monster);    
    if (end){ onNextLevel(); }
    }
  }