package com.dabkyu.dabkyu.util;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PageUtil {

	//메인 페이지 조회
	public String getMainPageList(int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);  // 전체 페이지 수
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);  // 전체 섹션 수
		int section = (int)Math.ceil(pageNum/(double)pageListCount);  // 현재 섹션 번호
		String pageList = "";
	
		if(totalPage > 1) {
	
			for(int i = 1; i <= pageListCount; i++) {
				// 1. ◀ 출력조건 (section 값이 1보다 커야함)
				if(section > 1 && i == 1)
					pageList += " <a href='/shop/main?page=" + Integer.toString((section - 2) * pageListCount + pageListCount)
							+ "&keyword=" + keyword + "'>◀</a>";
	
				// 2. 페이지 출력 중단 (전체 페이지가 범위를 벗어나면 중단)
				if(totalPage < (section - 1) * pageListCount + i) { break; }
	
				// 3. 현재 페이지와 같으면 링크를 붙이지 않음, 다르면 다른 페이지로 이동할 수 있는 링크를 붙임
				if(pageNum != (section - 1) * pageListCount + i)
					pageList += " <a href='/shop/main?page=" + Integer.toString((section - 1) * pageListCount + i)
							+ "&keyword=" + keyword + "'>" + Integer.toString((section - 1) * pageListCount + i) + "</a>";
				else
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section - 1) * pageListCount + i) + "</span>";
	
				// 4. ▶ 출력조건 (section이 1 이상일 때, 페이지리스트의 끝에서 출력)
				if(i == pageListCount && totalSection > 1 && totalPage >= i + (section - 1) * pageListCount + 1)
					pageList += " <a href='/shop/main?page=" + Integer.toString(section * pageListCount + 1) + "&keyword=" + keyword + "'>▶</a>";
			}
		}
		return pageList;
	}
	

	//상품 목록 조회
	public String getProductList(int pageNum, int postNum, int pageListCount, int totalCount, 
	String keyword, Long category1Seqno, Long category2Seqno, Long category3Seqno) {

		//pageNum : 현재 페이지 번호
		//postNum : 한 화면에 보여지는 게시물 행 갯수
		//pageListCount : 하단 페이지 리스트에 보여질 페이지 갯수
		//totalCount : 전체 행갯수
		//totalPage : 전체 페이지 갯수
		//section : 한 개의 페이지 목록.현재 section 번호 예) 1 2 3 4 5 --> section 1, 6 7 8 9 10 --> section 2
		//totalSection : 전체 section 갯수

		int totalPage = (int)Math.ceil(totalCount / (double) postNum);
		int totalSection = (int)Math.ceil(totalPage / (double) pageListCount);
		int section = (int)Math.ceil(pageNum / (double) pageListCount);
		String pageList = "";

		if (totalPage != 1) {

		for (int i = 1; i <= pageListCount; i++) {
		// 1. ◀ 출력조건
		// - section 값이 1보다 커야함.
		// - i == 1
		if (section > 1 && i == 1)
		pageList += " <a href='/shop/main?page=" + Integer.toString((section - 2) * pageListCount + pageListCount)
				+ "&keyword=" + keyword
				+ "&category1Seqno=" + category1Seqno
				+ "&category2Seqno=" + category2Seqno
				+ "&category3Seqno=" + category3Seqno + ">◀</a>";

		// 2. 페이지 출력 중단
		if (totalPage < (section - 1) * pageListCount + i) { 
		break; 
		}

		// 3. 현재 페이지와 다른 페이지를 구분하여 링크를 생성
		if (pageNum != (section - 1) * pageListCount + i)
		pageList += " <a href='/shop/main?page=" + Integer.toString((section - 1) * pageListCount + i)
				+ "&keyword=" + keyword
				+ "&category1Seqno=" + category1Seqno
				+ "&category2Seqno=" + category2Seqno
				+ "&category3Seqno=" + category3Seqno + ">"
				+ Integer.toString((section - 1) * pageListCount + i) + "</a>";
		else
		pageList += " <span style='font-weight: bold'>"
				+ Integer.toString((section - 1) * pageListCount + i) + "</span>";

		// 4. ▶ 출력 조건
		if (i == pageListCount && totalSection > 1 
		&& totalPage >= i + (section - 1) * pageListCount + 1)
		pageList += " <a href='/shop/main?page=" + Integer.toString(section * pageListCount + 1)
				+ "&keyword=" + keyword
				+ "&category1Seqno=" + category1Seqno
				+ "&category2Seqno=" + category2Seqno
				+ "&category3Seqno=" + category3Seqno + ">▶</a>";
			}
		}
		return pageList;
}

	public String getReviewList(int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);  // 전체 페이지 수
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);  // 전체 섹션 수
		int section = (int)Math.ceil(pageNum/(double)pageListCount);  // 현재 섹션 번호
		String pageList = "";

		if(totalPage > 1) {

			for(int i = 1; i <= pageListCount; i++) {
				// 1. ◀ 출력조건 (section 값이 1보다 커야함)
				if(section > 1 && i == 1)
					pageList += " <a href='/shop/reviewList?page=" + Integer.toString((section - 2) * pageListCount + pageListCount)
							+ "&keyword=" + keyword + "'>◀</a>";

				// 2. 페이지 출력 중단 (전체 페이지가 범위를 벗어나면 중단)
				if(totalPage < (section - 1) * pageListCount + i) { break; }

				// 3. 현재 페이지와 같으면 링크를 붙이지 않음, 다르면 다른 페이지로 이동할 수 있는 링크를 붙임
				if(pageNum != (section - 1) * pageListCount + i)
					pageList += " <a href='/shop/reviewList?page=" + Integer.toString((section - 1) * pageListCount + i)
							+ "&keyword=" + keyword + "'>" + Integer.toString((section - 1) * pageListCount + i) + "</a>";
				else
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section - 1) * pageListCount + i) + "</span>";

				// 4. ▶ 출력조건 (section이 1 이상일 때, 페이지리스트의 끝에서 출력)
				if(i == pageListCount && totalSection > 1 && totalPage >= i + (section - 1) * pageListCount + 1)
					pageList += " <a href='/shop/reviewList?page=" + Integer.toString(section * pageListCount + 1) + "&keyword=" + keyword + "'>▶</a>";
			}
		}
		return pageList;
	}

	public String getQuestionList(int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);  // 전체 페이지 수
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);  // 전체 섹션 수
		int section = (int)Math.ceil(pageNum/(double)pageListCount);  // 현재 섹션 번호
		String pageList = "";
	
		if(totalPage > 1) {
	
			for(int i = 1; i <= pageListCount; i++) {
				// 1. ◀ 출력조건 (section 값이 1보다 커야함)
				if(section > 1 && i == 1)
					pageList += " <a href='/purchase/questionList?page=" + Integer.toString((section - 2) * pageListCount + pageListCount)
							+ "&keyword=" + keyword + "'>◀</a>";
	
				// 2. 페이지 출력 중단 (전체 페이지가 범위를 벗어나면 중단)
				if(totalPage < (section - 1) * pageListCount + i) { break; }
	
				// 3. 현재 페이지와 같으면 링크를 붙이지 않음, 다르면 다른 페이지로 이동할 수 있는 링크를 붙임
				if(pageNum != (section - 1) * pageListCount + i)
					pageList += " <a href='/purchase/questionList?page=" + Integer.toString((section - 1) * pageListCount + i)
							+ "&keyword=" + keyword + "'>" + Integer.toString((section - 1) * pageListCount + i) + "</a>";
				else
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section - 1) * pageListCount + i) + "</span>";
	
				// 4. ▶ 출력조건 (section이 1 이상일 때, 페이지리스트의 끝에서 출력)
				if(i == pageListCount && totalSection > 1 && totalPage >= i + (section - 1) * pageListCount + 1)
					pageList += " <a href='/purchase/questionList?page=" + Integer.toString(section * pageListCount + 1) + "&keyword=" + keyword + "'>▶</a>";
			}
		}
		return pageList;
	}

	// 메일발송내역 페이지
	public String getMailSendPageList(int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);  // 전체 페이지 수
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);  // 전체 섹션 수
		int section = (int)Math.ceil(pageNum/(double)pageListCount);  // 현재 섹션 번호
		String pageList = "";
	
		if(totalPage > 1) {
	
			for(int i = 1; i <= pageListCount; i++) {
				// 1. ◀ 출력조건 (section 값이 1보다 커야함)
				if(section > 1 && i == 1)
					pageList += " <a href='/master/mail/mailSendList?page=" + Integer.toString((section - 2) * pageListCount + pageListCount)
							+ "&keyword=" + keyword + "'>◀</a>";
	
				// 2. 페이지 출력 중단 (전체 페이지가 범위를 벗어나면 중단)
				if(totalPage < (section - 1) * pageListCount + i) { break; }
	
				// 3. 현재 페이지와 같으면 링크를 붙이지 않음, 다르면 다른 페이지로 이동할 수 있는 링크를 붙임
				if(pageNum != (section - 1) * pageListCount + i)
					pageList += " <a href='/master/mail/mailSendList?page=" + Integer.toString((section - 1) * pageListCount + i)
							+ "&keyword=" + keyword + "'>" + Integer.toString((section - 1) * pageListCount + i) + "</a>";
				else
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section - 1) * pageListCount + i) + "</span>";
	
				// 4. ▶ 출력조건 (section이 1 이상일 때, 페이지리스트의 끝에서 출력)
				if(i == pageListCount && totalSection > 1 && totalPage >= i + (section - 1) * pageListCount + 1)
					pageList += " <a href='/master/mail/mailSendList?page=" + Integer.toString(section * pageListCount + 1) + "&keyword=" + keyword + "'>▶</a>";
			}
		}
		return pageList;
	}

    // 주문 목록 페이지
	public String getOrderList(int pageNum, int orderNum, int pagelistNum, int totalCount, String keyword) {
		
		// pageNum: 현재 페이지 번호
		// orderNum: 한 번에 보여줄 주문 행 갯수
		// pagelistNum: 하단 페이지리스트에 보여질 페이지 갯수
		// totalCount: 검색된 데이터 갯수
		// section: 한 개의 페이지 목록 예) 1 2 3 4 5 --> section #1
		//							   6 7 8 9 10 --> section #2
		// totalSection: 전체 section의 수
		
		int totalPage = (int)Math.ceil(totalCount/(double)orderNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pagelistNum);
		int section = (int)Math.ceil(pageNum/(double)pagelistNum);
  		String pageList = "";
		
		if(totalPage != 1) {
      for(int i=1;i<=pagelistNum;i++) {
        pageList += " <a href=/board/list?page=" + Integer.toString((section-2)*pagelistNum + pagelistNum) 
					+ "&keyword=" + keyword + ">◀</a>";
				//2. 페이지 출력 중단
				if(totalPage < (section-1)*pagelistNum+i) { break; } 
				//3. 인자로 가져 온 페이지값과 계산해서 나온 페이지값이 같으면 링크를 안 붙이고 다르면 다른 페이지로 이동할수 있는 링크를 붙임.
				if(pageNum != (section-1)*pagelistNum +i)
					pageList += " <a href=/board/list?page=" + Integer.toString((section-1)*pagelistNum+i) 
							+ "&keyword=" + keyword + ">" + Integer.toString((section-1)*pagelistNum+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pagelistNum+i) + "</span>";
				//4.▶ 출력 조건
				// i == pagelistNum --> 페이지리스트 갯수만큼 페이지번호를 출력 --> 페이지리스트의 끝
				// totalSection > 1 --> section이 1개 이상 존재 
				// totalPage >= i+(section-1)*pagelistNum+1 --> 아직까지 출력할 페이지가 남아 있음.
				if(i==pagelistNum && totalSection > 1 && totalPage >= i+(section-1)*pagelistNum+1)
					pageList += " <a href=/board/list?page=" + Integer.toString(section*pagelistNum+1) + "&keyword=" + keyword + ">▶</a>";
				
			}
		}
		return pageList;
	}

	//카테고리별 상품 목록 페이지
    public String getPageList(int pageNum, int postNum, int pageListCount, int totalCount, String catekeyword, Long CateSeqno) {
		//pageNum : 현재 페이지 번호
		//postNum : 한 화면에 보여지는 게시물 행 갯수
		//pageListCount : 하단 페이지 리스트에 보여질 페이지 갯수
		//totalCount : 전체 행갯수
		//totalPage : 전체 페이지 갯수
		//section : 한 개의 페이지 목록.현재 section 번호 예) 1 2 3 4 5 --> section 1, 6 7 8 9 10 --> section 2
		//totalSection : 전체 section 갯수
		
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {
				//1. ◀ 출력조건
				// - section 값이 1보다 커야함.
				// - i == 1
				if(section >1 && i ==1)
					pageList += " <a href=categoryProduct?cate=" + CateSeqno + "&page=" +Integer.toString((section-2)*pageListCount + pageListCount)
					+ "&keyword=" + catekeyword + ">◀</a>";
				//2. 페이지 출력 중단
				if(totalPage < (section-1)*pageListCount+i) { break; }
				//3. 인자로 가져 온 페이지값과 계산해서 나온 페이지값이 같으면 링크를 안 붙이고 다르면 다른 페이지로 이동할수 있는 링크를 붙임.
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=categoryProduct?cate=" + CateSeqno + "&page=" +Integer.toString((section-1)*pageListCount+i)
							+ "&keyword=" + catekeyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				//4.▶ 출력 조건
				// i == pageListCount --> 페이지리스트 갯수만큼 페이지번호를 출력 --> 페이지리스트의 끝
				// totalSection > 1 --> section이 1개 이상 존재
				// totalPage >= i+(section-1)*pageListCount+1 --> 아직까지 출력할 페이지가 남아 있음.
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=categoryProduct?cate=" + CateSeqno + "&page=" + Integer.toString(section*pageListCount+1) + "&keyword=" + catekeyword + ">▶</a>";
				
			}
		}
		return pageList;
	}

	public String getPageListNoKeyword(int pageNum, int postNum, int pageListCount, int totalCount) {
		
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {
				//1. ◀ 출력조건
				// - section 값이 1보다 커야함.
				// - i == 1
				if(section >1 && i ==1)
					pageList += " <a href=list?page=" + Integer.toString((section-2)*pageListCount + pageListCount) +  ">◀</a>";
				//2. 페이지 출력 중단
				if(totalPage < (section-1)*pageListCount+i) { break; }
				//3. 인자로 가져 온 페이지값과 계산해서 나온 페이지값이 같으면 링크를 안 붙이고 다르면 다른 페이지로 이동할수 있는 링크를 붙임.
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=list?page=" + Integer.toString((section-1)*pageListCount+i) + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				//4.▶ 출력 조건
				// i == pageListCount --> 페이지리스트 갯수만큼 페이지번호를 출력 --> 페이지리스트의 끝
				// totalSection > 1 --> section이 1개 이상 존재
				// totalPage >= i+(section-1)*pageListCount+1 --> 아직까지 출력할 페이지가 남아 있음.
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=list?page=" + Integer.toString(section*pageListCount+1) + ">▶</a>";
				
			}
		}
		return pageList;
	}

	
	public String getPageAddress(int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {
		
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/member/addrSearch?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
					+ "&addrSearch=" + keyword + ">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=/member/addrSearch?page=" + Integer.toString((section-1)*pageListCount+i) 
							+ "&addrSearch=" + keyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=/member/addrSearch?page=" + Integer.toString(section*pageListCount+1) + "&addrSearch=" + keyword + ">▶</a>";				
			}			
		}		
		return pageList;
	}

	//관리자페이지 고객정보리스트
	public String getPageClient(int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {
		
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/master/client?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
					+ "&keyword=" + keyword + ">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=/master/client?page=" + Integer.toString((section-1)*pageListCount+i) 
							+ "&keyword=" + keyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=/master/client?page=" + Integer.toString(section*pageListCount+1) + "&keyword=" + keyword + ">▶</a>";				
			}			
		}		
		return pageList;
	}

	//관리자페이지 상품리스트
	public String getPageProduct(int pageNum, int postNum, int pageListCount, int totalCount, String keyword){

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/master/productList?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
					+ "&addrSearch=" + keyword + ">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=/master/productList?page=" + Integer.toString((section-1)*pageListCount+i) 
							+ "&addrSearch=" + keyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=/master/productList?page=" + Integer.toString(section*pageListCount+1) + "&addrSearch=" + keyword + ">▶</a>";				
			}			
		}		
		return pageList;
	}

	//관리자페이지 주문리스트
	public String getPageOrder(int pageNum, int postNum, int pageListCount, int totalCount, String keyword1, Long keyword2){

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/master/order?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
					+ "&keyword=" + keyword1 + "&keyword=" + keyword2 +">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
						pageList += " <a href=/master/order?page=" + Integer.toString((section-1)*pageListCount+i) 
								+ "&keyword=" + keyword1 + "&keyword=" + keyword2 +">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
					else 
						pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
					if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
						pageList += " <a href=/master/order?page=" + Integer.toString(section*pageListCount+1) + "&keyword=" + keyword1 + "&keyword=" + keyword2 +">▶</a>";				
			}			
		}		
		return pageList;
	}

	//관리자페이지 문의리스트
	public String getPageQuestion(int pageNum, int postNum, int pageListCount, int totalCount, String keyword){
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/master/question?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
					+ "&addrSearch=" + keyword + ">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=/master/question?page=" + Integer.toString((section-1)*pageListCount+i) 
							+ "&addrSearch=" + keyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=/master/question?page=" + Integer.toString(section*pageListCount+1) + "&addrSearch=" + keyword + ">▶</a>";				
			}			
		}		
		return pageList;
	}


	//관리자페이지 리뷰리스트
   	public String getPageReview(int pageNum, int postNum, int pageListCount, int totalCount, Long keyword){

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/master/reviewList?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
					+ "&addrSearch=" + keyword + ">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=/master/reviewList?page=" + Integer.toString((section-1)*pageListCount+i) 
							+ "&addrSearch=" + keyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=/master/reviewList?page=" + Integer.toString(section*pageListCount+1) + "&addrSearch=" + keyword + ">▶</a>";				
			}			
		}		
		return pageList;
	}

	//관리자페이지 리뷰신고리스트
	public String getPageRoport(int pageNum, int postNum, int pageListCount, int totalCount, String keyword){

		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/master/reviewReport?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
					+ "&addrSearch=" + keyword + ">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=/master/reviewReport?page=" + Integer.toString((section-1)*pageListCount+i) 
							+ "&addrSearch=" + keyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=/master/reviewReport?page=" + Integer.toString(section*pageListCount+1) + "&addrSearch=" + keyword + ">▶</a>";				
			}			
		}		
		return pageList;
	}

   	//관리자페이지 쿠폰리스트
	public String getPageCoupon(int pageNum, int postNum, int pageListCount, int totalCount) {
		
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {

				if(section >1 && i ==1)
					pageList += " <a href=/master/couponList?page=" + Integer.toString((section-2)*pageListCount + pageListCount) 
				    		 + ">◀</a>";
				if(totalPage < (section-1)*pageListCount+i) { break; } 
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=/master/couponList?page=" + Integer.toString((section-1)*pageListCount+i) 
							 + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";		
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=/master/couponList?page=" + Integer.toString(section*pageListCount+1) + ">▶</a>";				
			}			
		}		
		return pageList;
	}
	//상품 검색 페이지
	public String getSearchPageList(int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {
	
		//pageNum : 현재 페이지 번호
		//postNum : 한 화면에 보여지는 게시물 행 갯수
		//pageListCount : 하단 페이지 리스트에 보여질 페이지 갯수
		//totalCount : 전체 행갯수
		//totalPage : 전체 페이지 갯수
		//section : 한 개의 페이지 목록.현재 section 번호 예) 1 2 3 4 5 --> section 1, 6 7 8 9 10 --> section 2
		//totalSection : 전체 section 갯수
		
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {
				//1. ◀ 출력조건
				// - section 값이 1보다 커야함.
				// - i == 1
				if(section >1 && i ==1)
					pageList += " <a href=searchAll?page=" +Integer.toString((section-2)*pageListCount + pageListCount)
					+ "&keyword=" + keyword + ">◀</a>";
				//2. 페이지 출력 중단
				if(totalPage < (section-1)*pageListCount+i) { break; }
				//3. 인자로 가져 온 페이지값과 계산해서 나온 페이지값이 같으면 링크를 안 붙이고 다르면 다른 페이지로 이동할수 있는 링크를 붙임.
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=searchAll?page=" +Integer.toString((section-1)*pageListCount+i)
							+ "&keyword=" + keyword + ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				//4.▶ 출력 조건
				// i == pageListCount --> 페이지리스트 갯수만큼 페이지번호를 출력 --> 페이지리스트의 끝
				// totalSection > 1 --> section이 1개 이상 존재
				// totalPage >= i+(section-1)*pageListCount+1 --> 아직까지 출력할 페이지가 남아 있음.
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=searchAll?page=" + Integer.toString(section*pageListCount+1) + "&keyword=" + keyword + ">▶</a>";
				
			}
		}
		return pageList;
	}
	//상품 상세 페이지내 리뷰 목록
	public String getProductReviewList(int pageNum, int postNum, int pageListCount, int totalCount, Long productSeqno) {
		//pageNum : 현재 페이지 번호
		//postNum : 한 화면에 보여지는 게시물 행 갯수
		//pageListCount : 하단 페이지 리스트에 보여질 페이지 갯수
		//totalCount : 전체 행갯수
		//totalPage : 전체 페이지 갯수
		//section : 한 개의 페이지 목록.현재 section 번호 예) 1 2 3 4 5 --> section 1, 6 7 8 9 10 --> section 2
		//totalSection : 전체 section 갯수
		
		int totalPage = (int)Math.ceil(totalCount/(double)postNum);
		int totalSection = (int)Math.ceil(totalPage/(double)pageListCount);
		int section = (int)Math.ceil(pageNum/(double)pageListCount);
		String pageList = "";
		if(totalPage != 1) {
			
			for(int i=1;i<=pageListCount;i++) {
				//1. ◀ 출력조건
				// - section 값이 1보다 커야함.
				// - i == 1
				if(section >1 && i ==1)
					pageList += " <a href=view?productSeqno=" + productSeqno +"&page=" +Integer.toString((section-2)*pageListCount + pageListCount)
					+  ">◀</a>";
				//2. 페이지 출력 중단
				if(totalPage < (section-1)*pageListCount+i) { break; }
				//3. 인자로 가져 온 페이지값과 계산해서 나온 페이지값이 같으면 링크를 안 붙이고 다르면 다른 페이지로 이동할수 있는 링크를 붙임.
				if(pageNum != (section-1)*pageListCount +i)
					pageList += " <a href=view?productSeqno=" + productSeqno + "&page=" + Integer.toString((section-1)*pageListCount+i)
							+ ">" + Integer.toString((section-1)*pageListCount+i) + "</a>";
				else 
					pageList += " <span style='font-weight: bold'>" + Integer.toString((section-1)*pageListCount+i) + "</span>";
				//4.▶ 출력 조건
				// i == pageListCount --> 페이지리스트 갯수만큼 페이지번호를 출력 --> 페이지리스트의 끝
				// totalSection > 1 --> section이 1개 이상 존재
				// totalPage >= i+(section-1)*pageListCount+1 --> 아직까지 출력할 페이지가 남아 있음.
				if(i==pageListCount && totalSection > 1 && totalPage >= i+(section-1)*pageListCount+1)
					pageList += " <a href=view?productSeqno=" + productSeqno + "&page=" + Integer.toString(section*pageListCount+1) + ">▶</a>";
				
			}
		}
		return pageList;
	}

}
