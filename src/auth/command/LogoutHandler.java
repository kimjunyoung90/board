package auth.command;

import mvc.command.CommandHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutHandler implements CommandHandler {

    @Override
    public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

        //현재 요청에 대한 세션을 반환함. getSession 메서드의 인자로 true를 전달할 경우 세션이 없으면 새로운 세션을 생성한 후 전달.
        HttpSession session = req.getSession(false);

        //세션이 존재하면 세션을 종료시킴
        if(session != null) {
            session.invalidate();
        }
        res.sendRedirect(req.getContextPath() + "/index.jsp");
        return null;
    }
}
