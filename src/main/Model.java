package main;

import java.util.List;

public interface Model{
  Camera camera();
  List<Entity> entities();
  void remove(Entity e);
  Cells cells();
  void onGameOver();
  void onNextLevel();
  List<Monster> dead();
  default void ping(){
    entities().forEach(e->e.ping(this));
    var end = entities().stream().noneMatch(e->e instanceof Monster)&& dead().isEmpty();
    if (end){ onNextLevel(); }
    }
  }