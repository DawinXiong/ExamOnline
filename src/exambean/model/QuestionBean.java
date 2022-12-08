package exambean.model;

public class QuestionBean {
	private String q_id = null; // ��Ŀ��
	private String q_type = null; // ��Ŀ����
	private String q_title = null; // ��Ŀ����
	private String q_select = null; // ��Ŀѡ��
	private String q_score = null; // ��Ŀ��ֵ
	private String q_key = null; // �𰸻�ؼ���
	private String q_img = null; // ��ͼ
	private String[] options = null;// ��ֺ����Ŀ����ѡ��

	
	
	public QuestionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionBean(String q_id, String q_type, String q_title, String q_select, String q_score, String q_key,
			String q_img) {
		super();
		this.q_id = q_id;
		this.q_type = q_type;
		this.q_title = q_title;
		this.q_select = q_select;
		this.q_score = q_score;
		this.q_key = q_key;
		this.q_img = q_img;
	}

	public String getQ_img() {
		return q_img;
	}

	public void setQ_img(String q_img) {
		this.q_img = q_img;
	}

	public String getQ_id() {
		return q_id;
	}

	public void setQ_id(String q_id) {
		this.q_id = q_id;
	}

	public String getQ_type() {
		return q_type;
	}

	public void setQ_type(String q_type) {
		this.q_type = q_type;
	}

	public String getQ_title() {
		return q_title;
	}

	public void setQ_title(String q_title) {
		this.q_title = q_title;
	}

	public String getQ_select() {
		return q_select;
	}

	public void setQ_select(String q_select) {
		this.q_select = q_select;
	}

	public String getQ_score() {
		return q_score;
	}

	public void setQ_score(String q_score) {
		this.q_score = q_score;
	}

	public String getQ_key() {
		return q_key;
	}

	public void setQ_key(String q_answer) {
		this.q_key = q_answer;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] selects) {
		this.options = selects;
	}
	

}
