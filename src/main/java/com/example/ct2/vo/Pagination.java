package com.example.ct2.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Pagination {

    // 한 페이지당 게시글 수
    private int pageSize = 10;

    // 한 블럭당 페이지 수
    private int rangeSize = 5;

    // 현재 페이지
    private int curPage = 1;

    // 현재 블럭
    private int curRange = 1;

    // 총 게시글 수
    private int listCnt;

    // 총 페이지 수
    private int pageCnt;

    // 총 블럭 수
    private int rangeCnt;

    // 시작 페이지
    private int startPage = 1;

    // 끝 페이지
    private int endPage = 1;

    // 시작
    private int startIndex = 0;

    // 이전 페이지
    private int prevPage;

    // 다음 페이지
    private int nextPage;

    public Pagination(int listCnt, int curPage) {
        setCurPage(curPage);
        /** 총 게시물 수 **/
        setListCnt(listCnt);

        /** 1. 총 페이지 수 **/
        setPageCnt(listCnt);
        /** 2. 총 블럭(range)수 **/
        setRangeCnt(pageCnt);
        /** 3. 블럭(range) setting **/
        rangeSetting(curPage);

        /** DB 질의를 위한 startIndex 설정 **/
        setStartIndex(curPage);
    }

    public void setPageCnt(int listCnt) {
        this.pageCnt = (int) Math.ceil(listCnt * 1.0 / pageSize);
    }

    public void setRangeCnt(int pageCnt) {
        this.rangeCnt = (int) Math.ceil(pageCnt * 1.0 / rangeSize);
    }

    public void rangeSetting(int curPage){

        setCurRange(curPage);
        this.startPage = (curRange - 1) * rangeSize + 1;
        this.endPage = startPage + rangeSize - 1;

        if(endPage > pageCnt){
            this.endPage = pageCnt;
        }

        this.prevPage = curPage - 1;
        this.nextPage = curPage + 1;
    }

    public void setCurRange(int curPage) {
        this.curRange = (int)((curPage - 1) / rangeSize) + 1;
    }

    public void setStartIndex(int curPage) {
        this.startIndex = (curPage - 1) * pageSize;
    }

}
