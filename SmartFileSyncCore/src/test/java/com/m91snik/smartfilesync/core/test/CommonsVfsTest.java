package com.m91snik.smartfilesync.core.test;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.junit.Test;

/**
 * Created by m91snik on 26.01.14.
 */
public class CommonsVfsTest {

    @Test
    public void testFileMonitor() throws FileSystemException {
        FileSystemManager fsManager = VFS.getManager();
        FileObject fileObject = fsManager.resolveFile("/home/m91snik/tmp");

        DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {
            @Override
            public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception {
                System.out.println("Created file " + fileChangeEvent.getFile().getName());
            }

            @Override
            public void fileDeleted(FileChangeEvent fileChangeEvent) throws Exception {
                System.out.println("Deleted file " + fileChangeEvent.getFile().getName());
            }

            @Override
            public void fileChanged(FileChangeEvent fileChangeEvent) throws Exception {
                System.out.println("Changed file " + fileChangeEvent.getFile().getName());
            }
        });
        fm.setRecursive(true);
        fm.addFile(fileObject);
        fm.start();
        while (true) {

        }
    }
}
