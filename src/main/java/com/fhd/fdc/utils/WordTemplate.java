package com.fhd.fdc.utils;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * 
 * ClassName:WordTemplate
 * Function: 用iText生成word 工具类
 * Reason:	 因为iText可以生成rtf文件，
 * 			把后缀名改为doc 就可以是一个word文档
 *
 * @author   胡迪新
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-8-1		下午03:35:30
 *
 * @see
 */
public class WordTemplate {

	
	private Document document;
	
	private BaseFont bf;
	
	public WordTemplate(String path) throws Exception {
		this.bf = BaseFont.createFont("STSongStd-Light",
 				"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		createDocument(new FileOutputStream(path));
	}
	
	public WordTemplate(String path, BaseFont bf) throws Exception {
		this.bf = bf;
		createDocument(new FileOutputStream(path));
	}
	
	public WordTemplate(OutputStream outputStream) throws Exception {
		this.bf = BaseFont.createFont("STSongStd-Light",
 				"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		createDocument(outputStream);
	}
	
	public WordTemplate(OutputStream outputStream, BaseFont bf) {
		this.bf = bf;
		createDocument(outputStream);
	}
	
	public WordTemplate(){
		this.document = new Document(PageSize.A4, 80, 80, 65, 65);
	}
	
	private void createDocument(OutputStream outputStream) {
		this.document = new Document(PageSize.A4, 80, 80, 65, 65); 
		RtfWriter2.getInstance(document,outputStream);
		this.document.open();
	}
	
	public void openDocument(String filePath) throws Exception {
		RtfWriter2.getInstance(this.document, new FileOutputStream(filePath));
		this.document.open();
		this.bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	}
	
	public void closeDocument() throws DocumentException{
		this.document.close();
	}
	
	public Table createTable(int cellNum, int rowNum,String[][] cols) throws Exception{
		Font fontChinese = new Font(bf, 10, Font.BOLD);
		Table aTable = new Table(cellNum,rowNum);
		
		int[] width = new int[cols.length];
		for (int i = 0; i < cols.length; i++) {
			width[i] = Integer.valueOf(cols[i][1]);
			aTable.addCell(createCell(cols[i][0], com.lowagie.text.Element.ALIGN_CENTER, com.lowagie.text.Element.ALIGN_CENTER, new Color(0, 0, 0), new Color(204, 153, 255), fontChinese));
		}
		
		aTable.setWidths(width);// 设置每列所占比例
		aTable.setWidth(100); // 占页面宽度 90%
		aTable.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);// 居中显示
		aTable.setAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);// 纵向居中显示
		aTable.setAutoFillEmptyCells(true); // 自动填满
		aTable.setBorderWidth(0); // 边框宽度
		aTable.setBorderColor(new Color(0, 125, 255)); // 边框颜色
		return aTable;
	}
	
	public Cell createCell(String cellName, int verticalAlignment, int horizontalAlignment, Color borderColor, Color backgroundColor,Font fontChinese) throws Exception {
		Cell cell = new Cell(new Phrase(cellName, fontChinese));
		cell.setVerticalAlignment(verticalAlignment);
		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setBorderColor(borderColor);
		cell.setBackgroundColor(backgroundColor);
		return cell;
	}
	
	/**
	 * @param titleStr 标题
	 * @param fontsize 字体大小
	 * @param fontStyle 字体样式
	 * @param elementAlign 对齐方式
	 * @throws DocumentException
	 */
	public void insertTitle(String titleStr,int fontsize,int fontStyle,int elementAlign) throws Exception{
		Font titleFont = new Font(this.bf, fontsize, fontStyle);
		Paragraph title = new Paragraph(titleStr);
		// 设置标题格式对齐方式
		//设置行距
		title.setLeading(30f);
		title.setAlignment(elementAlign);
		title.setFont(titleFont);
		
		this.document.add(title);
	}
	
	/**
	 * @param imgInput 图片流
	 * @param imageAlign 显示位置
	 * @param height 显示高度
	 * @param weight 显示宽度
	 * @param percent 显示比例
	 * @param heightPercent 显示高度比例
	 * @param weightPercent 显示宽度比例
	 * @param rotation 显示图片旋转角度
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void insertImg(byte[] imgInput,int imageAlign,int height,int width,int percent,int heightPercent,int widthPercent,int rotation) throws Exception{
		
		//添加图片
		Image img = Image.getInstance(imgInput);
		if(img==null)
			return;
		img.setAbsolutePosition(0, 0);
		img.setAlignment(imageAlign);
		img.scaleAbsolute(height, width);
		img.scalePercent(percent);
		img.scalePercent(heightPercent, widthPercent);
		img.setRotation(rotation);

		this.document.add(img);
	}
	
	/**
	 * 
	 * <pre>
	 * inserRichText:向文档中插入富文本
	 * 此方法中替换了一些特殊字符
	 * </pre>
	 * 
	 * @author 胡迪新
	 * @param richtext1
	 * @throws DocumentException
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	 */
	public void inserRichText(String richtext1) throws DocumentException,
			Exception {
		StringBuilder builder = new StringBuilder("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body>");
		richtext1 = StringUtils.replace(richtext1, "&ldquo;", "“");
		richtext1 = StringUtils.replace(richtext1, "&rdquo;", "”");
		richtext1 = StringUtils.replace(richtext1, "&iexcl;", "·");
		builder.append(StringUtils.replace(richtext1, "&nbsp;", ""));
		builder.append("</body></html>");
		replaceRichText(StringUtils.replace(builder.toString(), "<o:p></o:p>", ""));
	}


	public void replaceRichText(String richText)
			throws DocumentException, Exception {
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		org.dom4j.Document document = reader.read(IOUtils.toInputStream(richText,"UTF-8"));
		@SuppressWarnings("unchecked")
		List<Element> selectNodes = document.selectNodes("//p");
		for (Element element : selectNodes) {
			insertContext(element.getStringValue(), 12, Font.NORMAL, com.lowagie.text.Element.ALIGN_LEFT);
		}
	}
	
	/**
	 * @param contextStr 内容
	 * @param fontsize 字体大小
	 * @param fontStyle 字体样式
	 * @param elementAlign 对齐方式
	 * @throws DocumentException 
	 * @throws com.lowagie.text.DocumentException 
	 * @throws IOException 
	 */
	public void insertContext(String contextStr,int fontsize,int fontStyle,int elementAlign) throws Exception{
		// 正文字体风格
		Font contextFont = new Font(this.bf, fontsize, fontStyle);
		Paragraph context = new Paragraph(contextStr);
		//设置行距
		context.setLeading(30f);
		// 正文格式左对齐
		context.setAlignment(elementAlign);
		context.setFont(contextFont);
		// 离上一段落（标题）空的行数
		context.setSpacingBefore(5);
		// 设置第一行空的列数
		context.setFirstLineIndent(20);
		document.add(context);
	}

	public Document getDocument() {
		return document;
	}

	public BaseFont getBf() {
		return bf;
	}
	

	/**
	 * <pre>
	 * insertTitle:插入title
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param titleStr 内容
	 * @param fontsize 字体大小
	 * @param fontStyle 字体样式
	 * @param leading 行距
	 * @param elementAlign 对齐方式
	 * @throws Exception
	*/
	public void insertTitle(String titleStr,int fontsize,int fontStyle,float leading,int elementAlign) throws Exception{
		Font titleFont = new Font(this.bf, fontsize, fontStyle);
		Paragraph title = new Paragraph(titleStr);
		// 设置标题格式对齐方式
		//设置行距
		title.setLeading(leading);
		title.setAlignment(elementAlign);
		title.setFont(titleFont);
		
		this.document.add(title);
	}
	/**
	 * <pre>
	 * insertContext:插入Context
	 * </pre>
	 * 
	 * @author 张 雷
	 * @param contextStr 内容
	 * @param fontsize 字体大小
	 * @param fontStyle 字体样式
	 * @param leading 行距
	 * @param spacingBefore 段间距
	 * @param firstLineIndent 第一行空的列
	 * @param elementAlign
	 * @throws Exception
	 * @since  fhd　Ver 1.1
	*/
	public void insertContext(String contextStr,int fontsize,int fontStyle,float leading,int spacingBefore,int firstLineIndent,int elementAlign) throws Exception{
		// 正文字体风格
		Font contextFont = new Font(this.bf, fontsize, fontStyle);
		Paragraph context = new Paragraph(contextStr);
		//设置行距
		context.setLeading(leading);
		// 正文格式左对齐
		context.setAlignment(elementAlign);
		context.setFont(contextFont);
		// 离上一段落（标题）空的行数
		context.setSpacingBefore(spacingBefore);
		// 设置第一行空的列数
		context.setFirstLineIndent(firstLineIndent);
		document.add(context);
	}
}
