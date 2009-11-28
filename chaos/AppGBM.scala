// Gingerbread Man Strange Attractor
// 19:23 2009/11/01

import Math.{sin,cos,abs,sqrt,log,exp,pow}
import scala.collection.immutable._

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSGingerbreadMan]
{
	//----------------------------
	def fillParam(name:String ,
				phi:(Double)=>Double ,
				muRange : Stream[Double] ,
				nuRange : Stream[Double] ,
				ovalR   : Double )(map:((Double,Double))=>(Double,Double)) : Unit = {
		for ( mu<-muRange ; nu<-nuRange ){
			add( new DSGingerbreadMan( name , 
				phi , (mu,nu) , map , ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		//
		import Transform._
		val rot = rotate(-(3*Math.Pi)/4.0) 
		//
		var maps   = new Queue[(String,(Double)=>Double)]
		var params = new Queue[(Stream[Double],Stream[Double],Double)] 
		maps = maps enqueue ("_" ,{x=>abs(x)}            )
		// maps = maps enqueue ("2" ,{x=>x*x/(1+abs(x)) }   )
		// maps = maps enqueue ("R" ,{x=>sqrt(abs(x))}      )
		// maps = maps enqueue ("A1",{x=>x/(1+abs(x))}      )
		// maps = maps enqueue ("Q1",{x=>x/(1+x*x)}         )
		// maps = maps enqueue ("TS",{x=>sin(x)}            )
		// maps = maps enqueue ("TC",{x=>cos(x)}            )
		// maps = maps enqueue ("A0",{x=>1/(1+abs(x))}      )
		// maps = maps enqueue ("Q0",{x=>1/(1+x*x)}         )
		// maps = maps enqueue ("AA",{x=>abs(x)/(1+abs(x))} )
		//
		params = params enqueue (
					ParamRange.neighbor(1 , 0.01  ,0),
					ParamRange.neighbor(1 , 0.001 ,0),
					5)
		//
		// for(m<-maps ; p<-params){
			// fillParam(m._1,m._2,p._1,p._2,p._3)(rot)
		// }
		
		for(m<-maps ; p<-params){
			fillParam("RAW-"+m._1,m._2,p._1,p._2,p._3){p=>p}
		}
		
	}
}                       
