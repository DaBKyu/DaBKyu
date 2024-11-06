package com.dabkyu.dabkyu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="category1")
@Table(name="category1")
@Builder
public class Category1Entity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY1_SEQ")	
	@SequenceGenerator(name="CATEGORY1_SEQ", sequenceName = "category1_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="category1_seqno")
	private Long category1Seqno;
	
	@Column(name="category1_name", length=20, nullable=false)
	private String category1Name;
	

}
