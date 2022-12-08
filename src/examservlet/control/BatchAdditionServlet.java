package examservlet.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import examService.service.QuestionExcelService;
import examService.service.StuInfoExcelService;
import exambean.model.QuestionBean;
import exambean.model.StudentInfoBean;
import examdao.model.DatabassAccessObject;


/**
 * @author XDW
 * ��������û��Ķ�ȡExcel�ļ�����
 * ����ExcelService�����Excel�ļ���
 * �������ı���Ϣд�����ݿ���У�
 * ���⣬�����Խ��������ϴ���ͼƬ�������浽����Ŀ¼��
 */
@WebServlet("/HandleBatchAdd")
@MultipartConfig // ֧���ļ��ϴ�
public class BatchAdditionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // ���봦��
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String excelSorcePath = "";
		Integer mode = Integer.valueOf(request.getParameter("mode"));
		String savePath = getServletContext().getRealPath("/images");

		/**
		 * ���²���Ϊ��ȡ�ϴ�Excel�ļ������浽����Ŀ¼
		 */
		Part part = request.getPart("excel"); // Excle�ļ�
		String fileName = part.getSubmittedFileName(); // ��ȡpart������Я�����ļ�����
		if (fileName.length() > 0) { // ����ļ���Ϊ��
			savePath = getServletContext().getRealPath("/images");
			part.write(savePath + "/" + fileName);// �ϴ��ļ�������Ŀ¼��
			excelSorcePath = savePath + "/" + fileName;
			// ͼƬ��Ŀ��·������ǰ��ĿĿ¼��
		}

		/**
		 * ���²���Ϊ��ȡ�����ϴ�����ͼ
		 */
		for (Part imgPath : request.getParts()) { // �����ϴ���ÿһ��part
			if (imgPath.getName().startsWith("img")) {
				// �ϴ���ťҲ��part������������if������������
				// ֻ����part���Ʊ�ͷΪ"img"������ͼƬ����
				String fileName1 = imgPath.getSubmittedFileName(); // ��ȡ����ͼƬ���ļ���
				if (fileName1 == null || fileName1.length() == 0)
					break;
				try {
					imgPath.write(savePath + "/" + fileName1); // д��ͼƬ��tomcat�ķ���Ŀ¼��
					
				} catch (Exception e) {
					System.out.println(e);
				}

				/**
				 * ������Ҫ��ͼƬ�ӡ�tomcat����Ŀ¼������������ǰ��ĿĿ¼�� ԭ�򣺷���Ŀ¼��tomcat���������·������ϴ���ͼƬ��ҪǨ�Ƶ����ñ�����λ��
				 * Ŀ�ģ���ͼƬ��������ĿĿ¼���ϴ���ͼƬ���Ա��������������������
				 */
				try {// try�ܱ�֤������ȷ�ر�
					Path sorcePath = Paths.get(savePath + "/" + fileName1); // ͼƬ��ԭʼ·����tomcat�ķ���Ŀ¼�£�
					// ͼƬ�ĵ�Ŀ��·������ǰ��ĿĿ¼��
					Path targetPath = Paths.get("D:\\exam_online\\" + request.getContextPath()
							+ "/web/images/" + fileName1);
					Files.copy(sorcePath, targetPath, StandardCopyOption.REPLACE_EXISTING); // �����ļ������滻�Ѵ��ڵ��ļ�
				} catch (Exception e) {
				}

			}
		}

		try {
			DatabassAccessObject db;
			switch (mode) {
			case 1:
				List<QuestionBean> listExcel = QuestionExcelService.getAllByExcel(excelSorcePath);
				db = new DatabassAccessObject();
				for (QuestionBean queBean : listExcel) {
					if (!QuestionExcelService.isExist(queBean.getQ_id())) {
						db.insert("insert into question values (?,?,?,?,?,?,?) ; ", queBean.getQ_id(),
								queBean.getQ_type(), queBean.getQ_title(), queBean.getQ_select(), queBean.getQ_score(),
								queBean.getQ_key(), queBean.getQ_img());
					} else {
						db.modify(
								"update question set type = ? , title  = ? , `select` = ? , score = ? , `key` = ? , img = ? where number = ? ;",
								queBean.getQ_type(), queBean.getQ_title(), queBean.getQ_select(), queBean.getQ_score(),
								queBean.getQ_key(), queBean.getQ_img(), queBean.getQ_id());
					}
				}

				System.out.println("���ݵ���ɹ�");
				PrintWriter out = response.getWriter();
				out.println("<script language=javascript>alert('���ݵ���ɹ�');window.location='" + request.getContextPath()
						+ "/ShowQuePage';</script>");
				break;
			case 2:
				List<StudentInfoBean> stuListExcel = StuInfoExcelService.getAllByExcel(excelSorcePath);

				db = new DatabassAccessObject();
				for (StudentInfoBean stuBean : stuListExcel) {
					if (!StuInfoExcelService.isExist(stuBean.getID())) {
						db.insert("INSERT INTO student (ID,password,name,class,score) VALUES (?,?,?,?,?) ; ",
								stuBean.getID(), stuBean.getPassword(), stuBean.getName(), stuBean.getCLASS(),
								stuBean.getScore());
					} else {
						db.modify("UPDATE student SET  password = ? , name = ? , class = ? , score = ? WHERE ID = ? ;",
								stuBean.getPassword(), stuBean.getName(), stuBean.getCLASS(), stuBean.getScore(),
								stuBean.getID());
					}
				}

				System.out.println("���ݵ���ɹ�");
				PrintWriter out2 = response.getWriter();
				out2.println("<script language=javascript>alert('���ݵ���ɹ�');window.location='" + request.getContextPath()
						+ "/ShowStuPage';</script>");
			default:
				break;
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
