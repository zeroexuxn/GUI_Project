package system.admin.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SalesDataMerger {

	//같은 해의 상세 매출 내역 파일 통합
    public static void mergeMonthlySalesByYear(int year) {
        //생성되는 파일: src//files//sales_data//sales_detail 경로에 YYYY0000 형식으로 만들어짐
    	String mergedFileName="src//files//sales_data//sales_detail("+String.format("%04d0000).txt", year);
    	mergeDetailData(mergedFileName, String.format("%04d", year), String.format("%04d00", year));
    }
    //같은 달의 상세 매출 내역 파일 통합
    public static void mergeDailySalesByMonth(int year, int month) {
    	//생성되는 파일: src//files//sales_data//sales_detail 경로에 YYYYMM00 형식으로 만들어짐
        String mergedFileName="src//files//sales_data//sales_detail("+String.format("%04d%02d00).txt", year, month);
        mergeDetailData(mergedFileName, String.format("%04d%02d", year, month), String.format("%04d%02d00", year, month));
    }
    //같은 달의 통합 매출 내역 파일 통합
    public static void mergeDailySalesTotalByMonth(int year, int month) {
    	//생성되는 파일: src//files//sales_data//sales_total 경로에 YYYYMM00 형식으로 만들어짐
        String mergedFileName = "src//files//sales_data//sales_total(" + String.format("%04d%02d00).txt", year, month);
        mergeToTalData(mergedFileName, String.format("%04d%02d", year, month), String.format("%04d%02d00", year, month));
    }
    /* 메소드명: mergeData
     * 파라미터: String mergedFileName (합쳐진 데이터를 저장할 파일명), String target(데이터를 통합할 대상), String exclude(데이터 통합에서 제외할 파일명)
     * 반환값: 없음
     * 기능: taget으로 전달받은 문자열을 보유하고있는 모든 상세 매출 내역 파일의 내용 합치기
     *     exclude로 전달받은 파일명을 가진 상세 매출 내역 파일은 합치지 않음
     *     통합 완료된 데이터는 mergedFileName으로 전달받은 파일명으로 저장됨
     */
    public static void mergeDetailData(String mergedFileName, String target, String exclude) {
        List<String> fileNames=new ArrayList<>();						//데이터 통합 대상 파일들을 저장할 리스트
        File salesDataDirectory=new File("src/files/sales_data/");		//상세 매출 내역 파일 경로
        File[] filesInDirectory=salesDataDirectory.listFiles();			//디렉테러 내 파일 목록

        if (filesInDirectory != null) {//지정된 경로가 비어있지 않으면
            for (File file : filesInDirectory) {//filesInDirectory 배열 안에서 파일 하나씩 가져오기
                String fileName=file.getName();
                //파일 이름이 sales_detail+타겟 문자열로 시작하고 확장자가 txt이며 데이터 통합에서 제외할 파일명이 아니면
                if (fileName.startsWith("sales_detail(" + target) && fileName.endsWith(".txt") && !fileName.equals(exclude + ".txt")) {
                    fileNames.add(file.getAbsolutePath());//데이터 통합 대상 파일 리스트에 해당 파일의 절대경로 추가
                }
            }
        }

        try (BufferedWriter out=new BufferedWriter(new FileWriter(mergedFileName))) {
            Set<String> uniqueLines=new HashSet<>(); //중복 제외하기 위해 HashSet 생성

            for (String fileName : fileNames) {//fileNames 리스트에서 파일명 하나씩 가져오기
                BufferedReader in=new BufferedReader(new FileReader(fileName));//가져온 파일 읽기
                String line;//한 행씩 읽어올 변수

                while ((line=in.readLine()) != null) {//line이 파일을 한 행씩 읽을 수 있는 동안 (결과가 비어있지 않으면)
                    //행이 비어있지 않고 중복되지 않은 경우에만 추가
                    if (!line.equals("") && uniqueLines.add(line)) {
                    	out.write(line);
                    	out.newLine();
                    }
                }

                in.close();
            }

            System.out.println("매출 데이터 합치기가 완료되었습니다.");
        } catch (IOException e) {
            System.out.println("입출력 오류가 발생했습니다.");
            return;
        }
    }
    /* 메소드명: mergeToTalData
     * 파라미터: String mergedFileName (합쳐진 데이터를 저장할 파일명), String target(데이터를 통합할 대상), String exclude(데이터 통합에서 제외할 파일명)
     * 반환값: 없음
     * 기능: taget으로 전달받은 문자열을 보유하고있는 모든 통ㅇ합 매출 내역 파일의 내용 합치기
     * 		exclude로 전달받은 파일명을 가진 통합 매출 내역 파일은 합치지 않음
     *      통합 완료된 데이터는 mergedFileName으로 전달받은 파일명으로 저장됨
     */
    public static void mergeToTalData(String mergedFileName, String target, String exclude) {
        List<String> fileNames = new ArrayList<>();						//데이터 통합 대상 파일들을 저장할 리스트
        File salesDataDirectory = new File("src//files//sales_data//");//통합 매출 내역 파일 경로
        File[] filesInDirectory = salesDataDirectory.listFiles();		//디렉터리 내 파일 목록

        if (filesInDirectory != null) {//지정된 경로가 비어있지 않으면
            for (File file : filesInDirectory) {//파일 하니씩 가져오기
                String fileName = file.getName();
                //파일 이름이 sales_total+타겟 문자열로 시작하고 확장자가 txt이며 데이터 통합에서 제외할 파일명이 아니면
                if (fileName.startsWith("sales_total(" + target) && fileName.endsWith(".txt") && !fileName.equals(exclude + ".txt")) {
                    fileNames.add(file.getAbsolutePath());//데이터 통합 대상 파일 리스트에 해당 파일의 절대경로 추가
                }
            }
        }

        try (BufferedWriter out = new BufferedWriter(new FileWriter(mergedFileName))) {
            Set<String> uniqueLines = new HashSet<>();//중복 제외하기 위해 HashSet 생성

            for (String fileName : fileNames) {//fileNames 리스트에서 파일명 하나씩 가져오기
                BufferedReader in = new BufferedReader(new FileReader(fileName));
                String line;

                while ((line = in.readLine()) != null) {//line이 파일을 한 행씩 읽을 수 있는 동안 (결과가 비어있지 않으면)
                    //중복되지 않은 경우에만 추가
                    if (!line.equals("") && uniqueLines.add(line)) {
                    	out.write(line);
                    	out.newLine();
                    }
                }

                in.close();
            }

            System.out.println("매출 데이터 합치기가 완료되었습니다.");
        } catch (IOException e) {
            System.out.println("입출력 오류가 발생했습니다.");
            return;
        }
    }
}
