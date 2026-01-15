package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "privilege")
public class Privilege extends BaseEntity{
    @Column(name = "s_name")
    private String name;

    @Column(name = "s_code",unique = true)
    private String code;

    @JsonIgnore
    @ManyToMany(mappedBy = "privileges",cascade = CascadeType.ALL)
    private Collection<Role> roles;

    public Privilege(String name,String code) {
        this.name=name;
        this.code=code;
    }


}
