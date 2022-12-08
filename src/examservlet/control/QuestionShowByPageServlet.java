package examservlet.control;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exambean.model.QuestionBean;
import examdao.model.DatabassAccessObject;

/**
 * @author XDW
 * ��ҳ��ʾ�������Ŀ��Ϣ
 * Ĭ��ÿҳ10��
 */
@WebServlet("/ShowQuePage")
public class QuestionShowByPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		// ֮��������ݿ�Ĳ�ѯ
		try {
			// �����ݿ�Ĳ�ѯ����ж�������¼
			DatabassAccessObject db = new DatabassAccessObject();
			ResultSet rsTotal = db.query("select count(*) as total from question");
			if (rsTotal.next()) {
				// �����ҳ��ѹ��request�������ݸ�ǰ̨ҳ�档
				request.setAttribute("totalPage", 1 + (rsTotal.getInt("total") - 1) / per);
			}
			// �½�һ����̬����������Ų�ѯ���
			ArrayList<QuestionBean> queBeanAllList = new ArrayList<QuestionBean>();
			
			ResultSet rs=null;
			String q_title=request.getParameter("q_title");
			String q_type="";
			q_type=request.getParameter("q_type");
			if (q_title!=null) {
				rs=db.query("select * from question where title LIKE '%"+q_title+"%' ;");
			}else if(q_type!=null&&q_type.length()>0){
				
				rs=db.query("select * from question where type='"+q_type+"' ;");
			}
			else {
				rs = db.query("select\r\n" + 
						"    *\r\n" + 
						"from\r\n" + 
						"    question\r\n" + 
						"order by\r\n" + 
						"    case \r\n" + 
						"      when type='��ѡ��' then 1\r\n" + 
						"      when type='��ѡ��' then 2\r\n" + 
						"			when type='�ж���' then 3\r\n" + 
						"			when type='�����' then 4\r\n" + 
						"			when type='�����' then 5\r\n" + 
						"    end");
			}
			// ���в�ѯ�����������
			int total = 0;
			while (rs.next()) {
				QuestionBean queBean = new QuestionBean();
				queBean.setQ_id(rs.getString("number"));
				queBean.setQ_type(rs.getString("type"));
				queBean.setQ_title(rs.getString("title"));
				String selectString = rs.getString("select");
				if (selectString != null) {
					queBean.setQ_select(selectString);
					String[] temp = selectString.split("\\@");
					queBean.setOptions(temp);
				}
				queBean.setQ_score(rs.getString("score"));
				queBean.setQ_key(rs.getString("key"));
				queBean.setQ_img(rs.getString("img"));
				queBeanAllList.add(queBean);
				total++;
			}
			ArrayList<QuestionBean> queTableList = new ArrayList<QuestionBean>();
			for (int i = cpage * per; i < cpage * per + per && i < total; i++) {
				queTableList.add(queBeanAllList.get(i));
			}
			String temp=request.getParameter("modify_id");
			request.setAttribute("modify_id", temp);
			request.setAttribute("total", total);
			request.setAttribute("queTableList", queTableList);
			request.getRequestDispatcher("/teacher/teacher_que_manage.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
