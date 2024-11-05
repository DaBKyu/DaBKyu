package com.dabkyu.dabkyu.util;

public class PageUtil {

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
				//1. ◀ 출력조건
				// - section 값이 1보다 커야함.
				// - i == 1 
				if(section >1 && i ==1)
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
}
