package com.example.HCOData.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
@EntityListeners(Auditable.class)
public class Image extends Auditable {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    private Long id;
    private String uid;
    private String dominantLanguage;
    private String imageType;
    private String status;
    private String processReport;
    private String imageUrl;
    private String name;
    private String endpointUid;
    private String campaignUid;
    private String buyHour;
    private String buyDate;
    private Long articleCount;
    private BigDecimal total;
    private BigDecimal totalMax;
    private String locale;
    private String city;
    private String sign;
    private String subSign;
    private String phone;
    private String address;
    private String postalCode;
    private String geoLocation;
    private String statusModeration;


    @JsonIgnore
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<BareCodes> barecodes;

    @JsonIgnore
    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Payement> payment;

    @ManyToOne( cascade = CascadeType.MERGE)
    private Operation operations;

    @JsonIgnore
    @OneToMany(mappedBy = "image",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Product> products;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "image")
    @ToString.Exclude
    private List<PotentialDuplicateReceipts> potentialDuplicateReceipts;

    public void removeChild(Product child) {
        this.products.remove(child);
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "images", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ModerationFlags> moderationFlagsList = new HashSet<>();

    @Column(name = "image_md5")
    private String imageMD5;

    private String pageCount;

    private String originImageType;

    @Column(name = "is_called")
    private boolean isCalled = false;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private long productSize = 0;


    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", dominantLanguage='" + dominantLanguage + '\'' +
                ", imageType='" + imageType + '\'' +
                ", status='" + status + '\'' +
                ", processReport='" + processReport + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", endpointUid='" + endpointUid + '\'' +
                ", campaignUid='" + campaignUid + '\'' +
                ", buyHour='" + buyHour + '\'' +
                ", buyDate='" + buyDate + '\'' +
                ", articleCount=" + articleCount +
                ", total=" + total +
                ", totalMax=" + totalMax +
                ", locale='" + locale + '\'' +
                ", city='" + city + '\'' +
                ", sign='" + sign + '\'' +
                ", subSign='" + subSign + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", geoLocation='" + geoLocation + '\'' +
                ", statusModeration='" + statusModeration + '\'' +
                ", imageMD5='" + imageMD5 + '\'' +
                ", pageCount='" + pageCount + '\'' +
                ", originImageType='" + originImageType + '\'' +
                ", operations=" + (operations != null ? operations.getUid() : "null") +  // Assuming Operation has a getId() method
                '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(uid, image.uid);
    }



}


