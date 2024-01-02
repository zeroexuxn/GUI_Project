package system.admin.data;

import java.io.*;
import java.util.*;

import system.data.SalesTotalType;

public class SalesTotalList{

	
	private String date = null;
	private String fileName = "sales_total";

	private Scanner sc = null;
	
	// 생성자
	public SalesTotalList(Scanner sc) {
		this.sc = sc;
	}

	// 파일명 설정
	/* 메소드명: setFileName
	 * 파라미터: String date
	 * 		  date: 매출일자
	 * 반환값: 없음
	 * 기능 설명: 파리미터로 받은 매출일자 정보를 이용해 사용할 통합 매출 내역 파일의 이름을 설정한다.
	 */
	public void setFileName(String date) {
		// src//files//sales_datas//sales_total(YYYYMMDD).txt
		
		this.date = date;
		fileName = SalesTotalType.FILE_DIRECTORY + fileName + "(" + date + ").txt";
	}

	// 날짜 반환
	/* 메소드명: toDateFormat
	 * 파라미터: 없음
	 * 반환값: "YYYY년 MM월 DD일"
	 * 기능 설명: 현재 객체에 저장되어 있는 매출일자(YYYYMMDD) 정보를 "YYYY년 MM월 DD일" 형식으로 변환하여 출력한다.
	 */
	public String toDateFormat() {
		// YYYY년 MM월 DD일 형식으로 반환
		if (date.length()!=8) {
			return date;
		}
		return date.substring(0, 4) + "년 " + date.substring(4, 6) + "월 " + date.substring(6) + "일";
	}
	
	// 날짜 반환
	/* 메소드명: toDateFormatMonth
	 * 파라미터: 없음
	 * 반환값: "MM월"
	 * 기능 설명: 현재 객체에 저장되어 있는 매출일자(MM) 정보를 "MM월" 형식으로 변환하여 출력한다.
	 */
	public String toDateFormatMonth() {
		// MM월 형식으로 반환
		if (date.length()!=8) {
			return date;
		}
		return date.substring(4, 6) + "월 ";
	}
	
	// 날짜 반환
		/* 메소드명: toDateFormatMonth
		 * 파라미터: 없음
		 * 반환값: "MM월"
		 * 기능 설명: 현재 객체에 저장되어 있는 매출일자(MM) 정보를 "MM월" 형식으로 변환하여 출력한다.
		 */
		public String toDateFormatYear() {
			// YYYY년 형식으로 반환
			if (date.length()!=8) {
				return date;
			}
			return date.substring(0, 4) + "년 ";
		}
		
	// 전체 출력
	/* 메소드명: printWholeData
	 * 파라미터: 없음
	 * 반환값: 없음
	 * 기능 설명: src//files//sales_data에 존재하는 통합 매출 내역 파일(src//files//sales_data//sales_total(YYYYMMDD).txt) 중
	 * 		   YYYYMMDD가 AdminSalesTotalProcess의 58번쨰 줄에서 입력받은 date와 동일한 파일 내용 전체 출력
	 */
	public void printWholeData() {
		ArrayList<String[]> salesTotalList = new ArrayList<String[]>();
		BufferedReader in;	// 입력 스트림
		
		String s;					// 파일에서 읽어온 값(한 줄)이 저장될 변수
		String[] tmp;				// 읽어온 값을 구분자로 분리하여 저장할 배열
		int totalSalesAmt = 0;		// 총 거래 금액
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(fileName));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {  // 통합 매출 내역 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.printf("%s의 통합 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
			return;
		}

		// 파일에서 읽어온 값이 있는 동안
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				salesTotalList.add(tmp);
				
				totalSalesAmt += Integer.parseInt(tmp[SalesTotalType.INDEX_TOTAL_AMT]);
			}
			
			// 스트림 닫기
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("입출력 오류가 발생하였습니다.");
			return;
		}
		
		// 파일은 존재하나 파일 안에 내용이 없는 경우
		if(salesTotalList.size() == 0) {
			System.out.println("출력할 내용이 없습니다.");
			return;
		}
		
		// 출력 안내 문구
		System.out.printf("%s의 통합 매출 내역 %d건 전체를 출력합니다.\n", toDateFormat(), salesTotalList.size());

		
		int totalPage = (salesTotalList.size() % 15 != 0)? salesTotalList.size() / 15 + 1: salesTotalList.size() / 15;	// 한 번에 15건씩 출력한다고 가정할 때의 총 페이지 수
		int pointer = 0; // 리스트에 저장되어 있는 각 요소에 접근하기 위한 포인터
		
		// 모든 페이지 내용을 출력하기 위해 반복
		for(int currentPage=0; currentPage<totalPage; currentPage++) {
			System.out.printf("(%d / %d)\n", (currentPage+1), totalPage);	// (현재 페이지 / 전체 페이지 수)
			System.out.println("=======================================================================================================================================================================");
			System.out.println("유형  대기번호  거래일시           주문번호           거래금액    공급가액    부가세  결제방법  회원/쿠폰 ID        카드사명  카드번호             할부개월  승인번호    적립 회원 ID    적립 포인트  누적 포인트");
			System.out.println("=======================================================================================================================================================================");
			
			// 리스트에 저장되어 있는 요소를 15개씩 출력
			for( ; pointer<15*(currentPage+1); pointer++) {
				if(pointer == salesTotalList.size())
					break;
				
				System.out.println(toPrintTemplate(salesTotalList.get(pointer)));	// 출력 양식에 맞게 출력

			}
			System.out.println("=======================================================================================================================================================================");
			
			if(currentPage != (totalPage -1)) {
				System.out.print("다음 페이지를 확인하시려면 [Enter]를 입력해주세요\n");
				sc.nextLine();
			}
			
		}
		System.out.printf("총 매출액: %,d\n", totalSalesAmt);
		
	}
	
	// 조회
	/* 메소드명: searchData
	 * 파라미터: int index, String keyword
	 * 		  index: 칼럼의 인덱스 (ex. 2: 두 번째 칼럼)
	 * 		  keyword: 조회할 내용
	 * 반환값: 없음
	 * 기능 설명: 1. src//files//sales_data에 존재하는 통합 매출 내역 파일(src//files//sales_data//sales_total(YYYYMMDD).txt) 중
	 * 			  YYYYMMDD가 AdminSalesTotalProcess의 58번째 줄에서 입력받은 date와 동일한 파일 찾기
	 * 		   2. 1.에서 찾은 파일의 통합 매출 내역 목록 중 AdminSalesTotlProcess에서 입력받은 keyword가 index에 배정된 값과 같은 
	 * 			  통합 매출 내역을 찾아서 출력
	 */
	public void searchData(int index, String keyword) {
		ArrayList<String[]> salesTotalList = new ArrayList<String[]>();
		BufferedReader in;	// 입력 스트림
		
		String s;		// 파일에서 읽어온 값(한 줄)이 저장될 변수
		String[] tmp;	// 읽어온 값을 구분자로 분리하여 저장할 배열
		
		try {
			//System.out.println("입력 스트림을 생성중...");
			in = new BufferedReader(new FileReader(fileName));	// 파일 내용을 읽어오기 위한 스트림

		} catch (FileNotFoundException e) {  // 통합 매출 내역 파일이 없는 경우
			// TODO Auto-generated catch block
			System.out.printf("%s의 통합 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
			return;
		}

		// 파일에서 읽어온 값이 있는 동안
		try {
			while((s = in.readLine()) != null) {
				tmp = s.split("\\|");
				
				if(tmp[index].equals(keyword)) {	// index번째 값이 키워드가 일치하는 경우에만 리스트에 추가
					salesTotalList.add(tmp);
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
		if(salesTotalList.size() == 0) {
			System.out.println("조회된 내용이 없습니다.");
			return;
		}
			
		// 조회 기준 별 출력 안내 문구
		if(index == SalesTotalType.INDEX_ORDER_NO) {
			System.out.printf("%s의 통합 매출 내역 중 주문번호가 [%s]인 거래가 %d건 조회되었습니다.\n", toDateFormat(), keyword, salesTotalList.size());
		} else if(index == SalesTotalType.INDEX_TYPE) {
			System.out.printf("%s의 통합 매출 내역 중 [%s] 주문 건이 %d건 조회되었습니다.\n", toDateFormat(), keyword, salesTotalList.size());
		} else if(index == SalesTotalType.INDEX_PAY_METHOD) {
			System.out.printf("%s의 통합 매출 내역 중 [%s] 결제 건이 %d건 조회되었습니다.\n", toDateFormat(), keyword, salesTotalList.size());
		}
		
		int totalPage = (salesTotalList.size() % 15 != 0)? salesTotalList.size() / 15 + 1: salesTotalList.size() / 15;	// 한 번에 15건씩 출력한다고 가정할 때의 총 페이지 수
		int pointer = 0; // 리스트에 저장되어 있는 각 요소에 접근하기 위한 포인터

		// 모든 페이지 내용을 출력하기 위해 반복
		for(int currentPage=0; currentPage<totalPage; currentPage++) {
			System.out.printf("(%d / %d)\n", (currentPage+1), totalPage);	// (현재 페이지 / 전체 페이지 수)
			System.out.println("=======================================================================================================================================================================");
			System.out.println("유형  대기번호  거래일시           주문번호           거래금액    공급가액    부가세  결제방법  회원/쿠폰 ID        카드사명  카드번호             할부개월  승인번호    적립 회원 ID    적립 포인트  누적 포인트");
			System.out.println("=======================================================================================================================================================================");
			
			for( ; pointer<15*(currentPage+1); pointer++) {
				if(pointer == salesTotalList.size())
					break;
				
				System.out.println(toPrintTemplate(salesTotalList.get(pointer)));	// 출력 양식에 맞게 출력

			}
			System.out.println("=======================================================================================================================================================================");
			
			if(currentPage != (totalPage -1)) {
				System.out.print("다음 페이지를 확인하시려면 [Enter]를 입력해주세요\n");
				sc.nextLine();
			}
			
		}
	}
	
	// 매개변수로 받은 문자열 배열을 출력 양식의 문자열로 변환 후 반환
	/* 메소드명: toPrintTemplate
	 * 파라미터: String[] data
	 * 		  data: 통합 매출 내역 파일에서 읽어온 한 줄의 문자열
	 * 반환값: String str
	 * 		 str: 통합 매출 내역의 각 데이터를 출력 양식에 맞게 변환한 문자열
	 * 기능 설명: 구분자(|)로 구분되어 있는 통합 매출 내역 데이터를 받아 출력 양식에 맞는 형태로 변환하여 반환한다.
	 */
	private String toPrintTemplate(String[] data) {
		String str = "";
		
		str += data[SalesTotalType.INDEX_TYPE];
		str += "  " + data[SalesTotalType.INDEX_WAIT_NO];
		str += "   " + data[SalesTotalType.INDEX_TRANS_DT];
		str += "  " + data[SalesTotalType.INDEX_ORDER_NO];
		str += "  " + String.format("%,7d", Integer.parseInt(data[SalesTotalType.INDEX_TOTAL_AMT]));
		str += "  " + String.format("%,7d", Integer.parseInt(data[SalesTotalType.INDEX_SUPPLY_AMT]));
		str += "  " + String.format("%,6d", Integer.parseInt(data[SalesTotalType.INDEX_VAT]));
		str += "   " + data[SalesTotalType.INDEX_PAY_METHOD];
		str += "  " + String.format("%-15s", data[SalesTotalType.INDEX_DISCOUNT_ID]);
		str += "  " + String.format("%-5s", data[SalesTotalType.INDEX_CARD_NAME]);
		str += "  " + String.format("%16s", data[SalesTotalType.INDEX_CARD_NO]);
		str += "  " + String.format("%2s", data[SalesTotalType.INDEX_CARD_QUOTA]);
		str += "     " + String.format("%8s", data[SalesTotalType.INDEX_AUTH_CODE]);
		str += "  " + String.format("%11s", data[SalesTotalType.INDEX_MEMBER_ID]);
		str += "  " + String.format("%,7d", Integer.parseInt(data[SalesTotalType.INDEX_REWARD_PTS]));
		str += "  " + String.format("%,7d", Integer.parseInt(data[SalesTotalType.INDEX_TOTAL_PTS]));

		return str;
	}
	/* 메소드명: getRevenueData
	 * 파라미터: 없음
	 * 반환값: TreeMap productSalesData (key는 거래일, value는 총 거래 금액)
	 * 기능: 통합 매출 내역 파일에서 거래일을 키로 사용하여 상품별 매출액을 계산한 뒤 TreeMap 타입으로 저장
	 */
	public TreeMap<String, Integer> getRevenueData() {
        ArrayList<String[]> salesTotalList = new ArrayList<>();
        BufferedReader in;

        try {
            in = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.printf("%s의 상세 매출 데이터가 존재하지 않습니다.\n", toDateFormat());
            return new TreeMap<>();
        }

        String s;
        String[] tmp;

        try {
            while ((s = in.readLine()) != null) {
                tmp = s.split("\\|");
                salesTotalList.add(tmp);
            }

            in.close();

        } catch (IOException e) {
            System.out.println("입출력 오류가 발생하였습니다.");
            return new TreeMap<>();
        }

	    // 주문번호를 키로 사용하여 상품별 판매 매출을 계산
	    TreeMap<String, Integer> productSalesData = new TreeMap<>();
	    for (String[] data : salesTotalList) {
	    	
            String productTransDT = data[SalesTotalType.INDEX_TRANS_DT].substring(0,8);
            int salesTotalAmt = Integer.parseInt(data[SalesTotalType.INDEX_TOTAL_AMT]);

            productSalesData.put(productTransDT, productSalesData.getOrDefault(productTransDT, 0) + salesTotalAmt);
	        
	    }

	    return productSalesData;
	}
	/* 메소드명: printReveuneGraph
	 * 파라키터: 없음
	 * 반환값: 없음
	 * 기능: getRevenueData 메소드로 가져온 날짜와 해당 날짜의 매출액으로 그래프 출력
	 */
	public void printRevenueGraph() {
	    TreeMap<String, Integer> revenueData = getRevenueData();

	    if (revenueData.isEmpty()) {
	        System.out.println("상세 매출 데이터가 존재하지 않습니다.");
	        return;
	    }

	    System.out.printf("<< %s상품별 판매 매출 그래프 >>\n", toDateFormatMonth());

	    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(revenueData.entrySet());//날짜 기준으로 오름차순 정렬

	    int maxsalesTotalAmt = 0;  // 최대 판매 매출액을 저장할 변수
	    int countFactor = 0;     // 매출액 단위 계수

	    // 최대 판매 매출액을 찾아서 해당 단위 계수를 설정
	    for (Map.Entry<String, Integer> entry : sortedEntries) {

	        int salesTotalAmt = entry.getValue();

	        maxsalesTotalAmt = Math.max(maxsalesTotalAmt, salesTotalAmt);
	    }

	    if (maxsalesTotalAmt >= 10000000) {
	        countFactor = 1000000;
	    } else if (maxsalesTotalAmt >= 1000000) {
	        countFactor = 100000;
	    } else if (maxsalesTotalAmt >= 100000) {
	        countFactor = 10000;
	    } else {
	        countFactor = 1000;
	    }
	    
	    // 그래프 출력
	    System.out.println("거래일시\t\t│ 판매 매출");
	    System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
	    for (Map.Entry<String, Integer> entry : sortedEntries) {
	        String productTransDT = entry.getKey();
	        int salesTotalAmt = entry.getValue();

	        System.out.printf("%-8s \t│ ", productTransDT);

	        for (int i = 0; i < salesTotalAmt / countFactor; i++) {
	            System.out.print("■");
	        }
	        System.out.printf(" %,d원 ", salesTotalAmt);

	        System.out.println();
	    }
	}
}
