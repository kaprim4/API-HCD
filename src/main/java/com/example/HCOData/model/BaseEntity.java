package com.example.HCOData.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "ID", unique = true)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

}
