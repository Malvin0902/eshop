package id.ac.ui.cs.advprog.eshop.repository;

import java.util.List;

public interface IRepository<T> {
    T create(T item);
    List<T> findAll();
    T findById(String id);
    T update(T item);
    void delete(String id);
}