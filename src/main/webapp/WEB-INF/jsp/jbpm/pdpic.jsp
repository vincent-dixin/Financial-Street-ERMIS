<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="org.jbpm.api.*,java.io.*,java.util.zip.*,java.util.*"  %>
<% 




	String jarFileName = request.getParameter("jarFileName");
	String pngFileName = request.getParameter("pngFileName");
	//String pathx = 	this.getClass().getResource("/"+jarFileName).getFile();
	InputStream  inputStream = null;
	
	File file = new File(jarFileName);
	ZipFile zf = new ZipFile(file);   
    Enumeration entries = zf.entries();   
    BufferedReader input = new BufferedReader(new InputStreamReader(   
            System.in));   
    while (entries.hasMoreElements()) {   
        ZipEntry ze = (ZipEntry) entries.nextElement();   
        
        if (ze.getName().equals(pngFileName)) {   
            long size = ze.getSize();   
            if (size > 0) {   
                inputStream  = zf.getInputStream(ze);   
                 
            }   
        } 
    }
	
	response.reset();
	
	OutputStream os = response.getOutputStream();
	IOUtils.write(IOUtils.toByteArray(inputStream),os);
	
	os.close();
	os = null;
%>