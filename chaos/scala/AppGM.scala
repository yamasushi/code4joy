// Gumowski-Mira Strange Attractor
// 2009-09-09
// 2009-09-11
// 2009-09-28
import scala.collection.immutable._
import Math.{sin,cos,log,sqrt,abs,exp}

chaosParam.doMain(args)

object chaosParam extends ChaosParameter[DSGumowskiMira]
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
				phi , ( paramA , paramB ),(mu,nu) ,  numRing,ovalR ) )
		}
		//
		()
	}
	
	//----------------------------
	def setup() : Unit = {
		import GumowskiMiraPhi._
		
		var maps  =Queue[(String,(Double)=>Double)]()
		var params=Queue[((Double,Double),Stream[Double],Stream[Double],Double)]()
		
		maps=maps enqueue ("Quadratic",phi(psiQuadratic _ ))
		maps=maps enqueue ("Abs",phi(psiAbs       _ ))
		maps=maps enqueue ("Sqrt",phi(psiSqrt      _ ))
		maps=maps enqueue ("Quartic",phi(psiQuartic   _ ))
		maps=maps enqueue ("Cubic",phi(psiCubic     _ ))
		maps=maps enqueue ("Pow",phi(psiPow       _ ))
		maps=maps enqueue ("PowAbs",phi(psiPowAbs    _ ))
		maps=maps enqueue ("Trig",phi(psiTrig      _ ))
		maps=maps enqueue ("Log",phi(psiLog       _ ))
		maps=maps enqueue ("LogAbs",phi(psiLogAbs    _ ))
		maps=maps enqueue ("Circle",phi(psiCircle    _ ))
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor( 0   , 0.1  , 20 ) , 
			ParamRange.neighbor( 1.0 , 0.01 ,0) , 
			0.1 )
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor( 0.25 , 0.01 , 10 ) , 
			ParamRange.neighbor( 1.0  , 0.01 ,0) , 
			0.1 )
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor(-0.475 , 0.001 ,10) , 
			ParamRange.neighbor( 1.0   , 0.01  ,0) , 
			0.1 )
		
		for(m<-maps ; p<-params){
			fillParam(m._1,m._2,p._1,p._2,p._3,p._4)
		}
		
		()
	}
}

