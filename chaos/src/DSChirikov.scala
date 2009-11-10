// Chirikov standard map Strange Attractor
// 11:38 2009/11/04
//   http://www.scholarpedia.org/article/Chirikov_standard_map
//   http://mathworld.wolfram.com/StandardMap.html
//   http://en.wikipedia.org/wiki/Standard_map

import scala.collection.immutable._
import java.awt.{Graphics2D,Color}

import Math.{sin,cos,log,sqrt,abs,exp}

import DSChirikov._
class DSChirikov( val header:String ,
		val paramK : Double , 
		val ovalR:Double) extends ChaosImageParam with ChaosStreamCanvas
{
	override val colorFGMap   :(Int) => Color = ChaosImageParam.silverFGMap
	//
	val period = 2*Math.Pi
	val radius = period*0.5
	def remainder(x:Double):Double = {Math.IEEEremainder(x,period)}
	def remainder(p:(Double,Double)):(Double,Double) = {(remainder(p._1),remainder(p._2))}
	//
	override val initialPoints = PointsOfRing( ( 0 , 0 ) , numRing , period ).points map remainder 
	//
	override val chaosName = "csmchaos_" + header + "_" + 
						"("+paramK.formatted("%7.5f")+")" 
	//
	override val chaosSystem = new ChaosSystem {
		override def mapDifference( xp:(Double,Double) ) : (Double,Double) = {
			val (x,p) = xp
			//
			val pp = remainder( p + paramK * sin(x) )  
			val xx = remainder( x + pp              )
			//
			(xx,pp)
		}
		override def mapCoordinate(xp:(Double,Double)) : (Double,Double) = {
			val (x,p) = xp
			( Math.IEEEremainder( x + Math.Pi , 2*Math.Pi) , p )
		}
		override def validateParam : Boolean = {
			// coefficient of pi
			if( paramK < 1e-7 ) return false
			
			true 
		}
	}
	
}

object DSChirikov extends ChaosParameter[DSChirikov]
{
	def fillParam(name:String   ,
				kRange : Stream[Double] ,
				ovalR  : Double ) : Unit = {
		for ( k<-kRange ){
			add( new DSChirikov( name , 
				k , ovalR ) )
		}
		//
		()
	}
	
	//----------------------------
	def setup() : Unit = {
		
		fillParam("H",
			ParamRange.neighbor( 1. / 34.0     , 0.01 , 20 ) , // Hermann 
			1 )
			
		fillParam("CC",
			ParamRange.neighbor( 419. / 500.0  , 0.01 , 20 ) , // Cellette,Chierchia 1995 
			1 )
			
		fillParam("G",
			ParamRange.neighbor( 0.971635406   , 0.01 , 20 ) , // Greene 
			1 )
			
		fillParam("MP",
			ParamRange.neighbor( 63.0 / 64.0   , 0.01 , 20 ) , // MacKay , Percival 1995
			1 )
			
		fillParam("MA",
			ParamRange.neighbor( 4.0 / 3.0     , 0.01 , 20 ) , // Mather 1995
			1 )
			
		fillParam("_",
			ParamRange.neighbor( 2   , 0.01 , 20 ) , 
			1 )
			
	}
}

