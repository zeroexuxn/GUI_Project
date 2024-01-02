package system.data;

public class CategoryType {
	//필드
	public static final String FILE_NAME = "src/files/data/category.txt";			//카테고리 파일 경로
	public static final String TMP_FILE_NAME = "src/files/data/category_tmp.txt";	//수정된 카테고리 파일을 임시로 저장할 경로
	
	public static final int INDEX_CATEGORY_ID = 0;	// 카테고리 ID를 가지고 있는 인덱스 번호
	public static final int INDEX_NAME = 1;			// 카테고리명을 가지고 있는 인덱스 번호
	public static final int INDEX_ENABLE = 2;		// 카테고리 활성화 여부를 가지고 있는 인덱스 번호
	
	private String categoryId = null;	// 카테고리 ID
	private String name = null;			// 카테고리명
	private String enable = "Y";		//카테고리 활성화 여부 (기본값 Y)
	
	// 생성자
	public CategoryType(String categoryId, String name, String enable) {
		this.categoryId = categoryId;
		this.name = name;
		this.enable= enable;
	}

	// getter & setter
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEnable() {
		return enable;
	}
	
	public void setEnable(String enable) {
		this.enable=enable;
	}
	
	//Category 객체의 정보 문자열로 출력할 경우 출력 양식
	public void printCategory() {
		System.out.printf("%6s", this.categoryId);
		System.out.print("\t");
		System.out.printf("%12s",this.name);
		System.out.print("\t");
		System.out.printf("%4s", this.enable);
		System.out.println();
	}
}
