package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity{



    @Column(name = "s_role_name",unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<User> users;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "r_role_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id")

    )
    private Collection<Privilege> privileges;

    public Role(String name, Set<Privilege> privileges){
        this.name= name;
        this.privileges= privileges;
    }
    public Role(String name) {
		this.name = name;
	}
}
