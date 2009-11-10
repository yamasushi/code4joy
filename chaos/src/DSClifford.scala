// Clifford Strange Attractor
//   http://local.wasp.uwa.edu.au/~pbourke/fractals/clifford/
//   http://en.wikipedia.org/wiki/Clifford_A._Pickover
// 13:00 2009/11/03

import java.awt.{Graphics2D,Color}
import Math.{sin,cos,abs,sqrt,log,exp,pow}

import DSClifford._
class DSClifford(
		val header:String           , 
		//
		param  :(Double,Double,Double,Double) ,
		//
		val ovalR :Double) extends ChaosImageParam with ChaosStreamCanvas
{
	override val colorFGMap   :(Int) => Color = ChaosImageParam.silverFGMap

	val (a,b,c,d) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR ).points
	override val chaosName = "cpchaos_"+header+"_" + 
						"("+a.formatted("%7.5f")+"," +
							b.formatted("%7.5f")+"," +
							c.formatted("%7.5f")+"," +
							d.formatted("%7.5f")+")"
						
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:(Double,Double)) : (Double,Double) = {
			val (x,y) = p
			(	sin(a*y) + c*cos(a*x) , 
				sin(b*x) + d*cos(b*y))
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

object DSClifford extends ChaosParameter[DSClifford]
{
	//----------------------------
	def fillParam(name:String ,
				aRange  : Stream[Double] ,
				bRange  : Stream[Double] ,
				cRange  : Stream[Double] ,
				dRange  : Stream[Double] ,
				ovalR   : Double ) : Unit = {
		for ( a<-aRange ; b<-bRange ; c<-cRange ; d<-dRange){
			add( new DSClifford( name , 
				(a,b,c,d) , ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps:List[(String,(Double,Double)=>Double)] = Nil
		// fillParam("",
			// ParamRange.neighbor(-1.4  , 0.01 ,0) ,
			// ParamRange.neighbor( 1.6  , 0.01 ,0) ,
			// ParamRange.neighbor( 1    , 0.01 ,0) ,
			// ParamRange.neighbor( 0.7  , 0.01 ,0) ,
			// 1)
		fillParam("",
			ParamRange.neighbor( 1.1  , 0.01 ,0) ,
			ParamRange.neighbor(-1.0  , 0.01 ,0) ,
			ParamRange.neighbor( 1.0  , 0.01 ,0) ,
			ParamRange.neighbor( 1.5  , 0.01 ,0) ,
			1)
	}
}                       
