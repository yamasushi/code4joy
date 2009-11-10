// Peter de Jong Strange Attractor
//   http://demonstrations.wolfram.com/PeterDeJongAttractors/
//   http://local.wasp.uwa.edu.au/~pbourke/fractals/peterdejong/
//   http://www.complexification.net/gallery/machines/peterdejong/
// 13:00 2009/11/03

import java.awt.{Graphics2D,Color}
import Math.{sin,cos,abs,sqrt,log,exp,pow}

import DSPeterDeJong._
class DSPeterDeJong(
		val header:String           , 
		//
		param  :(Double,Double,Double,Double) ,
		//
		val ovalR :Double) extends ChaosImageParam with ChaosStreamCanvas
{
	override val colorFGMap   :(Int) => Color = ChaosImageParam.silverFGMap
	//
	val (a,b,c,d) = param
	//
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR ).points
	override val chaosName = "pdjchaos_"+header+"_" + 
						"("+a.formatted("%7.5f")+"," +
							b.formatted("%7.5f")+"," +
							c.formatted("%7.5f")+"," +
							d.formatted("%7.5f")+")"
						
	//
	override val chaosSystem = new ChaosSystem {
		override def mapDifference(p:(Double,Double)) : (Double,Double) = {
			val (x,y) = p
			(	sin(a*Math.Pi*y) - cos(b*Math.Pi*x) , 
				sin(c*Math.Pi*x) - cos(d*Math.Pi*y))
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			p
		}
		override def validateParam : Boolean = {
			if( abs(a)+abs(b) < 1e-7 ) return false
			if( abs(c)+abs(d) < 1e-7 ) return false
			true
		}
	}
}

object DSPeterDeJong extends ChaosParameter[DSPeterDeJong]
{
	//----------------------------
	def fillParam(name:String ,
				aRange  : Stream[Double] ,
				bRange  : Stream[Double] ,
				cRange  : Stream[Double] ,
				dRange  : Stream[Double] ,
				ovalR   : Double ) : Unit = {
		for ( a<-aRange ; b<-bRange ; c<-cRange ; d<-dRange){
			add( new DSPeterDeJong( name , 
				(a,b,c,d) , ovalR ) )
		}
		//
		()
	}
	//----------------------------
	def setup() : Unit = {
		var maps:List[(String,(Double,Double)=>Double)] = Nil
		// fillParam("",
			// ParamRange.neighbor( 0.56  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.53  , 0.01 ,0) ,
			// ParamRange.neighbor(-0.274 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.67  , 0.01 ,0) ,
			// 1)
		
		// fillParam("",
			// ParamRange.neighbor( 0.61  , 0.01 ,0) ,
			// ParamRange.neighbor(-0.74  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.498 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.291 , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.4035 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.4586 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.393  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.475  , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.53  , 0.01 ,0) ,
			// ParamRange.neighbor(-0.506 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.402 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.434 , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.368 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.394 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.362 , 0.01 ,0) ,
			// ParamRange.neighbor(-0.804 , 0.01 ,0) ,
			// 1)
			
		// fillParam("",
			// ParamRange.neighbor( 0.854 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.252 , 0.01 ,0) ,
			// ParamRange.neighbor( 0.22  , 0.01 ,0) ,
			// ParamRange.neighbor( 0.396 , 0.01 ,0) ,
			// 1)
			
		fillParam("",
			ParamRange.neighbor( 0.854 , 0.01 ,0) ,
			ParamRange.neighbor( 0.404 , 0.01 ,0) ,
			ParamRange.neighbor( 0.742 , 0.01 ,0) ,
			ParamRange.neighbor( 0.51  , 0.01 ,0) ,
			1)
	}
}                       
