package com.ndpmedia.rocketmq.stalker.executor;

import java.util.Date;
import java.util.List;

/**
 * Created by robert.xu on 2015/4/9.
 * monitor thread . when analysis thread is interrupted try to restart it.
 */
public class ExecutorMonitor extends Thread {

    private List<FileMonitor> fileMonitors;

    @Override
    public void run() {
        while (true) {
            System.out.println(new Date().toString() + " executor monitor wake up .");

            try {
                sleep(30 * 1000);
            } catch (InterruptedException e) {
                System.out.println(new Date().toString() + " executor monitor interrupted .");
            }

            if (null == fileMonitors || fileMonitors.isEmpty()) {
                System.out.println(new Date().toString() + " no file monitor is running .");

                continue;
            }

            for (FileMonitor fileMonitor : fileMonitors) {
                if (fileMonitor.isAlive())
                    continue;
                System.out.println(new Date().toString() + " file monitor " + fileMonitor.getPath() + " is stopped. try to restart .");
                FileMonitor file2 = new FileMonitor(fileMonitor.getPath());
                fileMonitors.remove(fileMonitor);
                file2.start();
                fileMonitors.add(file2);
            }
        }
    }

    public List<FileMonitor> getFileMonitors() {
        return fileMonitors;
    }

    public void setFileMonitors(List<FileMonitor> fileMonitors) {
        this.fileMonitors = fileMonitors;
    }
}
