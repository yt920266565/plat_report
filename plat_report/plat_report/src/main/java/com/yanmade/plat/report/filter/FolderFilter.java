package com.yanmade.plat.report.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author 0103379
 */
public class FolderFilter implements FileFilter {

    /**
     * 只接受文件夹
     * @param pathname
     * @return
     */
    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }
}
