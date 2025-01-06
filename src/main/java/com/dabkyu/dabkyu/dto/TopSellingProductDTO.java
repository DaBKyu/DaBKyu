package com.dabkyu.dabkyu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TopSellingProductDTO {
    private String productName;
    private long totalAmount;
    private String storedFilename; // 이미지 경로
    private Long productSeqno; // 제품 식별 번호


    public TopSellingProductDTO(String productName, long totalAmount,String storedFilename, Long productSeqno) {
        this.productName = productName;
        this.totalAmount = totalAmount;
        this.storedFilename = storedFilename;
        this.productSeqno = productSeqno;

    }


}
