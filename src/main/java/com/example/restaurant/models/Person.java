package com.example.restaurant.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "login")
    @NotEmpty
    private String login;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "customer")
    private List<MenuOrder> orders;

    public Person() {
    }

    public Person(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public List<MenuOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<MenuOrder> orders) {
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
