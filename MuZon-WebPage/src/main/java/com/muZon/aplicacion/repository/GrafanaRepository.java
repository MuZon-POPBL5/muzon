package com.muZon.aplicacion.repository;

import com.muZon.aplicacion.entity.GrafanaMetrics;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrafanaRepository extends CrudRepository<GrafanaMetrics, Integer>{

}
