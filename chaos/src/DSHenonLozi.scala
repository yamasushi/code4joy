// Henon/Lozi Strange Attractor
//   http://mathworld.wolfram.com/LoziMap.html
//   http://mathforum.org/advanced/robertd/lozi.html.
//  
// modified Lozi -->
//   http://www.emis.de/journals/HOA/DDNS/c754.pdf 
// 
// Peitgen, H.-O.; Jürgens, H.; and Saupe, D. 
// §12.1 in Chaos and Fractals: New Frontiers of Science. 
// New York: Springer-Verlag, p. 672, 1992.
//
// 9:00 2009/11/03

import java.awt.{Graphics2D,Color}
import Math.{sin,cos,abs,sqrt,log,exp,pow}
import scala.collection.immutable._

import DSHenonLozi._
class DSHenonLozi(
		val header:String           , 
		//
		phi    :(Double,Double)=>Double,	//phi(x,y) = x^2 --> Henon
											//phi(x,y) = |x| --> Lozi
											//phi(x,y) = y^2 - |x| --> modified Lozi
		param  :(Double,Double) ,
		//
		val ovalR :Double) extends ChaosImageParam with ChaosStreamCanvas
{
	val (alpha,beta) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR ).points
	override val chaosName = "hlchaos_"+header+"_" + 
						"("+alpha.formatted("%7.5f")+"," +
							beta .formatted("%7.5f")+")"
						
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:(Double,Double)) : (Double,Double) = {
			val (x,y) = p
			( 1 + y - alpha*phi(x,y) , beta*x)
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			p
		}
		override def validateParam : Boolean = {
			if( abs(alpha)<1e-7 ) return false
			if( abs(beta )<1e-7 ) return false
			true
		}
	}
}

object DSHenonLozi extends ChaosParameter[DSHenonLozi]
{
	//----------------------------
	def fillParam(name:String ,
				phi:(Double,Double)=>Double ,
				alphaRange : Stream[Double] ,
				betaRange  : Stream[Double] ,
				ovalR      : Double ) : Unit = {
		for ( alpha<-alphaRange ; beta<-betaRange ){
			add( new DSHenonLozi( name , 
				phi , (alpha,beta) , ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps = new Queue[(String,(Double,Double)=>Double)]
		maps = maps enqueue ( "H"  , {(x:Double,y:Double)=>x*x    }      ) 
		maps = maps enqueue ( "L"  , {(x:Double,y:Double)=>abs(x) }      ) 
		maps = maps enqueue ( "ML" , {(x:Double,y:Double)=>y*y - abs(x) }) 
		//
		var params = new Queue[(Stream[Double],Stream[Double],Double)] 
		params = params.enqueue(	ParamRange.neighbor(-1.8 , 0.01  ,0) ,
									ParamRange.neighbor( 0.3 , 0.001 ,0) , 0.1 )
					
		params = params.enqueue(	ParamRange.neighbor(-1.8 , 0.01  ,0) ,
									ParamRange.neighbor( 0.5 , 0.001 ,0) , 0.1 )
		
		params = params.enqueue(	ParamRange.neighbor( 1.4 , 0.01  ,0) ,
									ParamRange.neighbor( 0.3 , 0.001 ,0) , 0.1 )
		
		params = params.enqueue(	ParamRange.neighbor( 1.35 , 0.01  ,0) ,
									ParamRange.neighbor( 0.3  , 0.001 ,0) , 0.1 )
			
		params = params.enqueue(	ParamRange.neighbor( 0.2  , 0.01  ,0) ,
									ParamRange.neighbor( 1.01 , 0.001 ,0) , 0.1 )
		for( m<-maps ; p<-params){
			fillParam(m._1,m._2,p._1,p._2,p._3)
		}
	}
}                       
