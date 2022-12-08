package examservlet.control;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exambean.model.StudentInfoBean;
import examdao.model.DatabassAccessObject;

/**
 * @author XDW
 * ��ҳ��ʾѧ����Ϣ���ѧ����Ϣ
 * Ĭ��ÿҳ10��
 */
@WebServlet("/ShowStuPage")
public class StudentShowByPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		// ���û�в������ݹ���������ÿҳ��ʾ10����¼����ʾ��һҳ��
		// Ȼ�������������ѹ��request��������ǰ̨ҳ�档request������ǰ̨ҳ���յ������ٵ�������
		int cpage = 0;
		int per = 10;
		if (request.getParameter("cpage") != null) {
			cpage = Integer.parseInt(request.getParameter("cpage"));
		}
		request.setAttribute("cpage", cpage);
		if (request.getParameter("per") != null) {
			per = Integer.parseInt(request.getParameter("per"));
		}
		request.setAttribute("per", per);
		
		try {
			// ���ݿ�Ĳ�ѯ����ж�������¼
			DatabassAccessObject db=new DatabassAccessObject();
			ResultSet rsTotal = db
					.query("select count(*) as total from student");
			if (rsTotal.next()) {
				// �����ҳ��ѹ��request�������ݸ�ǰ̨ҳ�档
				request.setAttribute("totalPage", 1
						+ (rsTotal.getInt("total") - 1) / per);
			}
			// �½�һ����̬����������Ų�ѯ���
			ArrayList<StudentInfoBean> stuBeanAllList = new ArrayList<StudentInfoBean>();
			ResultSet rs=null;
			String s_ID=request.getParameter("s_ID");
			if (s_ID!=null) {
				rs=db.query("select * from student where ID LIKE '%"+s_ID+"%' ;");
			}else {
				rs = db.query("select * from student order by ID ;");
			}
			

			int total = 0;
			while (rs.next()) {
				StudentInfoBean stuBean = new StudentInfoBean();
				stuBean.setID(rs.getString("Id"));
				stuBean.setPassword(rs.getString("password"));
				stuBean.setName(rs.getString("Name"));
				stuBean.setCLASS(rs.getString("class"));
				stuBean.setScore(rs.getFloat("score"));
				stuBeanAllList.add(stuBean);
				total++;
			}
			// ��ͨ������cpage��per���Ҫ�ƻظ�ǰ̨��ʾ������
			ArrayList<StudentInfoBean> stuTableList = new ArrayList<StudentInfoBean>();
			for (int i = cpage * per; i < cpage * per + per && i < total; i++) {
				stuTableList.add(stuBeanAllList.get(i));
			}
			String temp=request.getParameter("modify_id");
			System.out.println(temp);
			request.setAttribute("modify_id", temp);
			request.setAttribute("total", total);
			request.setAttribute("stuTableList", stuTableList);
			request.getRequestDispatcher("/teacher/teacher_stu_manage.jsp").forward(request,
					response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
