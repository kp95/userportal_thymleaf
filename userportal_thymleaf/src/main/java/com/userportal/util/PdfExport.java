package com.userportal.util;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.userportal.domain.User;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

public class PdfExport extends ExtarctHelper{
	public void export(List<User> users,HttpServletResponse response) throws DocumentException, IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf", "users_");
	
		Document doc = new Document(PageSize.A4);
		PdfWriter.getInstance(doc, response.getOutputStream());
	
		doc.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of User", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		doc.add(paragraph);
		
		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.7f, 3.0f, 3.0f});
		
		writeTableHeader(table);
		writeTableData(table, users);
		
		doc.add(table);
		
		doc.close();
	
	
	
	
	
	}
	
	private void writeTableData(PdfPTable table,List<User> users) {
		users.forEach(u->{
			table.addCell(String.valueOf(u.getId()));
			table.addCell(u.getEmail());
			table.addCell(u.getUsername());
			table.addCell(u.getFirstName());
			table.addCell(u.getLastName());
			table.addCell(u.getRole());
			table.addCell(String.valueOf(u.isActive()));
			if(u.getJoinDate() == null) {table.addCell("ERROR");}
			else {table.addCell(u.getJoinDate().toString());}
			/*
			 * if(u.getLastLoginDate() == null) {table.addCell("Not Login yet");} else
			 * {table.addCell(u.getLastLoginDate().toString());}
			 */
		});
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);		
		
		cell.setPhrase(new Phrase("ID", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("E-mail", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("User Name", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("First Name", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Last Name", font));		
		table.addCell(cell);		
		
		cell.setPhrase(new Phrase("Role ", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled", font));		
		table.addCell(cell);	
		
		cell.setPhrase(new Phrase("Join Date", font));		
		table.addCell(cell);	
		
		/*
		 * cell.setPhrase(new Phrase("Last Login Date", font)); table.addCell(cell);
		 */
		
	}
}
