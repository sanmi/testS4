package org.fsm.throughput_s4.singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;


public class BeanStatistics
{
   private static Logger logger = Logger.getLogger(BeanStatistics.class);
   private List<MessageCounter> peCounters = new ArrayList<MessageCounter>();
   private int reportIntervalInSeconds;

   public void initialize()
   {
      System.out.println("Initializing bean statistics bean");
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.execute(new SnapshotThread());
   }

   class SnapshotThread implements Runnable
   {
      @Override
      public void run()
      {
         while(true)
         {
            try
            {
               Thread.sleep(reportIntervalInSeconds * 1000);
               log("PE statistics------");
               for(MessageCounter peCounter: peCounters)
               {
                  peCounter.output(logger);
               }
            }
            catch(InterruptedException e)
            {
               e.printStackTrace();
            }
         }
      }

      private void log(String msg)
      {
         logger.info(msg);
         System.out.println(msg);
      }
   }

   public static void setLogger(Logger logger)
   {
      BeanStatistics.logger = logger;
   }

   public void setPeCounters(List<MessageCounter> peCounters)
   {
      this.peCounters = peCounters;
   }

   public void setReportIntervalInSeconds(int sleepInterval)
   {
      this.reportIntervalInSeconds = sleepInterval;
   }
}
