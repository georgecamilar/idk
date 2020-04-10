package com.controller;

import com.model.Arbiter;
import com.model.Nota;
import com.model.Participant;

import java.io.Serializable;
import java.util.List;

public interface Service extends Serializable {
    Integer login(String username, String password);

    Nota saveNota(Nota nota);

    List<Participant> getAllTotalScores();

    List<Nota> getRaportList(Integer id);
}
