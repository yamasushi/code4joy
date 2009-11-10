// Ikeda Strange Attractor
// 2009-09-28

import scala.collection.immutable._
import java.awt.{Graphics2D,Color}

import DSIkeda._
class DSIkeda(
		val paramAB:(Double,Double) , 
		val paramKP:(Double,Double) , 
		val ovalR:Double) extends ChaosImageParam with ChaosStreamCanvas
{
	override val colorFGMap   :(Int) => Color = ChaosImageParam.silverFGMap
	//
	val (paramA,paramB) = paramAB
	val (paramK,paramP) = paramKP
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR).points
	//
	override val chaosName = "ichaos_" + 
						"("+paramA.formatted("%7.5f")+","+paramB.formatted("%7.5f")+")" +
						"("+paramK.formatted("%7.5f")+","+paramP.formatted("%7.5f")+")"
	
	override val chaosSystem = new ChaosSystem {
			override def mapDifference( pt:(Double,Double) ) : (Double,Double) = {
			val (x,y) = pt
			//
			val tn    = paramK - paramP/(1 + (x*x + y*y) )
			val sinTn = Math.sin(tn)
			val cosTn = Math.cos(tn)
			(	paramB*( x*cosTn - y*sinTn ) + paramA , 
				paramB*( x*sinTn + y*cosTn ) )
		}
	}
	
}

object DSIkeda extends ChaosParameter[DSIkeda]
{
	def setup() : Unit = {
		var paramsAB = new Queue[(Stream[Double],Stream[Double])]
		var paramsKP = new Queue[(Stream[Double],Stream[Double])]
		//
		paramsAB=paramsAB enqueue (
						ParamRange.neighbor(0.9,0.01,20) ,
						ParamRange.neighbor(0.9,0.01,0) )
		
		paramsKP=paramsKP enqueue (
						ParamRange.neighbor(0.4,0.01,0) ,
						ParamRange.neighbor(6.0,0.01,0) )
		
		paramsKP=paramsKP enqueue (
						ParamRange.neighbor(0.4,0.01,0) ,
						ParamRange.neighbor(7.7,0.01,0) )
		
		for( abRange<-paramsAB ; kpRange <-paramsKP ){
			for( a<-abRange._1 ; b<-abRange._2 ; k<-kpRange._1 ; p<-kpRange._2 ) {
				add( new DSIkeda(
							(a,b),(k,p), 0.1  ) )
			}
		}
		
	}
}
