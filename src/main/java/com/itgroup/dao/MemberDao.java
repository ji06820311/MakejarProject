package com.itgroup.dao;

import com.itgroup.bean.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//데이터 베이스와 직접 연동하여 CRUD 작업을 수행하시는 DAO 클래스
public class MemberDao {
    public MemberDao() {
        // 드라이버는 OracleDriver 클래스는 ojdbc6.jar 파일에 포함되어 있는 자바 클래스입니다.
        String drive = "oracle.jdbc.driver.OracleDriver";

        try {
            Class.forName(drive); // 동적 객체 생성하는 문법입니다.

        } catch (ClassNotFoundException e) {
            System.out.println("해당 드라이버가 존재하지 않습니다.");
            throw new RuntimeException(e);
        }
    }

    public int updataData(Member bean) {
        // 수정된 나의 정보 bean 사용하여 데이터 베이스에 수정합니다.
        int cnt = -1;

        String sql = "update members set name = ?,password = ?, gender = ?, birth = ?, marriage = ?, salary = ?, address = ?, manager = ?";
        sql += " where id = ? ";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn= this.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getPassword());
            pstmt.setString(3, bean.getGender());
            pstmt.setString(4, bean.getBirth());
            pstmt.setString(5, bean.getMarriage());
            pstmt.setInt(6, bean.getSalary());
            pstmt.setString(7, bean.getAddress());
            pstmt.setString(8, bean.getManager());
            pstmt.setString(9, bean.getId());

            cnt = pstmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }finally {
            try {
                if(pstmt != null){pstmt.close();}
                if(conn != null){conn.close();}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return cnt;
    }

    public int insertData(Member bean) {
        //웹 페이제어서 회원 정보를 입력하고 '가입' 버툰울 눌렸습니다.
        int cnt = -1;

        String sql = "insert into members(id, name, password, gender, birth, marriage, salary, address, manager)";
        sql += " values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, bean.getId());
            pstmt.setString(2, bean.getName());
            pstmt.setString(3, bean.getPassword());
            pstmt.setString(4, bean.getGender());
            pstmt.setString(5, bean.getBirth());
            pstmt.setString(6, bean.getMarriage());
            pstmt.setInt(7, bean.getSalary());
            pstmt.setString(8, bean.getAddress());
            pstmt.setString(9, bean.getManager());

            cnt = pstmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }finally {
            try {
                if(pstmt != null){pstmt.close();}
                if(conn != null){conn.close();}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return cnt;   //리턴은 제일 먼저 적는게 좋음
    }

    public Connection getConnection() {
        Connection conn = null; //접속 객체

        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "oraman";
        String password = "oracle";

        try {
            conn = DriverManager.getConnection(url, id, password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    public int getSize() {
        String sql = "select count(*) as cnt from members ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        int cnt = 0;
        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                cnt = rs.getInt("cnt");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cnt;
    }

    public Member getMemberOne(String id) {
        // 로그인 id 정보를 이용하여 해당 사용자의 정보를 bean 형태로 반환해줍니다.
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Member bean = null;   // 찾고자하는 회원의 정보

        String sql = "select * from members where id = ? ";

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {  // 1건 발견됨
                bean = new Member();
                bean.setId(rs.getString("id"));
                bean.setName(rs.getString("name"));
                bean.setPassword(rs.getString("password"));
                bean.setGender(rs.getString("gender"));
                bean.setBirth(String.valueOf(rs.getDate("birth")));
                bean.setMarriage(rs.getString("marriage"));
                bean.setSalary(rs.getInt("salary"));
                bean.setAddress(rs.getString("address"));
                bean.setManager(rs.getString("manager"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return bean;
    }

    public int deleteData(String id) { // 기본키를 사용하여 회원 탈퇴를 시도합니다.
        int cnt = -1;
        String sql = "delete from members where id = ?";

        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cnt;
    }

    public List<Member> selectAll() {
        List<Member> members = new ArrayList<Member>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        String sql = "select * from members order by name asc";

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
//                System.out.println(rs.getString(2));
//                System.out.println(rs.getInt(7));
//                System.out.println(rs.getString("id"));
//                System.out.println(rs.getString("gender"));

                Member bean = new Member();
                bean.setId(rs.getString("id"));
                bean.setName(rs.getString("name"));
                bean.setPassword(rs.getString("password"));
                bean.setGender(rs.getString("gender"));
                bean.setBirth(rs.getString("birth"));
                bean.setMarriage(rs.getString("marriage"));
                bean.setSalary(rs.getInt("salary"));
                bean.setAddress(rs.getString("address"));
                bean.setManager(rs.getString("manager"));

                members.add(bean);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return members;
    }


    public List<Member> findByGender(String gender) {
        // 성별 컬럼 gender을 사용하여 특정 성별의 회원들만 조회합니다.
        String sql = "select * from members where gender = ?";

        List<Member> members = new ArrayList<Member>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = this.getConnection();                 // 1. DB 연결
            pstmt = conn.prepareStatement(sql);          // 2. SQL 실행 준비
            pstmt.setString(1, gender);                  // 2-1. ? 바인딩
            rs = pstmt.executeQuery();                   // 2-2. SQL 실행

            // 3. 결과 매핑
            while (rs.next()) {
                Member bean = new Member();
                bean.setId(rs.getString("id"));
                bean.setName(rs.getString("name"));
                bean.setPassword(rs.getString("password"));
                bean.setGender(rs.getString("gender"));
                bean.setBirth(rs.getString("birth"));
                bean.setMarriage(rs.getString("marriage"));
                bean.setSalary(rs.getInt("salary"));
                bean.setAddress(rs.getString("address"));
                bean.setManager(rs.getString("manager"));
                members.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception ignore) {
            }
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception ignore) {
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignore) {
            }
        }

        return members;
    }



}
