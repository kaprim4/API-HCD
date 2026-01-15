package com.example.HCOData.model;


import lombok.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "moderation_flags")
public class ModerationFlags {
	    @Id
	    @GeneratedValue(generator = "sequence-generator")
	    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
	            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
	            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
	    private Long id;
	    private String flag;

	    @ManyToMany
	    private List<Image> images;

	    public ModerationFlags(String Flag) {
	        this.flag = Flag;
	    }

}
