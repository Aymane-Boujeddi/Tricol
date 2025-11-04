package com.tricol.service.impl;

import com.tricol.entity.Fournisseur;
import com.tricol.repository.FournisseurRepository;
import com.tricol.service.FournisseurService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FournisseurServiceImpl implements FournisseurService {
    
    private FournisseurRepository repository;
    
    public void setRepository(FournisseurRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<Fournisseur> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Fournisseur> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Fournisseur save(Fournisseur fournisseur) {
        return repository.save(fournisseur);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }




}
