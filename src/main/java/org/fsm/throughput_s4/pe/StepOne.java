package org.fsm.throughput_s4.pe;

import io.s4.dispatcher.EventDispatcher;
import io.s4.processor.AbstractPE;

import org.fsm.throughput_s4.adapter.Sender;
import org.fsm.throughput_s4.model.SimpleMessage;
import org.fsm.throughput_s4.singleton.MessageCounter;

public class StepOne extends AbstractPE
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
         System.out.println("StepOne received " + event.getId() + ", " + event.getValue());
      } else {
         counter.incrementInCount();
         //Now create three new events and dispatch them
         forward(event, 1);
         forward(event, 2);
         forward(event, 3);
      }
   }
   
   private void forward(SimpleMessage event, int i)
   {
      dispatcher.dispatchEvent("stepTwoMessage", addToEventId(event, i));
      counter.incrementOutCount();
   }

   private SimpleMessage addToEventId(SimpleMessage event, int i)
   {
      //event.setValue(event.getValue() + "_" + i); 
      event.setId(event.getId() + "_" + i); 
      return event;
   }

   public void output()
   {
      //System.out.println("1) id|" + id + "|count|" + peCounter );
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
