package com.devaldrete.repositories;

import java.util.Optional;
import java.util.List;

public interface IRepository<T> {
  public Optional<T> getById(Long id);

  public void save(T entity);

  public void deleteById(Long id);

  public List<T> getAll();

  public void update(T entity);
}
