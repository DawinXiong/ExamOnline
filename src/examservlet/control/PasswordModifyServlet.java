package examservlet.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exambean.model.LoginBean;
import examdao.model.DatabassAccessObject;

/**
 * @author XDW
 * ����ʵ�����ݱ������޸Ĺ���
 *
 */
@WebServlet("/HandlePassword")
public class PasswordModifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session=request.getSession();
		LoginBean loginBean=(LoginBean) session.getAttribute("loginBean");
		String ID = loginBean.getID();
		String role = loginBean.getRole();
		String newPassword = request.getParameter("newPassword");
		String newPasswordAgain = request.getParameter("newPasswordAgain");
		try {
			if (newPassword.equals(newPasswordAgain)) {	//�ж���������������Ƿ�һ��
				DatabassAccessObject db = new DatabassAccessObject();
				db.modify("update "+role+" set password=? where ID=?", newPassword, ID);
				String msg="�����޸ĳɹ��������µ�¼";
				PrintWriter out = response.getWriter();
				out.println("<script language=javascript>top.location='" + request.getContextPath()
						+ "/login.jsp';alert('" + msg + "')</script>");
			} else {
				String msg="������������벻һ�£�������ȷ�ϡ�";
				PrintWriter out = response.getWriter();
				out.println("<script language=javascript>window.history.go(-1);alert('" + msg + "')</script>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
