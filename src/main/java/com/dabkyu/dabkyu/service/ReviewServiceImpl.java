package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.ReviewDTO;
import com.dabkyu.dabkyu.dto.ReviewFileDTO;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberReviewLikeEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.MemberReviewLikeRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewFileRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
	private final ReviewFileRepository reviewFileRepository;
	private final MemberRepository memberRepository;
	private final MemberReviewLikeRepository memberReviewLikeRepository;

    //상품 리뷰 보기
	@Override
	public Page<ReviewEntity> list(int pageNum, int postNum, String keyword) throws Exception {
		//페이징 기준을 설정 --> 시작점, 증가분, 정렬 방식
		// (시작페이지 --> 0부터 시작, 한 화면에 보이는 행의 수, 정렬기준(Sort.by)
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"reviewSeqno"));
		return reviewRepository.findByEmailContainingOrRevContentContaining(keyword, keyword, pageRequest);
	}

	//리뷰 내용 보기
	@Override
	public ReviewDTO view(Long reviewSeqno) throws Exception {
		return reviewRepository.findById(reviewSeqno).map(view -> new ReviewDTO(view)).get();
		
	}

	//리뷰 내용 이전 보기
	@Override
	public Long pre_seqno(Long reviewSeqno,String keyword) throws Exception {
		return reviewRepository.pre_seqno(reviewSeqno, keyword, keyword)==null?0:reviewRepository.pre_seqno(reviewSeqno, keyword, keyword);	
	}

	//리뷰 내용 다음 보기
	@Override
	public Long next_seqno(Long reviewSeqno,String keyword) throws Exception {
		return reviewRepository.next_seqno(reviewSeqno, keyword, keyword)==null?0:reviewRepository.next_seqno(reviewSeqno, keyword, keyword);
	}
	
    //게시물 등록 하기
	@Override
	public void write(ReviewDTO review) throws Exception {
		review.setRevDate(LocalDateTime.now());
		reviewRepository.save(review.dtoToEntity(review));
	}
	
	//max seqno 구하기
	@Override
	public Long getMaxSeqno(String email) throws Exception{
		return reviewRepository.getMaxSeqno(email);
	}
	
	//첨부파일 정보 등록하기
	@Override
	public void fileInfoRegistry(ReviewFileDTO reviewFileDTO) throws Exception{
		reviewFileRepository.save(reviewFileDTO.dtoToEntity(reviewFileDTO));
	}
	
	//리뷰 상세 페이지에서 첨부된 파일 목록 보기
	@Override
	public List<ReviewFileDTO> fileListView(Long reviewSeqno) throws Exception{
		List<ReviewFileDTO> fileDTOs = new ArrayList<>();

		reviewFileRepository.findByReviewSeqno(reviewRepository.findById(reviewSeqno).get()).stream().forEach(list-> fileDTOs.add(new ReviewFileDTO(list)));
		return fileDTOs;
	}
	
	
	//게시물 삭제 하기
	@Override
	public void delete(Long reviewSeqno) throws Exception {
		ReviewEntity reviewEntity = reviewRepository.findById(reviewSeqno).get();
		reviewRepository.delete(reviewEntity);	
	}
	
	//게시물 삭제 시 게시물에 포함된 첨부 파일 전체 삭제
	@Override
	public void deleteFileList(Map<String, Object> data) throws Exception{
		
		ReviewFileEntity fileEntity = null;
		List<ReviewFileEntity> fileEntities = null;
		
		if(data.get("kind").equals("B")) { //전체파일삭제
			fileEntities = reviewFileRepository.findByReviewSeqno(reviewRepository.findById((Long)data.get("reviewSeqno")).get());	
			if (fileEntities != null && !fileEntities.isEmpty()) {
				reviewFileRepository.deleteAll(fileEntities);
			}
		
		}		
			
	}

	//도움이되었어요 체크 여부 확인
	@Override
	public MemberReviewLikeEntity likeCheckView(Long reviewSeqno,String email) throws Exception {

		ReviewEntity reviewEntity = reviewRepository.findById(reviewSeqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		return memberReviewLikeRepository.findBySeqnoAndEmail(reviewEntity,memberEntity);
	}
	
	//도움이되었어요 체크 등록
	@Override
	public void likeCheckRegistry(Long reviewSeqno, String email) throws Exception{
		
		ReviewEntity reviewEntity = reviewRepository.findById(reviewSeqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		MemberReviewLikeEntity memberReviewLikeEntity = MemberReviewLikeEntity.builder()
																			  .reviewSeqno(reviewEntity.getReviewSeqno())
																			  .email(memberEntity)
																			  .regdate(LocalDateTime.now())
																			  .build();
		memberReviewLikeRepository.save(memberReviewLikeEntity);		
	}
	
	//도움이되었어요 취소
	@Override
	public void likeCheckUpdate(Long seqno, String email) throws Exception{
		ReviewEntity reviewEntity = reviewRepository.findById(seqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		MemberReviewLikeEntity memberReviewLikeEntity = memberReviewLikeRepository.findBySeqnoAndEmail(reviewEntity, memberEntity);
		memberReviewLikeRepository.delete(memberReviewLikeEntity);
	}
	
	//리뷰 도움이되었어요 갯수 수정
	@Override
	public void reviewLikeUpdate(ReviewDTO review) throws Exception{
		ReviewEntity reviewEntity = reviewRepository.findById(review.getReviewSeqno()).get();
		reviewEntity.setLikecnt(review.getLikecnt());
		reviewRepository.save(reviewEntity);
	}

}
