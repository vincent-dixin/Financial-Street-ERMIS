/**
 * Resources.java
 * com.goldgov.baseframe.excel.util
 *
 * Function�� TODO 
 *
 *   ver     date      		author
 * ��������������������������������������������������������������������
 *   		 2011-2-15 		David
 *
 * Copyright (c) 2011, Firsthuida All Rights Reserved.
*/
/**
 * Resources.java
 * com.goldgov.baseframe.excel.util
 * Function�� TODO 
 *
 *   ver     date      		author
 * ��������������������������������������������������������������������
 *   		 2011-2-15        David
 *
 *com.fhd.fdc.utils.excel.util All Rights Reserved.
*/


package com.fhd.fdc.utils.excel.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

/**
 * ClassName:Resources
 * Function: TODO ADD FUNCTION
 * Reason:	 TODO ADD REASON
 *
 * @author   David
 * @version  
 * @since    Ver 1.1
 * @Date	 2011-2-15		下午06:39:24
 *
 * @see 	 
 */

public class Resources {
	/** Returns the URL of the resource on the classpath
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static URL getResourceURL(String resource) throws IOException {
	    URL url = null;
	    ClassLoader loader = Resources.class.getClassLoader();
	    if (loader != null) url = loader.getResource(resource);
	    if (url == null) url = ClassLoader.getSystemResource(resource);
	    if (url == null) throw new IOException("Could not find resource " + resource);
	    return url;
	}

	/** Returns the URL of the resource on the classpath
	   * @param loader The classloader used to load the resource
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static URL getResourceURL(ClassLoader loader, String resource) throws IOException {
	    URL url = null;
	    if (loader != null) url = loader.getResource(resource);
	    if (url == null) url = ClassLoader.getSystemResource(resource);
	    if (url == null) throw new IOException("Could not find resource " + resource);
	    return url;
	}

	/** Returns a resource on the classpath as a Stream object
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static InputStream getResourceAsStream(String resource) throws IOException {
	    InputStream in = null;
	    ClassLoader loader = Resources.class.getClassLoader();
	    if (loader != null) in = loader.getResourceAsStream(resource);
	    if (in == null) in = ClassLoader.getSystemResourceAsStream(resource);
	    if (in == null) throw new IOException("Could not find resource " + resource);
	    return in;
	}

	/** Returns a resource on the classpath as a Stream object
	   * @param loader The classloader used to load the resource
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
	    InputStream in = null;
	    if (loader != null) in = loader.getResourceAsStream(resource);
	    if (in == null) in = ClassLoader.getSystemResourceAsStream(resource);
	    if (in == null) throw new IOException("Could not find resource " + resource);
	    return in;
	}

	/** Returns a resource on the classpath as a Properties object
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static Properties getResourceAsProperties(String resource)
	      throws IOException {
	    Properties props = new Properties();
	    InputStream in = null;
	    String propfile = resource;
	    in = getResourceAsStream(propfile);
	    props.load(in);
	    in.close();
	    return props;
	}

	/** Returns a resource on the classpath as a Properties object
	   * @param loader The classloader used to load the resource
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static Properties getResourceAsProperties(ClassLoader loader, String resource)
	      throws IOException {
	    Properties props = new Properties();
	    InputStream in = null;
	    String propfile = resource;
	    in = getResourceAsStream(loader, propfile);
	    props.load(in);
	    in.close();
	    return props;
	}

	/** Returns a resource on the classpath as a Reader object
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static Reader getResourceAsReader(String resource) throws IOException {
	    return new InputStreamReader(getResourceAsStream(resource));
	}

	/** Returns a resource on the classpath as a Reader object
	   * @param loader The classloader used to load the resource
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static Reader getResourceAsReader(ClassLoader loader, String resource) throws IOException {
	    return new InputStreamReader(getResourceAsStream(loader, resource));
	}

	/** Returns a resource on the classpath as a File object
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static File getResourceAsFile(String resource) throws IOException {
	    return new File(getResourceURL(resource).getFile());
	}

	/** Returns a resource on the classpath as a File object
	   * @param loader The classloader used to load the resource
	   * @param resource The resource to find
	   * @throws IOException If the resource cannot be found or read
	   * @return The resource
	   */
	public static File getResourceAsFile(ClassLoader loader, String resource) throws IOException {
	    return new File(getResourceURL(loader, resource).getFile());
	}
}

