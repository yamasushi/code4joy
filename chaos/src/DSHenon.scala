// Henon Strange Attractor
// 2009-09-28

import scala.collection.immutable._
import java.awt.{Graphics2D,Color}

import DSHenon._
class DSHenon(
		val header:String           , 
		//
		val func  :(Double)=>Double , 
		val alpha :Double ,
		val period:Double ) extends ChaosImageParam with ChaosStreamCanvas
{
	override val colorFGMap   :(Int) => Color = ChaosImageParam.silverFGMap
	
	val radius = period*0.5
	def remainder(x:Double):Double = {Math.IEEEremainder(x,period)}
	def remainder(p:(Double,Double)):(Double,Double) = {(remainder(p._1),remainder(p._2))}
	override val initialPoints = PointsOfRing( ( 0 , 0 ) , numRing , period ).points map remainder 
	//
	
	override val chaosName = "hchaos_" + header + "_" + 
						"("+alpha.formatted("%7.5fpi") + ")"
	
	override val chaosSystem = new ChaosSystem{
		val theta = alpha * Math.Pi
		val cosT = Math.cos(theta)
		val sinT = Math.sin(theta)
		//
		override def mapDifference( pt:(Double,Double) ) : (Double,Double) = {
			val (x,y) = pt
			val temp  = y-func(x)
			val p = ( x*cosT - temp*sinT , x*sinT + temp*cosT )
			//
			remainder(p)
		}
		override def mapCoordinate(p:(Double,Double)) : (Double,Double) = {
			p 
		}
	}
	
}

object DSHenon extends ChaosParameter[DSHenon]
{
	//----------------------------
	def setup() : Unit = {
		numRing = 15
		def hFuncF_sq(x:Double) : Double = x*x
		def hFuncF_cu(x:Double) : Double = x*x*x
		def hFuncF_si(x:Double) : Double = 0.8*Math.sin(x)
		def hFuncF_in(x:Double) : Double = 0.1 / x
		
		var maps  = new Queue[(String,(Double)=>Double)]
		var params= new Queue[Double] 
		
		maps = maps enqueue ("sq",{x:Double=>x*x             })
		maps = maps enqueue ("cu",{x:Double=>x*x*x           })
		maps = maps enqueue ("si",{x:Double=>0.8*Math.sin(x) })
		maps = maps enqueue ("in",{x:Double=>0.1/x           })
		
		params = params enqueue List(0.47,0.07,0.30,0.39,0.61,0.84)
		
		for( m<-maps ; p<-params){
			add( new DSHenon(m._1 , 
						m._2,p,2*Math.Pi) )
		}
	}
}                       
