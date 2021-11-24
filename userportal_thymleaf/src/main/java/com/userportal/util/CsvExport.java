package com.userportal.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.userportal.domain.User;

public class CsvExport extends ExtarctHelper{

	public void export(List<User> users, HttpServletResponse response) {
		super.setResponseHeader(response, "text/csv", ".csv", "users_");
		
		try {
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			
			String[] csvHeader = {"User ID", "E-mail", "User Name","First Name", "Last Name", "Role", "Enabled","Join Date"};
			String[] fieldMapping = {"Id", "email", "username", "firstName", "lastName", "role", "active","joinDate"};
			
			csvWriter.writeHeader(csvHeader);
			
			users.forEach(user ->{
				try {
					csvWriter.write(user, fieldMapping);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			csvWriter.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
