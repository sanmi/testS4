package org.fsm.throughput_s4.adapter;

import io.s4.collector.EventWrapper;
import io.s4.listener.EventHandler;
import io.s4.listener.EventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fsm.throughput_s4.model.SimpleMessage;



public class Sender implements EventListener, Runnable
{
   public static final String CHECK = "CHECK";
   public static final int NUM_ITERATIONS = 10000;
   public static final int NUM_THREADS = 1;
   public static long MESSAGES_PER_SECOND = 3000;

   private static final int PAUSE_EVERY = 100;
   private static final int PAUSE_INTERVAL = 4;

   private static final int NUM_IDS = 20;
   private Set<EventHandler> handlers = new HashSet<EventHandler>();
   private long count = 0;
   private ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

   public void init() {
      for(int i = 0; i < NUM_THREADS; i++)
      {
         executor.submit(this);
      }
   }
   
   @Override
   public void run()
   {
      long messagesThisSecond = 0;
      long lastSecond = System.currentTimeMillis();
      
      try
      {
         //DEBUG, wait for initialization to complete.  bogus.
         Thread.sleep(20);

         //send END message
         for (EventHandler handler : handlers) {
            sendMessage(handler, new SimpleMessage(CHECK, "some data"));
         }
         
         long start = System.currentTimeMillis();
         for (int i=0; i<NUM_ITERATIONS; i++) {
            //Send the mock data
            for (EventHandler handler : handlers) {
               
               sendMessage(handler, new SimpleMessage(new Long(count % NUM_IDS).toString(), "some data"));
               
               count++;
               
               if (++messagesThisSecond >= MESSAGES_PER_SECOND) {
                  System.out.println("Sent " + MESSAGES_PER_SECOND + " messages");
                  long sleepTime = lastSecond + 1000 - System.currentTimeMillis();
                  if (sleepTime > 0) {
                     Thread.sleep(sleepTime);
                  }
                  messagesThisSecond = 0;
                  lastSecond = System.currentTimeMillis();
               } else if ((messagesThisSecond % PAUSE_EVERY) == 0) {
                  //pause every x messages
                  Thread.sleep(PAUSE_INTERVAL);
               }
            }
         }

         //send END message
         for (EventHandler handler : handlers) {
            sendMessage(handler, new SimpleMessage(CHECK, "some data"));
         }

         long duration = System.currentTimeMillis() - start;
         System.out.println("sender throughput|" + new Float(NUM_ITERATIONS) / (new Float(duration) / 1000) + "|messages per sec");
      }
      catch(InterruptedException e)
      {
         e.printStackTrace();
      }
   }

   public void sendMessage(EventHandler handler, SimpleMessage message)
   {
      EventWrapper eventWrapper = new EventWrapper("RawMessage", message, null);
      handler.processEvent(eventWrapper);
   }

   @Override
   public void addHandler(EventHandler handler)
   {
      handlers.add(handler);
   }

   @Override
   public boolean removeHandler(EventHandler handler)
   {
      return handlers.remove(handler);
   }

   @Override
   public int getId()
   {
      return 1;
   }

   @Override
   public String getAppName()
   {
      return "test sender";
   }


}
