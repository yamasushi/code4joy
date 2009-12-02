// Henon Strange Attractor
// 2009-09-13
// 2009-09-28
import scala.collection.immutable._
import Math.{sin,cos,log,sqrt,abs,exp}

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSHenon]
{
	//----------------------------
	def setup() : Unit = {
		numRing = 15
		
		def hFuncF_sq(x:Double) : Double = x*x
		def hFuncF_cu(x:Double) : Double = x*x*x
		def hFuncF_si(x:Double) : Double = 0.8*Math.sin(x)
		def hFuncF_in(x:Double) : Double = 0.1 / x
		
		def mapD1(r:Double)(p:Vector[Double]):Vector[Double] = {
			val theta = p.x
			val radius= p.y + r
			( radius*cos(theta) , radius*sin(theta) )
		}
		def mapD2(r:Double)(p:Vector[Double]):Vector[Double] = {
			val theta = p.y
			val radius= p.x + r
			( radius*cos(theta) , radius*sin(theta) )
		}
		val period=2*Math.Pi
		var funs  = new Queue[(String,(Double)=>Double)]
		var params= new Queue[Double] 
		var maps  = new Queue[(String,((Vector[Double])=>Vector[Double]))]
		
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
						f._2,p,m._2, numRing,period) )
		}
	}
}                       
