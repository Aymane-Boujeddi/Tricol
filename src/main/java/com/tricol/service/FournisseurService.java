package com.tricol.service;

import com.tricol.entity.Fournisseur;
import java.util.List;
import java.util.Optional;

public interface FournisseurService {
    List<Fournisseur> findAll();
    Optional<Fournisseur> findById(Long id);
    Fournisseur save(Fournisseur fournisseur);
    void deleteById(Long id);
}