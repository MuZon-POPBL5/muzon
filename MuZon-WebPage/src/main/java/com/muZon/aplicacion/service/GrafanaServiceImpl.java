package com.muZon.aplicacion.service;

import com.muZon.aplicacion.entity.GrafanaMetrics;

import com.muZon.aplicacion.repository.GrafanaRepository;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class GrafanaServiceImpl implements GrafanaService{

	@Autowired
	GrafanaRepository repository;

    @Override
    public GrafanaMetrics saveGrafanaMetrics(GrafanaMetrics grafanaMetrics) throws Exception {
        grafanaMetrics = repository.save(grafanaMetrics);
		return grafanaMetrics;
    }

   /* @Override
    public int loadCount() throws Exception {
        GrafanaMetrics grafanaM;
        grafanaM=repository.findById(1).orElseThrow(() -> new Exception("El usuario no existe."));
        
        return grafanaM.getloginCounts();
    }
    */

    
}

