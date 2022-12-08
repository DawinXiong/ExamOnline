package examService.service;

import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import examdao.model.DatabassAccessObject;

public class ScoreExcleService {


	public static Workbook getAllByDatabase() {
		Workbook wb = null;
		try {

			wb = new HSSFWorkbook();
			
			Sheet sheet = wb.createSheet("tb_stuScore");
			Row row = sheet.createRow(0);
			Cell cell = null;
			DatabassAccessObject db = new DatabassAccessObject();
			/**
			 * ��������
			 */
			String arr[] = {"ѧ��","�༶","����","��ѡ","��ѡ","�ж�","���","���","�ܷ�","����"};
			for (int j = 0; j < arr.length; j++) {
				cell = row.createCell(j);
				cell.setCellValue(arr[j]);
				
			}
			/**
			 * ������ֵ
			 */
			ResultSet rs = db.query("select score.ID,class,name,score_sing,score_muti,score_jud,score_fill,score_ess,score.score,grade from student join score on student.id=score.id;");
			int i=0;
			int j = 1;
			while (rs.next()) {
				
				row = sheet.createRow(j);
				j++;
				int rowNum = rs.getMetaData().getColumnCount();
				for (i = 0; i < rowNum; i++) {
					cell = row.createCell(i);
					String s=rs.getString(i+1);
					cell.setCellValue(s);
				}
								

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;

	}
}
