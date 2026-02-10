package com.devaldrete.repositories;

import java.util.List;
import java.util.Optional;
import com.devaldrete.domain.Base;

public abstract class Repository<T extends Base> implements IRepository<T> {

  private List<T> entities;

  public Repository(List<T> entities) {
    this.entities = entities;
  }

  @Override
  public void deleteById(Long id) {
    this.entities.removeIf(b -> b.getId().equals(id));
  }

  @Override
  public List<T> getAll() {
    return this.entities;
  }

  @Override
  public Optional<T> getById(Long id) {
    return this.entities.stream()
        .filter(b -> b.getId().equals(id))
        .findFirst();
  }

  @Override
  public void save(T entity) {
    this.entities.add(entity);
  }

  @Override
  public void update(T entity) {
    this.getById(entity.getId()).ifPresent(existing -> {
      this.deleteById(existing.getId());
      this.save(entity);
    });
  }
}
