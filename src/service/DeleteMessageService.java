package service;

import connection.ConnectionProvider;
import dao.MessageDao;
import jdbc.JdbcUtil;
import model.Message;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteMessageService {
    private static DeleteMessageService instance = new DeleteMessageService();
    public static DeleteMessageService getInstance() {
        return instance;
    }
    private DeleteMessageService() {

    }

    public void deleteMessage(int messageId, String password) {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            MessageDao messageDao = MessageDao.getInstance();
            Message message = messageDao.select(conn, messageId);
            if (message == null) {
                throw new MessageNotFoundException("메시지 없음");
            }
            if(!message.matchPassword(password)) {
                throw new InvalidPasswordException("bax password");
            }
            messageDao.delete(conn, messageId);
            conn.commit();
        } catch (SQLException ex) {
            JdbcUtil.rollback(conn);
            throw new ServiceException("삭제 실패:" + ex.getMessage(), ex);
        } catch (InvalidPasswordException | MessageNotFoundException ex) {
            JdbcUtil.rollback(conn);
            throw ex;
        } finally {
            JdbcUtil.close(conn);
        }
    }
}
