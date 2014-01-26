package com.m91snik.smartfilesync.core.test;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by m91snik on 26.01.14.
 */
public class WatchServiceTest {


    @Test
    public void testWatchService() throws IOException, InterruptedException {

        //create the watchService
        final WatchService watchService = FileSystems.getDefault().newWatchService();

        //register the directory with the watchService
        //for create, modify and delete events
        final Path path = Paths.get("/home/m91snik/tmp");
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        //start an infinite loop
        while (true) {

            //remove the next watch key
            final WatchKey key = watchService.take();

            //get list of events for the watch key
            for (WatchEvent<?> watchEvent : key.pollEvents()) {

                //get the filename for the event
                final WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
                final Path filename = ev.context();

                //get the kind of event (create, modify, delete)
                final WatchEvent.Kind<?> kind = watchEvent.kind();

                //print it out
                System.out.println(kind + ": " + filename);
            }

            //reset the key
            boolean valid = key.reset();

            //exit loop if the key is not valid
            //e.g. if the directory was deleted
            if (!valid) {
                break;
            }
        }

    }
}
