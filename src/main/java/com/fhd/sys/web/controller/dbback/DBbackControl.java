package com.fhd.sys.web.controller.dbback;

import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 
 * ClassName:DBbackControl
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   万业
 * @version  
 * @since    Ver 1.1
 * @Date	 2011	2011-4-6		上午11:03:51
 *
 */
@Controller
public class DBbackControl {
	
	/**
	 * 进入数据库备份页面
	 * @author 万业
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sys/dbbp/dbbackupList.do")
	public String DBbackupList(Model model, HttpServletRequest request)throws Exception
	{
		 return "/sys/dbbp/dbbackupList";
	}
	/**
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/sys/dbbp/dodbbackup.do")
	public void doDBback(Model model, HttpServletResponse response, HttpServletRequest request)throws Exception{
		/**
		 * dbtype:
		 * b_db2
		 * b_mysql
		 * b_oracle
		 * b_sqlserver
		 */
		
		PrintWriter out = null;
		String flag = "false";
		out = response.getWriter();
		
		String dbtype = request.getParameter("dbType");
		String dbVersion = request.getParameter("dbVersion");
		String dbname = request.getParameter("dbname");
		String dbusername = request.getParameter("dbusername");
		String dbuserpwd = request.getParameter("dbuserpwd");
		String dbbackfilename = request.getParameter("dbbackfilename");
		String dbaddress = request.getParameter("dbaddress");
		
		Process process=null;
		int rv = -1;
		try{
			
			/*String sql="Backup DataBase ermis4 to disk = 'E:\\dpt_workspace2\\firsthd-development-center\\web\\dbbp\\testback.bak'";
			SQLQuery query = o_sysUserDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			query.executeUpdate();*/
			
			
			// 配置文件读取文件路径：
			String backPath = ResourceBundle.getBundle("application").getString("DBbackPath");
			String scriptPath = ResourceBundle.getBundle("application").getString("DBbackScriptPath");
			String execStr="cmd /c start "+scriptPath + dbtype+ dbVersion+ ".bat"+" "+dbname+" "+dbusername+" "+dbuserpwd+" "+backPath+" "+dbbackfilename+" "+dbaddress;
			
			//String execstr = "OSQL -U "+dbusername+" -P "+dbuserpwd+" -S "+dbaddress+" -d ermis4 -Q "Backup DataBase ermis4 to disk = 'E:\dpt_workspace2\firsthd-development-center\web\dbbp\testback.bak'"
			
			process = Runtime.getRuntime().exec(execStr);
			rv=process.waitFor();
			if(rv==0){
				flag="true";
				out.write(flag);
			}
		}catch (Exception e) {
			e.printStackTrace();
			out.write(flag);
		} finally {
			out.close();
			if (process != null) {
				process.destroy();
				process = null;
            }

		}
		
		
		
	}
}
