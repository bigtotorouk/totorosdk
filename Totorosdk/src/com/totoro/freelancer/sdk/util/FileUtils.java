package com.totoro.freelancer.sdk.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

/**
 * Provides operations with files
 * 
 */
public final class FileUtils {

	private static final int BUFFER_SIZE = 8 * 1024; // 8 KB 
	private final static String FILE_EXTENSION_SEPARATOR = ".";
	
	private FileUtils() {
	}

	public static void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		while (true) {
			int count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			os.write(bytes, 0, count);
		}
	}

	public static void closeSilently(Closeable closeable) {
	    try {
	        closeable.close();
	    } catch (Exception e) {
            // Do nothing
        }
    }

    public static boolean copyFile(File source, File target) {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try {
            if (!target.exists()) {
                target.createNewFile();
            }
            fin = new FileInputStream(source);
            fout = new FileOutputStream(target);
            copyStream(fin, fout);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Delete the earliest file in the specified directory
     * @param dir The specified directory
     * @param exceptFile Exclude the file name
     */
    public static final void deleteEarliestFile(File dir, String exceptFile) {
        if (dir != null && dir.isDirectory()) {
            File earlyFile = null;
            File[] files = dir.listFiles();
            if (files.length == 0)
                return;
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if(f.getName().equals(exceptFile))
                    continue;
                if(earlyFile == null) {
                    earlyFile = files[i];
                    continue;
                }
                if (earlyFile.lastModified() > f.lastModified()) {
                    earlyFile = f;
                }
            }
            if(earlyFile != null)
                earlyFile.delete();
        }
    }

    /**
     * Read file
     * 
     * @param filePath 
     * 				The path of the file
     * @return StringBuilder
     * 				Return file content as StringBuffer, if the file doesn't exist return null
     */
    public static StringBuilder readFile(String filePath) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file != null && file.isFile()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!fileContent.toString().equals("")) {
                        fileContent.append("\r\n");
                    }
                    fileContent.append(line);
                }
                reader.close();
                return fileContent;
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException("IOException occurred. ", e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Write file
     * 
     * @param filePath
     * 				The path of the file
     * @param content
     * 				The content of the file
     * @param append 
     * 				If append to the end of the file, true : append��false : overwrite
     * @return
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * Write file
     * 
     * @param filePath 
     * 				The path of the file
     * @param stream 
     * 				Input content stream 
     * @return
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        OutputStream o = null;
        try {
            o = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * Read file, one line as a element of the String List
     * 
     * @param filePath 
     * 				The path of the file
     * @return List<String>
     * 				Return file content as a String List, if the file doesn't exist return null
     */
    public static List<String> readFileToList(String filePath) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file != null && file.isFile()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    fileContent.add(line);
                }
                reader.close();
                return fileContent;
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException("IOException occurred. ", e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get file name from the path (without extension)
     * 
     * @param filePath 
     * 				The path of the file
     * @return String
     * 				File name without extension
     * @see
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        } else {
            if (extenPosi == -1) {
                return filePath.substring(filePosi + 1);
            } else {
                return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
            }
        }
    }

    /**
     * Get file name from the path (with extension)
     * 
     * @param filePath 
     * 				The path of the file
     * @return String
     * 				File name with extension
     * @see
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return filePath;
        }
        return filePath.substring(filePosi + 1);
    }

    /**
     * Get folder name from the path
     * 
     * @param filePath 
     * 				The path of the file
     * @return String
     * 				The folder path
     * @see
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return "";
        }
        return filePath.substring(0, filePosi);
    }

    /**
     * Get the extension name from the path
     * 
     * @param filePath 
     * 				The path of the file
     * @return String
     * 				The extension name of the file
     * @see
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   ""
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        } else {
            if (filePosi >= extenPosi) {
                return "";
            }
            return filePath.substring(extenPosi + 1);
        }
    }

    /**
     * ����ļ�·��ѭ�������ļ����ļ���?br/>
     * <br/>
     * <strong>ע�⣺</strong><br/>
     * makeFolder("/data/chris")���ܴ���Users�ļ���, makeFolder("/data/chris/")���ܴ�����chris�ļ���
     * 
     * @param filePath �ļ�·��
     * @return �Ƿ�ɹ������ļ��У����ļ����Ѵ��ڣ�����true
     *         <ul>
     *         <li>��{@link FileUtils#getFolderName(String)}����Ϊ�գ�����false;</li>
     *         <li>���ļ��д��ڣ�����true</li>
     *         <li>���򷵻�{@link java.io.File#makeFolder}</li>
     *         </ul>
     */
    public static boolean makeFolder(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * �ж��ļ��Ƿ����?
     * 
     * @param filePath �ļ�·��
     * @return ���ڷ���true�����򷵻�false
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * �ж��ļ����Ƿ����?
     * 
     * @param directoryPath �ļ���·��
     * @return ���ڷ���true�����򷵻�false
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * ɾ���ļ����ļ���
     * <ul>
     * <li>·��Ϊnull����ַ�����true</li>
     * <li>·�������ڣ�����true</li>
     * <li>·�����ڲ���Ϊ�ļ����ļ��У�����{@link File#delete()}�����򷵻�false</li>
     * <ul>
     * 
     * @param path ·��
     * @return �Ƿ�ɾ��ɹ�?
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteFile(f.getAbsolutePath());
                    }
                }
                return file.delete();
            }
            return false;
        }
        return true;
    }

    /**
     * �õ��ļ���С
     * <ul>
     * <li>·��Ϊnull����ַ�����?1</li>
     * <li>·�����ڲ���Ϊ�ļ��������ļ���С�����򷵻�-1</li>
     * <ul>
     * 
     * @param path ·��
     * @return
     */
    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
    
	public static byte[] inputStreamToByte(InputStream is) {
		try{
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		if(offset <0){
			return null;
		}
		if(len <=0 ){
			return null;
		}
		if(offset + len > (int) file.length()){
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // ���������ļ���С������
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
}
