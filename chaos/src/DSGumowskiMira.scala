// Gumowski-Mira Strange Attractor
// 2009-09-09
// 2009-09-11
// 2009-09-28
// 10:25 2009/10/13
// 15:28 2009/10/16
// 
// see:
//   http://www.scipress.org/journals/forma/pdf/1502/15020121.pdf
//   http://math.cmaisonneuve.qc.ca/alevesque/chaos_fract/Attracteurs/Attracteurs.html// see also:

import scala.collection.immutable._
import java.awt.{Graphics2D,Color}

import Math.{sin,cos,log,sqrt,abs,exp}

import DSGumowskiMira._
class DSGumowskiMira( val header:String ,
		//
		val phi      :((Double)=>Double) , 
		val paramAB  :(Double,Double)    ,
		val paramMuNu:(Double,Double)    , 
		val ovalR:Double) extends ChaosImageParam with ChaosStreamCanvas
{
	val (paramA,paramB) = paramAB
	val (mu    ,nu)     = paramMuNu
	override val initialPoints = PointsOfRing((0,0),numRing,ovalR).points
	//
	override val chaosName = "gmchaos_" + header + "_" + 
						"("+paramA.formatted("%7.5f")+","+paramB.formatted("%7.5f")+")" +
						"("+mu    .formatted("%7.5f")+","+nu    .formatted("%7.5f")+")" 
	
	override val chaosSystem = new ChaosSystem {
		def gmF(x:Double) : Double = {
			mu * x + 2*(1-mu)*phi(x)
		}
		//
		def gmG(y:Double) : Double = {
			nu *y + paramA * (1-paramB*y*y)*y
		}
		//
		override def mapDifference( p:(Double,Double) ) : (Double,Double) = {
			val (x,y) = p
			//
			val xx = gmG(y) + gmF(x)
			val yy = -x + gmF(xx)
			//
			(xx,yy)
		}
	}
}

object DSGumowskiMira extends ChaosParameter[DSGumowskiMira]
{
	def fillParam(name:String ,
				phi:(Double)=>Double    ,
				paramAB : (Double,Double)        , 
				muRange : Stream[Double] ,
				nuRange : Stream[Double] ,
				ovalR   : Double ) : Unit = {
		val (paramA,paramB)        = paramAB
		for ( mu<-muRange ; nu<-nuRange ){
			add( new DSGumowskiMira( name , 
				phi , ( paramA , paramB ),(mu,nu) , ovalR ) )
		}
		//
		()
	}
	
	//----------------------------
	def setup() : Unit = {
		import GumowskiMiraPhi._
		
		var maps  =new Queue[(String,(Double)=>Double)]
		var params=new Queue[((Double,Double),Stream[Double],Stream[Double],Double)]
		
		maps=maps enqueue ("_",phi({x=>x*x        },{x=>x*x}))
		maps=maps enqueue ("A",phi({x=>abs(x)     },{x=>abs(x)    }))
		maps=maps enqueue ("Q",phi({x=>x*x*x*x    },{x=>x*x*x*x   }))
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor( 0   , 0.1  , 20 ) , 
			ParamRange.neighbor( 1.0 , 0.01 ,  1 ) , 
			0.1 )
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor( 0.25 , 0.01 , 10 ) , 
			ParamRange.neighbor( 1.0  , 0.01 ,  1 ) , 
			0.1 )
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor(-0.475 , 0.001 , 1 ) , 
			ParamRange.neighbor( 1.0   , 0.01  ,  1 ) , 
			0.1 )
		
		for(m<-maps ; p<-params){
			fillParam(m._1,m._2,p._1,p._2,p._3,p._4)
		}
		
		()
	}
}

