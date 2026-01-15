package com.example.HCOData.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "newBrand")
public class NewBrand {

	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "brandUid", columnDefinition = "VARCHAR(255)")
    private String Brand_uid;
    private String Brand_name;
    @Column(name = "Brand_uid_liaison")
    private String BrandUidLiaison;
    @Column(name = "isValidate")
    private Boolean isValidate = Boolean.FALSE;
}
