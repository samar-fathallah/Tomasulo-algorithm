package tiramisu;

import java.util.ArrayList;

public class Simulation {
	public static Bus CDB;
	public static ReservationStation[] Station;
	public static int[] GeneralRegisters;
	public static int CurrentCycle;
	public static float[] MemoryData;
	public static Boolean Writing;
	public static ArrayList<InstructionUnit> instructions;
	public static RegisterFile[] RegisterFile;

	// ADD,SUB 3 cycles
	// MULT/DIV 4 cycles
	// Store and load 2 cycles
//public static boolean Program(){
//	for(int )
//}
//	public static void printall(ReservationStation[] Station){
//		for(int i=0;i<Station.length;i++){
//			Station[i].PrintStation(i);
//		}
//	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//3 MULT 3 DIV 3 ADD 3 SUB
		Station=new ReservationStation[12];
		MemoryData=new float[101];
		GeneralRegisters=new int[11];
		RegisterFile=new RegisterFile[6];
		Writing=false;
		CurrentCycle=0;
		CDB=new Bus();
		
		//Instruction Load
		instructions=new ArrayList<InstructionUnit>();
		InstructionUnit ins1=new InstructionUnit("LW",4,1,2);
		instructions.add(ins1);
		InstructionUnit ins2=new InstructionUnit("SUB",2,4,2);
		instructions.add(ins2);
		InstructionUnit ins3=new InstructionUnit("MULT",1,1,3);
		instructions.add(ins3);
		InstructionUnit ins4=new InstructionUnit("ADD",1,5,3);
		instructions.add(ins4);
		//General Registers
				int c=1;
				for(int i=0;i<10;i++){
	                GeneralRegisters[i]=c;
					c+=4;
				}
		//Reservation Station
	  for(int i=0;i<12;i++){
		  Station[i]=new ReservationStation();
	  }
		//Memory Load
		int v=5;
		for(int i=1;i<101;i++){		
			MemoryData[i]=v;
			v+=4;
		}
		//RegisterFile
		int t=6;
		for(int i=1;i<6;i++){
			RegisterFile[i]=new RegisterFile(t);
			t+=1;
		}
		int cycle=0;
	//RUN
    while(true){
    //Execute
    	cycle+=1;
    	
    for(int i=0;i<12;i++){
    	if(Station[i].Vj !=-1 && Station[i].Vk !=-1 && Station[i].busy && Station[i].cyclesleft>=0){
    		System.out.println( "Cycle: "+ cycle+ " In Execution Now Station "+i+ " CycleNumber: "+Station[i].cyclesleft);
    	  Station[i].PrintStation(i);
    		Station[i].cyclesleft-=1;
    			
    		if(Station[i].cyclesleft==0 && Station[i].busy)
    		{
    		
    				if(Station[i].Op=="ADD")//add
    				Station[i].res=Station[i].Vj+Station[i].Vk;
    				if(Station[i].Op=="SUB")
    				Station[i].res=Station[i].Vj-Station[i].Vk;
    				if(Station[i].Op=="MULT")
        				Station[i].res=Station[i].Vj*Station[i].Vk;
    				if(Station[i].Op=="DIV")
        				Station[i].res=Station[i].Vj/Station[i].Vk;
    			
    		}
    	}
    	if(Station[i].Vj !=-1 && Station[i].Op=="SW"|| Station[i].Op=="LW"){
    		System.out.println( "Cycle: "+ cycle+" In Execution Now Station "+i+ " CycleNumber: "+Station[i].cyclesleft);
    		Station[i].cyclesleft-=1;
    		
    	}
    	
    	}
    //Writing
    // Write in stations/registerfile
    //Write to Stations
  
    //Write to Register File
    Writing=false;

    for(int i =0;i<12;i++){
    		if(Station[i].cyclesleft<0 && (!Writing) && Station[i].busy){
    			System.out.println("Cycle: "+ cycle+" Writing Now Station:");
    			Station[i].cyclesleft=0;
    			Station[i].PrintStation(i);
    			Writing=true;
    			//WRITING ON BUS 
    			CDB.Tag=i+1;
    			CDB.value=Station[i].res;
    			//for lw 
    			 if(i>=6 && i<=8){
					  int vj=(int) Station[i].Vj;
					  int add=Station[i].Address;
					  RegisterFile[vj].content=MemoryData[add];
					  CDB.value=MemoryData[add];
				  }
    			 //for sw
    			 if(i>=9 && i<=11){
    				 MemoryData[Station[i].Address]=Station[i].Vj;
    			 }
    			 //
    			//Remove from Station
    			Station[i].ResetStation();
    		    System.out.println("Station: "+ i + " Reseted");
    		
    			  for(int j=0;j<12;j++){
    				  
    			    	if(Station[j].Qj==CDB.Tag && Station[j].busy){
    			    		Station[j].Vj=CDB.value;
    			    		Station[j].Qj=-1;
    			    		Station[j].PrintStation(j);
    			    		
    			    	}
    			    	if(Station[j].Qk==CDB.Tag  && Station[j].busy){
    			    		Station[j].Vk=CDB.value;
    			    		Station[j].Qk=-1;
    			    	}
    			    
    			    }
    			  for(int k=1;k<6;k++){
    			    	if(RegisterFile[k].Qi==CDB.Tag){
    			    		RegisterFile[k].content=CDB.value;
    			    		RegisterFile[k].Qi=-1;
    			    		break;
    			    	}
    			    }
    			
    			break;
    		}
    	
    		
    }

   
	// ISSUE STAGE	MOVE TO BOTTOM
	if(!instructions.isEmpty()){
		
	
		System.out.println("Cycle: "+ cycle +" ISSUED " +instructions.get(0).Ins);
		//Check Operation
		if(instructions.get(0).Ins=="ADD" || instructions.get(0).Ins=="SUB" ){
			//Check for position
			for(int i=0;i<3;i++){
				if(!Station[i].busy){
					Station[i].busy=true;
					Station[i].Op=instructions.get(0).Ins;
					Station[i].cyclesleft=3;
				   
				 
			//Check RegisterFile
						if(RegisterFile[instructions.get(0).op1].Qi==-1){
							Station[i].Vj=RegisterFile[instructions.get(0).op1].content;
							}
						else{
							Station[i].Qj=RegisterFile[instructions.get(0).op1].Qi;
							}
						
					    if(RegisterFile[instructions.get(0).op2].Qi==-1)
					    	Station[i].Vk=RegisterFile[instructions.get(0).op2].content;
						else
							Station[i].Qk=RegisterFile[instructions.get(0).op2].Qi;
					    RegisterFile[instructions.get(0).des].Qi=i+1;
					  Station[i].PrintStation(i);
					
					  break; 
			 
				}
				
			}
		}
		if(instructions.get(0).Ins=="MULT" || instructions.get(0).Ins=="DIV" ){
			//Check for position
			for(int i=3;i<6;i++){
				if(!Station[i].busy){
					Station[i].busy=true;
					Station[i].Op=instructions.get(0).Ins;
						
					Station[i].cyclesleft=4;
			//Check RegisterFile
						if(RegisterFile[instructions.get(0).op1].Qi==-1)
							Station[i].Vj=RegisterFile[instructions.get(0).op1].content;
						else
							Station[i].Qj=RegisterFile[instructions.get(0).op1].Qi;
					    if(RegisterFile[instructions.get(0).op2].Qi==-1)
					    	Station[i].Vk=RegisterFile[instructions.get(0).op2].content;
						else
							Station[i].Qk=RegisterFile[instructions.get(0).op2].Qi;
					    Station[i].PrintStation(i);
					    RegisterFile[instructions.get(0).des].Qi=i+1;
					    break;      	
				}
			}
			
		}
		
		if(instructions.get(0).Ins=="LW"){
			//Check for position
			for(int i=6;i<9;i++){
				if(!Station[i].busy){
					Station[i].busy=true;
					Station[i].Op=instructions.get(0).Ins;
					Station[i].cyclesleft=2;
					
			//Check RegisterFile
					Station[i].Address=instructions.get(0).op1+GeneralRegisters[instructions.get(0).op2];
					Station[i].Vj=instructions.get(0).des;
					Station[i].PrintStation(i);
					RegisterFile[instructions.get(0).des].Qi=i+1;
					break;		
				}
			}
		}  	 
		if(instructions.get(0).Ins=="SW"){
			//Check for position
			for(int i=9;i<12;i++){
				if(!Station[i].busy){
					Station[i].busy=true;
					Station[i].Op=instructions.get(0).Ins;
					Station[i].cyclesleft=2;
			//Check RegisterFile
			Station[i].Address=instructions.get(0).op1+GeneralRegisters[instructions.get(0).op2];
			if(RegisterFile[instructions.get(0).des].Qi==-1)
				Station[i].Vj=RegisterFile[instructions.get(0).des].content;
			else
				Station[i].Qj=RegisterFile[instructions.get(0).op1].Qi; 
			Station[i].PrintStation(i);
			RegisterFile[instructions.get(0).des].Qi=i+1;
			break;
				}
			}
		}  	 
		
		
		instructions.remove(0);
	}   

    
    

   
    
    
    			
    			
    			
    			
    		}
    
		
	}
}
