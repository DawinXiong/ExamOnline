package examservlet.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import examdao.model.DatabassAccessObject;

/**
 * @author XDW
 * ��������������Ŀ����ɾ�Ĳ�
 */
@WebServlet("/HandleQue")
@MultipartConfig //֧���ļ��ϴ�
public class QuestionModifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // ���봦��
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String executeMode = request.getParameter("executeMode");
		int mode = Integer.parseInt(executeMode); // ת������
		System.out.println(mode);
		
		String number=request.getParameter("number");
		String type="";
		String title="";
		String score="";
		String key="";
		String select = "";
		String fileName ="";
		if (mode!=2) {	//�������ִ��ɾ������ִ��
			type=request.getParameter("type");
			title=request.getParameter("title");
			score=request.getParameter("score");
			key=request.getParameter("key");
			if (type.equals("��ѡ��")||type.equals("��ѡ��")) {
				String[] temp=request.getParameterValues("select");
				for (int i = 0; i < temp.length; i++) {
					select+=temp[i];
					if (i!=temp.length-1) {
						select+="@";
					}
				}
			}else {
				select=request.getParameter("select");
			}
			System.out.print(select);
			Part part = request.getPart("img"); //��Ŀ��ͼ
			fileName = part.getSubmittedFileName(); // ��ȡpart������Я�����ļ�����
			if (fileName.length() > 0) { //����û��ϴ�����Ŀ��ͼ
				String savePath = getServletContext().getRealPath("/images");
				part.write(savePath + "/" + fileName);//�ϴ�ͼƬ������Ŀ¼��

				try {//try�ܱ�֤�ļ�������ȷ�ر�
					//ͼƬ��ԭʼ·����tomcat�ķ���Ŀ¼�£�
					Path sorcePath = Paths.get(savePath + "/" + fileName); 
					//ͼƬ��Ŀ��·������ǰ��ĿĿ¼��
					Path targetPath = Paths.get("D:\\exam_online\\"+
							request.getContextPath()+"/web/images/" + fileName);
					//�����ļ����滻�Ѵ��ڵ��ļ�
					Files.copy(sorcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				} catch (Exception e) {}
				
			}
		}

		try {
			DatabassAccessObject db = new DatabassAccessObject();
			switch (mode) {
			case 0://��
				db.insert("insert into question values (?,?,?,?,?,?,?) ; ",number,type,title,select,score,key,fileName);
				break;
			case 1://��
				if (fileName!="") {
					db.modify("update question set type = ? , title  = ? , `select` = ? , score = ? , `key` = ? , img = ? where number = ? ;",type,title,select,score,key,fileName,number);
				}else {
					db.modify("update question set type = ? , title  = ? , `select` = ? , score = ? , `key` = ?  where number = ? ;",type,title,select,score,key,number);
				}
				break;
			case 2://ɾ
				db.modify("delete from question where number = ? ; ", number);
				break;
			default:
				break;
			}		
		} catch (Exception e) {
		}
		
		if (mode==0) {
			PrintWriter out = response.getWriter();
			out.println ("<script language=javascript>window.location='"+request.getContextPath()+"/teacher/teacher_que_add.jsp';alert('�ѳɹ������Ŀ');</script>");
		}else
		response.sendRedirect("ShowQuePage");

	}

}
