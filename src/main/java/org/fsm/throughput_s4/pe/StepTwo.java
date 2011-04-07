package org.fsm.throughput_s4.pe;

import io.s4.dispatcher.EventDispatcher;
import io.s4.processor.AbstractPE;

import org.fsm.throughput_s4.adapter.Sender;
import org.fsm.throughput_s4.model.SimpleMessage;
import org.fsm.throughput_s4.singleton.MessageCounter;

public class StepTwo extends AbstractPE
{
   private EventDispatcher dispatcher;
   private int peCounter = 0;
   private String id = null;
   private MessageCounter counter;

   public void setDispatcher(EventDispatcher dispatcher)
   {
      this.dispatcher = dispatcher;
   }

   public String getId()
   {
      return this.getClass().getName();
   }

   public void processEvent(SimpleMessage event) {
      peCounter++;
      if (id == null) {
         id = event.getId();
      }

      if (event.getId().equals(Sender.CHECK)) {
         System.out.println("StepTwo received " + event.getId() + ", " + event.getValue());
      } else {
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
