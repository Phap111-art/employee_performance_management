package com.example.springemployee.mapper;

public interface EntityMapper<E,D> {
    E toEntity(D dto);
    D toDto(E entity);
}
