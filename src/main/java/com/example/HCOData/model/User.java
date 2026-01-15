package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseEntity{
    @NotBlank
    @Column(name = "s_user_name", unique = true, nullable = false)
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "s_password")
    private String password;
    
    @Column(name = "d_creation")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dCreation;
    
    @Column(name = "is_First_cnx")
	private boolean isFirstConx;

    @OneToMany(mappedBy = "user")
            @ToString.Exclude
    Collection<Image> images;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role" , joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles;

    @Column(name = "account_blocked")
    private boolean accountBlocked;

    public User(String username,String password) {
		this.username = username;
		this.password = password;
	}
    
    public User(Set<Role> roles) {
		this.roles = roles;
	}
    
    
   
    
    

    
   
    
    

}
