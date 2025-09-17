package com.tustosc.setsail.Entiy;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class Role implements GrantedAuthority {

    private String name;
    private Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
    @Override
    public String toString(){
        return name;
    }

    public static Role from(String name){
        if(name == null || name.isEmpty()){
            return new Role("ROLE_USER");
        }
        if(name.equals("ROLE_ADMIN")){
            return new Role("ROLE_ADMIN");
        }
        else{
            return new Role("ROLE_USER");
        }
    }
    public static final Role admin() {
        return new Role("ROLE_ADMIN");
    }
    public static final Role user() {
        return new Role("ROLE_USER");
    }

}
