package com.dabkyu.dabkyu.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="category2")
@Table(name="category2")
@Builder
public class Category2Entity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY2_SEQ")	
	@SequenceGenerator(name="CATEGORY2_SEQ", sequenceName = "category2_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="category2_seqno")
	private Long category2Seqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="category1_seqno", nullable = false)
	private Category1Entity category1Seqno;
	
	@Column(name="category2_name", length=20, nullable=false)
	private String category2Name;
}
