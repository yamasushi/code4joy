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
			ParamRange.neighbor( 1.0 , 0.01 ,0) , 
			0.1 )
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor( 0.25 , 0.01 , 10 ) , 
			ParamRange.neighbor( 1.0  , 0.01 ,0) , 
			0.1 )
		
		params=params enqueue ( ( 0.008, 0.05 ) ,
			ParamRange.neighbor(-0.475 , 0.001 ,0) , 
			ParamRange.neighbor( 1.0   , 0.01  ,0) , 
			0.1 )
		
		for(m<-maps ; p<-params){
			fillParam(m._1,m._2,p._1,p._2,p._3,p._4)
		}
		
		()
	}
}

