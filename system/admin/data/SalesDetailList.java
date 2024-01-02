package system.admin.data;




import java.io.FileReader;

import java.io.IOException;

import java.util.*;

import system.data.SalesDetailType;

import java.io.*;
public class SalesDetailList {

	
	/*	상품 유형에 따라 value값 매핑
	 *  key: 메뉴, 포인트, 쿠폰
	 *  value: M, P, C
	 *  searchData메소드에서 keyword로 상품 유형을 입력받을 때, 그 상품 유형이 무엇을 뜻하는지 찾기 위해 사용
	 */
	private static final HashMap<String, String> TYPES = new HashMap<String, String>() { /**
		 * 
		 */
		private static final long serialVersionUID = 1L;//직렬화와 관련된 버전 UID

	{put("메뉴", "M"); put("포인트", "P"); put("쿠폰", "C"); } };
	
	private String date = null;
	private String fileName = "sales_detail";

	private Scanner sc = null;
	
	// 생성자
	public SalesDetailList(Scanner sc) {
		this.sc = sc;
	}
		
	// 파일명 설정
	/* 메소드명: setFileName
	 * 파라미터: String date
	 * 		  date: 매출일자
	 * 반환값: 없음
	 * 기능 설명: 파리미터로 받은 매출일자 정보를 이용해 사용할 상세 매출 내역 파일의 이름을 설정한다.
	 */
	public void setFileName(String date) {
		// src//files//sales_datas//sales_detail(YYYYMMDD).txt
		
		this.date = date;
		fileName = SalesDetailType.FILE_DIRECTORY + fileName + "(" + date + ").txt";
	}
	
	// 날짜 반환
	/* 메소드명: toDateFormat
	 * 파라미터: 없음
	 * 반환값: date문자열
	 * 기능 설명: 현재 객체에 저장되어 있는 매출일자(YYYYMMDD) 정보를 "YYYY년 MM월 DD일" 형식으로 변환하여 출력
	 * 		   만약 MM이 00이고 DD도 00이면 YYYY년 까지만 반환
	 * 		   DD가 00이면 YYYY년 MM월 까지만 반환
	 */
	public String toDateFormat() {
	    if (date.length()==8) {
	        String year=date.substring(0, 4);
	        String month=date.substring(4, 6);
	        String day=date.substring(6);

	        if (month.equals("00")) {
	            return year+"년";
	        } else if (day.equals("00")) {
	            if (month.startsWith("0")) {
	                month=month.substring(1);
	            }
	            return year+"년 "+month+"월";
	        } else {
	            if (month.startsWith("0")) {
	                month=month.substring(1);
	            }
	            if (day.startsWith("0")) {
	                day=day.substring(1);
	            }
	            return year+"년 "+month+"월 "+day+"일";
	        }
	    } else {
	        return date;
	    }
	}
	
	// 전체 출력
	/* 메소드명: printWholeData
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: src//files//sales_data에 존재하는 상세 매출 내역 파일(src//files//sales_data//sales_detail(YYYYMMDD).txt) 중
	 * 		   YYYYMMDD가 AdminSalesDetailProcess의 55번째 줄에서 입력받은 date와 동일한 파일 내용 전체 출력
	 */
	public void printWholeData() {
		ArrayList<String[]> salesDetalList = new ArrayList<String[]>();
		BufferedReader in;	// 입력 스트림
		
		String s;				// 파일에서 읽어온 값(한 줄)이 저장될 변수
		String[] tmp;			// 읽어온 값을 구분자로 분리하여 저장할 배열
	
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(fileName));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {  // 상세 매출 내역 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
			return;
		}
		
		// 파일에서 읽어온 값이 있는 동안
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				salesDetalList.add(tmp);
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		
		// 파일은 존재하나 파일 안에 내용이 없는 경우
		if(salesDetalList.size() == 0) {
			System.out.println("출력할 내용이 없습니다.");
			return;
		}
		
		// 출력 안내 문구
		System.out.printf("%s의 상세 매출 내역 %d건 전체를 출력합니다.\n", toDateFormat(), salesDetalList.size());
		
		int totalPage = (salesDetalList.size() % 15 != 0)? salesDetalList.size() / 15 + 1: salesDetalList.size() / 15;	// 한 번에 15건씩 출력한다고 가정할 때의 총 페이지 수
		int pointer = 0; // 리스트에 저장되어 있는 각 요소에 접근하기 위한 포인터
		
		// 모든 페이지 내용을 출력하기 위해 반복
		for(int currentPage=0; currentPage<totalPage; currentPage++) {
			System.out.printf("(%d / %d)\n", (currentPage+1), totalPage);	// (현재 페이지 / 전체 페이지 수)
			System.out.println("===========================================================================================");
			System.out.println("주문번호         유형  ID               내용                                    단가   수량     금액");
			System.out.println("===========================================================================================");
			
			// 리스트에 저장되어 있는 요소를 15개씩 출력
			for( ; pointer<15*(currentPage+1); pointer++) {
				if(pointer == salesDetalList.size())
					break;
				
				System.out.println(toPrintTemplate(salesDetalList.get(pointer)));	// 출력 양식에 맞게 출력

			}
			System.out.println("===========================================================================================");
			
			if(currentPage != (totalPage -1)) {
				System.out.print("다음 페이지를 확인하시려면 [Enter]를 입력해주세요\n");
				sc.nextLine();
			}
			
		}
	}
	
	// 조회
	/* 메소드명: searchData
	 * 파라미터: int index, String keyword
	 * 		  index: 칼럼의 인덱스 (ex. 2: 두 번째 칼럼)
	 * 		  keyword: 조회할 내용
	 * 반환값: 없음
	 * 기능 설명: 1. src//files//sales_data에 존재하는 상세 매출 내역 파일(src//files//sales_data//sales_detail(YYYYMMDD).txt) 중
	 * 			  YYYYMMDD가 AdminSalesDetailProcess의 55번째 줄에서 입력받은 date와 동일한 파일 찾기
	 * 		   2. 1.에서 찾은 파일의 상세 매출 내역 목록 중 AdminSalesDetailProcess에서 입력받은 keyword가 index에 배정된 값과 같은 
	 * 			  상세 매출 내역을 찾아서 출력
	 */
	public void searchData(int index, String keyword) {
		ArrayList<String[]> salesDetalList = new ArrayList<String[]>();
		BufferedReader in;	// 입력 스트림
		
		String s;				// 파일에서 읽어온 값(한 줄)이 저장될 변수
		String[] tmp;			// 읽어온 값을 구분자로 분리하여 저장할 배열
		String type = keyword; 	// 유형으로 조회하는 경우 유형의 종류(메뉴, 포인트, 쿠폰)
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(fileName));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {  // 상세 매출 내역 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
			return;
		}
		
		// 유형으로 조회하는 경우 각 유형에 따른 코드값(메뉴:M, 포인트:P, 쿠폰:C)으로 키워드 변경
		if(index == SalesDetailType.INDEX_TYPE) {
			keyword = TYPES.get(type);
		}
		
		// 파일에서 읽어온 값이 있는 동안
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				
				if(tmp[index].equals(keyword)) {	// index번째 값이 키워드가 일치하는 경우에만 리스트에 추가
					salesDetalList.add(tmp);
				}
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생하였습니다.");
			return;

		}
			
		// 파일은 존재하나 파일 안에 내용이 없는 경우
		if(salesDetalList.size() == 0) {
			System.out.println("조회된 내용이 없습니다.");
			return;
		}
		
		// 조회 기준 별 출력 안내 문구
		if(index == SalesDetailType.INDEX_ORDER_NO) {
			System.out.printf("%s의 상세 매출 내역 중 주문번호가 [%s]인 데이터가 %d건 조회되었습니다.\n", toDateFormat(), keyword, salesDetalList.size());
		} else if(index == SalesDetailType.INDEX_TYPE) {
			System.out.printf("%s의 상세 매출 내역 중 유형이 [%s]인 데이터가 %d건 조회되었습니다.\n", toDateFormat(), type, salesDetalList.size());
		} else if(index == SalesDetailType.INDEX_ID) {
			System.out.printf("%s의 상세 매출 내역 중 ID가 [%s]인 데이터가 %d건 조회되었습니다.\n", toDateFormat(), keyword, salesDetalList.size());
		}
		
		int totalPage = (salesDetalList.size() % 15 != 0)? salesDetalList.size() / 15 + 1: salesDetalList.size() / 15;	// 한 번에 15건씩 출력한다고 가정할 때의 총 페이지 수
		int pointer = 0; // 리스트에 저장되어 있는 각 요소에 접근하기 위한 포인터

		// 모든 페이지 내용을 출력하기 위해 반복
		for(int currentPage=0; currentPage<totalPage; currentPage++) {
			System.out.printf("(%d / %d)\n", (currentPage+1), totalPage);	// (현재 페이지 / 전체 페이지 수)
			System.out.println("===========================================================================================");
			System.out.println("주문번호         유형  ID               내용                                    단가   수량     금액");
			System.out.println("===========================================================================================");

			for( ; pointer<15*(currentPage+1); pointer++) {
				if(pointer == salesDetalList.size())
					break;
				
				System.out.println(toPrintTemplate(salesDetalList.get(pointer)));	// 출력 양식에 맞게 출력

			}
			System.out.println("===========================================================================================");
			
			if(currentPage != (totalPage -1)) {
				System.out.print("다음 페이지를 확인하시려면 [Enter]를 입력해주세요\n");
				sc.nextLine();
			}
			
		}
	}

	// 매개변수로 받은 문자열 배열을 출력 양식의 문자열로 변환 후 반환
	/* 메소드명: toPrintTemplate
	 * 파라미터: String[] data
	 * 		  data: 상세 매출 내역 파일에서 읽어온 한 줄의 문자열
	 * 반환값: String str
	 * 		 str: 상세 매출 내역의 각 데이터를 출력 양식에 맞게 변환한 문자열
	 * 기능 설명: 구분자(|)로 구분되어 있는 상세 매출 내역 데이터를 받아 출력 양식에 맞는 형태로 변환하여 반환한다.
	 */
	private String toPrintTemplate(String[] data) {
		String str = "";
		
		str += data[SalesDetailType.INDEX_ORDER_NO];
		str += "  " + data[SalesDetailType.INDEX_TYPE];
		str += "   " + String.format("%-15s", data[SalesDetailType.INDEX_ID]);
		str += "  " + String.format("%-30s", data[SalesDetailType.INDEX_NAME]);
		str += "\t" + String.format("%,5d", Integer.parseInt(data[SalesDetailType.INDEX_PRICE]));
		str += "  " + String.format("%3d", Integer.parseInt(data[SalesDetailType.INDEX_QUANTITY]));
		str += "  " + String.format("%,7d", Integer.parseInt(data[SalesDetailType.INDEX_TOTAL_PRICE]));

		return str;
	}
   /* 메소드명: getProductSalesData
	* 파라미터: 없음
	* 반환값: HashMap 타입 productSalesData (key는 상품명, value는 상품별 판매 개수)
	* 기능: 상세 매출 내역 파일에서 상품명을 키로 사용하여 상품별 총 판매 개수를 계산한 뒤 HashMap 타입으로 반환
	*     
	*/
    public HashMap<String, Integer> getProductSalesData() {
        ArrayList<String[]> salesDetailList=new ArrayList<>();
        BufferedReader in;

        try {
            in=new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {//파일이 존재하지 않을경우
            System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
            return new HashMap<>();
        }

        String s;
        String[] tmp;

        try {
            while ((s=in.readLine()) != null) {
                tmp=s.split("\\|");
                salesDetailList.add(tmp);
            }

            in.close();

        } catch (IOException e) {
            System.out.println("입출력 오류가 발생하였습니다.");
            return new HashMap<>();
        }

        // 상품명을 키로 사용하여 상품별 판매 개수를 계산
        HashMap<String, Integer> productSalesData=new HashMap<>();
        for (String[] data : salesDetailList) {
            if (!data[SalesDetailType.INDEX_NAME].equals("포인트 사용") && !data[SalesDetailType.INDEX_NAME].equals("쿠폰 할인")) {//상품명이 포인트 사용이나 쿠폰 할인인 경우는 상품이 팔린 경우가 아니기 때문에 제외
                String menuName=data[SalesDetailType.INDEX_NAME];
                int quantity=Integer.parseInt(data[SalesDetailType.INDEX_QUANTITY]);//판매 개수 가져오기
                //판매 개수를 누적해서 productSalesData에 저장
                productSalesData.put(menuName, productSalesData.getOrDefault(menuName, 0)+quantity);
            }
        }

        return productSalesData;
    }	
	/* 메소드명: printProductSalesGraph
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능: getProductSalesData 메소드로 가져온 상품명과 각 상품별 판매 개수로 그래프 출력하기.
	 * 	   판매량 기준 내림차순으로 그래프 출력 및 상품 판매량 최대 갯수에 따라 ■ 하나당 표현되는 판매 개수 유동적으로 설정
	 */
	public void printProductSalesGraph() {
		HashMap<String, Integer> menuSalesData=getProductSalesData();

	    if (menuSalesData.isEmpty()) {//상품별 판매 개수 데이터가 존재하지 않으면
	        System.out.println("상세 매출 데이터가 존재하지 않습니다.");
	        return;
	    }

	    System.out.printf("<< %s 메뉴별 판매 수량 그래프 >>\n", toDateFormat());

	    //1.productSalesData맵의 엔트리를 리스트 형태로 변환
	    //2. 1.의 과정으로 만들어진 리스트를 내림차순으로 정렬
	    List<Map.Entry<String, Integer>> menuEntries=new ArrayList<>(menuSalesData.entrySet());
	    menuEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

	    
	    //판매량 범위에 따라 ■ 하나당 표현되는 판매 개수 설정
	    int countFactor=1;
	    if (menuEntries.size()>0) {
	        int maxSalesCount=menuEntries.get(0).getValue();//위에서 내림차순으로 정렬한 리스트의 첫번째 값 가져오기 (내림차순 정렬을 했으므로 첫번째 값이 최댓값)
	        if (maxSalesCount>=1000) {

	            countFactor=100;
	        } else if (maxSalesCount>=100) {

	            countFactor=10;
	        }
	    }

	    //최대 상품명 길이 계산
	    int maxNameLength=0;
	    for (Map.Entry<String, Integer> entry : menuEntries) {//리스트의 각 엔트리에 대해 반복문 실행
	        String productName=entry.getKey();//현재 엔트리의 상품명 가져오기
	        maxNameLength=Math.max(maxNameLength, productName.length());//기존의 maxNameLength와 가져온 엔트리의 상품명 길이중 더 큰 값 maxNameLength로 저장
	    }

	    //그래프 출력
	    System.out.println("메뉴명\t\t\t│ 메뉴별 판매 수량");
	    System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

	    for (Map.Entry<String, Integer> entry : menuEntries) {//리스트의 각 엔트리에 대해 반복문 실행
	        String productName=entry.getKey();//상품명 가져오기
	        int salesCount=entry.getValue();  //가져온 상품의 판매량 가져오기

	        StringBuilder format=new StringBuilder("%-" + maxNameLength + "s \t│ ");//그래프 형식 문자열을 생성 (메뉴명 왼쪽 정렬, |로 구분선 형성)
	        int countBars=(int) Math.ceil(salesCount*1.0/countFactor);//판매량을 countFactor로 나누어 ■를 몇개 출력해야 하는지 계산 (아무리 낮은수라도 ■하나는 나오게 올림해서 계산)
	        for (int i=0; i<countBars; i++) {
	            format.append("■");
	        }
	        format.append(" %d개 ");

	        System.out.printf(format.toString(), productName, salesCount);
	        System.out.println();
	    }
	}
	/* 메소드명: getRevenueData
	 * 파라미터: 없음
	 * 반환값: HashMap productSalesData (key는 상품명, value는 상품별 매출액)
	 * 기능: 상세 매출 내역 파일에서 상품명을 키로 사용하여 상품별 매출액을 계산한 뒤 HashMap 타입으로 저장
	 */
	public HashMap<String, Integer> getRevenueData() {
        ArrayList<String[]> salesDetailList=new ArrayList<>();
        BufferedReader in;

        try {
            in=new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
            return new HashMap<>();
        }

        String s;
        String[] tmp;

        try {
            while ((s = in.readLine()) != null) {
                tmp = s.split("\\|");
                salesDetailList.add(tmp);
            }

            in.close();

        } catch (IOException e) {
            System.out.println("입출력 오류가 발생하였습니다.");
            return new HashMap<>();
        }

	    // 상품명을 키로 사용하여 상품별 판매 매출을 계산
	    HashMap<String, Integer> productSalesData = new HashMap<>();
	    for (String[] data : salesDetailList) {
	        if (!data[SalesDetailType.INDEX_NAME].equals("포인트 사용") && !data[SalesDetailType.INDEX_NAME].equals("쿠폰 할인")) {
	            String productName = data[SalesDetailType.INDEX_NAME];
	            int salesAmount = Integer.parseInt(data[SalesDetailType.INDEX_TOTAL_PRICE]);

	            productSalesData.put(productName, productSalesData.getOrDefault(productName, 0) + salesAmount);
	        }
	    }

	    return productSalesData;
	}
	/* 메소드명: printRevenueSalesGraph
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능: getRevenueData 메소드로 가져온 상품명과 각 상품별 매출액으로 그래프 출력하기.
	 * 	   매출액 기준 내림차순으로 그래프 출력 및 최대 매출액 범위에 따라 ■ 하나당 표현되는 매출액 유동적으로 설정
	 */

	public void printRevenueGraph() {
	    HashMap<String, Integer> revenueData = getRevenueData();

	    if (revenueData.isEmpty()) {
	        System.out.println("상세 매출 데이터가 존재하지 않습니다.");
	        return;
	    }

	    System.out.printf("<< %s 메뉴별 매출액 그래프 >>\n", toDateFormat());

	    //그래프를 판매 매출 기준 내림차순으로 정렬
	    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(revenueData.entrySet());
	    sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

	    int maxNameLength = 0;
	    int maxSalesAmount = 0;  // 최대 판매 매출액을 저장할 변수
	    int countFactor = 0;     // 매출액 단위 계수

	    //최대 판매 매출액을 찾아서 해당 단위 계수를 설정
	    for (Map.Entry<String, Integer> entry : sortedEntries) {
	        String productName = entry.getKey();
	        int salesAmount = entry.getValue();

	        maxNameLength = Math.max(maxNameLength, productName.length());
	        maxSalesAmount = Math.max(maxSalesAmount, salesAmount);
	    }

	    if (maxSalesAmount >= 10000000) {
	        countFactor = 1000000;
	    } else if (maxSalesAmount >= 1000000) {
	        countFactor = 100000;
	    } else if (maxSalesAmount >= 100000) {
	        countFactor = 10000;
	    } else {
	        countFactor = 1000;
	    }

	    // 그래프 출력
	    System.out.println("메뉴명\t\t\t│ 메뉴별 매출액");
	    System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
	    for (Map.Entry<String, Integer> entry : sortedEntries) {
	        String productName = entry.getKey();
	        int salesAmount = entry.getValue();

	        System.out.printf("%-" + maxNameLength + "s \t│ ", productName);

	        for (int i = 0; i < salesAmount / countFactor; i++) {
	            System.out.print("■");
	        }
	        System.out.printf(" %,d원 ", salesAmount);

	        System.out.println();
	    }
	}
	
	
}
