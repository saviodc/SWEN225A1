package main;

import java.util.List;
import java.util.Map;

public interface Model{
  Camera camera();
  List<Entity> entities();
  void remove(Entity e);
  Cells cells();
  void onGameOver();
  void onNextLevel();
  Map<Monster, Integer> dead();
  void filterDead();
  default void ping(){
    entities().forEach(e->e.ping(this));
    dead().entrySet().forEach(e->e.setValue(e.getValue()+1));
    filterDead();
    var end = entities().stream().noneMatch(e->e instanceof Monster)&& dead().isEmpty();
    if (end){ onNextLevel(); }
    }
  }