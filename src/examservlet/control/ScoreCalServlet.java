package examservlet.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exambean.model.LoginBean;
import exambean.model.QuestionBean;
import examdao.model.DatabassAccessObject;

/**
 * @author XDW
 * ���ฺ��ͳ�ƿ�������ķ�����
 * ÿ�����ͷֱ�Ƿ֣��ɼ��������ݿ⡣
 */
@WebServlet("/CalScoreServlet")
public class ScoreCalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(true);

		if (session == null) {
			response.sendRedirect("login.jsp");
		}
		//��ȡ��ǰ�洢��Session�����е��Ծ���Ŀ
		@SuppressWarnings("unchecked")
		ArrayList<QuestionBean> ques = (ArrayList<QuestionBean>) session.getAttribute("ques");
		String stuAnsArr[] = null;
		//ÿ�����ͷ�ֵ����ֵ��Ϊ0
		float score = 0;
		float score_muti = 0;
		float score_sing = 0;
		float score_jud = 0;
		float score_fill = 0;
		float score_ess = 0;
		for (int i = 0; i < ques.size(); ++i) {
			QuestionBean que = ques.get(i);
			stuAnsArr = request.getParameterValues(que.getQ_id());//��ȡÿ����Ĵ�
			//����Ƕ�ѡ�⣬���ڶ��ѡ��ֵ�������ҪgetParameterValues������ȡ���ֵ

			if (!que.getQ_type().equals("�����")) {//�Ǽ����ļǷַ�ʽ��ͬ��������������
				if (stuAnsArr != null) {
					String stuAns = ""; //ÿ����Ĵ�
					for (int j = 0; j < stuAnsArr.length; j++) {//��ѡ��ӵ�ж����
						stuAns += stuAnsArr[j];//��װѧ����
					}
					System.out.println(stuAns);
					if (stuAns.equalsIgnoreCase(que.getQ_key())) {	//ƥ��ѧ���𰸺���ȷ��
						switch (que.getQ_type()) { //ÿ����ֱ�Ƿ�
						case "��ѡ��":
							score_sing += Float.parseFloat(que.getQ_score());
							break;
						case "��ѡ��":
							score_muti += Float.parseFloat(que.getQ_score());
							break;
						case "�ж���":
							score_jud += Float.parseFloat(que.getQ_score());
							break;
						case "�����":
							score_fill += Float.parseFloat(que.getQ_score());
							break;
						default:
							break;
						}
					}
				}
			} else { //�������жϷ���
				String[] KEY_WORD = que.getQ_key().split("\\@");	//�����ȷ���еĹؼ���
				Float totalScore = Float.parseFloat(que.getQ_score());	//��ȡ������ֵ
				Float singleScore = 0.0f;	//ÿ���ķֵ��ϸ��
				String stuAns = "";
				if (stuAnsArr != null) {
					for (int j = 0; j < stuAnsArr.length; j++) {
						stuAns += stuAnsArr[j];	//��װѧ����
					}
				}
				// ʹ��contains����
				if (KEY_WORD != null) {	//����ؼ��ʴ���
					singleScore = totalScore / KEY_WORD.length; //���չؼ�������ϸ�ַ�ֵ
					for (int j = 0; j < KEY_WORD.length; j++) {
						if (stuAns.contains(KEY_WORD[j])) { //�жϿ��������Ƿ���ֹؼ���
							System.out.println(stuAns + "�����ؼ���:" + KEY_WORD[j]);
							score_ess += singleScore;
						} else {
							System.out.println("�������ؼ���:" + KEY_WORD[j]);
						}
					}
				}

			}
		}
		
		score = score_sing + score_muti + score_jud + score_fill + score_ess;
		String grade = "";
		int f = Math.round(score);
		int g = ((f < 0) == true ? 1 : 0) + ((f < 60) == true ? 1 : 0) + ((f < 75) == true ? 1 : 0)
				+ ((f < 85) == true ? 1 : 0) + ((f < 95) == true ? 1 : 0);
		switch (g) {
		case 0:
			grade = "����";
			break;
		case 1:
			grade = "����";
			break;
		case 2:
			grade = "�е�";
			break;
		case 3:
			grade = "����";
			break;
		case 4:
			grade = "������";
			break;
		case 5:
			grade = "ȱ��";
			break;
		default:
			break;
		}
		
		try {
			//ʵ�������ݿ����Ӷ��󣬰�������㵽�ķ�ֵ�ֱ�������ݿ���
			DatabassAccessObject db = new DatabassAccessObject();
			LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
			String ID = loginBean.getID();
			db.insert("update student set score = ? where ID = ? ", score, ID);
			
			ResultSet rs = db.query("select * from score where id = ?", ID);
			if (!rs.next()) {
				db.insert("insert into score values(?,?,?,?,?,?,?,?)", ID, score, score_sing, score_muti, score_jud,
						score_fill, score_ess,grade);
			} else {
				db.modify(
						"update score set score = ? , score_sing = ? , score_muti= ? , score_jud = ? , score_fill = ? , score_ess = ?,grade = ? where ID = ?  ;",
						score, score_sing, score_muti, score_jud, score_fill, score_ess,grade, ID);
			}
			rs = db.query("select * from student where id = ?", ID);
			rs.first();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(score);
		response.setContentType("text/html;charset=utf-8");
		
		//����������˰׾�������ʱ����ʣ��5�������ϣ����鿼�����������ض�����Ծ�ҳ�棻
		//������Ϊ����ɹ����ض����ѧ����Ϣҳ��
		PrintWriter out = response.getWriter();
		long curTime = System.currentTimeMillis() / 1000;
		long endTime = (long) session.getAttribute("endTime");
		if (score == 0 && endTime - curTime >= 300) {
			out.println("<script language=javascript>if(confirm('ʱ������ʣ�࣬����������')){window.location='" + request.getContextPath()
					+ "/student/student_exam_paper.jsp';}</script>");
			out.println("<script language=javascript>window.location='" + request.getContextPath()
					+ "/student/student.jsp';alert('�Ծ��Ѿ��ύ�����Բ��ĳɼ���');</script>");
			LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
			loginBean.setScore(score);

		} else {
			LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
			loginBean.setScore(score);
			session.removeAttribute("examTime");
			session.removeAttribute("endTime");
			out.println("<script language=javascript>window.location='" + request.getContextPath()
					+ "/student/student.jsp';alert('�Ծ��Ѿ��ύ�����Բ��ĳɼ���');</script>");
		}
	}
}
