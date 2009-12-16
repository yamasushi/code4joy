// Gumowski-Mira Strange Attractor
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
		
		var maps  =new Queue[(String,(Double)=>Double)]
		var params=new Queue[((Double,Double),Stream[Double],Stream[Double],Double)]
		
		maps=maps enqueue ("Quadratic",phi(psiQuadratic _ ))
		
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

