package com.example.demo.service;

import com.example.demo.DTO.LoginResponse;
import com.example.demo.modele.comptes;
import com.example.demo.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComptesService {
    public ComptesService(){}

    public ComptesService(CompteRepository compteRepository){
        this.compteRepository=compteRepository;
    }
    @Autowired
    private CompteRepository compteRepository;

    public List<comptes> getAllAccs() {
        return compteRepository.findAll();
    }
    public comptes findByUsername(String username){
        return compteRepository.findByUsername(username);
    }

    public Optional<comptes> getAccById(Long id) {
        return compteRepository.findById(id);
    }

    public comptes getAccountById(Long id) {
        return compteRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public comptes getAccountByusername(String username) {
        return compteRepository.findByUsername(username);
    }


    public boolean createCompte(comptes compte) {
        boolean res=false;
        try{
            compteRepository.save(compte);
            res=true;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return res;
    }
    public comptes updatecompte(int id, comptes newaccountData ) {
        comptes existingAccount = getAccountById((long)id);
        existingAccount.setUsername(newaccountData.getUsername());
        existingAccount.setEmail(newaccountData.getEmail());
        existingAccount.setPassword(newaccountData.getPassword());
        existingAccount.setPhone(newaccountData.getPhone());
        return compteRepository.save(existingAccount);
    }

    public boolean updateCompte(int id, comptes updatedCompte) {
        boolean res=false;
        try{
            compteRepository.save(updatedCompte);
            updatedCompte.setId(id);
            res=true;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return res;
    }

    public boolean deleteCompte(int id) {
        boolean res;
        if(compteRepository.existsById((long) id)) {
            compteRepository.deleteById((long) id);
            res=true;
        }
        else{

            res=false;
        }
        return res;
    }
    public LoginResponse authenticate(String email, String password) {
        comptes user = compteRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Debugging the role
        String role = user.getType();
        System.out.println("User role: " + role); // This will print the role in the server logs

        // Ensure the role is being compared correctly
        String token = role.equals("client") ? "client-token" : role.equals("admin") ? "admin-token" : null;

        if (token == null) {
            throw new IllegalArgumentException("Invalid role");
        }

        return new LoginResponse("Login successful", token);
    }

    public int countcompte() {
        return (int) compteRepository.count();
    }
    public int countcompteByType(String type) {
        return compteRepository.countByType  (type);
    }
    public int countcompteByFiliere(String fill) {
        return compteRepository.countByFiliere (fill);
    }

    public List<comptes> findByType(String type){
        return compteRepository.findAllByType(type);
    }
}
