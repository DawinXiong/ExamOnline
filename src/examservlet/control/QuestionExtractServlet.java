package examservlet.control;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exambean.model.QuestionBean;
import examdao.model.DatabassAccessObject;

/**
 * @author XDW
 * �������Զ���������࣬ʵ�ֹ��ܣ�
 * 1.ÿ�����������ȡָ������������ѡ�����5����
 * 2.�����е���Ŀ��Ϣ����QuestionBean���棻
 * 3.��Bean���붯̬����ArrayList����Ծ����飻
 * 4.���Ծ��������Session���󣬹���������
 */
@WebServlet("/HandlePaper")
public class QuestionExtractServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int tihao = 0;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<QuestionBean> listALL = new ArrayList<QuestionBean>();
		
		//��������
		//��ѡ���5������ѡ���4�����ж����5����������5����������3��
		int num1=5,num2=4,num3=5,num4=5,num5=3;
		int examTime=30;
		try {
			DatabassAccessObject db = new DatabassAccessObject();
			ResultSet rs=db.query("select * from paper");
			num1=Integer.valueOf(rs.getString("qty_sing"));
			num2=Integer.valueOf(rs.getString("qty_muti"));
			num3=Integer.valueOf(rs.getString("qty_jud"));
			num4=Integer.valueOf(rs.getString("qty_fill"));
			num5=Integer.valueOf(rs.getString("qty_ess"));
			examTime=Integer.valueOf(rs.getString("time"));
		} catch (Exception e) {
			
		}
		
		try {
			DatabassAccessObject db = new DatabassAccessObject();
			ResultSet rs = db.query("SELECT * FROM question");
			//����ÿ�����ͣ���������ͬ���͵��кŴ���˫��ѭ�������������ȡ��Ŀ
			LinkedList<Integer> list1 = new LinkedList<Integer>();
			LinkedList<Integer> list2 = new LinkedList<Integer>();
			LinkedList<Integer> list3 = new LinkedList<Integer>();
			LinkedList<Integer> list4 = new LinkedList<Integer>();
			LinkedList<Integer> list5 = new LinkedList<Integer>();
			while (rs.next()) {	//������������
				switch (rs.getString(2)) {	//���֧�����������
				case "��ѡ��":
					list1.add(rs.getRow());//��ȡ����ѡ������к�
					break;
				case "��ѡ��":
					list2.add(rs.getRow());
					break;
				case "�ж���":
					list3.add(rs.getRow());
					break;
				case "�����":
					list4.add(rs.getRow());
					break;
				case "�����":
					list5.add(rs.getRow());
					break;
				default:
					break;
				}
			}

					
			listALL.addAll(randomQue(list1, rs,num1));	
			listALL.addAll(randomQue(list2, rs,num2));	
			listALL.addAll(randomQue(list3, rs,num3));	
			listALL.addAll(randomQue(list4, rs,num4));	
			listALL.addAll(randomQue(list5, rs,num5));	

			tihao=0;	//��Ŵ�0��ʼ
			HttpSession session = request.getSession();
			session.setAttribute("examTime", examTime);
			session.setAttribute("ques", listALL);	//���Ծ����鱣�浽Session����
			//�ض����Ծ�ҳ�棬����������
			response.sendRedirect(request.getContextPath()+"/student/student_exam_paper.jsp");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * ����ÿ�����͡�ָ���������������
	 * @param list	��Ŀ�����洢��һ�����͵��кţ�
	 * @param rs	���ݱ�����
	 * @param count ��ȡָ������Ŀ����
	 * @return		����һ�����洢��count��ָ�����͵���Ŀ������
	 */
	public ArrayList<QuestionBean> randomQue(LinkedList<Integer> list,ResultSet rs,int count) {
		int m = -1;
		int index = -1;
		ArrayList<QuestionBean> listA = new ArrayList<QuestionBean>();
		while (list.size() > 0&&count>0) {
			count--;
			m = (int) (Math.random() * list.size());
			index = list.get(m);
			System.out.println(index);
			list.remove(m);
			tihao++;
			try {
				rs.absolute(index);
				QuestionBean queBean = new QuestionBean();
				queBean.setQ_id(String.valueOf(tihao));
				queBean.setQ_type(rs.getString(2));
				queBean.setQ_title(rs.getString(3));
				String selectString = rs.getString(4);
				System.out.println(rs.getString(2));
				queBean.setQ_score(rs.getString(5));
				queBean.setQ_key(rs.getString(6));
				queBean.setQ_img(rs.getString(7));
				if (selectString != null) {
					queBean.setQ_select(selectString);
					String[] temp = selectString.split("\\@");
					queBean.setOptions(temp);
				}
				listA.add(queBean);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listA;
	}
}
