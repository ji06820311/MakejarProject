package com.itgroup;

import com.itgroup.dao.MemberDao;

// 메인 클래스 대신 실제 모든 업무를 총 잭임지는 매니저 클래스
public class MemberManager {
    private MemberDao dao = null;

    public MemberManager() {
        this.dao = new MemberDao();
    }

    public void selestAll() { // 모든 회원 정보 조회

    }

    public void getSize() { // 몇명의 회원인지 조회하는 구문입니다.
        int cnt = dao.getSize();
        String message;
        if (cnt == 0) {
            message = "검색된 회원이 존재하지 않습니다.";
        } else {
            message = "검색된 회원은 총" + cnt + "명입니다";
        }
        System.out.println(message);
    }
}
