package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;

import com.dabkyu.dabkyu.entity.VisitorLogEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class VisitorLogDTO {
    private Long VisitorLogSeqno;
    private String ipAddress;
    private LocalDateTime visitDate;
   
    public VisitorLogDTO(VisitorLogEntity entity){
        this.VisitorLogSeqno = entity.getVisitorLogSeqno();
        this.ipAddress = entity.getIpAddress();
        this.visitDate = entity.getVisitDate();
    }

    public VisitorLogEntity dtoToEntity(VisitorLogDTO dto){
        VisitorLogEntity entity = VisitorLogEntity.builder()
                                                  .VisitorLogSeqno(dto.getVisitorLogSeqno())
                                                  .ipAddress(dto.getIpAddress())
                                                  .visitDate(dto.getVisitDate())
                                                  .build();
        return entity;
    }
}
