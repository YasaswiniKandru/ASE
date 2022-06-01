package FinalProject;

/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */

import simView.*;


import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;

public class AirstripSystem extends ViewableAtomic{

double RequestServingTime = 4;
entity domestic,inter,currentJob = null;
protected DEVSQueue f;

public AirstripSystem() {this("AirstripSystem");}

public AirstripSystem(String name){
    super(name);
    addInport("DomesticIn");
    addInport("InterIn");
    addOutport("Requestout");

    addTestInput("DomesticIn",new entity("testDomestic"));
    addTestInput("InterIn",new entity("testInter"),5);
}

public void initialize(){
     f = new DEVSQueue();
passivate();
}


public void  deltext(double e,message x)
{
Continue(e);

if(phaseIs("passive")){
   for (int i=0; i< x.getLength();i++){
     if (messageOnPort(x, "DomesticIn", i)) {
       domestic = x.getValOnPort("DomesticIn", i);
       currentJob = domestic;
       holdIn("active", RequestServingTime);
       //holdIn("active", ((entity)currentJob).getProcessingTime());
     }
     if (messageOnPort(x, "InterIn", i)) {
       inter = x.getValOnPort("InterIn", i);
       currentJob = inter;
       holdIn("active", RequestServingTime);
       //holdIn("active", ((entity)currentJob).getProcessingTime());
     }
   }
}
else if(phaseIs("active")){
    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x, "DomesticIn", i)) {
    	  domestic = x.getValOnPort("DomesticIn", i);
         f.add(domestic);
      }
      if (messageOnPort(x, "InterIn", i)) {
    	  inter = x.getValOnPort("InterIn", i);
          f.add(inter);    
      }
    }
 }
}


public void  deltint( )
{
if(phaseIs("active")){
   if(!f.isEmpty()) {
     currentJob = (entity)f.first();
     holdIn("active",RequestServingTime);     
     f.remove();
   }
   else
          passivate();
 }
}

public message  out( )
{
   message  m = new message();
   content con = makeContent("Requestout",
            new entity(currentJob.getName()));
   m.add(con);

  return m;
}

public String getTooltipText(){
	if(currentJob!=null)
	return super.getTooltipText()+"\n number of requests in queue:"+f.size()+
	"\n my current job is:" + currentJob.toString();
	else return "initial value";
}


}

