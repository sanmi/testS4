package org.fsm.throughput_s4.pe;

import io.s4.dispatcher.EventDispatcher;
import io.s4.processor.AbstractPE;

import org.fsm.throughput_s4.adapter.Sender;
import org.fsm.throughput_s4.model.SimpleMessage;
import org.fsm.throughput_s4.singleton.MessageCounter;

public class Fanout extends AbstractPE
{
   private EventDispatcher dispatcher;
   private MessageCounter counter;
   private EventDispatcher broadcastDispatcher;

   public void setBroadcastDispatcher(EventDispatcher broadcastDispatcher)
   {
      this.broadcastDispatcher = broadcastDispatcher;
   }

   public void setDispatcher(EventDispatcher dispatcher)
   {
      this.dispatcher = dispatcher;
   }

   public String getId()
   {
      return this.getClass().getName();
   }

   public void processEvent(SimpleMessage event) {

      if (event.getId().equals(Sender.CHECK)) {
         System.out.println("FANOUT received " + event.getId() + ", " + event.getValue());
         broadcastDispatcher.dispatchEvent("stepOneMessage", event);
         broadcastDispatcher.dispatchEvent("stepTwoMessage", event);
      } else {
         dispatcher.dispatchEvent("stepOneMessage", event);
         counter.incrementInCount();
      }
   }
   
   public void output()
   {
   }

   /**
    * @param counter the counter to set
    */
   public void setCounter(MessageCounter counter)
   {
      this.counter = counter;
   }

   @Override
   public Object clone()
   {
      counter.incrementPECount();
      return super.clone();
   }

}
