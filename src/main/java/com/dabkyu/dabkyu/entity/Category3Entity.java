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
@Entity(name="category3")
@Table(name="category3")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category3Entity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY3_SEQ")	
	@SequenceGenerator(name="CATEGORY3_SEQ", sequenceName = "category3_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="category3_seqno")
	private Long category3Seqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="category2_seqno", nullable = false)
	private Category2Entity category2Seqno;
	
	@Column(name="category3_name", length=20, nullable=false)
	private String category3Name;

	@Column(name="is_temporary", nullable=false, columnDefinition="boolean default false")
	@Builder.Default
	private boolean isTemporary = false;
}
