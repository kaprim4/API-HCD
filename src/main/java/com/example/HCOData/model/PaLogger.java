package com.example.HCOData.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.TypeDef;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "PaLogger")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class PaLogger {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@Column(name = "ID", unique = true, nullable = false)
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

    @Column(name = "date_reception")
    private LocalDateTime dateReception;
	
	@Column(name = "image_name")
	private String imageName;

	@Column(name = "operation_uid")
	private String operation;

	@Column(name = "status")
	private String status;

	private String imageUid;

	@Column(columnDefinition = "jsonb")
	@Type(type = "jsonb")
	private String iaResponse;
	


}
