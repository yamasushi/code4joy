// Henon Strange Attractor
// 2009-09-28

import scala.collection.immutable._
import java.awt.{Graphics2D,Color}
import Math.{sin,cos}

import DSHenon._
class DSHenon(
		val header:String           , 
		//
		val func  :(Double)=>Double , 
		val alpha :Double ,
		val map    : ((Double,Double))=>(Double,Double) ,
		val period:Double ) extends ChaosImageParam with ChaosStreamCanvas
{
	override val colorFGMap   :(Int) => Color = ChaosImageParam.silverFGMap
	
	val radius = period*0.5
	def remainder(x:Double):Double = {
		val rem = Math.IEEEremainder(x,period)
		//if(rem>=0) rem else rem + period
		rem
	}
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
			map(p) 
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
		
		def mapD1(r:Double)(p:(Double,Double)):(Double,Double) = {
			val theta = p._1
			val radius= p._2 + r
			( radius*cos(theta) , radius*sin(theta) )
		}
		def mapD2(r:Double)(p:(Double,Double)):(Double,Double) = {
			val theta = p._2
			val radius= p._1 + r
			( radius*cos(theta) , radius*sin(theta) )
		}
		val period=2*Math.Pi
		var funs  = new Queue[(String,(Double)=>Double)]
		var params= new Queue[Double] 
		var maps  = new Queue[(String,(((Double,Double))=>(Double,Double)))]
		
		// grav-lens : sq-d1 0.3pi
		
		funs = funs enqueue ("sq",{x:Double=>x*x             })
		// funs = funs enqueue ("cu",{x:Double=>x*x*x           })
		// funs = funs enqueue ("si",{x:Double=>0.8*Math.sin(x) })
		// funs = funs enqueue ("in",{x:Double=>0.1/x           })
		
		// params = params enqueue ParamRange.neighbor( 0.47 , 0.001 , 0 )
		// params = params enqueue ParamRange.neighbor( 0.07 , 0.001 , 0 )
		params = params enqueue ParamRange.neighbor( 0.30 , 0.01 , 0 )
		// params = params enqueue ParamRange.neighbor( 0.39 , 0.001 , 0 )
		// params = params enqueue ParamRange.neighbor( 0.61 , 0.001 , 0 )
		// params = params enqueue ParamRange.neighbor( 0.84 , 0.001 , 0 )
		
		// maps = maps enqueue (""  , { p:(Double,Double) => p } )
		maps = maps enqueue ("D1",mapD1(period) _ )
		// maps = maps enqueue ("D2",mapD2(period) _ )
		
		for(f<-funs ; p<-params ; m<-maps ){
			add( new DSHenon(f._1+"-"+m._1 , 
						f._2,p,m._2,period) )
		}
	}
}                       
