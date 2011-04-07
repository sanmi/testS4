package org.fsm.throughput_s4.singleton;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

public class MessageCounter
{
   private AtomicLong inCount = new AtomicLong(0);
   private AtomicLong outCount = new AtomicLong(0);
   private AtomicLong peCount = new AtomicLong(0);
   
   private String key;
   private Float expectedInCount;

   public void incrementInCount() {
      inCount.incrementAndGet();
   }
   public void incrementOutCount() {
      outCount.incrementAndGet();
   }
   public void incrementPECount() {
      peCount.incrementAndGet();
   }
   public void output(String key, Logger logger) {
      String msg = 
         "|key|" + key + 
         "|inCount|" + inCount + 
         "|outCount|" + outCount + 
         "|peCount|" + peCount +
         "|expectedIn|" + expectedInCount + 
         "|%Loss|" + 100 * ((expectedInCount - inCount.floatValue())/expectedInCount); 
      logger.info(msg);
      System.out.println(msg);
   }
   public void output(Logger logger) {
      output(key, logger);
   }
   public void reset() {
      inCount.set(0);
      outCount.set(0);
   }
   /**
    * @param key the key to set
    */
   public void setKey(String key)
   {
      this.key = key;
   }
   /**
    * @param expectedInCount the expectedInCount to set
    */
   public void setExpectedInCount(Float expected)
   {
      this.expectedInCount = expected;
   }
}
