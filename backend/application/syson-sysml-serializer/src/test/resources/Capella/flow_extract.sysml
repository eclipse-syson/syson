package Functions {
			private import Data::*;			
			action 'Root Function' : Function {
	   	    	action 'Capture Movement' : Function {
	   	    		//Corresponds to the description field in Capella
	        		doc description /*Interpret raw optical data into motion vectors.*/
	            	@StatusInfo {status = StatusKind::tbd;}
	            	//Correspond to a Capella Function Output Port and the allocated Exchange Item(s) (binding rawOpticalData) 
	            	out FOP1 [0..*] : ExchangeItem = (rawOpticalData);
	        	}
	        	
	   	    	action 'Transform Raw Optical Data' : Function {
	   	    		in FIP2 [0..*] : ExchangeItem = (rawOpticalData);
	   	    	}
	
	   	    	// A Functional Exchange
	   	    	flow 'Optical Data' : FunctionalExchange of ExchangeItem[*]
	   	    		from 'Capture Movement'.FOP1 to 'Transform Raw Optical Data'.FIP2;
	   	    	// Binding the Exchange Item(s) to the flow
	   	    	bind 'Optical Data'.payload = rawOpticalData;
	   	    	
	   	    
	   	    	// A Functional Chain	
	   	    	action 'read movement' : FunctionalChain {
	   	    		ref :>> involvedFunctionalExchanges = ('Optical Data');
	   	    		first start;
	   	    		then 'Root Function'.'Capture Movement';
	 	    		then 'Root Function'.'Transform Raw Optical Data';
	 	    		then done;
	   	    	}
	   	    }
    	}