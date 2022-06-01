
package FinalProject;

import simView.*;


import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import util.*;
import statistics.*;

public class InterTakeOffGenr extends ViewableAtomic{


  protected double int_gen_time;
  protected int count;
  protected rand r;

  public InterTakeOffGenr() {this("TakeOffGenr", 9);}

public InterTakeOffGenr(String name,double period){
   super(name);
   addInport("in");
   addOutport("InterTakeOffOut");

   int_gen_time = period ;
}

public void initialize(){
   holdIn("active", int_gen_time);
   r = new rand(123987979);
   count = 0;
}


public void  deltext(double e,message x)
{
Continue(e);
   for (int i=0; i< x.getLength();i++){
     if (messageOnPort(x, "in", i)) { //the stop message from tranducer
       passivate();
     }
   }
}


public void  deltint( )
{

if(phaseIs("active")){
   count = count +1;
//   holdIn("active",int_gen_time);
   holdIn("active",6+r.uniform(int_gen_time));
}
else passivate();
}

public message  out( )
{
//System.out.println(name+" out count "+count);
   message  m = new message();
   content con = makeContent("InterTakeOffOut", new entity("Inter_TakeOff_" + count));
  // content con = makeContent("out", new RequestEntity("Landing Request" + count, 5+r.uniform(20), 50+r.uniform(100), 1));
   m.add(con);

  return m;
}


}

