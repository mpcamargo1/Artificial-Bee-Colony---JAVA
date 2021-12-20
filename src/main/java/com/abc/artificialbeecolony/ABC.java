/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.abc.artificialbeecolony;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author marcospaulo
 */
public class ABC {

      private static int AGENT=50;
      private static int DIMENSION=2;
      private static int MAX_IT=10000;
      private static int MAX_IT_EB=50;
      private static int MAX_IT_OB=50;
      private static int MAX_TRIAL=50;


      private static double[] LOWER = {-15,-3};
      private static double[] UPPER = {-5,3};
      private static Random random = new Random(System.currentTimeMillis());
      private static double[][] bee = new double[AGENT][DIMENSION];
      private static double[] trial = new double[AGENT];
      private static double[] prob = new double[AGENT];
      private static double best_solution = 0;

      private static final DecimalFormat df = new DecimalFormat("0.0000");  

      public static void optimize(){
             initialize();
             for(int i=0;i<MAX_IT;i++){
                employed_bees();
                onlooker_bees();
                scout_bees();
              }
            printSolution();
      }

      private static void initialize(){
         for(int i=0;i<AGENT;i++){
             bee[i][0] = get_random(-15,-5);
             bee[i][1] = get_random(-3,3);

             trial[i] = 0;
             prob[i] = 0;
           }
      }

      private static double get_random(double min,double max){
          return random.nextDouble()*(max - min) + min;
      }

      private static double fitness(double[] x){
             if(function(x) >= 0)
                return 1/(1+function(x));
             else
                return 1 + (-1*function(x));
      }

      private static double function(double[] x){
          return Math.pow(x[0] + 2*x[1] - 7,2)
          + Math.pow(2*x[0] + x[1] - 5,2); // Booth Function 

          /*
          return 0.26*(Math.pow(x[0],2) + Math.pow(x[1],2))
          - 0.48*x[0]*x[1];  // Matyas Function
          */ 

          /*
          return 100 * (Math.sqrt(Math.abs(x[1] - 0.01 * Math.pow(x[0], 2))))
          + 0.01 * Math.abs(x[0] + 10); // Bukin N.6 Function
          */

          /*
          return Math.pow(Math.pow(x[0],2) + x[1]- 11,2) + Math.pow(x[0] + 
          Math.pow(x[1],2) - 7,2);  // Himmelblau Function
          */

          /*return Math.pow((x[0]  + 2*x[1] - 7),2)
          + Math.pow((2*x[0] + x[1] - 5),2);
          */

          /*https://en.wikipedia.org/wiki/Test_functions_for_optimization*/
      }

      private static void employed_bees(){
         for(int a=0;a<MAX_IT_EB;a++){
            for(int i=0;i<AGENT;i++){
               int index_j = random.nextInt(AGENT);
               int index_dimension = random.nextInt(DIMENSION);
               double[] aux = new double[DIMENSION];
               aux = Arrays.copyOfRange(bee[index_j],0, DIMENSION);

               aux[index_dimension] = bee[i][index_dimension] + get_random(-1,1)
               *(bee[i][index_dimension]-bee[index_j][index_dimension]);
               
               if(fitness(aux) > fitness(bee[i]))
                    bee[i] = Arrays.copyOfRange(aux,0, DIMENSION);
               else
                    trial[i]++;
            }
         }

      }

      private static void onlooker_bees(){
         buildprobabilities();
         int i;
         for(int a=0;a<MAX_IT_OB;a++){
             i = selectbee();
             int index_j = random.nextInt(AGENT);
             int index_dimension = random.nextInt(DIMENSION);
      
             double[] aux = new double[DIMENSION];
             aux = Arrays.copyOfRange(bee[index_j],0, DIMENSION);
             
             aux[index_dimension] = bee[i][index_dimension] + get_random(-1,1)
             *(bee[i][index_dimension] - bee[index_j][index_dimension]);

             if(fitness(aux) > fitness(bee[i]))
                 bee[i] = Arrays.copyOfRange(aux,0, DIMENSION);
             else
                 trial[i]++;
          }
      }
    
      private static void scout_bees(){
         for(int i=0;i<AGENT;i++){
            if(trial[i] >= MAX_TRIAL){
                 trial[i] = 0;
                 int dimension = random.nextInt(DIMENSION);
                 bee[i][dimension] = LOWER[dimension] + random.nextDouble()
                 *(UPPER[dimension] + LOWER[dimension]);
             }
         }
      }

      private static int selectbee(){
           double probabilities = random.nextDouble();
           double acc=0;
           int i,index_selected = 0;
            for(i=0;i<AGENT;i++){
               acc+=prob[i];
               if(probabilities < acc){
                    index_selected = i;
                    break;
               }
            }
           return index_selected;
      }

      private static void buildprobabilities(){
          double acc=0;
          for(int i=0;i<AGENT;i++)
             acc+= fitness(bee[i]);

          for(int a=0;a<AGENT;a++)
             prob[a] = fitness(bee[a])/acc;
      }

      private static void printSolution(){
          double best_solution = fitness(bee[0]);
          int index=0;
         
          for(int i=1;i<AGENT;i++){
             if(fitness(bee[i]) > best_solution){
               best_solution = fitness(bee[i]);
               index=i;
            }
          }

          for(int d=0;d<AGENT;d++)
             System.out.println("x = " + df.format(bee[d][0]) + " y = "
              + df.format(bee[d][1]));
          

          System.out.println("f(x,y) = " + df.format(function(bee[index]))
           + " | x = " + df.format(bee[index][0])
           + " , y = " + df.format(bee[index][1]));
          
      }
}
