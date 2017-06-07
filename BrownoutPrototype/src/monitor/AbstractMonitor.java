package monitor;

import brownout.BrownoutMain;

abstract class AbstractMonitor implements Runnable{

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
		monitor();
		try{
			Thread.sleep(BrownoutMain.TIME_INTERVAL * 1000);
		}catch (InterruptedException e) {
            e.printStackTrace(); 
        }
		
		}
	}
	
	abstract void monitor();

}
