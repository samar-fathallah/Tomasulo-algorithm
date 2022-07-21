package tiramisu;

public class ReservationStation
{
 // id 0-2 ADD/SUB
 // id 3-5 MULT/DIV
 // id 6-8 LW
 //int 9-11 SW
 Boolean busy; 
 //true busy or false msh busy
  String Op;
  float Vj;
  float Vk;
  float res;
  int Qj;
  int Qk;
  int Address;	
  int cyclesleft;

  public ReservationStation()
  {
	this.busy=false;
	this.Vj=-1;
	this.Vk=-1;
	this.Qj=-1;
	this.Qk=-1;
	this.res=0;
	this.cyclesleft=0;
  }
  //To string()
  //Reset all attributes()
 
public void ResetStation(){
	this.busy=false;
	this.Vj=-1;
	this.Vk=-1;
	this.Op="";
	this.Qj=-1;
	this.Qk=-1;
	this.res=0;
	this.Address=0;
	this.cyclesleft=0;
}
public void PrintStation(int i){
	System.out.println("Station:"+i +" busy:"+this.busy+" Vj:"+this.Vj+" Qj:"+this.Qj+ " Vk:"+this.Vk+ " Qk:"+this.Qk +" A:"+
this.Address+" Cycles Left:"+this.cyclesleft);
	
}
}
