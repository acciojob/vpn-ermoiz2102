package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="admin")

public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<ServiceProvider>ServiceProviders= new ArrayList<>();

    public Admin() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ServiceProvider> getServiceProviders() {
        return ServiceProviders;
    }

    public void setServiceProviders(List<ServiceProvider> ServiceProviders) {
        this.ServiceProviders = ServiceProviders;
    }
}
