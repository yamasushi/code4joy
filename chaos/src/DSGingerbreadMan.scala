// Gingerbread Man Strange Attractor
//   http://en.wikipedia.org/wiki/Gingerbreadman_map
//   http://mathworld.wolfram.com/GingerbreadmanMap.html
//   http://sprott.physics.wisc.edu/FRACTALS/booktext/sabook.pdf
//
//  Devaney, R. L.
//  "A Piecewise Linear Model for the Zones of Instability of an Area Preserving Map."
//  Physica D 10, 387-393, 1984.
//
//  Devaney, R. L. 
//  "The Gingerbreadman."
//  Algorithm 3, 15-16, Jan. 1992.
//
//  Dr. Mu.
//  "Cowculations: Gingerbread Man."
//  Quantum, pp. 55-57, January/February 1998.
//
//  Peitgen, H.-O. and Saupe, D. (Eds.). 
//  "A Chaotic Gingerbreadman." §3.2.3 in The Science of Fractal Images. 
//  New York: Springer-Verlag, pp. 149-150, 1988.

import java.awt.{Graphics2D,Color}
import Math.{sin,cos,abs,sqrt,log,exp,pow}
import scala.collection.immutable._

import DSGingerbreadMan._
class DSGingerbreadMan(
		val header:String           , 
		//
		phi    :(Double)=>Double,
		param  :(Double,Double) ,
		val map:((Double,Double))=>(Double,Double) ,
		//
		val ovalR :Double) extends ChaosImageParam with ChaosStreamCanvas
{
	val (mu,nu) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR ).points
	override val chaosName = "gbmchaos_"+header+"_" + 
						"("+mu.formatted("%7.5f")+"," +
							nu.formatted("%7.5f")+")"
						
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:(Double,Double)) : (Double,Double) = {
			val (x,y) = p
			(- nu*y + 1 + mu*phi(x) , x)
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			map(p)
		}
		override def validateParam : Boolean = {
			if( abs(mu)<1e-7 ) return false
			true
		}
	}
}

object DSGingerbreadMan extends ChaosParameter[DSGingerbreadMan]
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
		for(m<-maps ; p<-params){
			fillParam(m._1,m._2,p._1,p._2,p._3)(rot)
		}
		
		// for(m<-maps ; p<-params){
			// fillParam("RAW-"+m._1,m._2,p._1,p._2,p._3){p=>p}
		// }
		
	}
}                       
