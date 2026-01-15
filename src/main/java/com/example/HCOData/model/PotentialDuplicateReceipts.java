package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "duplicateReceipt")
public class PotentialDuplicateReceipts {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String uid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="image_id")
    @JsonIgnore
    private Image image;

    @OneToMany(mappedBy = "potentialDuplicateReceipts", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Flags> flags;
}
