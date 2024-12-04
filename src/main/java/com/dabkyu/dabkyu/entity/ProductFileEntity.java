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
@Entity(name="productFile")
@Table(name="product_file")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFileEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTFILE_SEQ")
	@SequenceGenerator(name="PRODUCTFILE_SEQ", sequenceName = "product_file_seq", initialValue = 1, allocationSize = 1)
    @Column(name="productfile_seqno", nullable=false)
    private Long productFileSeqno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="product_seqno", nullable = false)
	private ProductEntity productSeqno;
	
    @Column(name="org_filename", length=2000, nullable=false)
	private String orgFilename;
	
	@Column(name="stored_filename", length=2000, nullable=false)
	private String storedFilename;

	@Column(name="is_thumb", length=2,nullable=false)
	private String isThumb;

}
