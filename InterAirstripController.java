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

public class InterAirstripController extends ViewableAtomic{

double RequestServingTime = 6;
entity landing,takeoff,currentJob = null;
protected DEVSQueue q;

public InterAirstripController() {this("AirstripCenter");}

public InterAirstripController(String name){
    super(name);
    addInport("InterLandingIn");
    addInport("InterTakeOffIn");
    addOutport("InterOut");

    addTestInput("InterLandingIn",new entity("testLanding"));
    addTestInput("InterTakeOffIn",new entity("testTakeOff"),5);
}

public void initialize(){
     q = new DEVSQueue();
passivate();
}


public void  deltext(double e,message x)
{
Continue(e);

if(phaseIs("passive")){
   for (int i=0; i< x.getLength();i++){
     if (messageOnPort(x, "InterLandingIn", i)) {
       landing = x.getValOnPort("InterLandingIn", i);
       currentJob = landing;
       holdIn("active", RequestServingTime);
       //holdIn("active", ((entity)currentJob).getProcessingTime());
     }
     if (messageOnPort(x, "InterTakeOffIn", i)) {
       takeoff = x.getValOnPort("InterTakeOffIn", i);
       currentJob = takeoff;
       holdIn("active", RequestServingTime);
       //holdIn("active", ((entity)currentJob).getProcessingTime());
     }
   }
}
else if(phaseIs("active")){
    for (int i=0; i< x.getLength();i++){
      if (messageOnPort(x, "InterLandingIn", i)) {
    	 landing = x.getValOnPort("InterLandingIn", i);
         q.add(landing);
      }
      if (messageOnPort(x, "InterTakeOffIn", i)) {
          takeoff = x.getValOnPort("InterTakeOffIn", i);
          q.add(takeoff);    
      }
    }
 }
}


public void  deltint( )
{
if(phaseIs("active")){
   if(!q.isEmpty()) {
     currentJob = (entity)q.first();
     holdIn("active",RequestServingTime);
      
     //holdIn("active", ((vehicleEntity)currentJob).getProcessingTime());
     
     q.remove();
   }
   else
          passivate();
 }
}

public message  out( )
{
   message  m = new message();
   content con = makeContent("InterOut",
            new entity(currentJob.getName()));
   m.add(con);

  return m;
}

public String getTooltipText(){
	if(currentJob!=null)
	return super.getTooltipText()+"\n number of requests in queue:"+q.size()+
	"\n my current job is:" + currentJob.toString();
	else return "initial value";
}



}

