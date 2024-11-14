package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.dto.QuestionFileDTO;
import com.dabkyu.dabkyu.dto.ReviewDTO;
import com.dabkyu.dabkyu.dto.ReviewFileDTO;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.repository.QuestionFileRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionFileRepository questionFileRepository;

    // 문의 내역 보기
	@Override
	public Page<QuestionEntity> list(int pageNum, int postNum, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"queSeqno"));
		return questionRepository.findByQueTitleContainingOrEmail_EmailContainingOrQueContentContaining(keyword, keyword, keyword, pageRequest);
	}

    // 문의 내용 보기
	@Override
	public QuestionDTO view(Long queSeqno) throws Exception {
		return questionRepository.findById(queSeqno).map(view -> new QuestionDTO(view)).get();
		
	}

	// 문의 내용 이전 보기
	@Override
	public Long pre_seqno(Long queSeqno,String keyword) throws Exception {
		return questionRepository.pre_seqno(queSeqno, keyword, keyword, keyword)==null?0:questionRepository.pre_seqno(queSeqno, keyword, keyword, keyword);	
	}

	// 문의 내용 다음 보기
	@Override
	public Long next_seqno(Long queSeqno,String keyword) throws Exception {
		return questionRepository.next_seqno(queSeqno, keyword, keyword, keyword)==null?0:questionRepository.next_seqno(queSeqno, keyword, keyword, keyword);
	}
	
    // 문의 등록 하기
	@Override
	public void write(QuestionDTO question) throws Exception {
		question.setQueDate(LocalDateTime.now());
		questionRepository.save(question.dtoToEntity(question));
	}
	
	// max seqno 구하기
	@Override
	public Long getMaxSeqno(String email) throws Exception{
		return questionRepository.getMaxSeqno(email);
	}
	
	// 첨부파일 정보 등록하기
	@Override
	public void fileInfoRegistry(QuestionFileDTO questionFileDTO) throws Exception{
		questionFileRepository.save(questionFileDTO.dtoToEntity(questionFileDTO));
	}
	
	// 문의 상세 페이지에서 첨부된 파일 목록 보기
	@Override
	public List<QuestionFileDTO> fileListView(Long queSeqno) throws Exception{
		List<QuestionFileDTO> fileDTOs = new ArrayList<>();
		QuestionEntity questionEntity = questionRepository.findById(queSeqno).get();

		questionFileRepository.findByQueSeqno(questionEntity).stream().forEach(list-> fileDTOs.add(new QuestionFileDTO(list)));
		return fileDTOs;
	}
}
