package sandbox.xmlworker;
 
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
/**
 * @author iText
 */
public class CreateReportHtml2Pdf {

	public static String REPORT_ID = "RLDFIL00007_1";		// 리포트ID
	
	public static String ROOT_PATH;							// 템플릿 루트 패스
	public static String DEST;									// PDf 파일 생성 경로
	public static String HTML; 								// 템플릿 html 경로
    public static final String FONT_malgun = "resources/fonts/malgun.ttf";		//  맑은고딕
    public static final String FONT_batang = "resources/fonts/batang.ttc";		//  바탕
 
    public static void main(String[] args) throws IOException, DocumentException {
    	
    	// 파라미터로 리포트 ID가 있을 시 해당 값으로 변경
    	if(args.length == 1){
    		REPORT_ID = args[0].toString();	
    	}
    	
    	ROOT_PATH = "resources/template/" + REPORT_ID;
		DEST = ROOT_PATH + "/" + REPORT_ID + ".pdf";
		HTML =  ROOT_PATH + "/" + REPORT_ID +".html"; 
    	
    	System.out.println(" REPORT_ID		:	" 	+ REPORT_ID);
    	System.out.println(" ROOT_PATH		:	" 	+ ROOT_PATH);
    	System.out.println(" DEST				:	" 	+ DEST);
    	System.out.println(" HTML				:	" 	+ HTML);
    	
    	
    	
    	
    	
    	
    	// 레포트 변수 파라미터 값 설정(실제 구현 시에는 파라미터로 전달받아야 함)
    	Map<String, String> param = new HashMap<String, String>();
    	
    	param.put("send", "페이퍼코리아(주) 대표이사");	// 수신
    	param.put("ba", "2015");								// 제목
    	
    	param.put("stkcertAnulfee", "1,910,000");			// 주권
    	param.put("benfcertAnulfee", "0");					// 수익증권
    	param.put("subsrghtAnulfee", "300,000");			// 신주인수권증권
    	param.put("elwAnulfee", "0");							// 주식워런트증권
    	param.put("anulfeeTotal", "2,210,000");				// 합계
    	param.put("payDeadlineDd", "2016년 02월 01일까지 납부");			// 납부기한
    	param.put("anulfeePayMethdDsc", "신한은행 140-005-759925");		// 납부방법
    	
    	param.put("avgMktCap", "119,961,925,453");							// 12월말 일평균시가총액(원)
    	param.put("paySunlfee", "2,210,000");									// 납부금액(원)
    	param.put("remk", "-");		// 비고 
    	
    	param.put("anulfeeEnf", "2016. 01. 18");		// 시행일자
    	
    	
    	
    	
    	
    	
    	
    	// 이미지 파일경로 Replace (itext의 경우 full path를 모두 입력해야 정상적으로 이미지 표출됨)
    	String newHtml = roadLocalFile(HTML);
    	newHtml = newHtml.replaceAll("#ROOT_PATH#", ROOT_PATH);   	
    	
    	    	
    	
    	// 변수 변경
    	for(String key : param.keySet()){
    		newHtml = newHtml.replaceAll( "#" + key + "#", param.get(key) );
    		//System.out.println("[param] key : " + key + "  /  value : " + param.get(key));
    	}
    	
    	
    	// PDF파일 생성
    	File file = new File(DEST);
        file.getParentFile().mkdirs();
        
        
        
        
        // PDF 변환
        new CreateReportHtml2Pdf().createPdf(DEST, newHtml);
        
        
    }
 
    /**
     * html 문자열을 파라미터로 받아 PDF 변환
     * @param file
     * @param html
     * @throws IOException
     * @throws DocumentException
     */
    public void createPdf(String file, String html) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4, 60, 60, 20, 20);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        // step 3
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider("resources/fonts");
        
        // 파라미터로 받은 html String을 파일로 생성
        File tmpHtmlFile = new File(ROOT_PATH + "/tmp.html");
        FileOutputStream fos = new FileOutputStream(tmpHtmlFile);
        fos.write(html.getBytes());
        fos.close();
		        
        // PDF 변환
        XMLWorkerHelper.getInstance().parseXHtml(
        		writer, 
        		document, 
        		new FileInputStream(tmpHtmlFile),
        		null, 
        		Charset.forName("UTF-8"), 
        		fontImp
        );
        
        // step 5
        document.close();
        
        
        // step 6 자원해제
        tmpHtmlFile.delete();
        tmpHtmlFile = null;
    }
    
    
    /**
     * Local Path 파일을 읽음
     */
    private static String roadLocalFile(String filepath) {
        String readFile= "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"UTF-8"));
            String s;
            while ((s = in.readLine()) != null) {
                readFile+= s;
            }
            in.close();
        } catch (IOException e) {
        	System.err.println(e); // 에러가 있다면 메시지 출력
        }
     
        return readFile;
    }



    
}