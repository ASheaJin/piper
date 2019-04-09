package com.syswin.pipeline.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.tika.parser.txt.CharsetDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 601387 on 2019/03/28.
 */
public class ZipUtil {
	private final static Logger logger = LoggerFactory.getLogger(ZipUtil.class);
	public static void main(String[] args) {
		try {
			//zip("D:\\资料", "D:\\资料.zip", "123");
			unZip("D:\\bb9.zip", "D:\\bb9", "AD3E35CE-514C-4FCC-A64F-F491BDAE6424");
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩
	 *
	 * @param srcFile 源目录
	 * @param dest    要压缩的目录
	 * @param passwd  密码 不是必填
	 * @throws ZipException 异常
	 */
	public static void zip(String srcFile, String dest, String passwd) throws ZipException {
		File srcfile = new File(srcFile);

		//创建目标文件
		String destname = buildDestFileName(srcfile, dest);
		ZipParameters par = new ZipParameters();
		par.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		par.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

		if (passwd != null) {
			par.setEncryptFiles(true);
			par.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
			par.setPassword(passwd.toCharArray());
		}

		ZipFile zipfile = new ZipFile(destname);
		if (srcfile.isDirectory()) {
			zipfile.addFolder(srcfile, par);
		} else {
			zipfile.addFile(srcfile, par);
		}
	}

	/**
	 * 解压
	 *
	 * @param zipfile 压缩包文件
	 * @param dest    目标文件
	 * @param passwd  密码
	 * @throws ZipException 抛出异常
	 */
	public static void unZip(String zipfile, String dest, String passwd) throws ZipException {
		/**
		 * 秘邮上传的zip文件名存在两种编码，
		 * 当使用GBK出现异常时，用UTF-8再尝试一次，如果还不行就抛出异常
		 */
//		try {
//			unZipWithCharset(zipfile, dest, passwd, "UTF-8");
////			System.out.println("GB18030");
//		} catch (ZipException z) {
////			logger.info("unzip with GB18030 fail~ " + zipfile);
//			try {
//				unZipWithCharset(zipfile, dest, passwd, "GB18030");
////				System.out.println("UTF-8");
//			} catch (ZipException z1) {
//				logger.error(String.format("解压文件出错 zipfile=%s dest=%s pwd=%s", zipfile, dest, passwd), z1);
//				throw z1;
//			}
//		}


		ZipFile zipFile = new ZipFile(zipfile);

		if (!zipFile.isValidZipFile()) {
			throw new ZipException("压缩文件不合法，可能已经损坏！");
		}

		File file = new File(dest);
		if (file.isDirectory() && !file.exists()) {
			file.mkdirs();
		}


		UnzipParameters param = new UnzipParameters();
		zipFile.setFileNameCharset("ISO8859-1");
		List list = zipFile.getFileHeaders();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			FileHeader fh = (FileHeader) iterator.next();
			String fname = null;
			try {
				byte[] b = fh.getFileName().getBytes("ISO8859-1");
//				CharsetDetector charDetect = new CharsetDetector();
//				charDetect.setText(b);
//				String charSet = charDetect.detect().getName();
				fname = new String(b, "UTF-8");
				if (fname.getBytes("UTF-8").length != b.length) {
					fname = new String(b,"GB18030");//most possible charset
//					System.out.println("UTF-8");
				} else {

//					System.out.println("GB18030");
				}
			} catch (Throwable e) {
				System.out.println("others");
				fname = fh.getFileName();
			}

			System.out.println(fname);
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(passwd.toCharArray());
			}
			zipFile.extractFile(fh, dest, param, fname);
		}
	}

	public static void unZipWithCharset(String zipfile, String dest, String passwd, String charset) throws ZipException {
		ZipFile zfile = new ZipFile(zipfile);
		zfile.setFileNameCharset(charset);
		if (!zfile.isValidZipFile()) {
			throw new ZipException("压缩文件不合法，可能已经损坏！");
		}

		File file = new File(dest);
		if (file.isDirectory() && !file.exists()) {
			file.mkdirs();
		}

		if (zfile.isEncrypted()) {
			zfile.setPassword(passwd.toCharArray());
		}
		zfile.extractAll(dest);
	}

	public static String buildDestFileName(File srcfile, String dest) {
		if (dest == null) {
			if (srcfile.isDirectory()) {
				dest = srcfile.getParent() + File.separator + srcfile.getName() + ".zip";
			} else {
				String filename = srcfile.getName().substring(0, srcfile.getName().lastIndexOf("."));
				dest = srcfile.getParent() + File.separator + filename + ".zip";
			}
		} else {
			createPath(dest);//路径的创建
			if (dest.endsWith(File.separator)) {
				String filename = "";
				if (srcfile.isDirectory()) {
					filename = srcfile.getName();
				} else {
					filename = srcfile.getName().substring(0, srcfile.getName().lastIndexOf("."));
				}
				dest += filename + ".zip";
			}
		}
		return dest;
	}

	private static void createPath(String dest) {
		File destDir = null;
		if (dest.endsWith(File.separator)) {
			destDir = new File(dest);//给出的是路径时
		} else {
			destDir = new File(dest.substring(0, dest.lastIndexOf(File.separator)));
		}

		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}
}
