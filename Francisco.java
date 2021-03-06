package francisco;

import java.util.Random;

public class Francisco {
    
        public static int height = 10;
        public static int layers = 10;
        public static double e =  2.7182818284590452;
        public static double[] weights;

        public static void main(String[] args) {
            
            weights = new double[height*height*layers+3];
            weights = setup();
            
            double[] inputs = new double[height];
            
            //start by setting each value to 0
            for (int n = 0; n < height; n++){
                inputs[n] = 0;
            }
     
            inputs[0] = 1;
            inputs[1] = 0;
            inputs[2] = 1;
            inputs[3] = 0;
            /*
            .
            .
            .
            */
            inputs[height-1] = 1;
            
            train(inputs, inputs, true, 0.01);
            
            double[] outputs = new double[height];
            
            outputs = run_net(inputs);
            
            for (int t = 0; t<height; t++){
                System.out.println(outputs[t]);
            }
        
        }
      
        public static double[] setup(){
            double[] out = new double[height*height*layers+3];
            for (int i = 0; i < height*height*layers+3; i ++){
                out[i] = random_number();
                //System.out.println(out[i]);
            }
            return out;
        }
        
        public static void train(double[] input, double[] training, boolean print, double precision){
            
            for (int i = height*height*layers; i > 0; i = i-1){
                
                //get your weight:
                double errors[] = new double[height];
                double errors2[] =  new double[height];
                errors = run_net(input);
                weights[i] = weights[i] + 1;
                errors2 = run_net(input);
                for (int n = 0; n < height; n++){
                    errors[n] = errors2[n] - errors[n];
                }
                
                weights[i] = weights[i] - 1;
                
                double max = errors[0];
		int index = 0;
                //locate the most affected error:
		for (int ii = 0; ii < errors.length; ii++) 
		{
			if (max < Math.abs(errors[ii])) 
			{
				max = Math.abs(errors[ii]);
				index = ii;
			}
                        //System.out.println(errors[ii]);
		}
    
                long maxx = 100000;
                double cur_x = weights[i];
                double next_x = 0;
                int num = 0;
                double gamma = 0.1;
                double desired = training[index];
                
                while(num < maxx){
                    //get output
                    double net2[] = run_net(input);
                    double net = net2[index];
                    
                    next_x = cur_x;
                    cur_x = next_x-(gamma * (net-desired));
                    num++;
                    double dif = Math.abs(next_x - cur_x);
                    next_x = cur_x;
                    weights[i] = next_x;   
                   
                }
                
                //calc the error:
                double error_tot = 0;
                errors = run_net(input);
                for (int o = 0; o < height; o++){
                    error_tot = error_tot + (training[o] - errors[o]);
                }
        
                //print error:
                if (print == true){
                    System.out.println(error_tot);
                }
                
                if (Math.abs(error_tot) <= precision){
                    break;
                }
            }
           
        }
        
        public static double[] run_net (double[] inputs){
            
            double outputs[] = new double[height];
            double layer[][] = new double[height][layers];
            
            int trac1 = 0;
          
            
            //this method runs the network
            for (int i = 0; i < layers; i++){
                //for each layer, calc each neuron:
                for (int j = 0; j < height; j++){
                    //if it is the first time...
                    if (i == 0){
                        double output = 0;
            
                        for (int n = 0; n < height; n++){
                            output = output + weights[trac1]*inputs[n];
				
				trac1++;
                        }
                        
                        output = Math.pow(e, output) / (Math.pow(e, output) + 1); //sigmoid func

                        
                        layer[j][i] = output;
                        if (Double.isNaN(layer[j][i])){
                            layer[j][i] = 1E-3;
                        }
                    }
                    
                    //else if it is the last time...
                    else if (i == layers-1){
                        double output = 0;
            
                        for (int n = 0; n < height; n++){
                            output = output + weights[trac1]*layer[n][i-1];
				
				trac1++;
                        }

                        output = Math.pow(e, output) / (Math.pow(e, output) + 1); //sigmoid func

                        
                        layer[j][i] = output;
                        if (Double.isNaN(layer[j][i])){
                            layer[j][i] = 1E-3;
                        }
                        outputs[j] = layer[j][i];
                        
                        
                    }
                    
                    //else...
                    else {
                        double output = 0;
            
                        for (int n = 0; n < height; n++){
                            output = output + weights[trac1]*layer[n][i-1];
				trac1++;
				
                        }

                        output = Math.pow(e, output) / (Math.pow(e, output) + 1); //sigmoid func

                        
                        layer[j][i] = output;
                        if (Double.isNaN(layer[j][i])){
                            layer[j][i] = 1E-3;
                        }
                    }
                    
                    //System.out.println(layer[j][i]);
                    
                }
                
            }
            
            return outputs;
        }
    
        //fuction to genetrate random decimals
        public static double random_number(){
		Random rand = new Random();

		// Obtain a number between [0 - 199].
		double n = rand.nextInt(199);
		
		n = n/100;
		n = n - 1;
		return n;
        }
    
}
