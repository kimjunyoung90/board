package member.service;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class JoinService {
    private MemberDao memberDao = new MemberDao();

    public void join(JoinRequest joinReq) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            //가입하려는 아이디가 존재하면 트랜잭션 롤백하고 DuplicateIdException 발생시킴
            Member member = memberDao.selectById(conn, joinReq.getId());
            if (member != null) {
                JdbcUtil.rollback(conn);
                throw new DuplicateIdException();
            }

            //joinReq를 이용하여 Member 객체 생성하고, 데이터 삽입
            memberDao.insert(conn,
                    new Member(
                            joinReq.getId(),
                            joinReq.getName(),
                            joinReq.getPassword(),
                            new Date())
            );
            conn.commit();
        } catch (SQLException e) {
            JdbcUtil.rollback(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(conn);
        }
    }
}
