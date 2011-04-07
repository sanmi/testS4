package org.fsm.throughput_s4.model;

public class SimpleMessage
{
   String id;
   String value;

   public SimpleMessage(String id, String value)
   {
      super();
      this.id = id;
      this.value = value;
   }
   public SimpleMessage()
   {
      super();
   }
   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }
   /**
    * @param id the id to set
    */
   public void setId(String id)
   {
      this.id = id;
   }
   /**
    * @return the value
    */
   public String getValue()
   {
      return value;
   }
   /**
    * @param value the value to set
    */
   public void setValue(String value)
   {
      this.value = value;
   }
   

}
