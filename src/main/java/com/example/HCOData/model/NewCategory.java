package com.example.HCOData.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "newCategory")
public class NewCategory {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "categoryUid", columnDefinition = "VARCHAR(255)")
    private String category_uid;
    private String category_name;
    @Column(name = "isValidate")
    private Boolean isValidate = Boolean.FALSE;
}
