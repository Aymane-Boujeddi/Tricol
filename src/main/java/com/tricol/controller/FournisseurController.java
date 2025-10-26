package com.tricol.controller;

import com.tricol.entity.Fournisseur;
import com.tricol.service.FournisseurService;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class FournisseurController extends AbstractController {
    
    private FournisseurService fournisseurService;
    private JsonMapper jsonMapper = JsonMapper.builder().build();
    
    public void setFournisseurService(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String path = request.getServletPath();
        String method = request.getMethod();
        
        System.out.println("Request: " + method + " " + path);
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Context Path: " + request.getContextPath());
        
        if (path.equals("/api/fournisseurs") && "GET".equals(method)) {
            getAllFournisseurs(response);
        } else if (path.startsWith("/api/fournisseur/") && "GET".equals(method)) {
            getOneFournisseur(request, response, path);
        } else if (path.equals("/api/fournisseurs/create") && "POST".equals(method)) {
            createFournisseur(request, response);
        } else if (path.startsWith("/api/fournisseurs/update/") && "POST".equals(method)) {
            updateFournisseurWithId(request, response, path);
        } else if (path.equals("/api/fournisseurs/update") && "POST".equals(method)) {
            updateFournisseur(request, response);
        } else if (path.startsWith("/api/fournisseurs/delete/") && "POST".equals(method)) {
            deleteFournisseur(request, response, path);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Endpoint not found: \" + method + \" \" + path + \"\"}");
        }
        return null;
    }
    
    private void getAllFournisseurs(HttpServletResponse response) throws Exception {
        List<Fournisseur> fournisseurs = fournisseurService.findAll();
        response.getWriter().write(jsonMapper.writeValueAsString(fournisseurs));
    }
    
    private void getOneFournisseur(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        String idParam = path.substring("/api/fournisseur/".length());
        Long id = Long.parseLong(idParam);
        Optional<Fournisseur> fournisseur = fournisseurService.findById(id);
        
        if (fournisseur.isPresent()) {
            response.getWriter().write(jsonMapper.writeValueAsString(fournisseur.get()));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Fournisseur not found\"}");
        }
    }
    
    private void createFournisseur(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = readRequestBody(request);
        Fournisseur fournisseur = jsonMapper.readValue(json, Fournisseur.class);
        
        String validationError = validateFournisseur(fournisseur);
        if (validationError != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + validationError + "\"}");
            return;
        }
        
        Fournisseur saved = fournisseurService.save(fournisseur);
        response.getWriter().write(jsonMapper.writeValueAsString(saved));
    }
    
    private void updateFournisseur(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = readRequestBody(request);
        Fournisseur fournisseur = jsonMapper.readValue(json, Fournisseur.class);
        
        String validationError = validateFournisseur(fournisseur);
        if (validationError != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + validationError + "\"}");
            return;
        }
        
        Fournisseur updated = fournisseurService.save(fournisseur);
        response.getWriter().write(jsonMapper.writeValueAsString(updated));
    }
    
    private void updateFournisseurWithId(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        String idParam = path.substring("/api/fournisseurs/update/".length());
        Long id = Long.parseLong(idParam);
        
        String json = readRequestBody(request);
        Fournisseur fournisseur = jsonMapper.readValue(json, Fournisseur.class);
        fournisseur.setId(id);
        
        String validationError = validateFournisseur(fournisseur);
        if (validationError != null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + validationError + "\"}");
            return;
        }
        
        Fournisseur updated = fournisseurService.save(fournisseur);
        response.getWriter().write(jsonMapper.writeValueAsString(updated));
    }
    
    private void deleteFournisseur(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
        String idParam = path.substring("/api/fournisseurs/delete/".length());
        Long id = Long.parseLong(idParam);
        fournisseurService.deleteById(id);
        response.getWriter().write("{\"message\": \"Fournisseur deleted successfully\"}");
    }
    
    private String validateFournisseur(Fournisseur fournisseur) {
        if (fournisseur.getSociete() == null || fournisseur.getSociete().trim().isEmpty()) {
            return "Societe is required";
        }
        if (fournisseur.getEmail() == null || fournisseur.getEmail().trim().isEmpty()) {
            return "Email is required";
        }
        if (!fournisseur.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email format";
        }
        if (fournisseur.getTelephone() == null || fournisseur.getTelephone().trim().isEmpty()) {
            return "Telephone is required";
        }
        if (fournisseur.getIce() == null || fournisseur.getIce().trim().isEmpty()) {
            return "ICE is required";
        }
        if (fournisseur.getIce().length() != 15) {
            return "ICE must be 15 characters";
        }
        return null;
    }
    
    private String readRequestBody(HttpServletRequest request) throws Exception {
        StringBuilder json = new StringBuilder();
        String line;
        try (java.io.BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        return json.toString();
    }
}
