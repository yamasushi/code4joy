// Clifford Strange Attractor
//   http://local.wasp.uwa.edu.au/~pbourke/fractals/clifford/
//   http://en.wikipedia.org/wiki/Clifford_A._Pickover
// 13:00 2009/11/03

import java.awt.{Graphics2D,Color}
import Math.{sin,cos,abs,sqrt,log,exp,pow}

import DSSine._
class DSSine(
		val header:String           , 
		param  :(Double,Double) ,
		//
		val ovalR :Double) extends ChaosImageParam with ChaosStreamCanvas
{
	val (a,b) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR ).points
	override val chaosName = "sinchaos_"+header+"_" + 
						"("+a.formatted("%7.5f")+"," +
							b.formatted("%7.5f")+")"
						
	//
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:(Double,Double)) : (Double,Double) = {
			val (x,y) = p
			(	y , 
				a*sin(x) + b*y )
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			p
		}
		override def validateParam : Boolean = {
			if( abs(a)+abs(b) < 1e-7 ) return false
			true
		}
	}
}

object DSSine extends ChaosParameter[DSSine]
{
	//----------------------------
	def fillParam(name:String ,
				aRange  : Stream[Double] ,
				bRange  : Stream[Double] ,
				ovalR   : Double ) : Unit = {
		for ( a<-aRange ; b<-bRange ){
			add( new DSSine( name , 
				(a,b) , ovalR )) 
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps:List[(String,(Double,Double)=>Double)] = Nil
		fillParam("",
			ParamRange.neighbor( 1.8  , 0.01 , 20) ,
			ParamRange.neighbor( 0.3  , 0.01 , 20) ,
			5)
	}
}                       
