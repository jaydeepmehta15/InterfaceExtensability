package com.Name;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class DynamicClassload {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, MalformedURLException, InterruptedException {
		
		//URL url = new URL("file:C:\\Users\\jmehta\\Downloads\\MyCustomLib\\Mylib.jar");
		
		//URL url = new URL("file:\\C:\\Users\\jmehta\\Downloads\\MyCustomLib\\*");
		
		//URL[] urls = {url};
		//URLClassLoader loader = new URLClassLoader(urls);
		
		//File f = new File("C:\\Users\\jmehta\\Downloads\\MyCustomLib\\");
		
		//URL[] urls = { new URL("jar:file:" + f.getPath()) };
		
		//URLClassLoader loader = new URLClassLoader(urls);
		//System.out.println(loader.getURLs());
		
		//Class class1 = Class.forName("com.Name.MyClass", true, loader);
		
		
		//Below snippet Does not dynamically scans base directory for custom impl additions
		/*
		while(true ) {
		
		final String s = System.getProperty("java.class.path");
        final File[] path = (s == null) ? new File[0] : getClassPath(s);
        Thread.sleep(3000);
		
		try {
		Class class1 = Class.forName("com.Name.MyClass");
		
		MyContract obj = (MyContract)class1.newInstance();
		
		obj.doSomething();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		}
		*/

		//Users reflection Unsafe Approach to dynamically do runtime scan
		//Nosuchmethod exception on addUrl again this is illegal as this is already protected method IllegalAccessException
		/*
		while(true ) {
			
			final String s = System.getProperty("java.class.path");
	        final File[] path = (s == null) ? new File[0] : getClassPath(s);
	        Thread.sleep(3000);
			
			try {
				URLClassLoader currentloader = (URLClassLoader) DynamicClassload.class.getClassLoader();
				
				Method method = currentloader.getClass().getDeclaredMethod("addURL", new Class[] { URL.class });
			    method.setAccessible(true);
			    
			    File files[] = new File("C:\\Users\\jmehta\\Downloads\\MyCustomLib").listFiles();
			    ArrayList <URL> urls = null;
			    for (File file : files) {
			    	urls.add(file.toURI().toURL());
			    	
					
				}
			    
			    method.invoke(currentloader, new Object[] { urls.toArray() });
			Class class1 = Class.forName("com.Name.MyClass");
			
			MyContract obj = (MyContract)class1.newInstance();
			
			obj.doSomething();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			}
			*/
		
		//custom classloader and dynamic folder scan (avoid restarts) to load custom implementations.
		
		while(true ) {
			
			final String s = System.getProperty("java.class.path");
	        final File[] path = (s == null) ? new File[0] : getClassPath(s);
	        Thread.sleep(3000);
			
			try {
				
			File files[] = new File("C:\\Users\\jmehta\\Downloads\\MyCustomLib").listFiles();
			System.out.println(files.toString());
			ArrayList <URL> urls = new ArrayList<URL>();
			for (File file : files) {
				urls.add(file.toURI().toURL());		
			}
			
			System.out.println(urls.toArray(new URL[0]));
			
			URLClassLoader myloader = new URLClassLoader(urls.toArray(new URL[0]));
			Class class1 = Class.forName("com.Name.MyClass",true, myloader );
			
			
			
			MyContract obj = (MyContract)class1.newInstance();
			
			obj.doSomething();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			}
		
		
	}
	
	private static File[] getClassPath(String cp) {
        File[] path;
        if (cp != null) {
            int count = 0, maxCount = 1;
            int pos = 0, lastPos = 0;
            // Count the number of separators first
            while ((pos = cp.indexOf(File.pathSeparator, lastPos)) != -1) {
                maxCount++;
                lastPos = pos + 1;
            }
            path = new File[maxCount];
            lastPos = pos = 0;
            // Now scan for each path component
            while ((pos = cp.indexOf(File.pathSeparator, lastPos)) != -1) {
                if (pos - lastPos > 0) {
                    path[count++] = new File(cp.substring(lastPos, pos));
                } else {
                    // empty path component translates to "."
                    path[count++] = new File(".");
                }
                lastPos = pos + 1;
            }
            // Make sure we include the last path component
            if (lastPos < cp.length()) {
                path[count++] = new File(cp.substring(lastPos));
            } else {
                path[count++] = new File(".");
            }
            // Trim array to correct size
            if (count != maxCount) {
                File[] tmp = new File[count];
                System.arraycopy(path, 0, tmp, 0, count);
                path = tmp;
            }
        } else {
            path = new File[0];
        }
        // DEBUG
        //for (int i = 0; i < path.length; i++) {
        //  System.out.println("path[" + i + "] = " + '"' + path[i] + '"');
        //}
        return path;
    }

}
